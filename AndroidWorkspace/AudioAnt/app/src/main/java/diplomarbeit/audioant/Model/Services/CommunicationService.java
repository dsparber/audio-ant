package diplomarbeit.audioant.Model.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class CommunicationService extends Service {

    private static String TAG = "BOUND_SERICE";
    private IBinder binder = new MyBinder();
    private String text = "asgard";

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "the service was started");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    sendBroadcast();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        return START_STICKY;
    }

    public String getText() {
        return text;
    }

    private void sendBroadcast() {
        Intent intent = new Intent("Networkstuff");
        intent.putExtra("text", "broadcast");

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    public class MyBinder extends Binder {
        public CommunicationService getService() {
            return CommunicationService.this;
        }
    }
}