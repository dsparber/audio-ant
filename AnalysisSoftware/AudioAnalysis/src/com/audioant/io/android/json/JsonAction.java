package com.audioant.io.android.json;

import org.json.simple.JSONObject;

public abstract class JsonAction {

	protected JSONObject request;

	public JsonAction(JSONObject request) {
		this.request = request;
	}

	public abstract JSONObject getReply();
}
