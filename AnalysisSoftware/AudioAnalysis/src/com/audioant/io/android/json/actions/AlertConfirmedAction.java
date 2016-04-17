package com.audioant.io.android.json.actions;

import java.io.IOException;

import org.json.simple.JSONObject;

import com.audioant.io.android.json.JsonFields;
import com.audioant.io.android.json.JsonFields.AlertConfirmed.Request;
import com.audioant.io.android.json.JsonRequestAction;
import com.audioant.io.eventObserver.Events;
import com.audioant.io.raspberry.AlertController;

public class AlertConfirmedAction implements JsonRequestAction {

	public AlertConfirmedAction(JSONObject jsonObject) {
		try {
			AlertController.getInstance().confirmAlert();
			Events.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public AlertConfirmedAction() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject createRequest() {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonFields.ACTION_KEY, Request.ACTION_VALUE);

		return jsonObject;
	}
}
