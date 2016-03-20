package com.audioant.io.android.json.actions;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.audioant.alert.AlertSounds;
import com.audioant.audio.model.Sound;
import com.audioant.config.Config;
import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.GetAlertSounds.Reply;
import com.audioant.io.android.json.JsonReplyAction;
import com.audioant.tools.Base64Tool;

public class GetAlertSoundsAction extends JsonReplyAction {

	public GetAlertSoundsAction(JSONObject request) {
		super(request);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getReply() {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonFields.ACTION_KEY, Reply.ACTION_VALUE);

		JSONArray sounds = new JSONArray();

		for (Sound sound : AlertSounds.getAllSounds()) {

			JSONObject obj = new JSONObject();

			File f = new File(sound.getPath() + Config.ALERT_SOUNDS_FILE);

			obj.put(Reply.NAME_KEY, sound.getNameNotNull());
			obj.put(Reply.NUMBER_KEY, sound.getId());
			obj.put(Reply.FILE_NAME_KEY, f.getName());
			try {
				obj.put(Reply.FILE_CONTENT_KEY, Base64Tool.encode(f));
			} catch (IOException e) {
				e.printStackTrace();
			}

			sounds.add(obj);
		}

		JSONObject data = new JSONObject();
		data.put(Reply.SOUNDS_KEY, sounds);
		jsonObject.put(JsonFields.DATA_KEY, data);

		return jsonObject;
	}
}
