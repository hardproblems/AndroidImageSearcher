package com.codepath.imagesearcher.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.codepath.imagesearcher.R;
import com.codepath.imagesearcher.models.Settings;

public class SettingsActivity extends ActionBarActivity {
	private static Settings settings = new Settings();
	
	private Spinner spSize;
	private Spinner spColor;
	private Spinner spType;
	private TextView etSite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		setupViews();
		settings = (Settings) getIntent().getSerializableExtra(GridSearchActivity.SETTINGS_EXTRA);
	}
	
	private void setupViews() {
		spSize = (Spinner) findViewById(R.id.spImageSize);
		spColor = (Spinner) findViewById(R.id.spColorFilter);
		spType = (Spinner) findViewById(R.id.spImageType);
		setSpinnerToValue(spSize, settings.imgsz);
		setSpinnerToValue(spColor, settings.imgcolor);
		setSpinnerToValue(spType, settings.imgtype);
		
		etSite = (TextView) findViewById(R.id.etSite);
		if (settings.as_sitesearch != null) {
			etSite.setText(settings.as_sitesearch);
		}
	}

	private void setSpinnerToValue(Spinner spinner, String value) {
	    int index = 0;
	    SpinnerAdapter adapter = spinner.getAdapter();
	    for (int i = 0; i < adapter.getCount(); i++) {
	        if (adapter.getItem(i).equals(value)) {
	            index = i;
	        }
	    }
	    spinner.setSelection(index);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	public void onSaveSettings(View v) {
		settings.imgsz = spSize.getSelectedItem().toString();
		settings.imgcolor = spColor.getSelectedItem().toString();
		settings.imgtype = spType.getSelectedItem().toString();
		settings.as_sitesearch = etSite.getText().toString();
		Intent data = new Intent();
		data.putExtra(GridSearchActivity.SETTINGS_EXTRA, settings);
		setResult(RESULT_OK, data);
		// Close the screen and go back
		finish();
	}
}
