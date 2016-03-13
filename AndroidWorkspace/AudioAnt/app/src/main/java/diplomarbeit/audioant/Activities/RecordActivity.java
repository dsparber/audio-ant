package diplomarbeit.audioant.Activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

import diplomarbeit.audioant.Fragments.ShowTextAlert;
import diplomarbeit.audioant.Model.Helper.Settings;
import diplomarbeit.audioant.Model.Services.RecordSignalService;
import diplomarbeit.audioant.R;

public class RecordActivity extends AppCompatActivity {
    private String recordingHelpText;
    private String recordingHelpTextHeader;
    private Settings settings;
    private TextView textView_ChosenSound;
    private TextView textView_displayTime;
    private Uri uri_sound;
    private Button button_record;
    private Button button_play;
    private Button button_save;
    private MediaPlayer player;
    private String TAG = "FDBCK_REPLAY";
    private EditText geräuschName;
    private Thread timeThread;
    private boolean geräuschSchonAufgenommen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initElements();
    }

    public void initElements() {
        recordingHelpText = getResources().getString(R.string.recording_description);
        recordingHelpTextHeader = getResources().getString(R.string.recording_description_header);
        settings = new Settings(this);
        textView_ChosenSound = (TextView) findViewById(R.id.record_textView_sound_chosen);
        setChosenSoundToTextView(settings.getCurrentSound());
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
    }

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

    public void setChosenSoundToTextView(Uri uri) {
        Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
        String title = ringtone.getTitle(this);
        textView_ChosenSound.setText(title);
    }

    public void showRecordingHelp(View v) {
        ShowTextAlert textAlert = new ShowTextAlert();
        textAlert.setText(recordingHelpText);
        textAlert.setHeader(recordingHelpTextHeader);
        textAlert.show(getFragmentManager(), "recording help");
    }

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
            changeButtonText(button_play);
            resetTimeOfTextView();
            startTimeThread();
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

    public String getAbsoluteFileLocation() {
        File dir = new File(Environment.getExternalStorageDirectory(), ".AudioAnt");
        return dir.getAbsolutePath() + "/Signalton.mp3";

    }

    public void showNotificationDialog(View v) {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Benachrichtigungston auswählen");
        if (uri_sound != null) {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, uri_sound);
        } else {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, settings.getCurrentSound());
        }
        this.startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (uri != null) {
                uri_sound = uri;
                setChosenSoundToTextView(uri);
            }
        }
    }
}
