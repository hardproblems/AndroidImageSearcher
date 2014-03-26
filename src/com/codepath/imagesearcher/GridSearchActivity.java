package com.codepath.imagesearcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class GridSearchActivity extends ActionBarActivity {
	private static final AsyncHttpClient client = new AsyncHttpClient();
	private static final String IMAGE_SEARCH_BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=";
	public static final String SETTINGS_EXTRA="settings";
	public static final int SETTINGS_REQUEST = 9;
	
	private static Settings settings = new Settings();
	
	private String lastQuery="";
	private GridView gvImageResults;
	private SearchView searchView;
	private ImageResultArrayAdapter imageAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid_search);
		setupViews();
		handleIntent(getIntent());
	}
	
	private void setupViews() {
		gvImageResults = (GridView) findViewById(R.id.gvImageResults);
		imageAdapter = new ImageResultArrayAdapter(this);
		gvImageResults.setAdapter(imageAdapter);
		gvImageResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent fullSizeIntent = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				fullSizeIntent.putExtra(ImageDisplayActivity.IMAGE_URL_EXTRA, imageAdapter.getItem(position).getUrl());
				startActivity(fullSizeIntent);
			}
		});
		gvImageResults.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				doSearch(lastQuery, totalItemsCount);
			}
		});
	}
	
	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      doSearch(query);
	    }
	}
	
	private void doSearch(String query) {
		imageAdapter.clear();
		doSearch(query, 0);
	}
	
	private void doSearch(String query, int start) {
		String uri = buildFullQueryUri(query, start);
		client.get(uri, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				try {
					JSONArray jsonArray = response.getJSONObject("responseData").getJSONArray("results");
					imageAdapter.addAll(ImageResult.fromJsonArray(jsonArray));
					Log.d("DEBUG", "Successfully retrieved image results");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			};
			
			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				Log.e("ERROR", "Error getting response from api", e);
			};
		} );
	}
	
	private String buildFullQueryUri(String query, int start) {
		StringBuilder sb = new StringBuilder(IMAGE_SEARCH_BASE_URL);
		sb.append(Uri.encode(query));
		if (settings != null) {
			sb.append(settings.toQueryString());
		}
		sb.append("&start=").append(start);
		return sb.toString();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.grid_search, menu);
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    searchView = (SearchView) menu.findItem(R.id.miSearch).getActionView();
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
	    searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(gvImageResults.getWindowToken(), 0);
				doSearch(query);
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String arg0) {
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.miSettings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			intent.putExtra(SETTINGS_EXTRA, settings);
			startActivityForResult(intent, SETTINGS_REQUEST);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SETTINGS_REQUEST) {
			if (resultCode == RESULT_OK) {
				settings = (Settings) data.getSerializableExtra(SETTINGS_EXTRA);
			}
		}
	}
}