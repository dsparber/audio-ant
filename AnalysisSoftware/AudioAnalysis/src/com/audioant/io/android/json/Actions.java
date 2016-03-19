package com.audioant.io.android.json;

public interface Actions {

	public interface SaveSound {
		String REQUEST = "saveSound";
		String REPLY = "soundLearnedFeedback";
	}

	public interface ListOfSounds {
		String REQUEST = "getListOfSounds";
		String REPLY = "listOfSounds";
	}

	public interface SoundInfo {
		String REQUEST = "getMoreInfoToSound";
		String REPLY = "moreInfoToSound";
	}

	public interface DeleteSound {
		String REQUEST = "deleteSound";
		String REPLY = "soundDeleted";
	}

	public interface ChangeSoundAlert {
		String REQUEST = "changeSoundAlert";
		String REPLY = "soundChanged";
	}

	public interface CurrentSettings {
		String REQUEST = "getCurrentSettings";
		String REPLY = "currentSettings";
	}

	public interface ChangeSettings {
		String REQUEST = "changeSettings";
		String REPLY = "settingsChanged";
	}

	public interface WifiConnections {
		String REQUEST = "connectToWifiNetwork";
		String REPLY = "WifiConnectedFeedback";
	}

	public interface RecognisedSound {
		String REQUEST = "recognisedSound";
		String REPLY = "receivedRecognisedSound";
	}
}
