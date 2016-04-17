package diplomarbeit.audioant.Model.Helper;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

/**
 * Created by Benjamin on 10.03.2016.
 */
public class WifiHelper {

    private final String TAG = "WIFI_HELPER";
    private WifiManager wifiManager;
    private Context context;

    public WifiHelper(Context context) {
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }


    public void connectToWifi(String ssid, String password) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "\"" + ssid + "\"";
        wifiConfiguration.preSharedKey = "\"" + password + "\"";
        wifiConfiguration.status = WifiConfiguration.Status.ENABLED;

        //wait untli wifi is turned on when started with wifi turned off
        while (!wifiManager.isWifiEnabled()) {
            try {
                Thread.sleep(50);
                Log.d(TAG, "had to wait");
            } catch (InterruptedException e) {

            }
        }

        int netId = -1;

        //Damit netzwerke nicht doppelt auftauchen
        List<WifiConfiguration> bekannteNetzwerke = wifiManager.getConfiguredNetworks();
        for (int i = 0; i < bekannteNetzwerke.size(); i++) {
            if (bekannteNetzwerke.get(i).SSID.equals("\"" + ssid + "\"")) {
                netId = bekannteNetzwerke.get(i).networkId;
            }
        }
        if (netId == -1) {
            netId = wifiManager.addNetwork(wifiConfiguration);
        }
        wifiManager.enableNetwork(netId, true);
        wifiManager.setWifiEnabled(true);
    }

    public void setWifiEnabled(boolean enable) {
        wifiManager.setWifiEnabled(enable);
    }

    public void startScan() {
        wifiManager.startScan();
    }


    public boolean isConnected() {
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isWifiEnabled() {
        return wifiManager.isWifiEnabled();
    }

    public String getCurrentNetworkSSID() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            return wifiInfo.getSSID();
        }
        return "";
    }

    public List<ScanResult> getScanResults() {
        return wifiManager.getScanResults();
    }

    public SupplicantState getSupplicantState() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSupplicantState();
    }


}
