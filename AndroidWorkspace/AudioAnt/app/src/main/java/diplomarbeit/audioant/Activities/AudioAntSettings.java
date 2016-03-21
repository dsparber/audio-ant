package diplomarbeit.audioant.Activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import diplomarbeit.audioant.Fragments.ShowTextAlert;
import diplomarbeit.audioant.Model.Helper.WifiHelper;
import diplomarbeit.audioant.Model.Services.CommunicationService;
import diplomarbeit.audioant.R;

public class AudioAntSettings extends AppCompatActivity {

    private final String TAG = "AA_SETTINGS";
    private CheckBox checkBox_lightSignals;
    private CheckBox checkBox_audioSignals;
    private WifiHelper wifiHelper;
    private EditText wlanName;
    private boolean lightSignals;
    private boolean audioSignals;
    private int alertSoundId = 0;
    private boolean serviceIsBound = false;
    private CommunicationService communicationService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.MyBinder binder = (CommunicationService.MyBinder) service;
            communicationService = binder.getService();
            Toast.makeText(getApplicationContext(), "jetzt", Toast.LENGTH_SHORT).show();
            serviceIsBound = true;
            try {
                JSONObject object = new JSONObject();
                object.put("action", "getCurrentSettings");
                communicationService.sendText(object.toString());
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
            try {
                JSONObject obj = new JSONObject(intent.getStringExtra("json"));
                JSONObject data = obj.getJSONObject("data");

                lightSignals = data.getBoolean("lightSignals");
                audioSignals = data.getBoolean("audioSignals");
                alertSoundId = data.getInt("alertSoundId");

                checkBox_audioSignals.setChecked(audioSignals);
                checkBox_lightSignals.setChecked(lightSignals);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        bindToCommunicationService();
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                sendSaveSettingsJson();
                unbindFromCommunicationService();
                super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        sendSaveSettingsJson();
        unbindFromCommunicationService();
        super.onBackPressed();
    }

    public void sendSaveSettingsJson() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", "changeSettings");
            JSONObject data = new JSONObject();
            data.put("lighSignals", checkBox_lightSignals.isChecked());
            data.put("audioSignals", checkBox_audioSignals.isChecked());
            data.put("alertSoundId", alertSoundId);
            jsonObject.put("data", data);
            communicationService.sendText(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_ant_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wifiHelper = new WifiHelper(this);
        wlanName = (EditText) findViewById(R.id.editText_wifi_name);

        checkBox_audioSignals = (CheckBox) findViewById(R.id.checkBox_audiosignale);
        checkBox_lightSignals = (CheckBox) findViewById(R.id.checkBox_lichtsignale);

        LocalBroadcastManager.getInstance(this).registerReceiver(currentSettingsReceiver, new IntentFilter("currentSettings"));
    }

    public void buttonClicked(View v) {
        Button button = (Button) v;
        switch (button.getId()) {
            case R.id.button_wlan_infos_senden:
                Toast.makeText(getApplicationContext(), wifiHelper.getSSID(), Toast.LENGTH_SHORT).show();
                break;
        }
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
}
