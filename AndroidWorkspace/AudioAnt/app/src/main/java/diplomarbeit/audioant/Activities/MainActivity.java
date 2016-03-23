package diplomarbeit.audioant.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import diplomarbeit.audioant.Model.Services.CommunicationService;
import diplomarbeit.audioant.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";
    private static boolean serviceIsBound = false;
    private CommunicationService communicationService;
    private long start;
    private long stop;
    private TextView textView_audioant_connection_status;
    private ImageView imageView_verbunden;
    private AlertDialog loadingDialog;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("text")) {
                String text = intent.getStringExtra("text");
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        }
    };
    private BroadcastReceiver connectedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleConnectedReceived(context, intent);
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.MyBinder binder = (CommunicationService.MyBinder) service;
            communicationService = binder.getService();
            if (communicationService.isConnected()) {
                imageView_verbunden.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_24dp));
                textView_audioant_connection_status.setText(getResources().getString(R.string.main_label_connected));
            } else {
                imageView_verbunden.setImageDrawable(getResources().getDrawable(R.drawable.ic_wifi_not_connected_24dp));
                textView_audioant_connection_status.setText(getResources().getString(R.string.main_label_not_connected));
                showLoadingDialog();
            }
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
        showDialogIfNecessary();

        textView_audioant_connection_status = (TextView) findViewById(R.id.main_textView_verbunden);
        imageView_verbunden = (ImageView) findViewById(R.id.ic_verbunden);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("Test"));
        LocalBroadcastManager.getInstance(this).registerReceiver(connectedReceiver, new IntentFilter("verbunden"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindToCommunicationService();
    }



    public void handleConnectedReceived(Context context, Intent intent) {
        try {
            loadingDialog.dismiss();
            textView_audioant_connection_status.setText(getResources().getString(R.string.main_label_connected));
            imageView_verbunden.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_24dp));
        } catch (NullPointerException e) {
            Log.d(TAG, "could not dismiss dialog");
        }
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
        try {
            unbindService(serviceConnection);
            serviceIsBound = false;
        } catch (Exception e) {
            Log.d(TAG, "service could not be unbound because it wasn't bound");
        }
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

    public void showLoadingDialog() {
        loadingDialog = new ProgressDialog(MainActivity.this);
        loadingDialog.setMessage("Verbindung zu  Server wird aufgebaut....");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    public void startRecordActivity(View v) {
        unbindFromCommunicationService();
        Intent i = new Intent(MainActivity.this, RecordActivity.class);
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
