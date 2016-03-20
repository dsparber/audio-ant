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
import android.view.View;
import android.widget.Toast;

import diplomarbeit.audioant.Model.Services.CommunicationService;
import diplomarbeit.audioant.R;

public class MainActivity extends AppCompatActivity {

    private CommunicationService communicationService;
    private static boolean serviceIsBound = false;
    private long start;
    private long stop;
    private static final String TAG = "MAIN_ACTIVITY";

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("text")) {
                String text = intent.getStringExtra("text");
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.MyBinder binder = (CommunicationService.MyBinder) service;
            communicationService = binder.getService();
            stop = System.currentTimeMillis();
            Log.d(TAG, "" + (stop - start));
            Toast.makeText(MainActivity.this, "jetzt", Toast.LENGTH_SHORT).show();
            serviceIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceIsBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = System.currentTimeMillis();
        bindToCommunicationService();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("Test"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDialogIfNecessary();
    }

    public void bindToCommunicationService() {
        if (!serviceIsBound) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(MainActivity.this, CommunicationService.class);
                    bindService(i, serviceConnection, 0);
                    startService(i);
                }
            });
            t.start();
        }
    }

    public void unbindFromCommunicationService() {
        unbindService(serviceConnection);
        serviceIsBound = false;
    }

    public void sendTextToService(View v) {
        communicationService.sendText("Beispieltext");
    }

    public void showDialogIfNecessary() {
        Log.d(TAG, "Entered showDialogIfNecessary");
        Intent i = getIntent();
        if (i.hasExtra("message")) {
            String message = i.getStringExtra("message");
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Information");
            alert.setMessage(message);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.show();
        }
    }

    public void startRecordActivity(View v) {
        unbindFromCommunicationService();
        Intent i = new Intent(MainActivity.this, RecordActivity.class);
        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
    }

    public void startListActivity(View v) {
        unbindFromCommunicationService();
        startActivity(new Intent(MainActivity.this, ListActivity.class));
    }

    public void startSettingsActivity(View v) {
        unbindFromCommunicationService();
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    public void startAudioAntSettingsActivity(View v) {
        unbindFromCommunicationService();
        startActivity(new Intent(MainActivity.this, AudioAntSettings.class));
    }


}
