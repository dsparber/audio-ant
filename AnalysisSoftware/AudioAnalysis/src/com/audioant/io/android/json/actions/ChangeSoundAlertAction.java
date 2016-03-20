package com.audioant.io.android.json.actions;

import org.json.simple.JSONObject;

import com.audioant.io.android.json.JsonReplyAction;
import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.ChangeSoundAlert;

public class ChangeSoundAlertAction extends JsonReplyAction {

	public ChangeSoundAlertAction(JSONObject request) {
		super(request);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getReply() {

		JSONObject jsonObject = new JSONObject();

		jsonObject.put(JsonFields.ACTION_KEY, ChangeSoundAlert.Reply.ACTION_VALUE);

		return jsonObject;
	}

}
