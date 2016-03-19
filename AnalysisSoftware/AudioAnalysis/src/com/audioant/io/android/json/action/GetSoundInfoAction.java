package com.audioant.io.android.json.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import org.json.simple.JSONObject;

import com.audioant.audio.learning.LearnedSounds;
import com.audioant.audio.model.Sound;
import com.audioant.config.Config;
import com.audioant.io.android.json.JsonAction;
import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.GetSoundInfo;
import com.audioant.io.android.json.JsonFields.GetSoundInfo.Reply;

public class GetSoundInfoAction extends JsonAction {

	private int number;

	public GetSoundInfoAction(JSONObject request) {
		super(request);
		number = Integer.parseInt((String) request.get(JsonFields.DATA_KEY));
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getReply() {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonFields.ACTION_KEY, GetSoundInfo.Reply.ACTION_VALUE);

		JSONObject data = new JSONObject();

		Sound sound = LearnedSounds.getSounds().get(number);

		data.put(Reply.NAME_KEY, sound.getNameNotNull());
		data.put(Reply.NUMBER_KEY, sound.getNumber());
		data.put(Reply.ALERT, "Not implemented");

		File soundFile = new File(sound.getPath() + Config.LEARNED_SOUNDS_FILE);

		data.put(Reply.FILE_NAME_KEY, soundFile.getName());

		int size = (int) soundFile.length();
		byte[] bytes = new byte[size];
		try {
			FileInputStream fs = new FileInputStream(soundFile);
			fs.read(bytes);
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		data.put(Reply.FILE_CONTENT_KEY, Base64.getEncoder().encodeToString(bytes));

		jsonObject.put(JsonFields.DATA_KEY, data);

		return jsonObject;
	}
}
