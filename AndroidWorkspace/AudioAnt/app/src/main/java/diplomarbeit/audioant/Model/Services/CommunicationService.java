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
import java.io.Reader;
import java.net.Socket;

import diplomarbeit.audioant.Activities.AlarmActivity;
import diplomarbeit.audioant.Activities.MainActivity;
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
        return START_NOT_STICKY;
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


    //  Initialisation methods
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
                                connectToServerThread();
                            }
                        });
                        tt.start();

                        try {
                            Thread.sleep(2000);
                            if (!isConnected) {
                                tt.interrupt();
                                break B;
                            } else break A;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        t.start();
    }

    public void reconnectToAudioAntHotspot() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                A:
                while (true) {
                    while (true) {
                        Thread tt = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isConnected) {
                                    try {
                                        socket = new Socket(new Constants().AA_HOTSPOT_IP, new Constants().AA_HOTSPOT_PORT);
                                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                        printer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                                        isConnected = true;
                                        Log.d(TAG, "Erneutes verbinden zum Server erfolgreich!!");
                                        Intent i = new Intent("reconnected");
                                        LocalBroadcastManager.getInstance(CommunicationService.this).sendBroadcast(i);
                                        listenForAudioAntMessages(reader);

                                        socketClosedOnPurpose = false;

                                    } catch (IOException e) {
                                        Log.d(TAG, "Verbindung zum Server konnte nicht aufgebaut werden");
                                    }
                                }
                            }
                        });
                        tt.start();

                        try {
                            Thread.sleep(50000);
                            if (!isConnected) break;
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

    synchronized void connectToServerThread() {
        if (!isConnected) {
            try {
                socket = new Socket(new Constants().AA_HOTSPOT_IP, new Constants().AA_HOTSPOT_PORT);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                isConnected = true;
                Intent i = new Intent("verbunden");
                LocalBroadcastManager.getInstance(CommunicationService.this).sendBroadcast(i);
                Log.d(TAG, "Verbindung zum Server erfolgreich");
                listenForAudioAntMessages(reader);

            } catch (IOException e) {
                Log.d(TAG, "Verbindung zum Server konnte nicht aufgebaut werden");
            }
        }
    }

    private void listenForAudioAntMessages(final BufferedReader reader) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String incoming;
                try {
                    while ((incoming = reader.readLine()) != null) {
                        Log.d(TAG, incoming);
                        if (checkIfRecognisedSounds(incoming)) {
                            startAlarmActivity(incoming);
                        } else {
                            sendLocalBroadcast(incoming);
                        }
                    }
                    Log.d(TAG, "Die Verbindung zum Server wurde verloren");
                    if (!socketClosedOnPurpose) {
                        Log.d(TAG, "trying to reconnect");
                        initialiseNetworkConnection();
                        handleServerDisconnected();
                        isConnected = false;
                    }
                } catch (IOException e) {
                    Log.d(TAG, "server closed");
                    if (!socketClosedOnPurpose) {
                        Log.d(TAG, "trying to reconnect");
                        isConnected = false;
                        handleServerDisconnected();
                        initialiseNetworkConnection();
                    }
                }
            }
        });
        t.start();

    }


    //  sending methods (server & activities)
    public void sendToServer(String content) {
        try {
            Log.d(TAG, "string wird an server gesendet");
            printer.write(content);
            printer.write("\r");
            printer.flush();
        } catch (NullPointerException e) {
            Log.d(TAG, "fehler beim senden, server stream nicht bereit");

        }
    }

    private void sendLocalBroadcast(String s) {
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

    private boolean checkIfRecognisedSounds(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String action = object.getString("action");
            if (action.equals("recognisedSound")) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void startAlarmActivity(String jsonFile) {
        Intent i = new Intent(CommunicationService.this, AlarmActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtra("json", jsonFile);
        startActivity(i);
    }


    //  Methods that handle the server connection
    public void handleServerDisconnected() {
        Intent i = new Intent(CommunicationService.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void closeSocket() {
        try {
            socketClosedOnPurpose = true;
            isConnected = false;
            socket.close();
            reader = null;
            printer = null;
            Log.d(TAG, "Socket closed");
        } catch (IOException e) {
            Log.d(TAG, "socket could not be closed");
        }
    }


    public boolean isConnected() {
        return isConnected;
    }


    public class MyBinder extends Binder {
        public CommunicationService getService() {
            return CommunicationService.this;
        }
    }
}