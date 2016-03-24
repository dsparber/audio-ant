package com.audioant.io.android.json;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.audioant.io.android.json.JsonFields.AlertConfirmed;
import com.audioant.io.android.json.JsonFields.ChangeSettings;
import com.audioant.io.android.json.JsonFields.DeleteSound;
import com.audioant.io.android.json.JsonFields.GetAlertSounds;
import com.audioant.io.android.json.JsonFields.GetAllSounds;
import com.audioant.io.android.json.JsonFields.GetSettings;
import com.audioant.io.android.json.JsonFields.GetSoundInfo;
import com.audioant.io.android.json.JsonFields.RecognisedSound;
import com.audioant.io.android.json.JsonFields.SaveSound;
import com.audioant.io.android.json.JsonFields.SetWifi;
import com.audioant.io.android.json.JsonFields.UpdateSound;
import com.audioant.io.android.json.actions.AlertConfirmedAction;
import com.audioant.io.android.json.actions.ChangeSettingsAction;
import com.audioant.io.android.json.actions.DeleteSoundAction;
import com.audioant.io.android.json.actions.GetAlertSoundsAction;
import com.audioant.io.android.json.actions.GetAllSoundsAction;
import com.audioant.io.android.json.actions.GetSettingsAction;
import com.audioant.io.android.json.actions.GetSoundInfoAction;
import com.audioant.io.android.json.actions.RecognisedSoundAction;
import com.audioant.io.android.json.actions.SaveSoundAction;
import com.audioant.io.android.json.actions.SetWifiAction;
import com.audioant.io.android.json.actions.UpdateSoundAction;

public class JsonFactory {

	public static JsonAction createAction(String request) throws ParseException {

		JSONParser parser = new JSONParser();

		Object obj = parser.parse(request);
		JSONObject json = (JSONObject) obj;

		String action = (String) json.get(JsonFields.ACTION_KEY);

		switch (action) {
		case SaveSound.Request.ACTION_VALUE:
			return new SaveSoundAction(json);
		case DeleteSound.Request.ACTION_VALUE:
			return new DeleteSoundAction(json);
		case GetAllSounds.Request.ACTION_VALUE:
			return new GetAllSoundsAction(json);
		case GetSoundInfo.Request.ACTION_VALUE:
			return new GetSoundInfoAction(json);
		case GetSettings.Request.ACTION_VALUE:
			return new GetSettingsAction(json);
		case UpdateSound.Request.ACTION_VALUE:
			return new UpdateSoundAction(json);
		case ChangeSettings.Request.ACTION_VALUE:
			return new ChangeSettingsAction(json);
		case SetWifi.Request.ACTION_VALUE:
			return new SetWifiAction(json);
		case RecognisedSound.Reply.ACTION_VALUE:
			return new RecognisedSoundAction();
		case GetAlertSounds.Request.ACTION_VALUE:
			return new GetAlertSoundsAction(json);
		case AlertConfirmed.Request.ACTION_VALUE:
			return new AlertConfirmedAction(json);
		default:
			return null;
		}
	}

}
