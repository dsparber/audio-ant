package com.audioant.io.android.json.action;

import org.json.simple.JSONObject;

import com.audioant.io.android.json.JsonAction;
import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.SetWifi;

public class SetWifiAction extends JsonAction {

	public SetWifiAction(JSONObject request) {
		super(request);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getReply() {

		JSONObject jsonObject = new JSONObject();

		jsonObject.put(JsonFields.ACTION_KEY, SetWifi.Reply.ACTION_VALUE);

		return jsonObject;
	}

}
