package com.ethan.mlife.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ethan.mlife.common.HttpRequestMethod;
import com.ethan.mlife.dao.AbstractBusDao;
import com.ethan.mlife.entity.Line;
import com.ethan.mlife.entity.Station;
import com.ethan.mlife.util.HttpRequestClient;
import com.ethan.mlife.util.StringUtil;

public class SuZhouBusJsonImpl extends AbstractBusDao {

	/**
	 * 查询 线路Url
	 */
	private final String LineUrl = "http://content.2500city.com/Json?method=SearchBusLine&lineName=%s";
	/**
	 * 查询站台Url
	 */
	private final String StationUrl = "http://content.2500city.com/Json?method=SearchBusStation&standName=%s";
	/**
	 * 查询线路详情Url
	 */
	private final String LineDetailUrl = "http://content.2500city.com/Json?method=GetBusLineDetail&Guid=%s";
	/**
	 * 查询站台详情Url
	 */
	private final String StationDetailUrl = "http://content.2500city.com/Json?method=GetBusStationDetail&NoteGuid=%s";

	/**
	 * 获取线路列表
	 * 
	 * @param line
	 * @return
	 */
	public List<Line> getBusLine(Line line) {
		// TODO Auto-generated method stub
		List<Line> listLine = new ArrayList<Line>();
		StringBuilder sb = new StringBuilder();

		boolean blnOk = HttpRequestClient.getHttpRequestClient()
				.getHttpResponseData(
						String.format(this.LineUrl, line.getLineNo()),
						urlEncode, HttpRequestMethod.Get,
						null, sb);
		if (blnOk) {
			try {
				JSONObject jsonObject = new JSONObject(sb.toString());
				// 错误代码
				String strErrorCode = jsonObject.getString("errorCode");
				// 错误消息
				String strErrorMsg = jsonObject.getString("errorMessage");
				// 线路列表
				JSONArray jsonList = jsonObject.getJSONArray("list");
				if ("0".equals(strErrorCode)) {
					for (int i = 0; i < jsonList.length(); i++) {
						JSONObject json = (JSONObject) jsonList.opt(i);
						Line item = new Line();
						item.setId(json.getString("Guid"));
						item.setDirection(json.getString("LDirection"));
						item.setLineNo(json.getString("LName"));
						item.setUrlLink(String.format(this.LineDetailUrl,
								item.getId()));
						int intFirstIndex = item.getLineNo().indexOf(
								line.getLineNo());
						int intLastIndex = item.getLineNo().lastIndexOf(
								line.getLineNo());

						// 只显示线路只出现1次的
						if (intFirstIndex > -1 && intFirstIndex == intLastIndex) {
							listLine.add(item);
						}
					}
				} else {
					Log.e(SuZhouBusJsonImpl.class.toString(),
							String.format("getBusLine:%s", strErrorMsg));
				}
			} catch (JSONException ex) {
				Log.e(SuZhouBusJsonImpl.class.toString(), ex.toString());
			}
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
		// TODO Auto-generated method stub
		List<Station> listStation = new ArrayList<Station>();
		StringBuilder sb = new StringBuilder();

		boolean blnOk = HttpRequestClient.getHttpRequestClient()
				.getHttpResponseData(
						String.format(this.StationUrl, station.getName()),
						urlEncode, HttpRequestMethod.Get,
						null, sb);
		if (blnOk) {
			try {
				JSONObject jsonObject = new JSONObject(sb.toString());
				// 错误代码
				String strErrorCode = jsonObject.getString("errorCode");
				// 错误消息
				String strErrorMsg = jsonObject.getString("errorMessage");
				// 线路列表
				JSONArray jsonList = jsonObject.getJSONArray("list");
				if ("0".equals(strErrorCode)) {
					for (int i = 0; i < jsonList.length(); i++) {
						JSONObject json = (JSONObject) jsonList.opt(i);
						Station item = new Station();
						item.setId(UUID.randomUUID().toString());
						item.setScode(json.getString("NoteGuid"));
						item.setUrlLink(String.format(this.StationDetailUrl,
								item.getScode()));
						item.setName(json.getString("Name"));
						item.setDistrict(json.getString("Canton"));
						item.setStreet(json.getString("Road"));
						item.setArea(json.getString("Sect"));
						item.setDirection(json.getString("Direct"));
						listStation.add(item);
					}
				} else {
					Log.e(SuZhouBusJsonImpl.class.toString(),
							String.format("getBusStation:%s", strErrorMsg));
				}
			} catch (JSONException ex) {
				Log.e(SuZhouBusJsonImpl.class.toString(), ex.toString());
			}
		}

		if (listStation.isEmpty() && listStation.size() == 0) {
			return this.queryStationFromHis(station);
		}
		return listStation;
	}

	public List<Station> getLineStation(Line line) {
		// TODO Auto-generated method stub
		List<Station> list = new ArrayList<Station>();
		StringBuilder sb = new StringBuilder();
		boolean blnOk = HttpRequestClient.getHttpRequestClient()
				.getHttpResponseData(line.getUrlLink(), urlEncode,
						HttpRequestMethod.Get, null, sb);

		if (blnOk) {
			try {
				JSONObject jsonObject = new JSONObject(sb.toString());
				// 错误代码
				String strErrorCode = jsonObject.getString("errorCode");
				// 错误消息
				String strErrorMsg = jsonObject.getString("errorMessage");
				// 线路详情列表
				JSONArray jsonList = jsonObject.getJSONObject("list")
						.getJSONArray("StandInfo");

				if ("0".equals(strErrorCode)) {
					for (int i = 0; i < jsonList.length(); i++) {
						JSONObject json = (JSONObject) jsonList.opt(i);
						Station station = new Station();
						station.setId(json.getString("SGuid"));
						station.setScode(json.getString("SCode"));
						station.setName(json.getString("SName"));
						station.setUrlLink(String.format(this.StationDetailUrl,
								station.getScode()));
						station.setVeNumber(json.getString("BusInfo"));
						station.setPassTime(json.getString("InTime"));
						list.add(station);
					}
				} else {
					Log.e(SuZhouBusJsonImpl.class.toString(),
							String.format("getLineStation:%s", strErrorMsg));
				}
			} catch (JSONException ex) {
				Log.e(SuZhouBusJsonImpl.class.toString(), ex.toString());
			}
		}
		return list;
	}

	public List<Line> getStationLine(Station station) {
		// TODO Auto-generated method stub
		List<Line> list = new ArrayList<Line>();
		StringBuilder sb = new StringBuilder();
		boolean blnOk = HttpRequestClient.getHttpRequestClient()
				.getHttpResponseData(station.getUrlLink(), urlEncode,
						HttpRequestMethod.Get, null, sb);
		if (blnOk) {
			try {
				JSONObject jsonObject = new JSONObject(sb.toString());
				// 错误代码
				String strErrorCode = jsonObject.getString("errorCode");
				// 错误消息
				String strErrorMsg = jsonObject.getString("errorMessage");
				// 站台详情列表
				JSONArray jsonList = jsonObject.getJSONArray("list");

				if ("0".equals(strErrorCode)) {
					for (int i = 0; i < jsonList.length(); i++) {
						JSONObject json = (JSONObject) jsonList.opt(i);
						Line line = new Line();
						line.setId(json.getString("Guid"));
						line.setLineNo(json.getString("LName"));
						line.setDirection(json.getString("LDirection"));
						line.setVeNumber(json.getString("DBusCard"));
						line.setUpdateTime(json.getString("InTime"));
						line.setVeStation(json.getString("SName"));
						line.setSpacing(json.getString("Distince"));

						// 不显示负数站距
						if (StringUtil.isNullOrEmpty(line.getSpacing()))
							line.setSpacing("待发");
						else {
							try {
								Integer spacing = new Integer(line.getSpacing());
								if (spacing >= 0)
									line.setSpacing(spacing.toString());
								else {
									line.setSpacing("待发");
									line.setVeNumber(null);
								}
							} catch (Exception ex) {
								line.setSpacing("待发");
							}
						}
						line.setUrlLink(String.format(this.LineDetailUrl,
								line.getId()));
						list.add(line);
					}
				} else {
					Log.e(SuZhouBusJsonImpl.class.toString(),
							String.format("getStationLine:%s", strErrorMsg));
				}
			} catch (JSONException ex) {
				Log.e(SuZhouBusJsonImpl.class.toString(), ex.toString());
			}
		}

		// 按站距排序
		Collections.sort(list, new Comparator<Line>() {
			public int compare(Line object1, Line object2) {
				Integer intSpacing1;
				Integer intSpacing2;
				try {
					intSpacing1 = Integer.valueOf(object1.getSpacing());
				} catch (Exception ex) {
					return 1;
				}

				try {
					intSpacing2 = Integer.valueOf(object2.getSpacing());
				} catch (Exception ex) {
					return -1;
				}

				// 按点击数排序
				return intSpacing1.compareTo(intSpacing2);
			}
		});
		return list;
	}
}
