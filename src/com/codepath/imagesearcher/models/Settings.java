package com.codepath.imagesearcher;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import android.net.Uri;

public class Settings implements Serializable {
	private static final long serialVersionUID = -8902094699381429100L;
	
	String imgsz;
	String imgcolor;
	String imgtype;
	String as_sitesearch;
	
	public String toQueryString() {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(imgsz)) {
			sb.append("&imgsz=").append(imgsz);
		}
		if (StringUtils.isNotBlank(imgcolor)) {
			sb.append("&imgcolor=").append(imgcolor);
		}
		if (StringUtils.isNotBlank(imgtype)) {
			sb.append("&imgtype=").append(StringUtils.strip(imgtype));
		}
		if (StringUtils.isNotBlank(as_sitesearch)) {
			sb.append("&as_sitesearch=").append(Uri.encode(as_sitesearch));
		}
		return sb.toString();
	}
}
