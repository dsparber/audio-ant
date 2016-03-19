package com.audioant.io.android.json.action;

import org.json.simple.JSONObject;

import com.audioant.io.android.json.JsonAction;
import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.DeleteSound;

public class DeleteSoundAction extends JsonAction {

	public DeleteSoundAction(JSONObject request) {
		super(request);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getReply() {

		JSONObject jsonObject = new JSONObject();

		jsonObject.put(JsonFields.ACTION_KEY, DeleteSound.Reply.ACTION_VALUE);

		return jsonObject;
	}

}
