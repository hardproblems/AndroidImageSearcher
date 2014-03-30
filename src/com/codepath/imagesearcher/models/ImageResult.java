package com.codepath.imagesearcher;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ImageResult {
	private String url;
	private String tbUrl;
	private String content;
	
	public String getUrl() {
		return url;
	}
	
	public String getTbUrl() {
		return tbUrl;
	}
	
	public String getContent() {
		return content;
	}
	
	public static ImageResult fromJson(JSONObject jsonObject) {
		ImageResult result = new ImageResult();
		try {
			result.url = jsonObject.getString("url");
			result.tbUrl = jsonObject.getString("tbUrl");
			result.content = jsonObject.getString("content");
		} catch (JSONException e) {
			Log.w("WARN", "Could not serialize ImageResult from json", e);
			return null;
		}
		return result;
	}
	
	public static ArrayList<ImageResult> fromJson(JSONArray jsonArray) {
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				ImageResult image = fromJson(jsonArray.getJSONObject(i));
				if (image != null) {
					results.add(image);
				}
			} catch (JSONException e) {
				Log.w("WARN", String.format("Cannot get jsonObject at index %s from jsonArray", i), e);
				continue;
			}
		}
		return results;
	}
}
