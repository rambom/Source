package com.android.jetman.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	public static boolean getHttpResponseData(String url, String encode,
			HttpRequestMethod method, List<NameValuePair> params,
			StringBuilder sb) {
		// http请求对象
		HttpRequestBase httpRequest = null;
		HttpEntity httpentity = null;
		HttpPost httpPost = null;
		try {
			switch (method) {
			case Get:
				httpRequest = null != params && !params.isEmpty() ? new HttpGet(
						String.format("%s?%s", url,
								URLEncodedUtils.format(params, encode)))
						: new HttpGet(url.toString());
				break;
			case Post:
				httpPost = new HttpPost(url.toString());
				httpentity = new UrlEncodedFormEntity(params, encode);
				httpPost.setEntity(httpentity);
				httpRequest = httpPost;
				break;
			}

			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				sb.append(EntityUtils.toString(httpResponse.getEntity()));
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Http请求方式
	 * 
	 * @author Ethan
	 * 
	 */
	public enum HttpRequestMethod {
		Get, Post
	}
}
