package com.ethan.mlife.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.ethan.mlife.common.HttpRequestMethod;
import com.ethan.mlife.dao.AbstractBusDao;
import com.ethan.mlife.entity.Line;
import com.ethan.mlife.entity.Station;
import com.ethan.mlife.util.HttpRequestClient;

/**
 * @author Ethan
 * 
 */
public class SuZhouBusRegularImpl extends AbstractBusDao {
	/**
	 * 线路内容正则表达式
	 */
	private static Pattern busContentPattern = Pattern
			.compile("<span id=\"MainContent_DATA\">(.+?)</span>");
	/**
	 * 线路列表正则表达式
	 */
	private static Pattern busLineRowPattern = Pattern
			.compile("<tr><td><a href=\"(.+?)\">(.+?)</a></td><td>(\\s?|.+?)</td></tr>");
	/**
	 * 线路详细正则表达式
	 */
	private static Pattern busLineDetailPattern = Pattern
			.compile("<tr><td><a href=\"(.+?)\">(.+?)</a></td><td>(\\s?|.+?)</td><td>(\\s?|.+?)</td><td>(\\s?|.+?)</td></tr>");
	/**
	 * 线路GUID正则表达式
	 */
	private static Pattern busLineGuidPattern = Pattern
			.compile("LineGuid=(.+?)&amp;LineInfo");
	/**
	 * 站台列表正则表达式
	 */
	private static Pattern busStationRowPattern = Pattern
			.compile("<tr><td><a href=\"(.+?)\">(.+?)</a></td><td>(\\s?|.+?)</td><td>(\\s?|.+?)</td><td>(\\s?|.+?)</td><td>(\\s?|.+?)</td><td>(\\s?|.+?)</td></tr>");;
	/**
	 * 站台详细正则表达式
	 */
	private static Pattern busStationDetailPattern = Pattern
			.compile("<tr><td><a href=\"(.+?)\">(.+?)</a></td><td>(\\s?|.+?)</td><td>(\\s?|.+?)</td><td>(\\s?|.+?)</td><td>(\\s?|.+?)</td></tr>");;

	/**
	 * 获取线路列表
	 * 
	 * @param line
	 * @return
	 */
	public List<Line> getBusLine(Line line) {
		List<Line> listLine = new ArrayList<Line>();
		StringBuilder sb = new StringBuilder();
		boolean blnOk = false;
		try {

			// 设置查询线路编号
			busLinePostParams.put(searchBusLinePostParamName, line.getLineNo());

			List<NameValuePair> listParams = new ArrayList<NameValuePair>();

			for (Entry<String, String> entry : busLinePostParams.entrySet()) {
				listParams.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}

			blnOk = HttpRequestClient.getHttpRequestClient()
					.getHttpResponseData(busLineUrl, urlEncode,
							HttpRequestMethod.Post, listParams, sb);

			if (blnOk) {
				Matcher mcContent = busContentPattern.matcher(sb);
				if (mcContent.find()) {
					Matcher mcLine = busLineRowPattern.matcher(mcContent
							.group(1));
					while (mcLine.find()) {
						Line item = new Line();
						item.setUrlLink(String.format("%s%s", busBaseUrl,
								mcLine.group(1).trim()));
						item.setLineNo(StringEscapeUtils.unescapeHtml4(mcLine
								.group(2).trim()));

						int intFirstIndex = item.getLineNo().indexOf(
								line.getLineNo());
						int intLastIndex = item.getLineNo().lastIndexOf(
								line.getLineNo());

						// 只显示线路只出现1次的
						if (intFirstIndex > -1 && intFirstIndex == intLastIndex) {
							item.setDirection(StringEscapeUtils
									.unescapeHtml4(mcLine.group(3).trim()));
							Matcher mcUid = busLineGuidPattern.matcher(item
									.getUrlLink());

							item.setId(mcUid.find() ? mcUid.group(1).trim()
									: UUID.randomUUID().toString());

							listLine.add(item);
						}
					}
				}
			}
		} catch (Exception ex) {
			Log.e(SuZhouBusRegularImpl.class.toString(), ex.toString());
		}

		return listLine;
	}

	/**
	 * 获取站台列表
	 * 
	 * @param station
	 *            站台名
	 * @return
	 */
	public List<Station> getBusStation(Station station) {
		List<Station> listStation = new ArrayList<Station>();
		StringBuilder sb = new StringBuilder();
		try {
			// 设置查询站台名称
			busStationPostParams.put(searchBusStationPostParamName,
					station.getName());

			List<NameValuePair> listParams = new ArrayList<NameValuePair>();

			for (Entry<String, String> entry : busStationPostParams.entrySet()) {
				listParams.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}

			if (HttpRequestClient.getHttpRequestClient().getHttpResponseData(
					busStationUrl, urlEncode, HttpRequestMethod.Post,
					listParams, sb)) {
				Matcher mcContent = busContentPattern.matcher(sb);
				if (mcContent.find()) {
					Matcher mcLine = busStationRowPattern.matcher(mcContent
							.group(1));
					while (mcLine.find()) {
						Station item = new Station();
						item.setUrlLink(String.format("%s%s", busBaseUrl,
								mcLine.group(1).trim()));
						item.setName(StringEscapeUtils.unescapeHtml4(mcLine
								.group(2).trim()));
						item.setScode(StringEscapeUtils.unescapeHtml4(mcLine
								.group(3).trim()));
						item.setDistrict(StringEscapeUtils.unescapeHtml4(mcLine
								.group(4).trim()));
						item.setStreet(StringEscapeUtils.unescapeHtml4(mcLine
								.group(5).trim()));
						item.setArea(StringEscapeUtils.unescapeHtml4(mcLine
								.group(6).trim()));
						item.setDirection(StringEscapeUtils
								.unescapeHtml4(mcLine.group(7).trim()));
						listStation.add(item);
					}
				}
			}
		} catch (Exception ex) {
			Log.e(SuZhouBusRegularImpl.class.toString(), ex.toString());
		}
		if (listStation.isEmpty() && listStation.size() == 0) {
			return this.queryStationFromHis(station);
		}
		return listStation;
	}

	/**
	 * 获取线路站台
	 * 
	 * @param line
	 * @return
	 */
	public List<Station> getLineStation(Line line) {
		List<Station> list = new ArrayList<Station>();
		StringBuilder sb = new StringBuilder();
		try {
			// 加载测试数据,开发时用
			if (HttpRequestClient.getHttpRequestClient().getHttpResponseData(
					line.getUrlLink(), urlEncode, HttpRequestMethod.Get, null,
					sb)) {
				Matcher mcContent = busContentPattern.matcher(sb);
				if (mcContent.find()) {
					Matcher mcLineInfo = busLineDetailPattern.matcher(mcContent
							.group(1));
					while (mcLineInfo.find()) {
						Station station = new Station();
						station.setUrlLink(String.format("%s%s", busBaseUrl,
								mcLineInfo.group(1).trim()));
						station.setName(StringEscapeUtils
								.unescapeHtml4(mcLineInfo.group(2).trim()));
						station.setScode(StringEscapeUtils
								.unescapeHtml4(mcLineInfo.group(3).trim()));
						station.setVeNumber(StringEscapeUtils
								.unescapeHtml4(mcLineInfo.group(4).trim()));
						station.setPassTime(StringEscapeUtils
								.unescapeHtml4(mcLineInfo.group(5).trim()));
						list.add(station);
					}
				}
			}
		} catch (Exception ex) {
			Log.e(SuZhouBusRegularImpl.class.toString(), ex.toString());
		}
		return list;
	}

	/**
	 * 获取站台线路
	 * 
	 * @param station
	 * @return
	 */
	public List<Line> getStationLine(Station station) {
		List<Line> list = new ArrayList<Line>();
		StringBuilder sb = new StringBuilder();
		try {

			if (HttpRequestClient.getHttpRequestClient().getHttpResponseData(
					station.getUrlLink(), urlEncode, HttpRequestMethod.Get,
					null, sb)) {
				Matcher mcContent = busContentPattern.matcher(sb);
				if (mcContent.find()) {
					Matcher mcStation = busStationDetailPattern
							.matcher(mcContent.group(1));
					while (mcStation.find()) {
						Line line = new Line();
						line.setUrlLink(String.format("%s%s", busBaseUrl,
								mcStation.group(1).trim()));
						line.setLineNo(StringEscapeUtils
								.unescapeHtml4(mcStation.group(2).trim()));
						line.setDirection(StringEscapeUtils
								.unescapeHtml4(mcStation.group(3).trim()));
						line.setVeNumber(StringEscapeUtils
								.unescapeHtml4(mcStation.group(4).trim()));
						line.setUpdateTime(StringEscapeUtils
								.unescapeHtml4(mcStation.group(5).trim()));
						line.setSpacing(StringEscapeUtils
								.unescapeHtml4(mcStation.group(6).trim()));
						Matcher mcUid = busLineGuidPattern.matcher(line
								.getUrlLink());
						line.setId(mcUid.find() ? mcUid.group(1).trim() : UUID
								.randomUUID().toString());
						list.add(line);
					}
				}
			}
		} catch (Exception ex) {
			Log.e(SuZhouBusRegularImpl.class.toString(), ex.toString());
		}
		return list;
	}
}
