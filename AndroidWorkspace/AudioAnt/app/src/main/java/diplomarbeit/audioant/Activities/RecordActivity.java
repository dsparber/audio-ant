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
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import diplomarbeit.audioant.Fragments.ShowTextAlert;
import diplomarbeit.audioant.Model.Classes.SoundListItem;
import diplomarbeit.audioant.Model.Helper.AlertSoundHelper;
import diplomarbeit.audioant.Model.Helper.Settings;
import diplomarbeit.audioant.Model.Services.CommunicationService;
import diplomarbeit.audioant.Model.Services.RecordSignalService;
import diplomarbeit.audioant.R;

public class RecordActivity extends AppCompatActivity {
    private static boolean serviceIsBound = false;
    private AlertSoundHelper alertSoundHelper;
    private ProgressDialog waitingForResponse;
    private String recordingHelpText;
    private String recordingHelpTextHeader;
    private Settings settings;
    private TextView textView_ChosenSound;
    private TextView textView_displayTime;
    private Button button_record;
    private Button button_play;
    private Button button_save;
    private MediaPlayer player;
    private String TAG = "RECORDING_ACTIVITY";
    private EditText geräuschName;
    private Thread timeThread;
    private int chosenAlertNumber = 1;
    private ArrayAdapter<SoundListItem> arrayAdapter;
    private boolean geräuschSchonAufgenommen = false;
    private CommunicationService communicationService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.MyBinder binder = (CommunicationService.MyBinder) service;
            communicationService = binder.getService();
            requestSoundsIfFirstStart();
            Log.d(TAG, "Der service ist jetzt verbunden");
            serviceIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceIsBound = false;
        }
    };
    private BroadcastReceiver soundLearnedFeedbackReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {
            handleSoundLearnedFeedback(context, intent);
        }
    };
    private BroadcastReceiver getAllAlertSounds = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleGetAllAlertSounds(context, intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initElements();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindToCommunicationService();
    }

    @Override
    public void onBackPressed() {
        unbindFromCommunicationService();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                unbindFromCommunicationService();
                super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //  Initialisation methods
    public void initElements() {
        recordingHelpText = getResources().getString(R.string.recording_description);
        recordingHelpTextHeader = getResources().getString(R.string.recording_description_header);
        settings = new Settings(this);
        alertSoundHelper = new AlertSoundHelper();

        textView_ChosenSound = (TextView) findViewById(R.id.record_textView_sound_chosen);
        button_record = (Button) findViewById(R.id.record_button_start_recording);
        button_play = (Button) findViewById(R.id.record_button_replay);
        geräuschName = (EditText) findViewById(R.id.record_editText_geräuschname);
        button_save = (Button) findViewById(R.id.button_geräusch_speichern);
        textView_displayTime = (TextView) findViewById(R.id.record_textView_aufnahme_dauer);

        geräuschName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!geräuschName.getText().equals("") && geräuschSchonAufgenommen == true)
                    button_save.setEnabled(true);
                else button_save.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //has to be changed to soundLearnedFeedback
        LocalBroadcastManager.getInstance(this).registerReceiver(soundLearnedFeedbackReceiver, new IntentFilter("soundLearnedFeedback"));
        LocalBroadcastManager.getInstance(this).registerReceiver(getAllAlertSounds, new IntentFilter("alertSounds"));
        arrayAdapter = new ArrayAdapter<>(
                RecordActivity.this,
                android.R.layout.select_dialog_singlechoice);
        alertSoundHelper.readAlerts(arrayAdapter);
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
        }
    }


    //  Methods that implement what should happen on received Broadcasts or finished Timers
    public void handleSoundLearnedFeedback(Context context, Intent intent) {
        try {
            JSONObject j = new JSONObject(intent.getStringExtra("json"));
            JSONObject data = j.getJSONObject("data");

            final AlertDialog.Builder learnedStatusDialog = new AlertDialog.Builder(RecordActivity.this);
            learnedStatusDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    unbindFromCommunicationService();
                    Intent i = new Intent(RecordActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
            });

            if(data.getBoolean("successful")) {
                learnedStatusDialog.setMessage("Das Lernen war erfolgreich!");
                learnedStatusDialog.setTitle("Erfolg");
            } else {
                learnedStatusDialog.setMessage("Es konnte leider kein Ton erkannt werden.");
                learnedStatusDialog.setTitle("Fehler");
            }

            if (waitingForResponse != null)
                waitingForResponse.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    learnedStatusDialog.show();
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handleGetAllAlertSounds(Context context, Intent intent) {
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
            alertSoundHelper.readAlerts(arrayAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //  Methods that show dialogs
    public void showChooseAlertDialog(View v) {

        final AlertDialog.Builder chooseAlertDialog = new AlertDialog.Builder(RecordActivity.this);
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
                chosenAlertNumber = chosenAlert.getNumber();
                textView_ChosenSound.setText(chosenAlert.getName());
                alertSoundHelper.playAlertFromName(chosenAlert.getName());
            }
        });
        listView.setSelection(chosenAlertNumber);
        chooseAlertDialog.setView(listView);
        chooseAlertDialog.show();
    }

    public void showRecordingHelp(View v) {
        ShowTextAlert textAlert = new ShowTextAlert();
        textAlert.setText(recordingHelpText);
        textAlert.setHeader(recordingHelpTextHeader);
        textAlert.show(getFragmentManager(), "recording help");
    }


    //  Methods for time textView
    public void startTimeThread() {
        timeThread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTextViewTime();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        timeThread.start();
    }

    public void updateTextViewTime() {
        String unformattedTime = (String) textView_displayTime.getText();
        int minutes = Integer.parseInt(unformattedTime.split(":")[0]);
        int seconds = Integer.parseInt(unformattedTime.split(":")[1]) + 1;
        int time = minutes * 60 + seconds;
        minutes = time / 60;
        seconds = time % 60;
        String min = "" + minutes;
        String sec = "" + seconds;
        if (minutes < 10) min = "0" + min;
        if (seconds < 10) sec = "0" + sec;
        textView_displayTime.setText(min + ":" + sec);
    }

    public void resetTimeOfTextView() {
        textView_displayTime.setText("00:00");
    }


    //  OnClick Methods for buttons
    public void recordButtonClicked(View v) {
        geräuschSchonAufgenommen = true;
        if (button_record.getText().equals(getResources().getString(R.string.record_button_start_recording))) {
            resetTimeOfTextView();
            startTimeThread();
            startService(new Intent(RecordActivity.this, RecordSignalService.class));
        } else {
            stopService(new Intent(RecordActivity.this, RecordSignalService.class));
            timeThread.interrupt();
        }
        changeButtonText(button_record);
    }

    public void playButtonClicked(View v) {
        if (button_play.getText().equals(getResources().getString(R.string.record_button_start_replay))) {
            player = new MediaPlayer();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer player) {
                    try {
                        player.release();
                        player = null;
                        changeButtonText(button_play);
                        timeThread.interrupt();
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Der MediaPlayer wurde bereits händisch geschlossen
                    }
                }
            });

            try {
                player.setDataSource(getAbsoluteFileLocation());
                player.prepare();
                player.start();

                changeButtonText(button_play);
                resetTimeOfTextView();
                startTimeThread();
            } catch (Exception e) {
                Log.e(TAG, "could not play audio", e);
            }
        } else {
            try {
                changeButtonText(button_play);
                player.release();
                player = null;
                timeThread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
                //Der MediaPlayer wurde automatisch wieder geschlossen
            }

        }
    }

    public void sendButtonClicked(View v) {

        showWaitingForReplyInfo();

        JSONObject object = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("fileContent", fileToString(new File(getAbsoluteFileLocation())));
            data.put("name", geräuschName.getText());
            data.put("fileExtension", ".mp3");
            data.put("alertId", chosenAlertNumber);

            object.put("action", "saveSound");
            object.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        communicationService.sendToServer(object.toString());
    }

    public void changeButtonText(Button b) {
        switch (b.getId()) {
            case R.id.record_button_start_recording:
                if (b.getText().equals(getResources().getString(R.string.record_button_start_recording))) {
                    b.setText(getResources().getString(R.string.record_button_stop_recording));
                } else b.setText(getResources().getString(R.string.record_button_start_recording));
                break;
            case R.id.record_button_replay:
                if (b.getText().equals(getResources().getString(R.string.record_button_start_replay))) {
                    b.setText(getResources().getString(R.string.record_button_stop_replay));
                } else b.setText(getResources().getString(R.string.record_button_start_replay));
                break;
        }
    }


    //  Methods concerning files
    public String fileToString(File file) {

//      Converts the audio File to a byte array
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            FileInputStream fs = new FileInputStream(file);
            fs.read(bytes);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "Die datei zum umwandeln existiert nicht");
        } catch (IOException e) {
            Log.d(TAG, "Die Datei konnte nicht als byte[] gespeichert werden");
        }

//      Decodes the byteArray into a String and returns it
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public String getAbsoluteFileLocation() {
        File dir = new File(Environment.getExternalStorageDirectory(), ".AudioAnt");
        return dir.getAbsolutePath() + "/Signalton.mp3";

    }


    //  Methods for binding and unbinding to the communicationService
    public void bindToCommunicationService() {
        if (!serviceIsBound) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(RecordActivity.this, CommunicationService.class);
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

    private void showWaitingForReplyInfo() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                waitingForResponse = new ProgressDialog(RecordActivity.this);
                waitingForResponse.setMessage("Der aufgenommene Ton wird analysiert");
                waitingForResponse.setCancelable(false);
                waitingForResponse.show();
            }
        });
    }


}
