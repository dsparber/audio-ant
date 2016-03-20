package com.audioant.io.android.json;

import org.json.simple.JSONObject;

public abstract class JsonRequestAction implements JsonAction {

	public abstract JSONObject createRequest();
}
