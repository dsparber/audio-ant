package com.audioant.io.android.json.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.audioant.alert.AlertSettings;
import com.audioant.audio.learning.LearnedSounds;
import com.audioant.audio.model.Sound;
import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.GetSoundInfo;
import com.audioant.io.android.json.JsonFields.GetSoundInfo.Reply;
import com.audioant.io.android.json.JsonReplyAction;

public class GetSoundInfoAction extends JsonReplyAction {

	private int number;

	public GetSoundInfoAction(JSONObject request) {
		super(request);
		number = (int) ((long) request.get(JsonFields.DATA_KEY));
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getReply() {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonFields.ACTION_KEY, GetSoundInfo.Reply.ACTION_VALUE);

		JSONObject data = new JSONObject();

		Sound sound = LearnedSounds.getSound(number);

		data.put(Reply.NAME_KEY, sound.getNameNotNull());
		data.put(Reply.NUMBER_KEY, sound.getId());

		Integer defaultAlertId = null;
		try {
			defaultAlertId = AlertSettings.getInstance().getAlertSoundId();
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}

		Integer alertId = (sound.getAlertId() != null) ? sound.getAlertId() : defaultAlertId;

		data.put(Reply.ALERT, alertId);

		File soundFile = new File(sound.getPath() + sound.getSoundFile());

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
