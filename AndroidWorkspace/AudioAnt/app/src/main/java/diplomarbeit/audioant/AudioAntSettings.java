package diplomarbeit.audioant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AudioAntSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_ant_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
