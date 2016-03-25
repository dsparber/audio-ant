package diplomarbeit.audioant.Activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import diplomarbeit.audioant.Fragments.ShowTextAlert;
import diplomarbeit.audioant.Model.Classes.SoundListItem;
import diplomarbeit.audioant.Model.Helper.AlertSoundHelper;
import diplomarbeit.audioant.Model.Services.CommunicationService;
import diplomarbeit.audioant.R;

public class MoreInfoToSoundActivity extends AppCompatActivity {

    private AlertSoundHelper alertSoundHelper;
    private MediaPlayer player;
    private ArrayAdapter<SoundListItem> arrayAdapter;
    private int chosenAlertNumber;
    private boolean serviceIsBound = false;
    private TextView textView_chosenSound;
    private int soundId;

    private CommunicationService communicationService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.MyBinder binder = (CommunicationService.MyBinder) service;
            communicationService = binder.getService();
            serviceIsBound = true;
            try {
                JSONObject object = new JSONObject();
                object.put("action", "getMoreInfoToSound");
                object.put("data", soundId);
                communicationService.sendToServer(object.toString());
            } catch (JSONException e) {
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceIsBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info_to_sound);
        getSupportActionBar().setHomeButtonEnabled(true);

        soundId = getIntent().getIntExtra("soundId", -1);

        alertSoundHelper = new AlertSoundHelper();

        textView_chosenSound = (TextView) findViewById(R.id.more_info_textview_gewählt);


    }

    @Override
    protected void onResume() {
        super.onResume();
        bindToCommunicationService();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                unbindFromCommunicationService();
                super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        unbindFromCommunicationService();
        super.onBackPressed();
    }


    //  Methods for binding and unbinding to the communicationService
    public void bindToCommunicationService() {
        if (!serviceIsBound) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(getApplicationContext(), CommunicationService.class);
                    bindService(i, serviceConnection, 0);
                    startService(i);
                }
            });
            t.start();
        }
    }

    public void unbindFromCommunicationService() {
        try {
            unbindService(serviceConnection);
            serviceIsBound = false;
        } catch (Exception e) {
        }
    }


    //  Methods for showing dialogs
    public void showChooseAlertDialog(View v) {

        alertSoundHelper.readAlerts(arrayAdapter);

        final AlertDialog.Builder chooseAlertDialog = new AlertDialog.Builder(MoreInfoToSoundActivity.this);
        chooseAlertDialog.setTitle(getResources().getString(R.string.notification_choose_header));
        chooseAlertDialog.setNegativeButton("Fertig", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (player != null) {
                    player.release();
                    player = null;
                    dialog.dismiss();
                }
            }
        });

        final ListView listViewSounds = new ListView(this);
        listViewSounds.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listViewSounds.setAdapter(arrayAdapter);
        listViewSounds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listViewSounds.setSelection(position);
                SoundListItem chosenAlert = arrayAdapter.getItem(position);
                chosenAlertNumber = chosenAlert.getNumber();
                textView_chosenSound.setText(chosenAlert.getName());
                alertSoundHelper.playAlertFromName(chosenAlert.getName());
            }
        });
        chooseAlertDialog.setView(listViewSounds);
        chooseAlertDialog.show();
    }

    public void showMoreInfoHelp(View v) {
        ShowTextAlert textAlert = new ShowTextAlert();
        textAlert.setText(getResources().getString(R.string.more_info_description));
        textAlert.setHeader(getResources().getString(R.string.more_info_description_header));
        textAlert.show(getFragmentManager(), "notifications help");
    }

}
