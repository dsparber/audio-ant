package diplomarbeit.audioant.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import diplomarbeit.audioant.Fragments.ShowTextAlert;
import diplomarbeit.audioant.R;

public class AudioAntSettings extends AppCompatActivity {

    String wlanHelpText;
    String wlanHelpTextHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_ant_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wlanHelpText = getResources().getString(R.string.wlan_description);
        wlanHelpTextHeader = getResources().getString(R.string.wlan_description_header);



    }

    public void showWlanHelp(View v) {
        ShowTextAlert textAlert = new ShowTextAlert();
        textAlert.setText(wlanHelpText);
        textAlert.setHeader(wlanHelpTextHeader);
        textAlert.show(getFragmentManager(), "wlan help");
    }
}
