package diplomarbeit.audioant.Activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import diplomarbeit.audioant.Model.Classes.SoundListItem;
import diplomarbeit.audioant.R;

public class MoreInfoToSoundActivity extends AppCompatActivity {

    private MediaPlayer player;
    private ArrayAdapter<SoundListItem> arrayAdapter;
    private int chosenAlertNumber;
    private TextView textView_chosenSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info_to_sound);
        getSupportActionBar().setHomeButtonEnabled(true);

        textView_chosenSound = (TextView) findViewById(R.id.more_info_textview_gewählt);


    }

}
