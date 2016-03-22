package com.audioant.io.android.json;

public interface JsonFields {

	String ACTION_KEY = "action";
	String DATA_KEY = "data";

	public interface SaveSound {
		public interface Request {
			String ACTION_VALUE = "saveSound";
			String ALERT_ID_KEY = "alertId";
			String NAME_KEY = "name";
			String FILE_CONTENT = "fileContent";
			String FILE_EXTENSION = "fileExtension";
		}

		public interface Reply {
			String ACTION_VALUE = "soundLearnedFeedback";
			String SUCCESS_KEY = "successful";
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

	public interface GetAlertSounds {
		public interface Request {
			String ACTION_VALUE = "getAlertSounds";
		}

		public interface Reply {
			String ACTION_VALUE = "alertSounds";
			String SOUNDS_KEY = "sounds";
			String FILE_CONTENT_KEY = "fileContent";
			String FILE_NAME_KEY = "fileName";
			String NAME_KEY = "name";
			String NUMBER_KEY = "number";
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
			String ALERT = "alert";
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

	public interface UpdateSound {
		public interface Request {
			String ACTION_VALUE = "updateSound";
			String ID_KEY = "id";
			String NAME_KEY = "name";
			String ALERT_KEY = "alertId";
		}

		public interface Reply {
			String ACTION_VALUE = "soundUpdated";
			String SUCCESS_KEY = "success";
		}
	}

	public interface ChangeSettings {
		public interface Request {
			String ACTION_VALUE = "changeSettings";
		}

		public interface Reply {
			String ACTION_VALUE = "settingsChanged";
			String SUCCESS_KEY = "success";
		}
	}

	public interface SetWifi {
		public interface Request {
			String ACTION_VALUE = "connectToWifiNetwork";
			String SSID_KEY = "SSID";
			String PSK_KEY = "password";
		}

		public interface Reply {
			String ACTION_VALUE = "wifiNetworkAdded";
			String SUCCESS_KEY = "successful";
			String PASSWORD_OK_KEY = "passwordOk";
			String SSID_OK_KEY = "ssidOk";
		}
	}

	public interface RecognisedSound {
		public interface Request {
			String ACTION_VALUE = "recognisedSound";
			String SOUND_NAME_KEY = "name";
			String SOUND_NUMBER_KEY = "number";
			String SOUNDS_KEY = "sounds";
		}

		public interface Reply {
			String ACTION_VALUE = "receivedRecognisedSound";
		}
	}
}
