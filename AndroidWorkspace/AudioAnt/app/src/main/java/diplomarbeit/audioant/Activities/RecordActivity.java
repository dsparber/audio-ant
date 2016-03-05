package diplomarbeit.audioant.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import diplomarbeit.audioant.Fragments.ChooseSoundAlert;
import diplomarbeit.audioant.Fragments.ShowTextAlert;
import diplomarbeit.audioant.R;

public class RecordActivity extends AppCompatActivity {
    String recordingHelpText;
    String recordingHelpTextHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recordingHelpText = getResources().getString(R.string.recording_description);
        recordingHelpTextHeader = getResources().getString(R.string.recording_description_header);

        setContentView(R.layout.activity_record);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void showRecordingHelp(View v) {
        ShowTextAlert textAlert = new ShowTextAlert();
        textAlert.setText(recordingHelpText);
        textAlert.setHeader(recordingHelpTextHeader);
        textAlert.show(getFragmentManager(), "recording help");
    }

    public void showNotificationDialog(View v) {
        ChooseSoundAlert soundAlert = new ChooseSoundAlert();
        soundAlert.show(getFragmentManager(), "choose_not");
    }
}
