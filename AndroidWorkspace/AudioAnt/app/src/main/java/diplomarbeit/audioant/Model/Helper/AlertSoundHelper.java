package diplomarbeit.audioant.Model.Helper;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Base64;
import android.widget.ArrayAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import diplomarbeit.audioant.Model.Classes.SoundListItem;


/**
 * Created by Benjamin on 25.03.2016.
 */
public class AlertSoundHelper {

    private MediaPlayer player;

    public AlertSoundHelper() {

    }


    //  Methods concerning the alert sounds the app can receive and play
    public void playAlertFromName(String name) {
        File folder = new File(new File(Environment.getExternalStorageDirectory(), ".AudioAnt"), "Alerts");
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            String[] items = files[i].getName().split("-");
            if (items[1].equals(name)) {
                try {
                    if (player != null) {
                        player.release();
                        player = null;
                    }
                    player = new MediaPlayer();
                    player.setDataSource(files[i].getAbsolutePath());
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void deleteExistingAlerts() {
        File dir = new File(new File(Environment.getExternalStorageDirectory(), ".AudioAnt"), "Alerts");
        deleteRecursive(dir);
    }

    public void saveAlert(String fileName, String fileContent) {
        File dir = new File(new File(Environment.getExternalStorageDirectory(), ".AudioAnt"), "Alerts");
        dir.mkdirs();
        File f = new File(dir, fileName);

        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = Base64.decode(fileContent, Base64.NO_WRAP);
        try {
            FileOutputStream fos = new FileOutputStream(f.getAbsoluteFile());
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readAlerts(ArrayAdapter<SoundListItem> arrayAdapterSounds) {
        File folder = new File(new File(Environment.getExternalStorageDirectory(), ".AudioAnt"), "Alerts");
        File[] files = folder.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String[] items = files[i].getName().split("-");
                int number = Integer.parseInt(items[0]);
                String name = items[1];
                arrayAdapterSounds.add(new SoundListItem(name, number));
            }
        }
    }

    public void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }


}
