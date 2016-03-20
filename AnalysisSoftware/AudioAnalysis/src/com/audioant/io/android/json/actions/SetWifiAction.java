package com.audioant.io.android.json.actions;

import java.io.IOException;

import org.json.simple.JSONObject;

import com.audioant.io.android.json.JsonReplyAction;
import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.SetWifi.Reply;
import com.audioant.io.android.json.JsonFields.SetWifi.Request;
import com.audioant.io.raspberry.WifiController;

public class SetWifiAction extends JsonReplyAction {

	String ssid;
	String psk;

	public SetWifiAction(JSONObject request) {
		super(request);
		JSONObject data = (JSONObject) request.get(JsonFields.DATA_KEY);
		ssid = (String) data.get(Request.SSID_KEY);
		psk = (String) data.get(Request.PSK_KEY);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getReply() {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonFields.ACTION_KEY, Reply.ACTION_VALUE);

		JSONObject data = new JSONObject();

		boolean pskOk = psk.length() >= 8 && psk.length() < 64;
		boolean ssidOk = ssid.length() <= 32 && ssid.length() > 0;

		boolean success = pskOk && ssidOk;

		try {
			if (success) {
				WifiController.getInstance().addNetwork(ssid, psk);
			}
		} catch (IOException e) {
			success = false;
			e.printStackTrace();
		}

		data.put(Reply.SUCCESS_KEY, new Boolean(success));
		data.put(Reply.SSID_OK_KEY, new Boolean(ssidOk));
		data.put(Reply.PASSWORD_OK_KEY, new Boolean(pskOk));

		jsonObject.put(JsonFields.DATA_KEY, data);

		return jsonObject;
	}

}
