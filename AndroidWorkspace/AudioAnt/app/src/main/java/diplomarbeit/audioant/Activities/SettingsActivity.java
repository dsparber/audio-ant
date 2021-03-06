package diplomarbeit.audioant.Activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import diplomarbeit.audioant.Fragments.ShowTextAlert;
import diplomarbeit.audioant.Model.Helper.Settings;
import diplomarbeit.audioant.R;

public class SettingsActivity extends AppCompatActivity {
    private LinearLayout linearLayout_choose_notification;
    private Settings settings;
    private CheckBox checkBox_led;
    private CheckBox checkBox_vibration;
    private CheckBox checkBox_audio;
    private TextView textView_chosen_sound;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initElements();
        initValuesFromSettings();
        hideElementsIfNecessary();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (uri != null) {
                settings.setSound(uri);
                setChosenSoundToTextView();
            }
        }
    }


    //  Initialising methods
    private void initElements() {
        linearLayout_choose_notification = (LinearLayout) findViewById(R.id.settings_linearlayout_choose_notification);
        settings = new Settings(this);
        checkBox_audio = (CheckBox) findViewById(R.id.settings_checkBox_signalton);
        checkBox_led = (CheckBox) findViewById(R.id.settings_checkBox_led);
        checkBox_vibration = (CheckBox) findViewById(R.id.settings_checkbox_vibration);
        textView_chosen_sound = (TextView) findViewById(R.id.settings_textView_sound_chosen);
    }

    public void initValuesFromSettings() {
        checkBox_audio.setChecked(settings.getUseAudioSignal());
        checkBox_led.setChecked(settings.getUseFlash());
        checkBox_vibration.setChecked(settings.getUseVibration());
        if (!settings.getCurrentSound().toString().equals("")) {
            setChosenSoundToTextView();
        }
    }


    //  Methods that show dialogs
    public void showInfo(View v) {
        ShowTextAlert textAlert = new ShowTextAlert();
        textAlert.setText(getResources().getString(R.string.settings_notifications_description));
        textAlert.setHeader(getResources().getString(R.string.settings_notifications_description_header));
        textAlert.show(getFragmentManager(), "notifications help");
    }

    public void showNotificationDialog(View v) {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Benachrichtigungston auswählen");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, settings.getCurrentSound());

        this.startActivityForResult(intent, 1);
    }


    //  Updating views methods
    public void setChosenSoundToTextView() {
        Ringtone ringtone = RingtoneManager.getRingtone(this, settings.getCurrentSound());
        String title = ringtone.getTitle(this);
        textView_chosen_sound.setText(title);
    }

    public void hideElementsIfNecessary() {
        if (!checkBox_audio.isChecked()) linearLayout_choose_notification.setVisibility(View.GONE);
    }


    //  Onclick Methods
    public void checkBoxClicked(View v) {
        CheckBox checkBox = (CheckBox) v;
        switch (checkBox.getId()) {
            case R.id.settings_checkBox_signalton:
                settings.setUseAudio(checkBox.isChecked());
                if (checkBox.isChecked())
                    linearLayout_choose_notification.setVisibility(View.VISIBLE);
                else linearLayout_choose_notification.setVisibility(View.GONE);
                break;
            case R.id.settings_checkBox_led:
                settings.setUseFlash(checkBox.isChecked());
                break;
            case R.id.settings_checkbox_vibration:
                settings.setUseVibration(checkBox.isChecked());
                break;
        }
    }


}
