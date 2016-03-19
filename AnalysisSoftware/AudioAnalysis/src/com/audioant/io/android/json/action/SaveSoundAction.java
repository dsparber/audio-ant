package com.audioant.io.android.json.action;

import org.json.simple.JSONObject;

import com.audioant.io.android.json.JsonAction;
import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.SaveSound;

public class SaveSoundAction extends JsonAction {

	public SaveSoundAction(JSONObject request) {
		super(request);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getReply() {

		JSONObject jsonObject = new JSONObject();

		jsonObject.put(JsonFields.ACTION_KEY, SaveSound.Reply.ACTION_VALUE);

		return jsonObject;
	}

}
