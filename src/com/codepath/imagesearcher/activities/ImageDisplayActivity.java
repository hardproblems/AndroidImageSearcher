package com.codepath.imagesearcher.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;

import com.codepath.imagesearcher.R;
import com.squareup.picasso.Picasso;

public class ImageDisplayActivity extends Activity {
	public static final String IMAGE_URL_EXTRA = "imageUrl";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		String url = (String) getIntent().getSerializableExtra(IMAGE_URL_EXTRA);
		ImageView fullSizeView = (ImageView) findViewById(R.id.ivFullSize);
		Picasso.with(this).load(url).into(fullSizeView);
		fullSizeView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				finish();
				return true;
			}
		});
	}
}
