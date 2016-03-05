package diplomarbeit.audioant.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import diplomarbeit.audioant.Fragments.ShowTextAlert;
import diplomarbeit.audioant.R;

public class RecordActivity extends AppCompatActivity {
    String recordingHelpText = "Um ein Geräusch aufzunehmen befolgen Sie die folgenden Schritte:\n" +
            "1. Drücken Sie auf die \"Geräusch aufnehmen\" Schaltfläche um die Aufnahme auf ihrem Gerät zu starten.\n" +
            "2. Verursachen Sie das Geräusch das gelernt werden soll, zum Beispiel durch drücken der Türklingel.\n" +
            "3. Betätigen Sie die Schaltfläche erneut um die Aufnahme zu beenden.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void showRecordingHelp(View v)
    {
        ShowTextAlert textAlert = new ShowTextAlert();
        textAlert.setText(recordingHelpText);
        textAlert.setHeader("Aufnahmehilfe");
        textAlert.show(getFragmentManager(), "recording help");
    }
}
