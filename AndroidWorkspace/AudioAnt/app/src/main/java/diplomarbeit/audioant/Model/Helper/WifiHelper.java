package diplomarbeit.audioant.Model.Helper;

import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by Benjamin on 10.03.2016.
 */
public class WifiHelper {

    private WifiManager wifiManager;

    public WifiHelper(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public boolean isConnected() {
        return false;
    }

    public String getSSID() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            return wifiInfo.getSSID();
        }
        return "";
    }

    public void connectToWifi(String ssid, String password) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "\"" + ssid + "\"";
        wifiConfiguration.preSharedKey = "\"" + password + "\"";
        wifiConfiguration.status = WifiConfiguration.Status.ENABLED;

        //Damit netzwerke nicht doppelt auftauchen
        List<WifiConfiguration> bekannteNetzwerke = wifiManager.getConfiguredNetworks();
        for (int i = 0; i < bekannteNetzwerke.size(); i++) {
            if (bekannteNetzwerke.get(i).SSID.equals("\"" + ssid + "\"")) {
                int netId = bekannteNetzwerke.get(i).networkId;
                wifiManager.removeNetwork(netId);
            }
        }
        int netId = wifiManager.addNetwork(wifiConfiguration);
        wifiManager.enableNetwork(netId, true);
        wifiManager.setWifiEnabled(true);
    }

}
