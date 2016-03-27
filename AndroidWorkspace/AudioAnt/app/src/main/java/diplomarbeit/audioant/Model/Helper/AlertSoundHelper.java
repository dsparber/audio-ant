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

//      Get all learned sounds in directory
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
//          Check if this is the sound that should be played (by name)
            String[] items = files[i].getName().split("-");
            if (items[1].equals(name)) {
                try {
                    if (player != null) {
                        player.release();
                        player = null;
                    }
//                  start playing the sound
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
//      All alerts that might exist from previous application versions are deleted
        File dir = new File(new File(Environment.getExternalStorageDirectory(), ".AudioAnt"), "Alerts");
        deleteRecursive(dir);
    }

    public void saveAlert(String fileName, String fileContent) {
//      The directory is created
        File dir = new File(new File(Environment.getExternalStorageDirectory(), ".AudioAnt"), "Alerts");
        dir.mkdirs();
//      The file the alert should be saved in is created
        File f = new File(dir, fileName);

//      Deletes the alert if it already exists
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
//      The audio data of the file is converted to a byte array
        byte[] data = Base64.decode(fileContent, Base64.NO_WRAP);
        try {
//          The byte array is written to the file
            FileOutputStream fos = new FileOutputStream(f.getAbsoluteFile());
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readAlerts(ArrayAdapter<SoundListItem> arrayAdapterSounds) {
//      Get the directory
        File folder = new File(new File(Environment.getExternalStorageDirectory(), ".AudioAnt"), "Alerts");
//      Get all files in that directory
        File[] files = folder.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
//              add the file name to the ArrayAdapter passed at method call
                String[] items = files[i].getName().split("-");
                int number = Integer.parseInt(items[0]);
                String name = items[1];
                arrayAdapterSounds.add(new SoundListItem(name, number));
            }
        }
    }

    public void deleteRecursive(File fileOrDirectory) {
//      Deletes a directory and all its files via recursive call
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }


}
