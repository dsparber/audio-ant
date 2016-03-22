package diplomarbeit.audioant.Model.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * Created by Benjamin on 11.03.2016.
 */
public class Settings {
    private static final String FILE_KEY = "AudioAnt_Settings";
    private static final String ledKey = "USE_LED";
    private static final String audioKey = "USE_AUDIO";
    private static final String uriKey = "URI";
    private static final String soundsLoadedKey = "SOUNDS_LOADED";
    private static final String localWifiName = "WIFI_NAME";
    private SharedPreferences sharedPref;


    public Settings(Context context) {

        sharedPref = context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE);
    }


    public String getLocalWifiName() {
        return sharedPref.getString(localWifiName, "");
    }

    public void setLocalWifiName(String name) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(localWifiName, name);
        editor.commit();
    }

    public boolean getUseFlash() {
        return sharedPref.getBoolean(ledKey, false);
    }

    public void setUseFlash(boolean verwenden) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(ledKey, verwenden);
        editor.commit();
    }

    public boolean getSoundsLoaded() {
        return sharedPref.getBoolean(soundsLoadedKey, false);
    }

    public void setSoundsLoaded(boolean loaded) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(soundsLoadedKey, loaded);
        editor.commit();
    }

    public boolean getUseAudioSignal() {
        return sharedPref.getBoolean(audioKey, false);
    }

    public Uri getCurrentSound() {
        return Uri.parse(sharedPref.getString(uriKey, ""));
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
