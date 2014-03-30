package com.codepath.imagesearcher;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultArrayAdapter(Context context) {
		this(context, new ArrayList<ImageResult>());
	}
	
	public ImageResultArrayAdapter(Context context, List<ImageResult> objects) {
		super(context, R.layout.item_image_result, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_image_result, null);
		}
		ImageResult result = getItem(position);
		String tbUrl = result.getTbUrl();
		convertView.setContentDescription(result.getContent());
		Picasso.with(getContext()).load(tbUrl).into((ImageView)convertView);
		return convertView;
	}
}
