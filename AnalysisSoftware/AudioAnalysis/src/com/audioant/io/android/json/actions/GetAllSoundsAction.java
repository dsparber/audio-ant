package com.audioant.io.android.json.actions;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.audioant.audio.learning.LearnedSounds;
import com.audioant.audio.model.Sound;
import com.audioant.io.android.json.JsonReplyAction;
import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.GetAllSounds;

public class GetAllSoundsAction extends JsonReplyAction {

	public GetAllSoundsAction(JSONObject request) {
		super(request);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getReply() {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonFields.ACTION_KEY, GetAllSounds.Reply.ACTION_VALUE);

		JSONArray sounds = new JSONArray();
		List<Sound> soundList = LearnedSounds.getSounds();

		for (Sound s : soundList) {
			JSONObject sound = new JSONObject();
			sound.put(GetAllSounds.Reply.SOUND_NAME_KEY, s.getNameNotNull());
			sound.put(GetAllSounds.Reply.SOUND_NUMBER_KEY, new Integer(s.getNumber()));
			sounds.add(sound);
		}

		JSONObject data = new JSONObject();
		data.put(GetAllSounds.Reply.SOUNDS_KEY, sounds);
		jsonObject.put(JsonFields.DATA_KEY, data);

		return jsonObject;
	}

}
