package diplomarbeit.audioant.Model.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class RecordSignalService extends Service {
    private MediaRecorder recorder;
    private String TAG = "SERVICE_RECORD";
    private boolean alreadyStarted = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//      Check if the service is already running and start recording if not
        if (!alreadyStarted) {
            alreadyStarted = true;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    recordToFile();
                }
            });
            t.run();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
//      executed when stopService(....) is called from any class
        super.onDestroy();
        stopRecording();
        Log.d(TAG, "Die Aufnahme wurde angehalten!");
    }


    public void recordToFile() {
//      Initialising the MediaRecorder and specifying recording properties
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setAudioEncodingBitRate(16);
        recorder.setAudioSamplingRate(16000);

//      Create the file the sound should be recorded to
        File f = new File(getBaseDir() + "/Signalton.mp3");
        if (f.exists()) {
            f.delete();
        }

        try {
            f.createNewFile();
            recorder.setOutputFile(f.getAbsolutePath());

//          Start recording audio to file
            recorder.prepare();
            recorder.start();


            Log.d(TAG, "Die Aufnahme wurde gestartet");
        } catch (IOException e) {
            Log.e(TAG, "Die Aufnahme konnte nicht gestartet werden", e);
        }
    }

    public void stopRecording() {
//      Aufnahme anhalten
        recorder.stop();
        recorder.release();
        recorder = null;
        Log.d(TAG, "Die Aufnahme wurde beendet");
    }


    //  returns the Directory the audio files are saved in, creates it first if necessary
    public File getBaseDir() {
        File dir = new File(Environment.getExternalStorageDirectory(), ".AudioAnt");
        dir.mkdirs();
        return dir;
    }
}
