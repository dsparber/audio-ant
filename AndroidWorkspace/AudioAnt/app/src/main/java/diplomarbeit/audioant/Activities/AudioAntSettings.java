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
import android.media.MediaPlayer;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import diplomarbeit.audioant.Fragments.ShowTextAlert;
import diplomarbeit.audioant.Model.Classes.SoundListItem;
import diplomarbeit.audioant.Model.Helper.AlertSoundHelper;
import diplomarbeit.audioant.Model.Helper.Constants;
import diplomarbeit.audioant.Model.Helper.Settings;
import diplomarbeit.audioant.Model.Helper.WifiHelper;
import diplomarbeit.audioant.Model.Services.CommunicationService;
import diplomarbeit.audioant.R;

public class AudioAntSettings extends AppCompatActivity {

    private final String TAG = "AA_SETTINGS";
    private AlertSoundHelper alertSoundHelper;
    private CheckBox checkBox_lightSignals;
    private CheckBox checkBox_audioSignals;
    private WifiHelper wifiHelper;
    private EditText wlanName;
    private EditText wlanPassword;
    private Settings settings;
    private boolean connectedToNewNetwork = false;
    private boolean readyForResult = false;
    private String chosenNetwork = "";
    private MediaPlayer player;
    private AlertDialog loadingDialog;
    private TextView textView_ChosenSound;
    private int alertSoundId = 0;
    private ArrayAdapter<SoundListItem> arrayAdapterSounds;
    private ArrayAdapter<String> arrayAdapterNetworks;

    private BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            handleWifiNetworksRecieved(c, intent);
        }
    };
    private boolean serviceIsBound = false;
    private CommunicationService communicationService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.MyBinder binder = (CommunicationService.MyBinder) service;
            communicationService = binder.getService();
            requestSoundsIfFirstStart();
            Log.d(TAG, "Der service ist jetzt verbunden");
            serviceIsBound = true;
            try {
                JSONObject object = new JSONObject();
                object.put("action", "getCurrentSettings");
                communicationService.sendToServer(object.toString());
                Log.d(TAG, "AudioAnt settings were requested");
            } catch (JSONException e) {
                Log.d(TAG, "Json could not be created");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceIsBound = false;
        }
    };
    private BroadcastReceiver currentSettingsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleSettingsReceived(context, intent);
        }
    };
    private BroadcastReceiver getAllAlertSounds = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleAlertSounds(context, intent);
        }
    };
    private BroadcastReceiver reconnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleWifiReconnectReceived(context, intent);
        }
    };
    private BroadcastReceiver wifiChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleWifiChangedReceived(context, intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_ant_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        alertSoundHelper = new AlertSoundHelper();

        wifiHelper = new WifiHelper(this);
        wlanName = (EditText) findViewById(R.id.editText_wifi_name);
        wlanPassword = (EditText) findViewById(R.id.editText_wifi_password);

        checkBox_audioSignals = (CheckBox) findViewById(R.id.checkBox_audiosignale);
        checkBox_lightSignals = (CheckBox) findViewById(R.id.checkBox_lichtsignale);
        textView_ChosenSound = (TextView) findViewById(R.id.audioAnt_settings_textView_chosen_sound);

        LocalBroadcastManager.getInstance(this).registerReceiver(currentSettingsReceiver, new IntentFilter("currentSettings"));
        LocalBroadcastManager.getInstance(this).registerReceiver(getAllAlertSounds, new IntentFilter("alertSounds"));
        LocalBroadcastManager.getInstance(this).registerReceiver(reconnectReceiver, new IntentFilter("reconnected"));

        arrayAdapterSounds = new ArrayAdapter<>(AudioAntSettings.this, android.R.layout.select_dialog_singlechoice);
        arrayAdapterNetworks = new ArrayAdapter<String>(AudioAntSettings.this, android.R.layout.select_dialog_singlechoice);

        registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiHelper.startScan();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindToCommunicationService();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                sendSaveSettingsJson();
                try {
                    unbindFromCommunicationService();
                    unregisterReceiver(wifiScanReceiver);
                    unregisterReceiver(wifiChangedReceiver);
                    unregisterReceiver(reconnectReceiver);
                } catch (IllegalArgumentException e) {
                    Log.d(TAG, "tried to unregister receiver that was not registered");
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        sendSaveSettingsJson();
        try {
            unregisterReceiver(wifiScanReceiver);
            unregisterReceiver(wifiChangedReceiver);
            unregisterReceiver(reconnectReceiver);
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "tried to unregister receiver that was not registered");
        }
        unbindFromCommunicationService();
        super.onBackPressed();
    }


    //  Methods that implement what should happen on received Broadcasts or finished Timers
    public void handleAlertSounds(Context context, Intent intent) {
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("json"));
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray sounds = data.getJSONArray("sounds");
            alertSoundHelper.deleteExistingAlerts();
            for (int i = 0; i < sounds.length(); i++) {
                JSONObject sound = (JSONObject) sounds.get(i);
                String name = sound.getString("name");
                String fileName = sound.getString("fileName");
                int number = sound.getInt("number");
                String fileContent = sound.getString("fileContent");
                String fullFileName = number + "-" + name + "-" + fileName;

                alertSoundHelper.saveAlert(fullFileName, fileContent);
            }
            settings.setSoundsLoaded(true);
            alertSoundHelper.readAlerts(arrayAdapterSounds);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handleSettingsReceived(Context context, Intent intent) {
        try {
            JSONObject obj = new JSONObject(intent.getStringExtra("json"));
            JSONObject data = obj.getJSONObject("data");

            boolean lightSignals = data.getBoolean("lightSignals");
            boolean audioSignals = data.getBoolean("audioSignals");
            alertSoundId = data.getInt("alertSoundId");

            checkBox_audioSignals.setChecked(audioSignals);
            checkBox_lightSignals.setChecked(lightSignals);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handleWifiNetworksRecieved(Context context, Intent intent) {
        if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
            List<ScanResult> scanResults = wifiHelper.getScanResults();
            arrayAdapterNetworks.clear();
            for (int i = 0; i < scanResults.size(); i++) {
                arrayAdapterNetworks.add(scanResults.get(i).SSID);
            }
        }
    }

    public void handleWifiChangedReceived(Context context, Intent intent) {
        final Intent i = intent;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkInfo networkInfo = i.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null) {
                    if (networkInfo.isConnected()) {
                        String ssid = wifiHelper.getCurrentNetworkSSID();
                        if (ssid.equals(wlanName.getText() + "")) {
                            if (wifiHelper.getSupplicantState().equals(SupplicantState.COMPLETED)) {
                                Log.d(TAG, "conneted to new network");
                                connectedToNewNetwork = true;
                                readyForResult = true;
                                reconnectToHotspot();
                            }
                        } else if (ssid.equals(new Constants().AA_HOTSPOT_NAME)) {
                            if (wifiHelper.getSupplicantState().equals(SupplicantState.COMPLETED)) {
                                if (readyForResult) {
                                    Log.d(TAG, "connected to AudioAnt Hotspot");
                                    initialiseConnectionToAudioAnt();
                                }
                            }
                        }
                    }
                }
            }
        });
        t.start();
    }

    public void handleWifiReconnectReceived(Context context, Intent intent) {
        loadingDialog.dismiss();
        if (connectedToNewNetwork) {

            sendConnectToLocalWifiJson();
            Toast.makeText(this, "es hat funktioniert!!!", Toast.LENGTH_SHORT).show();
            new Settings(this).setLocalWifiName("" + wlanName.getText());
            readyForResult = false;
        } else {
            Toast.makeText(this, "es hat nicht funktioniert!!!", Toast.LENGTH_SHORT).show();

//            AlertDialog.Builder alert = new AlertDialog.Builder(AudioAntSettings.this);
//            alert.setTitle("Fehler!");
//            alert.setMessage(getResources().getString(R.string.audioant_settings_message_connect_error));
//            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            alert.show();
        }
    }


    //  initialisation methods + method invoked on buttonClick
    public void buttonClicked(View v) {
        Button button = (Button) v;
        switch (button.getId()) {
            case R.id.button_wlan_infos_senden:
                showLoadingDialog();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connectedToNewNetwork = false;
                        registerReceiver(wifiChangedReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
                        communicationService.closeSocket();
                        wifiHelper.connectToWifi("" + wlanName.getText(), "" + wlanPassword.getText());
                        startWifiConnectTimer();
                    }
                });
                t.start();
                break;
            case R.id.audioant_settings_button_wlan_list:
                chooseWifiFromListDialog();
                break;
        }
    }

    public void initialiseConnectionToAudioAnt() {
        communicationService.reconnectToAudioAntHotspot();
    }

    public void requestSoundsIfFirstStart() {
        settings = new Settings(this);
        if (!settings.getSoundsLoaded()) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("action", "getAlertSounds");
                communicationService.sendToServer(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            alertSoundHelper.readAlerts(arrayAdapterSounds);
        }
    }


    //  Sending Json Methods
    public void sendConnectToLocalWifiJson() {
        try {
            JSONObject object = new JSONObject();
            object.put("action", "connectToWifiNetwork");
            JSONObject data = new JSONObject();
            data.put("SSID", "" + wlanName.getText());
            data.put("password", "" + wlanPassword.getText());
            object.put("data", data);
            communicationService.sendToServer(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendSaveSettingsJson() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", "changeSettings");
            JSONObject data = new JSONObject();
            data.put("lightSignals", checkBox_lightSignals.isChecked());
            data.put("audioSignals", checkBox_audioSignals.isChecked());
            data.put("alertSoundId", alertSoundId);
            jsonObject.put("data", data);
            communicationService.sendToServer(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //  binding and unbinding to service Methods
    public void bindToCommunicationService() {
        if (!serviceIsBound) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(getApplicationContext(), CommunicationService.class);
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


    //  Methods that show different Dialogs
    public void showChooseAlertDialog(View v) {

        final AlertDialog.Builder chooseAlertDialog = new AlertDialog.Builder(AudioAntSettings.this);
        chooseAlertDialog.setTitle(getResources().getString(R.string.notification_choose_header));
        chooseAlertDialog.setNegativeButton("Fertig", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (player != null) {
                    player.release();
                    player = null;
                    dialog.dismiss();
                }
            }
        });

        final ListView listViewSounds = new ListView(this);
        listViewSounds.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listViewSounds.setAdapter(arrayAdapterSounds);
        listViewSounds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listViewSounds.setSelection(position);
                SoundListItem chosenAlert = arrayAdapterSounds.getItem(position);
                alertSoundId = chosenAlert.getNumber();
                textView_ChosenSound.setText(chosenAlert.getName());
                alertSoundHelper.playAlertFromName(chosenAlert.getName());
            }
        });
        listViewSounds.setSelection(alertSoundId);
        chooseAlertDialog.setView(listViewSounds);
        chooseAlertDialog.show();
    }

    public void showLoadingDialog() {
        loadingDialog = new ProgressDialog(AudioAntSettings.this);
        loadingDialog.setMessage("Die Eingegebenen Daten werden überprüft");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    public void chooseWifiFromListDialog() {
        final AlertDialog.Builder chooseWifiDialog = new AlertDialog.Builder(AudioAntSettings.this);
        chooseWifiDialog.setTitle(getResources().getString(R.string.audioant_settings_choose_wifi_header));
        chooseWifiDialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        chooseWifiDialog.setPositiveButton("Fertig", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                wlanName.setText(chosenNetwork);
            }
        });

        final ListView listViewNetworks = new ListView(this);
        listViewNetworks.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listViewNetworks.setAdapter(arrayAdapterNetworks);
        listViewNetworks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listViewNetworks.setSelection(position);
                chosenNetwork = arrayAdapterNetworks.getItem(position);
            }
        });

        chooseWifiDialog.setView(listViewNetworks);
        chooseWifiDialog.show();

    }

    public void showWlanHelp(View v) {
        ShowTextAlert textAlert = new ShowTextAlert();
        switch (v.getId()) {
            case R.id.info_wlan_importance:
                textAlert.setText(getResources().getString(R.string.wlan_description));
                textAlert.setHeader(getResources().getString(R.string.wlan_description_header));
                textAlert.show(getFragmentManager(), "wlan help");
                break;
            case R.id.info_audioant_notifications:
                textAlert.setText(getResources().getString(R.string.audioant_benachrichtigungen_description));
                textAlert.setHeader(getResources().getString(R.string.audioant_benachrichtigungen_description_header));
                textAlert.show(getFragmentManager(), "wlan help");
                break;
        }
    }


    //  Methods concerning the network settings input testing process
    public void startWifiConnectTimer() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    if (!connectedToNewNetwork) {
                        Log.d(TAG, "Entweder passwort oder ssid sind falsch!!");
                        readyForResult = true;
                        reconnectToHotspot();
                    }
                    Log.d(TAG, "zeit vorbei");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void reconnectToHotspot() {
        Log.d(TAG, "trying to reconnect to hotspot");
        wifiHelper.connectToWifi(new Constants().AA_HOTSPOT_NAME, new Constants().AA_HOTSPOT_PW);
    }
}
