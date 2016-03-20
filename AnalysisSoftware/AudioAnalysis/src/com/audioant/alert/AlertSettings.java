package com.audioant.alert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.audioant.config.Config;

public class AlertSettings {

	private boolean lightSignals;
	private boolean audioSignals;
	private Integer alertSoundId;

	private static AlertSettings alertSettings;

	public static AlertSettings getInstance() throws FileNotFoundException, IOException, ParseException {
		if (alertSettings == null) {
			alertSettings = loadJson();
		}
		return alertSettings;
	}

	private AlertSettings(boolean lightSignals, boolean audioSignals, Integer alertSoundId) throws IOException {
		this.lightSignals = lightSignals;
		this.audioSignals = audioSignals;
		this.alertSoundId = alertSoundId;
		saveJson();
	}

	public boolean isLightSignals() {
		return lightSignals;
	}

	public void setLightSignals(boolean lightSignals) throws IOException {
		this.lightSignals = lightSignals;
		saveJson();
	}

	public boolean isAudioSignals() {
		return audioSignals;
	}

	public void setAudioSignals(boolean audioSignals) throws IOException {
		this.audioSignals = audioSignals;
		saveJson();
	}

	public Integer getAlertSoundId() {
		return alertSoundId;
	}

	public void setAlertSoundId(Integer alertSoundId) throws IOException {
		this.alertSoundId = alertSoundId;
		saveJson();
	}

	private static AlertSettings loadJson() throws FileNotFoundException, IOException, ParseException {
		File f = new File(Config.ALERT_SETTINGS_FILE_PATH);

		JSONObject jsonObject;
		Boolean audioSignals;
		Boolean lightSignals;
		Integer alertSoundId;

		if (f.exists()) {
			jsonObject = (JSONObject) new JSONParser().parse(new FileReader(f));
			audioSignals = (Boolean) jsonObject.get(Config.ALERT_SETTINGS_KEY_AUDIO_SIGNALS);
			lightSignals = (Boolean) jsonObject.get(Config.ALERT_SETTINGS_KEY_LIGHT_SIGNALS);
			alertSoundId = (Integer) jsonObject.get(Config.ALERT_SETTINGS_KEY_ALERT_SOUND_ID);
		} else {
			audioSignals = Config.ALERT_SETTINGS_INIT_VALUE_AUDIO_SIGNALS;
			lightSignals = Config.ALERT_SETTINGS_INIT_VALUE_LIGHT_SIGNALS;
			alertSoundId = Config.ALERT_SETTINGS_INIT_VALUE_ALERT_SOUND_ID;
		}

		return new AlertSettings(lightSignals, audioSignals, alertSoundId);
	}

	private void saveJson() throws IOException {
		File f = new File(Config.ALERT_SETTINGS_FILE_PATH);
		BufferedWriter writer = new BufferedWriter(new FileWriter(f));
		writer.write(getJson().toJSONString());
		writer.close();
	}

	@SuppressWarnings("unchecked")
	public JSONObject getJson() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(Config.ALERT_SETTINGS_KEY_AUDIO_SIGNALS, new Boolean(audioSignals));
		jsonObject.put(Config.ALERT_SETTINGS_KEY_LIGHT_SIGNALS, new Boolean(lightSignals));
		jsonObject.put(Config.ALERT_SETTINGS_KEY_ALERT_SOUND_ID, alertSoundId);
		return jsonObject;
	}

	public void setValues(JSONObject jsonObject) throws IOException {
		audioSignals = (Boolean) jsonObject.get(Config.ALERT_SETTINGS_KEY_AUDIO_SIGNALS);
		lightSignals = (Boolean) jsonObject.get(Config.ALERT_SETTINGS_KEY_LIGHT_SIGNALS);
		alertSoundId = (Integer) jsonObject.get(Config.ALERT_SETTINGS_KEY_ALERT_SOUND_ID);
		saveJson();
	}
}
