package diplomarbeit.audioant.Model.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import diplomarbeit.audioant.Model.Helper.Constants;

public class CommunicationService extends Service {

    private static String TAG = "BOUND_SERICE";
    Socket socket = null;
    private IBinder binder = new MyBinder();
    private BufferedReader reader;
    private PrintWriter printer;
    private boolean isConnected = false;
    private boolean socketClosedOnPurpose = false;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "service created");
        initialiseNetworkConnection();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Service was bound");
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "the service was started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service was destroyed");
        try {
            reader.close();
            printer.close();
        } catch (IOException e) {

        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "The service was unbound");
        return super.onUnbind(intent);
    }

    public void sendText(String text) {
        try {
            Log.d(TAG, "string wird an server gesendet");
            printer.write(text);
            printer.write("\r");
            printer.flush();
        } catch (NullPointerException e) {
            Log.d(TAG, "fehler beim senden, server stream nicht bereit");

        }
    }

    public void reconnectToAudioAntHotspot() {
        initialiseNetworkConnectionAgain();
    }

    private void sendReceivedData(String s) {
        Intent intent;
        try {
            JSONObject jsonObject = new JSONObject(s);
            intent = new Intent(jsonObject.getString("action"));
            intent.putExtra("json", jsonObject.toString());

        } catch (JSONException e) {
            intent = new Intent("Test");
            intent.putExtra("text", s);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d(TAG, "sending broadcast");

    }

    public void closeSocket() {
        try {
            socket.close();
            reader = null;
            printer = null;
            socketClosedOnPurpose = true;
            isConnected = false;
            Log.d(TAG, "Socket closed");
        } catch (IOException e) {
            Log.d(TAG, "socket could not be closed");
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    private void initialiseNetworkConnectionAgain() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                A:
                while (true) {
                    B:
                    while (true) {

                        Thread tt = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isConnected) {
                                    try {
                                        socket = new Socket(new Constants().AA_HOTSPOT_IP, new Constants().AA_HOTSPOT_PORT);
                                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                        printer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                                        Log.d(TAG, "Erneutes verbinden zum Server erfolgreich!!");
                                        Intent i = new Intent("reconnected");
                                        LocalBroadcastManager.getInstance(CommunicationService.this).sendBroadcast(i);
                                        listenForAudioAntMessages();

                                        isConnected = true;
                                        socketClosedOnPurpose = false;
                                    } catch (IOException e) {
                                        Log.d(TAG, "Verbindung zum Server konnte nicht aufgebaut werden");
                                    }
                                }
                            }

                        });
                        tt.start();
                        try {
                            Thread.sleep(10000);
                            if (!isConnected) break B;
                            else break A;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        t.start();
    }

    private void initialiseNetworkConnection() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                A:
                while (true) {
                    B:
                    while (true) {

                        Thread tt = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isConnected) {
                                    try {
                                        socket = new Socket(new Constants().AA_HOTSPOT_IP, new Constants().AA_HOTSPOT_PORT);
                                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                        printer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                                        Intent i = new Intent("verbunden");
                                        LocalBroadcastManager.getInstance(CommunicationService.this).sendBroadcast(i);
                                        Log.d(TAG, "Verbindung zum Server erfolgreich");
                                        listenForAudioAntMessages();

                                        isConnected = true;
                                    } catch (IOException e) {
                                        Log.d(TAG, "Verbindung zum Server konnte nicht aufgebaut werden");
                                    }
                                }
                            }
                        });
                        tt.start();


                        try {
                            Thread.sleep(10000);
                            if (!isConnected) break B;
                            else break A;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        t.start();
    }

    private void listenForAudioAntMessages() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String incoming;
                try {
                    while ((incoming = reader.readLine()) != null) {
                        sendReceivedData(incoming);
                    }
                    Log.d(TAG, "Die Verbindung zum Server wurde verloren");
                    if (!socketClosedOnPurpose) {
                        Log.d(TAG, "trying to reconnect");
                        initialiseNetworkConnection();
                        isConnected = false;
                    }
                } catch (IOException e) {
                    Log.d(TAG, "server closed");
                    if (!socketClosedOnPurpose) {
                        Log.d(TAG, "trying to reconnect");
                        isConnected = false;
                        initialiseNetworkConnection();
                    }
                }
            }
        });
        t.start();

    }

    public class MyBinder extends Binder {
        public CommunicationService getService() {
            return CommunicationService.this;
        }
    }
}