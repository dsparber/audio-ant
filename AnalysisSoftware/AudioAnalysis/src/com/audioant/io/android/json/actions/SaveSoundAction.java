package com.audioant.io.android.json.actions;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.json.simple.JSONObject;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

import com.audioant.audio.learning.LearnedSounds;
import com.audioant.audio.learning.SoundFileLearner;
import com.audioant.audio.model.Sound;
import com.audioant.config.Config;
import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.SaveSound.Reply;
import com.audioant.io.android.json.JsonFields.SaveSound.Request;
import com.audioant.io.android.json.JsonReplyAction;
import com.audioant.tools.Base64Tool;

public class SaveSoundAction extends JsonReplyAction {

	private boolean success;

	public SaveSoundAction(JSONObject request) {
		super(request);

		success = true;

		JSONObject data = (JSONObject) request.get(JsonFields.DATA_KEY);

		Integer alertID = (int) ((long) data.get(Request.ALERT_ID_KEY));
		alertID = (alertID == -1) ? null : alertID;

		String name = (String) data.get(Request.NAME_KEY);

		String fileContent = (String) data.get(Request.FILE_CONTENT);
		String fileExtension = ((String) data.get(Request.FILE_EXTENSION));

		Sound sound = new Sound(name, Config.LEARNED_SOUNDS_FOLDER, LearnedSounds.getNextNumber(),
				Config.LEARNED_SOUNDS_FILE_NAME + fileExtension, alertID);

		String pathname = sound.getPath() + sound.getSoundFile();
		File file = new File(pathname);
		file.getParentFile().mkdirs();

		try {
			file = Base64Tool.decode(fileContent, file.getAbsolutePath());

			// Convert
			String inputFile = file.getAbsolutePath();
			String outputFile = file.getAbsolutePath().replace(fileExtension, Config.LEARNED_SOUNDS_FILE_DEFAULT_EXTENSION);

			ProcessBuilder builder = new ProcessBuilder(Config.FFMPEG_COMMAND, Config.FFMPEG_OVERRIDE, Config.FFMPEG_INPUT, inputFile, outputFile);
			Process process = builder.start();
			process.waitFor();

			process.getErrorStream().close();
			process.getInputStream().close();
			process.getOutputStream().close();

			SoundFileLearner learner = new SoundFileLearner(sound, file.getAbsolutePath().replace(fileExtension, Config.LEARNED_SOUNDS_FILE_DEFAULT_EXTENSION));
			learner.extractFeatures();

			LearnedSounds.addSound(sound);
			LearnedSounds.saveSounds();

		} catch (IOException | LineUnavailableException | REngineException | REXPMismatchException
				| UnsupportedAudioFileException | ParserConfigurationException | TransformerException | InterruptedException e) {
			e.printStackTrace();
			success = false;
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
