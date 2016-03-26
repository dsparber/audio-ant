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

import diplomarbeit.audioant.Activities.AlarmActivity;
import diplomarbeit.audioant.Activities.MainActivity;
import diplomarbeit.audioant.Model.Helper.Constants;

public class CommunicationService extends Service {

    private static String TAG = "BOUND_SERICE";
    private Socket socket = null;
    private IBinder binder = new MyBinder();
    private BufferedReader reader;
    private PrintWriter printer;
    private boolean isConnected = false;
    private boolean socketClosedOnPurpose = false;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "service created");
        initialiseNetworkConnection(false);
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
    public void initialiseNetworkConnection(final boolean reconnect) {
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
//                              Try to connect to the Server
                                connectToServer(reconnect);
                            }
                        });
                        tt.start();

                        try {
//                          Try connecting to the server again if not successful after two seconds
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

    synchronized void connectToServer(boolean reconnect) {
        if (!isConnected) try {
//          Establish a connection to the server
            socket = new Socket(new Constants().AA_HOTSPOT_IP, new Constants().AA_HOTSPOT_PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            isConnected = true;

//          Start listening for incoming data from the server
            listenForAudioAntMessages(reader);

//          Inform Activities about completed connection process
            Intent i;
            if (reconnect) {
                Log.d(TAG, "Erneutes verbinden zum Server erfolgreich!!");
                i = new Intent("reconnected");
            } else {
                i = new Intent("verbunden");
                Log.d(TAG, "Verbindung zum Server erfolgreich");
            }
            LocalBroadcastManager.getInstance(CommunicationService.this).sendBroadcast(i);
        } catch (IOException e) {
            Log.d(TAG, "Verbindung zum Server konnte nicht aufgebaut werden");
        }
    }

    private void listenForAudioAntMessages(final BufferedReader reader) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String incoming;
                try {
//                  Listen for incoming messages
                    while ((incoming = reader.readLine()) != null) {
                        Log.d(TAG, incoming);

//                      Check if the incoming data indicates a recognised sound
                        if (checkIfRecognisedSounds(incoming)) {
//                          Start the activity that notifies the user
                            startAlarmActivity(incoming);
                        } else {
//                          Send the received data to the activities
                            sendLocalBroadcast(incoming);
                        }
                    }
                    Log.d(TAG, "Die Verbindung zum Server wurde verloren");
                    if (!socketClosedOnPurpose) {
                        Log.d(TAG, "trying to reconnect");
                        initialiseNetworkConnection(false);
                        handleServerDisconnected();
                        isConnected = false;
                    }
                } catch (IOException e) {
                    Log.d(TAG, "server closed");
                    if (!socketClosedOnPurpose) {
                        Log.d(TAG, "trying to reconnect");
                        isConnected = false;
                        handleServerDisconnected();
                        initialiseNetworkConnection(false);
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
//      Send local broadcast with received json object as extra
        Intent intent;
        try {
            JSONObject jsonObject = new JSONObject(s);
            intent = new Intent(jsonObject.getString("action"));
            intent.putExtra("json", jsonObject.toString());

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            Log.d(TAG, "sending broadcast");

        } catch (JSONException e) {
            Log.d(TAG, "received invalid json");
        }
    }

    private boolean checkIfRecognisedSounds(String s) {
//      Check if incoming JSON indicates a recognised sound
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
//      Start Activity that informs the user
        Intent i = new Intent(CommunicationService.this, AlarmActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        i.putExtra("json", jsonFile);
        startActivity(i);
    }


    //  Methods that handle the server connection
    public void handleServerDisconnected() {
//      Return to MainActivity if connection to server is lost
        Intent i = new Intent(CommunicationService.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void closeSocket() {
//      Close the socket
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