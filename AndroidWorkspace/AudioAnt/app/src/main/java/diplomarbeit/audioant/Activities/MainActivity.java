package diplomarbeit.audioant.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import diplomarbeit.audioant.Model.Helper.Constants;
import diplomarbeit.audioant.Model.Helper.Settings;
import diplomarbeit.audioant.Model.Helper.WifiHelper;
import diplomarbeit.audioant.Model.Services.CommunicationService;
import diplomarbeit.audioant.R;

/**
 * Starting Activity of the application, handles initial connection with the AudioAnt
 * User can navigate to different functions of the App
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";
    private static boolean serviceIsBound = false;
    Constants constants;
    Settings settings;
    private CommunicationService communicationService;
    private long start;
    private long stop;
    private boolean connectedToServer = false;
    private CountDownTimer serverConnectionTimer;
    private TextView textView_audioant_connection_status;
    private WifiHelper wifiHelper;
    private ImageView imageView_verbunden;
    private ProgressDialog connectingToWifi;
    private AlertDialog loadingDialog;
    private AlertDialog.Builder notConnectedToWifiNetworkDialog;
    private AlertDialog.Builder firstStartDialog;


    private BroadcastReceiver wifiConnectedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleWificonnected(context, intent);
        }
    };

    private BroadcastReceiver connectedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleConnectedReceived(context, intent);
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.MyBinder binder = (CommunicationService.MyBinder) service;
            communicationService = binder.getService();
            if (!wifiHelper.isConnected()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        showNotConnectedToWifiDialog();
                    }
                }).start();
            } else {
                if (communicationService.isConnected()) {
                    imageView_verbunden.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_24dp));
                    textView_audioant_connection_status.setText(getResources().getString(R.string.main_label_connected));
                } else {
                    imageView_verbunden.setImageDrawable(getResources().getDrawable(R.drawable.ic_wifi_not_connected_24dp));
                    textView_audioant_connection_status.setText(getResources().getString(R.string.main_label_not_connected));
                    showLoadingDialog();
                    serverConnectionTimer.start();
                }
                stop = System.currentTimeMillis();
                Log.d(TAG, "" + (stop - start));
                Toast.makeText(MainActivity.this, "jetzt", Toast.LENGTH_SHORT).show();
                serviceIsBound = true;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceIsBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiHelper = new WifiHelper(this);

        constants = new Constants();
        settings = new Settings(this);
        serverConnectionTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                handleServerConnectTimerFinish();
            }
        };


        start = System.currentTimeMillis();
        showDialogIfNecessary();


        textView_audioant_connection_status = (TextView) findViewById(R.id.main_textView_verbunden);
        imageView_verbunden = (ImageView) findViewById(R.id.ic_verbunden);
        LocalBroadcastManager.getInstance(this).registerReceiver(connectedReceiver, new IntentFilter("verbunden"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindToCommunicationService();
    }

    @Override
    protected void onPause() {
        unbindFromCommunicationService();
        super.onPause();
    }


    //  Methods that implement what should happen on received Broadcasts or finished Timers
    public void handleConnectedReceived(Context context, Intent intent) {
        try {
            connectedToServer = true;
            loadingDialog.dismiss();
            textView_audioant_connection_status.setText(getResources().getString(R.string.main_label_connected));
            imageView_verbunden.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_24dp));
        } catch (NullPointerException e) {
            Log.d(TAG, "could not dismiss dialog");
        }
    }

    public void handleServerConnectTimerFinish() {
        if (!connectedToServer) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "server connection timed out!");
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("Keine Verbindung Möglich");
                    dialog.setMessage(getResources().getString(R.string.main_message_no_server_connection));
                    dialog.setNegativeButton("Schließen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            stopService(new Intent(MainActivity.this, CommunicationService.class));
                            android.os.Process.killProcess(android.os.Process.myPid());
                            MainActivity.super.onDestroy();
                        }
                    });
                    dialog.show();
                }
            });
        }
    }

    public void handleWificonnected(Context context, Intent intent) {
        final Intent i = intent;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkInfo networkInfo = i.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null) {
                    if (networkInfo.isConnected()) {
                        String ssid = wifiHelper.getCurrentNetworkSSID();
                        if (ssid.equals(constants.AA_HOTSPOT_NAME) || ssid.equals(settings.getLocalWifiName())) {
                            if (wifiHelper.getSupplicantState().equals(SupplicantState.COMPLETED)) {
                                if (ssid.equals(constants.AA_HOTSPOT_NAME)) {
//                                    settings.setStartedBefore(true);
                                }
                                Log.d(TAG, "conneted to wifi");
                                try {
                                    connectingToWifi.dismiss();
                                } catch (Exception e) {
                                    Log.d(TAG, "Wifi connecction dialog could not be dismissed");
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showLoadingDialog();
                                        connectedToServer = false;
                                        serverConnectionTimer.start();
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
        t.start();
    }



    public void bindToCommunicationService() {
        if (!serviceIsBound) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(MainActivity.this, CommunicationService.class);
                    bindService(i, serviceConnection, 0);
                    startService(i);
                }
            });
            t.start();
        }
    }

    public void unbindFromCommunicationService() {
        try {
            unbindService(serviceConnection);
            serviceIsBound = false;
        } catch (Exception e) {
            Log.d(TAG, "service could not be unbound because it wasn't bound");
        }
    }


    //  Show different Dialogs
    public void showDialogIfNecessary() {
        Log.d(TAG, "Entered showDialogIfNecessary");
        Intent i = getIntent();
        if (i.hasExtra("message")) {
            String message = i.getStringExtra("message");
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Information");
            alert.setMessage(message);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.show();
        }
    }

    public void showLoadingDialog() {
        loadingDialog = new ProgressDialog(MainActivity.this);
        loadingDialog.setMessage("Verbindung zu  Server wird aufgebaut....");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    public void showNotConnectedToWifiDialog() {
        if (!settings.getStartedBefore()) {
            firstStartDialog = new AlertDialog.Builder(MainActivity.this);
            firstStartDialog.setTitle("Herzlich willkommen!");
            firstStartDialog.setMessage(getResources().getString(R.string.main_message_first_usage));
            firstStartDialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            firstStartDialog.setPositiveButton("Verbinden", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    showConnectingToNetworkDialog();
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (!wifiHelper.isWifiEnabled()) {
                                wifiHelper.setWifiEnabled(true);
                            }
                            wifiHelper.connectToWifi(constants.AA_HOTSPOT_NAME, constants.AA_HOTSPOT_PW);
                            registerReceiver(wifiConnectedReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
                        }
                    });
                    t.run();
                }
            });
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    firstStartDialog.show();
                }
            });
        } else {
            if (settings.getLocalWifiName().equals("")) {
                notConnectedToWifiNetworkDialog = new AlertDialog.Builder(MainActivity.this);
                notConnectedToWifiNetworkDialog.setTitle("Keine Wlan verbindung");
                notConnectedToWifiNetworkDialog.setMessage(getResources().getString(R.string.main_message_no_wifi_connection_hotspot));
                notConnectedToWifiNetworkDialog.setPositiveButton("Verbinden", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showConnectingToNetworkDialog();
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (!wifiHelper.isWifiEnabled()) {
                                    wifiHelper.setWifiEnabled(true);
                                }
                                wifiHelper.connectToWifi(constants.AA_HOTSPOT_NAME, constants.AA_HOTSPOT_PW);
                                registerReceiver(wifiConnectedReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
                            }
                        });
                        t.run();
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notConnectedToWifiNetworkDialog.show();
                    }
                });
            } else {
                ///implement connection to local wifi + sending
            }
        }
    }

    public void showConnectingToNetworkDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connectingToWifi = new ProgressDialog(MainActivity.this);
                connectingToWifi.setMessage("Verbindung zum Wlan Netzwerk wird aufgebaut...");
                connectingToWifi.setCancelable(false);
                connectingToWifi.show();
            }
        });
    }


    //  Navigate to other Activities
    public void startRecordActivity(View v) {
        startActivity(new Intent(MainActivity.this, RecordActivity.class));
    }

    public void startListActivity(View v) {
        startActivity(new Intent(MainActivity.this, ListActivity.class));
    }

    public void startSettingsActivity(View v) {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    public void startAudioAntSettingsActivity(View v) {
        startActivity(new Intent(MainActivity.this, AudioAntSettings.class));
    }
}
