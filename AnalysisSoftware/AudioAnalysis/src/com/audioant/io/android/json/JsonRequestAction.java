package com.audioant.io.android.json;

import org.json.simple.JSONObject;

public interface JsonRequestAction extends JsonAction {

	JSONObject createRequest();
}
