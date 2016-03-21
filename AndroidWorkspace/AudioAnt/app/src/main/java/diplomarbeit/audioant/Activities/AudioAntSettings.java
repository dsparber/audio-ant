package diplomarbeit.audioant.Activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import diplomarbeit.audioant.Fragments.ShowTextAlert;
import diplomarbeit.audioant.Model.Classes.SoundListItem;
import diplomarbeit.audioant.Model.Helper.Settings;
import diplomarbeit.audioant.Model.Helper.WifiHelper;
import diplomarbeit.audioant.Model.Services.CommunicationService;
import diplomarbeit.audioant.R;

public class AudioAntSettings extends AppCompatActivity {

    private final String TAG = "AA_SETTINGS";
    private CheckBox checkBox_lightSignals;
    private CheckBox checkBox_audioSignals;
    private WifiHelper wifiHelper;
    private EditText wlanName;
    private Settings settings;
    private boolean lightSignals;
    private boolean audioSignals;
    private MediaPlayer player;
    private TextView textView_ChosenSound;
    private int alertSoundId = 0;
    private ArrayAdapter<SoundListItem> arrayAdapter;
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
    private BroadcastReceiver getAllAlertSounds = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                JSONObject jsonObject = new JSONObject(intent.getStringExtra("json"));
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray sounds = data.getJSONArray("sounds");
                deleteExistingAlerts();
                for (int i = 0; i < sounds.length(); i++) {
                    JSONObject sound = (JSONObject) sounds.get(i);
                    String name = sound.getString("name");
                    String fileName = sound.getString("fileName");
                    int number = sound.getInt("number");
                    String fileContent = sound.getString("fileContent");
                    String fullFileName = number + "-" + name + "-" + fileName;

                    saveAlert(fullFileName, fileContent);
                }
                settings.setSoundsLoaded(true);
                readAlerts();

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

    public void requestSoundsIfFirstStart() {
        settings = new Settings(this);
        if (!settings.getSoundsLoaded()) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("action", "getAlertSounds");
                communicationService.sendText(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            readAlerts();
        }
    }

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

        final ListView listView = new ListView(this);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setSelection(position);
                SoundListItem chosenAlert = arrayAdapter.getItem(position);
                alertSoundId = chosenAlert.getNumber();
                textView_ChosenSound.setText(chosenAlert.getName());
                playAlertFromName(chosenAlert.getName());
            }
        });
        listView.setSelection(alertSoundId);
        chooseAlertDialog.setView(listView);
        chooseAlertDialog.show();
    }

    public void playAlertFromName(String name) {
        File folder = new File(new File(Environment.getExternalStorageDirectory(), ".AudioAnt"), "Alerts");
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            String[] items = files[i].getName().split("-");
            if (items[1].equals(name)) {
                try {
                    if (player != null) {
                        player.release();
                        player = null;
                    }
                    player = new MediaPlayer();
                    player.setDataSource(files[i].getAbsolutePath());
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void deleteExistingAlerts() {
        File dir = new File(new File(Environment.getExternalStorageDirectory(), ".AudioAnt"), "Alerts");
        deleteRecursive(dir);
    }

    public void saveAlert(String fileName, String fileContent) {
        File dir = new File(new File(Environment.getExternalStorageDirectory(), ".AudioAnt"), "Alerts");
        dir.mkdirs();
        File f = new File(dir, fileName);

        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = Base64.decode(fileContent, Base64.NO_WRAP);
        try {
            FileOutputStream fos = new FileOutputStream(f.getAbsoluteFile());
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Alert saved from json");
    }

    public void readAlerts() {
        File folder = new File(new File(Environment.getExternalStorageDirectory(), ".AudioAnt"), "Alerts");
        File[] files = folder.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String[] items = files[i].getName().split("-");
                int number = Integer.parseInt(items[0]);
                String name = items[1];
                arrayAdapter.add(new SoundListItem(name, number));
                Log.d(TAG, "Found file " + files[i].getName());
            }
        }
    }

    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
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
        textView_ChosenSound = (TextView) findViewById(R.id.audioAnt_settings_textView_chosen_sound);

        LocalBroadcastManager.getInstance(this).registerReceiver(currentSettingsReceiver, new IntentFilter("currentSettings"));
        LocalBroadcastManager.getInstance(this).registerReceiver(getAllAlertSounds, new IntentFilter("alertSounds"));
        arrayAdapter = new ArrayAdapter<>(
                AudioAntSettings.this,
                android.R.layout.select_dialog_singlechoice);
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
