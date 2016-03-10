package diplomarbeit.audioant.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import diplomarbeit.audioant.Fragments.ShowTextAlert;
import diplomarbeit.audioant.Model.Helper.WifiHelper;
import diplomarbeit.audioant.R;

public class AudioAntSettings extends AppCompatActivity {

    private String wlanHelpText;
    private String wlanHelpTextHeader;
    private SeekBar seekBar_lautstärke;
    private WifiHelper wifiHelper;
    private EditText wlanName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_ant_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Demo using Toast
        seekBar_lautstärke = (SeekBar) findViewById(R.id.seekBar_lautstärke);
        seekBar_lautstärke.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int lautstärke = seekBar_lautstärke.getProgress();
                Toast.makeText(getApplicationContext(), ("Lautstärke: " + lautstärke), Toast.LENGTH_SHORT).show();
            }
        });

        wifiHelper = new WifiHelper(this);
        wlanName = (EditText) findViewById(R.id.editText_wifi_name);
        wlanName.setText(wifiHelper.getSSID());
        //continue here

    }

    public void buttonClicked(View v) {
        Button button = (Button) v;
        switch (button.getId()) {
            case R.id.button_wlan_einstellungen_öffnen:
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                break;
            case R.id.button_wlan_infos_senden:
                Toast.makeText(getApplicationContext(), wifiHelper.getSSID(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //Demo using Toast
    public void checkBoxClicked(View v) {
        CheckBox checkBox = (CheckBox) v;
        switch (checkBox.getId()) {
            case R.id.checkBox_audiosignale:
                Toast.makeText(getApplicationContext(), ("Audiosignale " + checkBox.isChecked()), Toast.LENGTH_SHORT).show();
                break;
            case R.id.checkBox_lichtsignale:
                Toast.makeText(getApplicationContext(), ("Lichtsignale: " + checkBox.isChecked()), Toast.LENGTH_SHORT).show();
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
