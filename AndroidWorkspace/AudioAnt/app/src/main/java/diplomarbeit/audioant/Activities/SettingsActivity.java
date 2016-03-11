package diplomarbeit.audioant.Activities;

import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import diplomarbeit.audioant.Fragments.ShowTextAlert;
import diplomarbeit.audioant.Model.Helper.Settings;
import diplomarbeit.audioant.R;

public class SettingsActivity extends AppCompatActivity {


//    Next change name of notification

    private LinearLayout linearLayout_choose_notification;
    private Settings settings;
    private CheckBox checkBox_led;
    private CheckBox checkBox_audio;
    private TextView textView_chosen_sound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initElements();
        initValuesFromSettings();
        hideElementsIfNecessary();
    }

    private void initElements() {
        linearLayout_choose_notification = (LinearLayout) findViewById(R.id.settings_linearlayout_choose_notification);
        settings = new Settings(this);
        checkBox_audio = (CheckBox) findViewById(R.id.settings_checkBox_signalton);
        checkBox_led = (CheckBox) findViewById(R.id.settings_checkBox_led);
        textView_chosen_sound = (TextView) findViewById(R.id.settings_textView_ton_gewählt);
    }

    public void initValuesFromSettings() {
        checkBox_audio.setChecked(settings.getUseAudioSignal());
        checkBox_led.setChecked(settings.getUseFlash());
        if (!settings.getCurrentSound().toString().equals("")) {
            textView_chosen_sound.setText((settings.getCurrentSound().toString()));
        }
    }

    public void hideElementsIfNecessary() {
        if (!checkBox_audio.isChecked()) linearLayout_choose_notification.setVisibility(View.GONE);
    }

    public void showInfo(View v) {
        ShowTextAlert textAlert = new ShowTextAlert();
        switch (v.getId()) {
            case R.id.settings_textView_benachrichtigungen_label_:
                textAlert.setText(getResources().getString(R.string.settings_notifications_description));
                textAlert.setHeader(getResources().getString(R.string.settings_notifications_description_header));
                textAlert.show(getFragmentManager(), "notifications help");
                break;
        }
    }

    //Demo using Toast
    public void checkBoxClicked(View v) {
        CheckBox checkBox = (CheckBox) v;
        switch (checkBox.getId()) {
            case R.id.settings_checkBox_signalton:
                Toast.makeText(getApplicationContext(), ("Warnton verwenden: " + checkBox.isChecked()), Toast.LENGTH_SHORT).show();
                settings.setUseAudio(checkBox.isChecked());
                if (checkBox.isChecked())
                    linearLayout_choose_notification.setVisibility(View.VISIBLE);
                else linearLayout_choose_notification.setVisibility(View.GONE);
                break;
            case R.id.settings_checkBox_led:
                settings.setUseFlash(checkBox.isChecked());
                Toast.makeText(getApplicationContext(), ("Blitz verwenden: " + checkBox.isChecked()), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void showNotificationDialog(View v) {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Benachrichtigungston auswählen");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        this.startActivityForResult(intent, 1);
    }

    //Handle the notification tone selected by the user
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (uri != null) {
                settings.setSound(uri);
                Toast.makeText(this, uri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
