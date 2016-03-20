package com.audioant.io.android.json;

import org.json.simple.JSONObject;

public abstract class JsonReplyAction implements JsonAction {

	protected JSONObject request;

	public JsonReplyAction(JSONObject request) {
		this.request = request;
	}

	public abstract JSONObject getReply();
}
