package com.ethan.mlife.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.ethan.mlife.common.HttpRequestMethod;

public class HttpRequestClient {

	private static Object syncObject = new Object();
	private static volatile HttpRequestClient singletonInstance = null;
	private HttpClient httpClient = null;
	public static boolean initFlag = false;

	private HttpRequestClient() {
	}

	/**
	 * @return singletonInstance
	 */
	public static HttpRequestClient getHttpRequestClient() {
		if (null == singletonInstance) {
			synchronized (syncObject) {
				if (null == singletonInstance) {
					singletonInstance = new HttpRequestClient();
					// 初始化 HttpClient
					singletonInstance.initHttpClient();
					initFlag = true;
				}
			}
		}
		return singletonInstance;
	}

	/**
	 * init HttpClient settings
	 */
	private void initHttpClient() {

		HttpParams httpParams = new BasicHttpParams();

		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
		HttpProtocolParams.setUseExpectContinue(httpParams, true);

		/* 从连接池中取连接的超时时间 */
		ConnManagerParams.setTimeout(httpParams, 1000);
		/* 连接超时 */
		HttpConnectionParams.setConnectionTimeout(httpParams, 2000);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(httpParams, 3000);

		SchemeRegistry schReg = new SchemeRegistry();
		// 支持 http
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		// 支持 https
		schReg.register(new Scheme("https", PlainSocketFactory
				.getSocketFactory(), 433));

		// 使用线程安全的连接管理来创建HttpClient
		ClientConnectionManager cm = new ThreadSafeClientConnManager(
				httpParams, schReg);

		this.httpClient = new DefaultHttpClient(cm, httpParams);
	}

	/**
	 * 返回http请求数据
	 * 
	 * @param url
	 *            请求地址
	 * @param encode
	 *            请求编码
	 * @param method
	 *            请求方式(post,get)
	 * @param params
	 *            请求数据
	 * @param sb
	 *            返回数据
	 * @return
	 */
	public boolean getHttpResponseData(String url, String encode,
			HttpRequestMethod method, List<NameValuePair> params,
			StringBuilder sb) {

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

			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				sb.append(EntityUtils.toString(httpResponse.getEntity()));
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			Log.e(HttpRequestClient.class.toString(), e.toString());
		} catch (ClientProtocolException e) {
			Log.e(HttpRequestClient.class.toString(), e.toString());
		} catch (IOException e) {
			Log.e(HttpRequestClient.class.toString(), e.toString());
		} catch (Exception e) {
			Log.e(HttpRequestClient.class.toString(), e.toString());
		}
		return false;
	}

	/**
	 * 关闭 httpClient
	 */
	public void shutdownHttpRequestClient() {
		if (null != this.httpClient
				&& null != this.httpClient.getConnectionManager()) {
			httpClient.getConnectionManager().shutdown();
		}
	}
}
