package diplomarbeit.audioant.Activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
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
    private boolean serviceIsBound = false;
    private long start;
    private long stop;
    private static final String TAG = "MAIN_ACTIVITY";

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String text = intent.getStringExtra("text");
            Log.d(TAG, "Received Broadcast");
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.MyBinder binder = (CommunicationService.MyBinder) service;
            communicationService = binder.getService();
            stop = System.currentTimeMillis();
            Log.d("ASDF", "" + (stop - start));
            Toast.makeText(MainActivity.this, "jetzt", Toast.LENGTH_SHORT).show();
            serviceIsBound = true;

            Toast.makeText(MainActivity.this, communicationService.getText(), Toast.LENGTH_SHORT).show();
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
        findViewById(R.id.button_lernen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecordActivity.class));
            }
        });
        findViewById(R.id.button_alle_zeigen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        });
        findViewById(R.id.button_app_einstellungen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), 1);
            }
        });
        findViewById(R.id.button_einstellungen_audioant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AudioAntSettings.class));
            }
        });

        findViewById(R.id.main_textView_verbunden).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        start = System.currentTimeMillis();
        bindToCommunicationService();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("Networkstuff"));
    }

    public void bindToCommunicationService() {
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
