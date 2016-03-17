package diplomarbeit.audioant.Model.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * Created by Benjamin on 11.03.2016.
 */
public class Settings {
    private SharedPreferences sharedPref;
    private static final String FILE_KEY = "AudioAnt_Settings";
    private static final String ledKey = "USE_LED";
    private static final String audioKey = "USE_AUDIO";
    private static final String uriKey = "URI";


    public Settings(Context context) {

        sharedPref = context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE);
    }

    public boolean getUseFlash() {
        return sharedPref.getBoolean(ledKey, false);
    }

    public boolean getUseAudioSignal() {
        return sharedPref.getBoolean(audioKey, false);
    }

    public Uri getCurrentSound() {
        return Uri.parse(sharedPref.getString(uriKey, ""));
    }


    public void setUseFlash(boolean verwenden) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(ledKey, verwenden);
        editor.commit();
    }

    public void setUseAudio(boolean verwenden) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(audioKey, verwenden);
        editor.commit();
    }

    public void setSound(Uri uri) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(uriKey, uri.toString());
        editor.commit();
    }
}