package diplomarbeit.audioant.Model.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by benni on 12.3.2016.
 */
public class RecordSignalService extends Service {
    private MediaRecorder recorder;
    private String TAG = "RECORDER";
    private boolean alreadyStarted = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SERVICE", "GESTARTET");
        if (alreadyStarted) {
            Toast.makeText(this, "Der Service läuft bereits!", Toast.LENGTH_SHORT).show();
        } else {
            alreadyStarted = true;
            Toast.makeText(RecordSignalService.this, "The service was started", Toast.LENGTH_SHORT).show();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    File f = recordToFile();
                    try {
                        Thread.sleep(10000);
                        stopRecording();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            Thread thread = new Thread(r);
            thread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
        Toast.makeText(RecordSignalService.this, "The service was destroyed", Toast.LENGTH_SHORT).show();
    }

    public File recordToFile() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        File f = new File(getBaseDir() + "/Signalton.mp3");
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
            recorder.setOutputFile(f.getAbsolutePath());
            recorder.prepare();
            recorder.start();
            return f;
        } catch (IOException e) {
            Log.e(TAG, "could not start recording", e);
        }
        return null;
    }

    public void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    public static File getBaseDir() {
        File dir = new File(Environment.getExternalStorageDirectory(), ".AudioAnt");
        // ggf. Verzeichnisse anlegen
        dir.mkdirs();
        return dir;
    }
}
