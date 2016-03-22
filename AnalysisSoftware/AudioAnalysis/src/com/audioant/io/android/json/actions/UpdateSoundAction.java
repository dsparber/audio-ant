package com.audioant.io.android.json.actions;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.json.simple.JSONObject;

import com.audioant.alert.AlertSounds;
import com.audioant.audio.learning.LearnedSounds;
import com.audioant.audio.model.Sound;
import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.UpdateSound.Reply;
import com.audioant.io.android.json.JsonFields.UpdateSound.Request;
import com.audioant.io.android.json.JsonReplyAction;

public class UpdateSoundAction extends JsonReplyAction {

	private boolean success;

	public UpdateSoundAction(JSONObject request) {
		super(request);
		success = false;
		JSONObject data = (JSONObject) request.get(JsonFields.DATA_KEY);
		int soundId = (int) ((long) data.get(Request.ID_KEY));
		Integer alertId = (int) ((long) data.get(Request.ALERT_KEY));
		alertId = (alertId == -1) ? null : alertId;
		String name = (String) data.get(Request.NAME_KEY);

		Sound s = LearnedSounds.getSound(soundId);

		if (s != null) {
			success = true;
			s.setName(name);
			Sound alert = AlertSounds.getSound(alertId);
			if (alert != null) {
				s.setAlertId(alert.getId());
				try {
					LearnedSounds.saveSounds();
				} catch (ParserConfigurationException | TransformerException e) {
					success = false;
					e.printStackTrace();
				}

			}
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
