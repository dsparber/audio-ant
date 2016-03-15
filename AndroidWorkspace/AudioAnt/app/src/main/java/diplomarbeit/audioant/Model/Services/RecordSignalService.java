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

//      Check if the service is already running
        if (alreadyStarted) {
            Log.d(TAG, "Die Aufnahme laeuft bereits");
        } else {
            alreadyStarted = true;
            recordToFile();
        }
        return START_NOT_STICKY;
    }

    public void recordToFile() {
//      Initialising the MediaRecorder and specifying recording properties
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setAudioEncodingBitRate(16);
        recorder.setAudioSamplingRate(44100);
        File f = new File(getBaseDir() + "/Signalton.mp3");
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
            recorder.setOutputFile(f.getAbsolutePath());
//          Start recording and save to file
            recorder.prepare();
            recorder.start();
            Log.d(TAG, "Der Service laeuft bereits!");
        } catch (IOException e) {
            Log.e(TAG, "Die Aufnahme konnte nicht gestartet werden", e);
        }
    }

    @Override
    public void onDestroy() {
//      called when stopService(....) is invoked from any class
        super.onDestroy();
        stopRecording();
        Log.d(TAG, "Die Aufnahme wurde angehalten!");
    }

    public void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        Log.d(TAG, "Die Aufnahme wurde beendet");
    }

//  returns the Directory the audio files are saved in as a file
    public static File getBaseDir() {
        File dir = new File(Environment.getExternalStorageDirectory(), ".AudioAnt");
//      Create directory if necessary
        dir.mkdirs();
        return dir;
    }
}
