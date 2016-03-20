package com.audioant.io.android.json.actions;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.json.simple.JSONObject;

import com.audioant.RaspberryTool;
import com.audioant.audio.learning.LearnedSounds;
import com.audioant.io.android.json.JsonReplyAction;
import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.DeleteSound.Reply;;

public class DeleteSoundAction extends JsonReplyAction {

	private int id;

	public DeleteSoundAction(JSONObject request) {
		super(request);
		id = Integer.parseInt((String) request.get(JsonFields.DATA_KEY));
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getReply() {

		JSONObject jsonObject = new JSONObject();

		LearnedSounds.deleteSound(id);
		try {
			LearnedSounds.saveSounds();
		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}
		RaspberryTool.restartAnalysis();

		jsonObject.put(JsonFields.ACTION_KEY, Reply.ACTION_VALUE);

		return jsonObject;
	}

}
