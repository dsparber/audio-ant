package com.audioant.io.android.json;

public interface JsonFields {

	String ACTION_KEY = "action";
	String DATA_KEY = "data";

	public interface SaveSound {
		public interface Request {
			String ACTION_VALUE = "saveSound";
		}

		public interface Reply {
			String ACTION_VALUE = "soundLearnedFeedback";
		}
	}

	public interface DeleteSound {
		public interface Request {
			String ACTION_VALUE = "deleteSound";
		}

		public interface Reply {
			String ACTION_VALUE = "soundDeleted";
		}
	}

	public interface GetAllSounds {
		public interface Request {
			String ACTION_VALUE = "getListOfSounds";
		}

		public interface Reply {
			String ACTION_VALUE = "listOfSounds";
			String SOUNDS_KEY = "sounds";
			String SOUND_NAME_KEY = "name";
			String SOUND_NUMBER_KEY = "number";
		}
	}

	public interface GetSoundInfo {
		public interface Request {
			String ACTION_VALUE = "getMoreInfoToSound";
		}

		public interface Reply {
			String ACTION_VALUE = "moreInfoToSound";
			String FILE_CONTENT_KEY = "fileContent";
			String FILE_NAME_KEY = "fileName";
			String NAME_KEY = "name";
			String NUMBER_KEY = "number";
			String ALERT = "sound";
		}
	}

	public interface GetSettings {
		public interface Request {
			String ACTION_VALUE = "getCurrentSettings";
		}

		public interface Reply {
			String ACTION_VALUE = "currentSettings";
		}
	}

	public interface ChangeSoundAlert {
		public interface Request {
			String ACTION_VALUE = "changeSoundAlert";
		}

		public interface Reply {
			String ACTION_VALUE = "soundChanged";
		}
	}

	public interface ChangeSettings {
		public interface Request {
			String ACTION_VALUE = "changeSettings";
		}

		public interface Reply {
			String ACTION_VALUE = "settingsChanged";
		}
	}

	public interface SetWifi {
		public interface Request {
			String ACTION_VALUE = "connectToWifiNetwork";
		}

		public interface Reply {
			String ACTION_VALUE = "wifiConnectedFeedback";
		}
	}

	public interface RecognisedSound {
		public interface Request {
			String ACTION_VALUE = "recognisedSound";
		}

		public interface Reply {
			String ACTION_VALUE = "receivedRecognisedSound";
		}
	}
}
