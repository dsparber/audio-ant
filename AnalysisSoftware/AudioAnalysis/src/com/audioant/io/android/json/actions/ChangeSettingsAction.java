package com.audioant.io.android.json.actions;

import org.json.simple.JSONObject;

import com.audioant.io.android.json.JsonAction;
import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.ChangeSettings;

public class ChangeSettingsAction extends JsonAction {

	public ChangeSettingsAction(JSONObject request) {
		super(request);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getReply() {

		JSONObject jsonObject = new JSONObject();

		jsonObject.put(JsonFields.ACTION_KEY, ChangeSettings.Reply.ACTION_VALUE);

		return jsonObject;
	}

}
