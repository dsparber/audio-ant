package diplomarbeit.audioant.Model.Helper;

import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Benjamin on 10.03.2016.
 */
public class WifiHelper {

    private WifiManager wifiMgr;

    public WifiHelper(Context context) {
        wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public boolean isConnected() {
        return false;
    }

    public String getSSID() {
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            return wifiInfo.getSSID();
        }
        return "";
    }

}
