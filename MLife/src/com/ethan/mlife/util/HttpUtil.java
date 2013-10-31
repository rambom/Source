package com.ethan.mlife.util;

import java.net.URI;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import android.net.Uri;

public class HttpUtil {

	/**
	 * 对url中文编码
	 * 
	 * @param url
	 * @return
	 */
	public static String urlEncode(String url) {
		return Uri.encode(url.trim(), "&/=?:");
	}

	/**
	 * 解析URL参数
	 * 
	 * @param url
	 * @param encode
	 * @return
	 */
	public static List<NameValuePair> parseUrlQueryString(String url,
			String encode) {
		return URLEncodedUtils.parse(
				URI.create(Uri.encode(url.trim(), "&/?:")), encode);

	}
}
