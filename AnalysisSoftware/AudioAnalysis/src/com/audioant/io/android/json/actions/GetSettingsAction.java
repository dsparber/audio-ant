package com.audioant.io.android.json.actions;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.audioant.alert.AlertSettings;
import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.GetSettings;
import com.audioant.io.android.json.JsonReplyAction;

public class GetSettingsAction extends JsonReplyAction {

	public GetSettingsAction(JSONObject request) {
		super(request);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getReply() {

		JSONObject jsonObject = new JSONObject();

		jsonObject.put(JsonFields.ACTION_KEY, GetSettings.Reply.ACTION_VALUE);

		try {
			jsonObject.put(JsonFields.DATA_KEY, AlertSettings.getInstance().getJson());
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}

}
