package com.audioant.io.android.json.actions;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.audioant.audio.model.Sound;
import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.RecognisedSound.Request;
import com.audioant.io.android.json.JsonRequestAction;

public class RecognisedSoundAction extends JsonRequestAction {

	private List<Sound> soundList;

	public RecognisedSoundAction() {
		soundList = new ArrayList<Sound>();
	}

	public void setSoundList(List<Sound> soundList) {
		this.soundList = soundList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject createRequest() {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonFields.ACTION_KEY, Request.ACTION_VALUE);

		JSONArray sounds = new JSONArray();
		for (Sound s : soundList) {
			JSONObject sound = new JSONObject();
			sound.put(Request.SOUND_NAME_KEY, s.getNameNotNull());
			sound.put(Request.SOUND_NUMBER_KEY, new Integer(s.getNumber()));
			sounds.add(sound);
		}

		JSONObject data = new JSONObject();
		data.put(Request.SOUNDS_KEY, sounds);
		jsonObject.put(JsonFields.DATA_KEY, data);

		return jsonObject;
	}

}
