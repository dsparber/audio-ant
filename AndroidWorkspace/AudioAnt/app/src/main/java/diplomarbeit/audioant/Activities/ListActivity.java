package diplomarbeit.audioant.Activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import diplomarbeit.audioant.Fragments.ShowTextAlert;
import diplomarbeit.audioant.Model.Services.CommunicationService;
import diplomarbeit.audioant.R;

public class ListActivity extends AppCompatActivity {

    private final String TAG = "LIST_ACTIVITY";
    private boolean serviceIsBound = false;
    private ListView listOfSounds;
    private ArrayList<SoundListItem> listItems = new ArrayList<SoundListItem>();
    private ArrayAdapter<SoundListItem> adapter;
    private CommunicationService communicationService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.MyBinder binder = (CommunicationService.MyBinder) service;
            communicationService = binder.getService();
            Toast.makeText(getApplicationContext(), "jetzt", Toast.LENGTH_SHORT).show();
            serviceIsBound = true;
            try {
                JSONObject object = new JSONObject();
                object.put("action", "getListOfSounds");
                communicationService.sendText(object.toString());
                Log.d(TAG, "list of sounds was requested");
            } catch (JSONException e) {
                Log.d(TAG, "Json could not be created");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceIsBound = false;
        }
    };
    private BroadcastReceiver soundDeletedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            listItems = new ArrayList<SoundListItem>();
            try {
                JSONObject object = new JSONObject();
                object.put("action", "getListOfSounds");
                communicationService.sendText(object.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private BroadcastReceiver listOfSoundsReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {
            try {
                listOfSounds = (ListView) findViewById(R.id.list);
                adapter = new ArrayAdapter<SoundListItem>(ListActivity.this, android.R.layout.simple_expandable_list_item_1, listItems);
                listOfSounds.setAdapter(adapter);
                listOfSounds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final SoundListItem selectedItem = listItems.get(position);
                        AlertDialog.Builder alert = new AlertDialog.Builder(ListActivity.this);
                        alert.setTitle("Ton löschen");
                        alert.setMessage("Wollen sie Das Geräusch \"" + selectedItem.getSoundName() + "\" wirklich löschen?");
                        alert.setNegativeButton("Nein", null);
                        alert.setPositiveButton("Ja, löschen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendDeleteSoundCommand(selectedItem.getNumber());

                            }
                        });
                        alert.show();
                    }
                });
                JSONObject j = new JSONObject(intent.getStringExtra("json"));
                JSONObject data = j.getJSONObject("data");
                JSONArray sounds = data.getJSONArray("sounds");
                for (int i = 0; i < sounds.length(); i++) {
                    JSONObject sound = sounds.getJSONObject(i);
                    SoundListItem item = new SoundListItem(sound.getString("name"), sound.getInt("number"));
                    listItems.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        bindToCommunicationService();
    }

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
            Log.d(TAG, "service could not be unbound because it wasn't bound");
        }
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

    public void showListHelp(View v) {
        ShowTextAlert textAlert = new ShowTextAlert();
        textAlert.setText(getResources().getString(R.string.list_description));
        textAlert.setHeader(getResources().getString(R.string.list_description_header));
        textAlert.show(getFragmentManager(), "notifications help");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_files);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LocalBroadcastManager.getInstance(this).registerReceiver(listOfSoundsReceiver, new IntentFilter("listOfSounds"));
        LocalBroadcastManager.getInstance(this).registerReceiver(soundDeletedReceiver, new IntentFilter("soundDeleted"));
    }

    private void sendDeleteSoundCommand(int soundNumber) {
        JSONObject object = new JSONObject();
        try {
            object.put("action", "deleteSound");
            object.put("data", soundNumber);
            communicationService.sendText(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class SoundListItem {
        private String soundName;
        private int number;

        public SoundListItem(String soundName, int number) {
            this.soundName = soundName;
            this.number = number;
        }

        public String getSoundName() {
            return soundName;
        }

        public int getNumber() {
            return number;
        }


        @Override
        public String toString() {
            return soundName;
        }
    }
}
