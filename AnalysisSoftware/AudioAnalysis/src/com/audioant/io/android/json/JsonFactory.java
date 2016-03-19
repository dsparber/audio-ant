package com.audioant.io.android.json;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.audioant.io.android.json.JsonFields.ChangeSettings;
import com.audioant.io.android.json.JsonFields.ChangeSoundAlert;
import com.audioant.io.android.json.JsonFields.DeleteSound;
import com.audioant.io.android.json.JsonFields.GetAllSounds;
import com.audioant.io.android.json.JsonFields.GetSettings;
import com.audioant.io.android.json.JsonFields.GetSoundInfo;
import com.audioant.io.android.json.JsonFields.SaveSound;
import com.audioant.io.android.json.JsonFields.SetWifi;
import com.audioant.io.android.json.action.ChangeSettingsAction;
import com.audioant.io.android.json.action.ChangeSoundAlertAction;
import com.audioant.io.android.json.action.DeleteSoundAction;
import com.audioant.io.android.json.action.GetAllSoundsAction;
import com.audioant.io.android.json.action.GetSettingsAction;
import com.audioant.io.android.json.action.GetSoundInfoAction;
import com.audioant.io.android.json.action.SaveSoundAction;
import com.audioant.io.android.json.action.SetWifiAction;

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
		case ChangeSoundAlert.Request.ACTION_VALUE:
			return new ChangeSoundAlertAction(json);
		case ChangeSettings.Request.ACTION_VALUE:
			return new ChangeSettingsAction(json);
		case SetWifi.Request.ACTION_VALUE:
			return new SetWifiAction(json);
		default:
			return null;
		}
	}

}
