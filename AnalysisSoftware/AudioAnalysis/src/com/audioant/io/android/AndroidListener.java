package com.audioant.io.android;

import java.util.Observable;
import java.util.Observer;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.audioant.io.android.json.JsonAction;
import com.audioant.io.android.json.JsonFactory;

public class AndroidListener implements Observer {

	@Override
	public void update(Observable o, Object arg) {

		String request = (String) arg;

		try {
			JsonAction action = JsonFactory.createAction(request);
			JSONObject reply = action.getReply();
			AndroidConnection.write(reply.toJSONString());

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public static AndroidListener getInstance() {
		return new AndroidListener();
	}
}
