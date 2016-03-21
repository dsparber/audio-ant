package com.audioant.io.android.json.actions;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.audioant.alert.AlertSettings;
import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.ChangeSettings.Reply;
import com.audioant.io.android.json.JsonReplyAction;

public class ChangeSettingsAction extends JsonReplyAction {

	boolean success;

	public ChangeSettingsAction(JSONObject request) {
		super(request);

		success = true;

		JSONObject data = (JSONObject) request.get(JsonFields.DATA_KEY);
		try {
			AlertSettings.getInstance().setValues(data);
		} catch (IOException | ParseException e) {
			success = false;
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getReply() {

		JSONObject jsonObject = new JSONObject();

		jsonObject.put(JsonFields.ACTION_KEY, Reply.ACTION_VALUE);

		JSONObject data = new JSONObject();
		data.put(Reply.SUCCESS_KEY, success);
		jsonObject.put(JsonFields.DATA_KEY, data);

		return jsonObject;
	}

}
