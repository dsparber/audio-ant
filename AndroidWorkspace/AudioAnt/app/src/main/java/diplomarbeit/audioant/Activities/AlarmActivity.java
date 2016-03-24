package diplomarbeit.audioant.Activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import diplomarbeit.audioant.Model.Helper.Settings;
import diplomarbeit.audioant.Model.Services.CommunicationService;
import diplomarbeit.audioant.R;

public class AlarmActivity extends AppCompatActivity {

    private final String TAG = "ALARM";
    private LinearLayout layout;
    private Settings settings;
    private Thread flashThread;

    private boolean serviceIsBound = false;
    private boolean continueFlash = true;
    private boolean continueVibration = true;
    private boolean continueSound = true;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private CommunicationService communicationService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CommunicationService.MyBinder binder = (CommunicationService.MyBinder) service;
            communicationService = binder.getService();
            serviceIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceIsBound = false;
        }
    };
    private BroadcastReceiver alertHandledByAudioAntReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopAlarmingMethods();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        layout = (LinearLayout) findViewById(R.id.alarm_linear_layout);
        settings = new Settings(this);

        LocalBroadcastManager.getInstance(AlarmActivity.this).registerReceiver(alertHandledByAudioAntReceiver, new IntentFilter("alertConfirmed"));

        Intent i = getIntent();
        initialiseElements(i);
        startNotifications();
    }

    @Override
    protected void onResume() {
        bindToCommunicationService();
        super.onResume();
    }

    @Override
    protected void onPause() {
        unbindFromCommunicationService();
        super.onPause();
    }


    //  Initialisation Methods
    private void initialiseElements(Intent intent) {
        try {
            JSONObject object = new JSONObject(intent.getStringExtra("json"));
            JSONObject data = object.getJSONObject("data");
            JSONArray sounds = data.getJSONArray("sounds");
            createSoundNameTextViews(sounds);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startNotifications() {
        if (settings.getUseFlash()) {
            if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                doFlashBlink();
            }
        }

        if (settings.getUseAudioSignal()) {
            if (!settings.getCurrentSound().toString().equals("")) {
                playAlertSound();
            }
        }

        if (settings.getUseVibration()) {
            startVibrating();
        }
    }

    private void createSoundNameTextViews(JSONArray sounds) throws JSONException {
        for (int i = 0; i < sounds.length(); i++) {
            JSONObject alarm = sounds.getJSONObject(i);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;

            TextView textView = new TextView(this);
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            textView.setText(alarm.getString("name") + " (Nummer: " + alarm.getInt("number") + ")");
            textView.setTextSize(getResources().getDimension(R.dimen.text_size_xlarge));
            textView.setTextAppearance(this, android.R.style.TextAppearance_Large);
            textView.setLayoutParams(layoutParams);

            layout.addView(textView, 0);
        }
    }


    //  Thread logic
    public void doFlashBlink() {
        flashThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (continueFlash) {
                    try {
                        Camera cam = Camera.open();
                        Camera.Parameters p = cam.getParameters();
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        cam.setParameters(p);
                        cam.startPreview();
                        Thread.sleep(500);
                        cam.stopPreview();
                        cam.release();
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        flashThread.start();
    }

    public void playAlertSound() {
        if (settings.getCurrentSound().toString() != "") {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(AlarmActivity.this, settings.getCurrentSound());
                        mediaPlayer.prepare();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                if (continueSound) {
                                    mediaPlayer.start();
                                }
                            }
                        });
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }

    public void startVibrating() {
        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (continueVibration) {
                    vibrator.vibrate(1000);
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();

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
            Log.d(TAG, "service could not be unbound because it wasn't bound");
        }
    }


    //  Server communication
    public void sendHandledJson() {
        try {
            JSONObject object = new JSONObject();
            object.put("action", "alertConfirmed");
            communicationService.sendToServer(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //  OnClick Methods
    public void buttonClicked(View v) {
        stopAlarmingMethods();
    }


    public void stopAlarmingMethods() {
        try {
            continueFlash = false;
            continueSound = false;
            continueVibration = false;

            while (!serviceIsBound) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            sendHandledJson();

            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            AlarmActivity.super.onDestroy();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}













