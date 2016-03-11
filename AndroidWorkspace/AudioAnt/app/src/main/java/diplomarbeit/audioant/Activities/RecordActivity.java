package diplomarbeit.audioant.Activities;

import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

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
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Benachrichtigungston auswählen");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        this.startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (uri != null) {
                Toast.makeText(this, uri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
