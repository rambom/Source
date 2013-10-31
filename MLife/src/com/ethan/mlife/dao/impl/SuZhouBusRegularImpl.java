package com.ethan.mlife.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.ethan.mlife.MLifeApp;
import com.ethan.mlife.R;
import com.ethan.mlife.common.Constants;
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
	private static Pattern busLineContentPattern;
	/**
	 * 线路列表正则表达式
	 */
	private static Pattern busLineRowPattern;
	/**
	 * 线路详细正则表达式
	 */
	private static Pattern busLineDetailPattern;
	/**
	 * 线路GUID正则表达式
	 */
	private static Pattern busLineGuidPattern;
	/**
	 * 站台内容正则表达式
	 */
	private static Pattern busStationContentPattern;
	/**
	 * 站台列表正则表达式
	 */
	private static Pattern busStationRowPattern;
	/**
	 * 站台详细正则表达式
	 */
	private static Pattern busStationDetailPattern;
	/**
	 * 公交查询根站点
	 */
	public final String busBaseUrl = "http://www.szjt.gov.cn/apts/";
	/**
	 * 线路查询URL
	 */
	public final String busLineUrl = "http://www.szjt.gov.cn/apts/APTSLine.aspx";
	/**
	 * 站台查询URL
	 */
	public final String busStationUrl = "http://www.szjt.gov.cn/apts/default.aspx";
	/**
	 * 加载测试数据
	 */
	private static boolean loadTestData;
	/**
	 * 线路查询post参数
	 */
	private static List<NameValuePair> busLinePostParams = new ArrayList<NameValuePair>();
	/**
	 * 站台查询post参数
	 */
	private static List<NameValuePair> busStationPostParams = new ArrayList<NameValuePair>();

	static {
		loadTestData = Constants.LOAD_TEST_BUS_DATA.equalsIgnoreCase(MLifeApp
				.getContext().getResources()
				.getString(R.string.busLoadTestData));

		busLineContentPattern = Pattern.compile(MLifeApp.getContext()
				.getString(R.string.busLineContentPattern));

		busLineRowPattern = Pattern.compile(MLifeApp.getContext().getString(
				R.string.busLineRowPattern));

		busLineDetailPattern = Pattern.compile(MLifeApp.getContext().getString(
				R.string.busLineDetailPattern));

		busLineGuidPattern = Pattern.compile(MLifeApp.getContext().getString(
				R.string.busLineGuidPattern));

		busStationContentPattern = Pattern.compile(MLifeApp.getContext()
				.getString(R.string.busStationContentPattern));

		busStationRowPattern = Pattern.compile(MLifeApp.getContext().getString(
				R.string.busStationRowPattern));

		busStationDetailPattern = Pattern.compile(MLifeApp.getContext()
				.getString(R.string.busStationDetailPattern));

		// 加载线路查询请求参数
		for (String str : MLifeApp.getContext().getResources()
				.getStringArray(R.array.busLinePostParam)) {
			String[] arr = str.split("#");
			if (arr.length == 2) {
				busLinePostParams.add(new BasicNameValuePair(arr[0], arr[1]));
			} else {
				busLinePostParams.add(new BasicNameValuePair(arr[0], ""));
			}
		}
		busLinePostParams.add(new BasicNameValuePair(MLifeApp.getContext()
				.getString(R.string.busLinePostLineName), ""));

		// 加载站台查询请求参数
		for (String str : MLifeApp.getContext().getResources()
				.getStringArray(R.array.busStationPostParam)) {
			String[] arr = str.split("#");
			if (arr.length == 2) {
				busStationPostParams
						.add(new BasicNameValuePair(arr[0], arr[1]));
			} else {
				busStationPostParams.add(new BasicNameValuePair(arr[0], ""));
			}
		}
		busStationPostParams.add(new BasicNameValuePair(MLifeApp.getContext()
				.getString(R.string.busStationPostStationName), ""));
	}

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
			busLinePostParams.set(
					busLinePostParams.size() - 1,
					new BasicNameValuePair(MLifeApp.getContext().getString(
							R.string.busLinePostLineName), line.getLineNo()));

			// 加载测试数据,开发时用
			if (loadTestData) {
				blnOk = initTestBusLine(sb);
			} else {
				blnOk = HttpRequestClient.getHttpRequestClient()
						.getHttpResponseData(busLineUrl, urlEncode,
								HttpRequestMethod.Post,
								busLinePostParams, sb);
			}
			if (blnOk) {
				Matcher mcContent = busLineContentPattern.matcher(sb);
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
		boolean blnOk = false;
		try {

			// 设置查询站台名称
			busStationPostParams.set(
					busStationPostParams.size() - 1,
					new BasicNameValuePair(MLifeApp.getContext().getString(
							R.string.busStationPostStationName), station
							.getName()));
			// 加载测试数据,开发时用
			if (loadTestData) {
				blnOk = initTestBusStation(sb);
			} else {
				blnOk = HttpRequestClient.getHttpRequestClient()
						.getHttpResponseData(busStationUrl, urlEncode,
								HttpRequestMethod.Post,
								busStationPostParams, sb);
			}
			if (blnOk) {
				Matcher mcContent = busStationContentPattern.matcher(sb);
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
		boolean blnOk = false;

		try {
			// 加载测试数据,开发时用
			if (loadTestData) {
				blnOk = initTestLineStation(sb);
			} else {
				blnOk = HttpRequestClient.getHttpRequestClient()
						.getHttpResponseData(line.getUrlLink(), urlEncode,
								HttpRequestMethod.Get, null,
								sb);
			}
			if (blnOk) {
				Matcher mcContent = busLineContentPattern.matcher(sb);
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
		boolean blnOk = false;
		try {
			// 加载测试数据,开发时用
			if (loadTestData) {
				blnOk = initTestStationLine(sb);
			} else {
				blnOk = HttpRequestClient.getHttpRequestClient()
						.getHttpResponseData(station.getUrlLink(), urlEncode,
								HttpRequestMethod.Get, null,
								sb);
			}

			if (blnOk) {
				Matcher mcContent = busStationContentPattern.matcher(sb);
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

	private boolean initTestBusLine(StringBuilder sb) {
		sb.append("<span id=\"ctl00_MainContent_Message\"><?xml version=\"1.0\" encoding=\"utf-16\"?><table cellspacing=\"0\" border=\"1\"><tr><th>线路</th><th>方向</th></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=B8AB93A1-BF8F-BB60-DD57-D4A75AB57448&amp;LineInfo=51(绿宝广场首末站=&gt;越溪首末站)\">51</a></td><td>绿宝广场首末站=&gt;越溪首末站</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=A8BE1153-A8C9-BD7C-7D43-DA4423AC0F33&amp;LineInfo=51(越溪首末站=&gt;绿宝广场首末站)\">51</a></td><td>越溪首末站=&gt;绿宝广场首末站</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=328a6c52-ae29-4ecd-a796-264987b6e80b&amp;LineInfo=511(葑门换乘站)\">511</a></td><td>葑门换乘站</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=f5c24669-546d-4eed-9c0d-c7b5dc5283a1&amp;LineInfo=511(灵岩山)\">511</a></td><td>灵岩山</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=435B9068-C36C-0CB0-AB87-E3C998AA5C99&amp;LineInfo=512(灵岩山首末站=&gt;东南环首末站)\">512</a></td><td>灵岩山首末站=&gt;东南环首末站</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=0784E3B7-2585-BC10-5C40-7570AEC1539C&amp;LineInfo=512(东南环首末站=&gt;灵岩山首末站)\">512</a></td><td>东南环首末站=&gt;灵岩山首末站</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=7a417b27-7730-4504-b159-a8cfe86c6729&amp;LineInfo=513(葑门换乘站)\">513</a></td><td>葑门换乘站</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=3176d075-6114-4946-85e8-1c872f21fc0d&amp;LineInfo=513(徐浜村)\">513</a></td><td>徐浜村</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=a9ce0a7a-3d30-4b08-ab55-99f8db579345&amp;LineInfo=514(国际教育园)\">514</a></td><td>国际教育园</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=fd052133-0d27-4f1e-8280-a5e4e001ca1b&amp;LineInfo=514(官渎里立交桥)\">514</a></td><td>官渎里立交桥</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=5C0570AD-F1ED-4D26-8C60-7059A4D45372&amp;LineInfo=518(火车站北广场首末站)\">518</a></td><td>火车站北广场首末站</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=D1D3E2D3-ECED-419E-B300-83355D1A8B2F&amp;LineInfo=518(甪直汽车站停车场)\">518</a></td><td>甪直汽车站停车场</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=62BCFF10-B6B8-BDE8-E8DC-EC65D6D865C2&amp;LineInfo=551(东南环首末站=&gt;吴中区职教中心首末站)\">551</a></td><td>东南环首末站=&gt;吴中区职教中心首末站</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=94F43C10-DCA2-0C50-250B-08EB03B75F58&amp;LineInfo=551(吴中区职教中心首末站=&gt;东南环首末站)\">551</a></td><td>吴中区职教中心首末站=&gt;东南环首末站</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=EE11CD51-B90B-3600-C9E9-4F8223525095&amp;LineInfo=651(塘江头=&gt;新四军太湖游击队纪念馆)\">651</a></td><td>塘江头=&gt;新四军太湖游击队纪念馆</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=87DD7115-21E8-8ACB-8E0E-B30CC3BB4ECE&amp;LineInfo=651(新四军太湖游击队纪念馆=&gt;塘江头)\">651</a></td><td>新四军太湖游击队纪念馆=&gt;塘江头</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=D1317A83-EC73-243B-FEAA-3DE490163CFD&amp;LineInfo=851(望亭首末站)\">851</a></td><td>望亭首末站</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=66865FEB-1C4A-969A-9ED6-97CF98F82B80&amp;LineInfo=851(中翔家电小商品市场停车场)\">851</a></td><td>中翔家电小商品市场停车场</td></tr></table></span>");
		return true;
	}

	private boolean initTestBusStation(StringBuilder sb) {
		sb.append("<span id=\"ctl00_MainContent_Message\"><?xml version=\"1.0\" encoding=\"utf-16\"?><table cellspacing=\"0\" border=\"1\"><tr><th>站名</th><th>编号</th><th>所在行政区</th><th>所在道路</th><th>所在路段</th><th>站点方位</th></tr><tr><td><a href=\"default.aspx?StandCode=FDP&amp;StandName=车斜三号桥\">车斜三号桥</a></td><td>FDP</td><td>工业园区</td><td>永庆路</td><td>东方大道-普惠路</td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=GRW&amp;StandName=车斜三号桥\">车斜三号桥</a></td><td>GRW</td><td>工业园区</td><td>永庆路</td><td>东方大道-普惠路</td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=MVK&amp;StandName=打三港\">打三港</a></td><td>MVK</td><td>吴中区</td><td>车坊金山路</td><td></td><td>南</td></tr><tr><td><a href=\"default.aspx?StandCode=MVF&amp;StandName=打三港\">打三港</a></td><td>MVF</td><td>吴中区</td><td>车坊金山路</td><td></td><td>北</td></tr><tr><td><a href=\"default.aspx?StandCode=BUS&amp;StandName=公交三场\">公交三场</a></td><td>BUS</td><td>高新区</td><td>塔园路</td><td>竹园路-玉山路</td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=EZD&amp;StandName=公交三场\">公交三场</a></td><td>EZD</td><td>高新区</td><td>塔园路</td><td>竹园路-玉山路</td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=NGN&amp;StandName=广济北路三\">广济北路三</a></td><td>NGN</td><td>相城区</td><td>广济北路</td><td>312国道-阳澄湖西路</td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=NHG&amp;StandName=广济北路三\">广济北路三</a></td><td>NHG</td><td>相城区</td><td>广济北路</td><td></td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=ABC&amp;StandName=国泰三村东\">国泰三村东</a></td><td>ABC</td><td>吴中区</td><td>无名路</td><td></td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=ABA&amp;StandName=国泰三村东\">国泰三村东</a></td><td>ABA</td><td>吴中区</td><td>无名路</td><td></td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=ABE&amp;StandName=国泰三村南门\">国泰三村南门</a></td><td>ABE</td><td>吴中区</td><td>无名路</td><td></td><td>北</td></tr><tr><td><a href=\"default.aspx?StandCode=ABF&amp;StandName=国泰三村南门\">国泰三村南门</a></td><td>ABF</td><td>吴中区</td><td>无名路</td><td></td><td>南</td></tr><tr><td><a href=\"default.aspx?StandCode=FYG&amp;StandName=横泾三工区\">横泾三工区</a></td><td>FYG</td><td>吴中区</td><td>木东公路</td><td>吴中大道－姑苏路</td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=DNC&amp;StandName=横泾三工区\">横泾三工区</a></td><td>DNC</td><td>吴中区</td><td>木东公路</td><td>吴中大道－姑苏路</td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=DZF&amp;StandName=华通花园三区\">华通花园三区</a></td><td>DZF</td><td>高新区</td><td>无名路</td><td></td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=FPN&amp;StandName=华通花园三区\">华通花园三区</a></td><td>FPN</td><td>高新区</td><td>东唐路</td><td></td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=AZK&amp;StandName=黄桥工业三区\">黄桥工业三区</a></td><td>AZK</td><td>相城区</td><td>永方路</td><td></td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=ABG&amp;StandName=黄桥工业三区\">黄桥工业三区</a></td><td>ABG</td><td>相城区</td><td>永方路</td><td></td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=FXM&amp;StandName=莲花路三\">莲花路三</a></td><td>FXM</td><td>工业园区</td><td>莲花路</td><td>阳澄湖大道－重元寺</td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=GJW&amp;StandName=莲花路三\">莲花路三</a></td><td>GJW</td><td>工业园区</td><td>莲花路</td><td>阳澄湖大道－重元寺</td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=GDX&amp;StandName=莲花新村三区\">莲花新村三区</a></td><td>GDX</td><td>工业园区</td><td>东延路</td><td>车斜路-莲池街</td><td>南</td></tr><tr><td><a href=\"default.aspx?StandCode=GWY&amp;StandName=莲花新村三区\">莲花新村三区</a></td><td>GWY</td><td>工业园区</td><td>东延路</td><td>车斜路-莲池街</td><td>北</td></tr><tr><td><a href=\"default.aspx?StandCode=KVD&amp;StandName=龙景花园三区\">龙景花园三区</a></td><td>KVD</td><td>高新区</td><td>无名路</td><td></td><td>南</td></tr><tr><td><a href=\"default.aspx?StandCode=JYY&amp;StandName=龙景花园三区\">龙景花园三区</a></td><td>JYY</td><td>高新区</td><td>无名路</td><td></td><td>北</td></tr><tr><td><a href=\"default.aspx?StandCode=EWW&amp;StandName=龙景花园三四区\">龙景花园三四区</a></td><td>EWW</td><td>高新区</td><td></td><td></td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=DFP&amp;StandName=龙景花园三四区\">龙景花园三四区</a></td><td>DFP</td><td>高新区</td><td></td><td></td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=GYB&amp;StandName=浦庄大道三千方桥\">浦庄大道三千方桥</a></td><td>GYB</td><td>吴中区</td><td>浦庄大道</td><td></td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=GMR&amp;StandName=浦庄大道三千方桥\">浦庄大道三千方桥</a></td><td>GMR</td><td>吴中区</td><td>浦庄大道</td><td></td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=EWB&amp;StandName=群星三路\">群星三路</a></td><td>EWB</td><td>工业园区</td><td>通园路</td><td>东方大道-东兴路</td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=JBT&amp;StandName=群星三路\">群星三路</a></td><td>JBT</td><td>工业园区</td><td>通园路</td><td>东方大道-东兴路</td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=BJP&amp;StandName=三多巷\">三多巷</a></td><td>BJP</td><td>沧浪区</td><td>书院巷</td><td>人民路－东大街</td><td>北</td></tr><tr><td><a href=\"default.aspx?StandCode=DDG&amp;StandName=三多巷\">三多巷</a></td><td>DDG</td><td>沧浪区</td><td>书院巷</td><td>人民路－东大街</td><td>南</td></tr><tr><td><a href=\"default.aspx?StandCode=NBF&amp;StandName=三姑里\">三姑里</a></td><td>NBF</td><td>吴中区</td><td>长浜路</td><td>东方大道-苏同黎路</td><td>南</td></tr><tr><td><a href=\"default.aspx?StandCode=NBN&amp;StandName=三姑里\">三姑里</a></td><td>NBN</td><td>吴中区</td><td>长浜路</td><td>东方大道-苏同黎路</td><td>北</td></tr><tr><td><a href=\"default.aspx?StandCode=EBX&amp;StandName=三官塘\">三官塘</a></td><td>EBX</td><td>相城区</td><td>无名路</td><td></td><td>南</td></tr><tr><td><a href=\"default.aspx?StandCode=DFN&amp;StandName=三官塘\">三官塘</a></td><td>DFN</td><td>相城区</td><td>无名路</td><td></td><td>北</td></tr><tr><td><a href=\"default.aspx?StandCode=NXK&amp;StandName=三家村\">三家村</a></td><td>NXK</td><td>相城区</td><td>苏嘉杭便道</td><td>春申湖东路-如元路</td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=NXM&amp;StandName=三家村\">三家村</a></td><td>NXM</td><td>相城区</td><td>苏嘉杭便道</td><td>春申湖东路-如元路</td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=NCC&amp;StandName=三马村\">三马村</a></td><td>NCC</td><td>吴中区</td><td>长浜路</td><td>东方大道-苏同黎路</td><td>北</td></tr><tr><td><a href=\"default.aspx?StandCode=NBA&amp;StandName=三马村\">三马村</a></td><td>NBA</td><td>吴中区</td><td>长浜路</td><td>东方大道-苏同黎路</td><td>南</td></tr><tr><td><a href=\"default.aspx?StandCode=NSV&amp;StandName=三毗村\">三毗村</a></td><td>NSV</td><td>吴中区</td><td>松海路</td><td></td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=NSW&amp;StandName=三毗村\">三毗村</a></td><td>NSW</td><td>吴中区</td><td>松海路</td><td></td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=HBR&amp;StandName=三千方桥\">三千方桥</a></td><td>HBR</td><td>吴中区</td><td>木东公路</td><td></td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=FHX&amp;StandName=三千方桥\">三千方桥</a></td><td>FHX</td><td>吴中区</td><td>木东公路</td><td></td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=GKD&amp;StandName=三塘村\">三塘村</a></td><td>GKD</td><td>吴中区</td><td>东山大道</td><td></td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=ACZ&amp;StandName=三塘村\">三塘村</a></td><td>ACZ</td><td>吴中区</td><td>东山大道</td><td></td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=AGJ&amp;StandName=三香公园\">三香公园</a></td><td>AGJ</td><td>沧浪区</td><td>三香路</td><td>阊胥路－桐泾南路</td><td>北</td></tr><tr><td><a href=\"default.aspx?StandCode=GER&amp;StandName=三香公园\">三香公园</a></td><td>GER</td><td>沧浪区</td><td>三香路</td><td>阊胥路－桐泾南路</td><td>南</td></tr><tr><td><a href=\"default.aspx?StandCode=GRU&amp;StandName=三香新村\">三香新村</a></td><td>GRU</td><td>沧浪区</td><td>三香路</td><td>阊胥路－桐泾南路</td><td>南</td></tr><tr><td><a href=\"default.aspx?StandCode=EZA&amp;StandName=三香新村\">三香新村</a></td><td>EZA</td><td>沧浪区</td><td>三香路</td><td>阊胥路－桐泾南路</td><td>北</td></tr><tr><td><a href=\"default.aspx?StandCode=CEZ&amp;StandName=三星显示器\">三星显示器</a></td><td>CEZ</td><td>工业园区</td><td>方洲路</td><td>长阳街-凤里街</td><td>南</td></tr><tr><td><a href=\"default.aspx?StandCode=FMC&amp;StandName=三星显示器\">三星显示器</a></td><td>FMC</td><td>工业园区</td><td>方洲路</td><td>长阳街-凤里街</td><td>北</td></tr><tr><td><a href=\"default.aspx?StandCode=DNB&amp;StandName=三洋\">三洋</a></td><td>DNB</td><td>吴中区</td><td>孙武路</td><td>金山路-灵天路</td><td>南</td></tr><tr><td><a href=\"default.aspx?StandCode=EKZ&amp;StandName=三洋\">三洋</a></td><td>EKZ</td><td>吴中区</td><td>孙武路</td><td>金山路-灵天路</td><td>北</td></tr><tr><td><a href=\"default.aspx?StandCode=JTX&amp;StandName=三洋村北\">三洋村北</a></td><td>JTX</td><td>高新区</td><td>太湖大道</td><td></td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=JTY&amp;StandName=三洋村北\">三洋村北</a></td><td>JTY</td><td>高新区</td><td>太湖大道</td><td></td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=JUC&amp;StandName=三洋村南\">三洋村南</a></td><td>JUC</td><td>高新区</td><td>太湖大道</td><td></td><td>北</td></tr><tr><td><a href=\"default.aspx?StandCode=JUB&amp;StandName=三洋村南\">三洋村南</a></td><td>JUB</td><td>高新区</td><td>太湖大道</td><td></td><td>南</td></tr><tr><td><a href=\"default.aspx?StandCode=JTZ&amp;StandName=三洋港桥\">三洋港桥</a></td><td>JTZ</td><td>高新区</td><td>太湖大道</td><td></td><td>南</td></tr><tr><td><a href=\"default.aspx?StandCode=JUA&amp;StandName=三洋港桥\">三洋港桥</a></td><td>JUA</td><td>高新区</td><td>太湖大道</td><td></td><td>北</td></tr><tr><td><a href=\"default.aspx?StandCode=ANU&amp;StandName=三元坊\">三元坊</a></td><td>ANU</td><td>沧浪区</td><td>人民路</td><td>竹辉路-十全街</td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=DWC&amp;StandName=三元坊\">三元坊</a></td><td>DWC</td><td>沧浪区</td><td>人民路</td><td>竹辉路-十全街</td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=MXD&amp;StandName=三元坊（十全街）\">三元坊（十全街）</a></td><td>MXD</td><td>沧浪区</td><td>十全街</td><td>乌鹊桥路-人民路</td><td>南</td></tr><tr><td><a href=\"default.aspx?StandCode=MWZ&amp;StandName=三元坊（十全街）\">三元坊（十全街）</a></td><td>MWZ</td><td>沧浪区</td><td>十全街</td><td>乌鹊桥路-人民路</td><td>北</td></tr><tr><td><a href=\"default.aspx?StandCode=CSM&amp;StandName=三元新村\">三元新村</a></td><td>CSM</td><td>金阊区</td><td>西环路</td><td>干将路－金门路</td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=CXK&amp;StandName=三元新村\">三元新村</a></td><td>CXK</td><td>金阊区</td><td>西环路</td><td>干将路－金门路</td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=PSB&amp;StandName=新浒花园三区\">新浒花园三区</a></td><td>PSB</td><td>高新区</td><td>永莲路</td><td></td><td>东</td></tr><tr><td><a href=\"default.aspx?StandCode=PSC&amp;StandName=新浒花园三区\">新浒花园三区</a></td><td>PSC</td><td>高新区</td><td>永莲路</td><td></td><td>西</td></tr><tr><td><a href=\"default.aspx?StandCode=DMZ&amp;StandName=新浒花园三区北门\">新浒花园三区北门</a></td><td>DMZ</td><td>高新区</td><td>浒扬路</td><td></td><td>北</td></tr><tr><td><a href=\"default.aspx?StandCode=GPZ&amp;StandName=新浒花园三区北门\">新浒花园三区北门</a></td><td>GPZ</td><td>高新区</td><td>浒扬路</td><td></td><td>南</td></tr></table></span>");
		return true;
	}

	private boolean initTestLineStation(StringBuilder sb) {
		sb.append("<span id=\"ctl00_MainContent_Message\"><?xml version=\"1.0\" encoding=\"utf-16\"?><table cellspacing=\"0\" border=\"1\"><tr><th>站台</th><th>编号</th><th>车牌</th><th>进站时间</th></tr><tr><td><a href=\"default.aspx?StandCode=HMF&amp;StandName=绿宝广场首末站\">绿宝广场首末站</a></td><td>HMF</td><td>苏E-R9172</td><td>09:52:40</td></tr><tr><td><a href=\"default.aspx?StandCode=FDJ&amp;StandName=客运西站\">客运西站</a></td><td>FDJ</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=CCM&amp;StandName=苏州乐园\">苏州乐园</a></td><td>CCM</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=DZJ&amp;StandName=百合花公寓\">百合花公寓</a></td><td>DZJ</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=DRG&amp;StandName=飞利浦\">飞利浦</a></td><td>DRG</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=HBD&amp;StandName=锦华苑\">锦华苑</a></td><td>HBD</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=CJE&amp;StandName=新城花园酒店\">新城花园酒店</a></td><td>CJE</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=EGK&amp;StandName=附二医院\">附二医院</a></td><td>EGK</td><td>苏E-R9176</td><td>09:53:09</td></tr><tr><td><a href=\"default.aspx?StandCode=ARK&amp;StandName=彩虹新村\">彩虹新村</a></td><td>ARK</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=DVV&amp;StandName=彩香新村\">彩香新村</a></td><td>DVV</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=GRU&amp;StandName=三香新村\">三香新村</a></td><td>GRU</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=GER&amp;StandName=三香公园\">三香公园</a></td><td>GER</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=FBU&amp;StandName=胥门\">胥门</a></td><td>FBU</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=FUE&amp;StandName=新市桥北\">新市桥北</a></td><td>FUE</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=FPS&amp;StandName=新市桥南\">新市桥南</a></td><td>FPS</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=ARM&amp;StandName=盘溪新村\">盘溪新村</a></td><td>ARM</td><td>苏E-R9129</td><td>09:53:32</td></tr><tr><td><a href=\"default.aspx?StandCode=AEE&amp;StandName=水香六村\">水香六村</a></td><td>AEE</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=BUY&amp;StandName=城西中学\">城西中学</a></td><td>BUY</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=HXS&amp;StandName=吴中西路南\">吴中西路南</a></td><td>HXS</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=HXP&amp;StandName=宝带西路北\">宝带西路北</a></td><td>HXP</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=JFH&amp;StandName=龙西路南\">龙西路南</a></td><td>JFH</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=ABM&amp;StandName=大华调剂市场\">大华调剂市场</a></td><td>ABM</td><td>苏E-R9186</td><td>09:50:21</td></tr><tr><td><a href=\"default.aspx?StandCode=CZJ&amp;StandName=新家桥\">新家桥</a></td><td>CZJ</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=DGT&amp;StandName=嘉福桥\">嘉福桥</a></td><td>DGT</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=CPB&amp;StandName=苏蠡花园\">苏蠡花园</a></td><td>CPB</td><td>苏E-R9177</td><td>09:53:40</td></tr><tr><td><a href=\"default.aspx?StandCode=BVC&amp;StandName=跃进桥\">跃进桥</a></td><td>BVC</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=FCD&amp;StandName=蠡墅\">蠡墅</a></td><td>FCD</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=FUR&amp;StandName=复兴桥\">复兴桥</a></td><td>FUR</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=BEW&amp;StandName=苏蠡路彭泾\">苏蠡路彭泾</a></td><td>BEW</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=CXH&amp;StandName=莫舍桥东\">莫舍桥东</a></td><td>CXH</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=DWN&amp;StandName=石湖华城(石湖之韵)\">石湖华城(石湖之韵)</a></td><td>DWN</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=HDS&amp;StandName=国际教育园工职院\">国际教育园工职院</a></td><td>HDS</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=CBS&amp;StandName=国际教育园建设交通校\">国际教育园建设交通校</a></td><td>CBS</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=BPW&amp;StandName=国际教育园旅游财经校\">国际教育园旅游财经校</a></td><td>BPW</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=CNS&amp;StandName=希文路\">希文路</a></td><td>CNS</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=GMJ&amp;StandName=一川街\">一川街</a></td><td>GMJ</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=EAR&amp;StandName=文正学院\">文正学院</a></td><td>EAR</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=HDT&amp;StandName=蓝缨学校\">蓝缨学校</a></td><td>HDT</td><td></td><td></td></tr><tr><td><a href=\"default.aspx?StandCode=REE&amp;StandName=越溪首末站南\">越溪首末站南</a></td><td>REE</td><td>苏E-R9183</td><td>09:45:59</td></tr></table></span>");
		return true;
	}

	private boolean initTestStationLine(StringBuilder sb) {
		sb.append("<span id=\"ctl00_MainContent_Message\"><?xml version=\"1.0\" encoding=\"utf-16\"?><table cellspacing=\"0\" border=\"1\"><tr><th>线路</th><th>方向</th><th>车牌</th><th>更新时间</th><th>站距</th></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=e24505a4-704a-4e8f-b136-3d9bee315353&amp;LineInfo=2(津梁街首末站=&gt;灵岩山首末站)\">2</a></td><td>津梁街首末站=&gt;灵岩山首末站</td><td>苏ES8790</td><td>9:45:08</td><td>6</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=4e398d14-87c7-4488-8b37-ecc72dcad1d7&amp;LineInfo=262(沪-解)\">262</a></td><td>沪-解</td><td>苏E-2G631</td><td>09:50:32</td><td>5</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=59ba611f-6438-469f-9c43-95faf815af9d&amp;LineInfo=303(石路南=&gt;阳山花苑)\">303</a></td><td>石路南=&gt;阳山花苑</td><td>苏e-4e890</td><td>09:51:10</td><td>2</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=5d55c8c3-9e54-48cc-b7b2-e67fa4873242&amp;LineInfo=304(枫桥工业园)\">304</a></td><td>枫桥工业园</td><td>苏E-33471</td><td>09:51:59</td><td>1</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=77ae98b2-96b3-4a4c-9916-885b5c995e50&amp;LineInfo=308(马涧小区)\">308</a></td><td>马涧小区</td><td>苏E-35263</td><td>09:50:50</td><td>4</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=b7c1cf12-6e1b-43d2-a7ef-dcd433d1ef9f&amp;LineInfo=312(解放—天池山)\">312</a></td><td>解放—天池山</td><td>无</td><td></td><td>无车</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=f31f856a-5e07-41c3-8af4-98a3590ff8ed&amp;LineInfo=317(火-金)\">317</a></td><td>火-金</td><td>苏E-49851</td><td>09:51:39</td><td>5</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=49f30453-c47e-427a-9eb9-46a80628b1ed&amp;LineInfo=321(解-树)\">321</a></td><td>解-树</td><td>无</td><td></td><td>无车</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=25cffd2c-e09c-4b8b-bc9d-d8526c15e60e&amp;LineInfo=35(马涧小区)\">35</a></td><td>马涧小区</td><td>苏E-4D488</td><td>09:50:03</td><td>3</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=b8b1a2ae-dfdf-4454-85f7-980298a6639b&amp;LineInfo=38(孙庄路)\">38</a></td><td>孙庄路</td><td>苏E-10847</td><td>09:51:55</td><td>4</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=2f3022aa-d016-455f-8d42-e96fc113ec41&amp;LineInfo=39(太湖东路=&gt;茶乌浜)\">39</a></td><td>太湖东路=&gt;茶乌浜</td><td>苏e-s1068</td><td>09:44:49</td><td>1</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=c32f4e6f-1f2e-4538-9ecb-82b02e915bb4&amp;LineInfo=40(西线)\">40</a></td><td>西线</td><td>苏E-S0691</td><td>09:51:51</td><td>进站</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=3854deba-8258-451f-afdf-abec6653acff&amp;LineInfo=502(火-东)\">502</a></td><td>火-东</td><td>苏E-S0993</td><td>09:52:08</td><td>6</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=A8BE1153-A8C9-BD7C-7D43-DA4423AC0F33&amp;LineInfo=51(越溪首末站=&gt;绿宝广场首末站)\">51</a></td><td>越溪首末站=&gt;绿宝广场首末站</td><td>苏E-R9192</td><td>09:51:05</td><td>11</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=f5c24669-546d-4eed-9c0d-c7b5dc5283a1&amp;LineInfo=511(灵岩山)\">511</a></td><td>灵岩山</td><td>苏E-35392</td><td>09:51:36</td><td>1</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=93f43d00-1a5e-4699-b382-a00cc34a00c0&amp;LineInfo=60(体育中心首末站南)\">60</a></td><td>体育中心首末站南</td><td>苏E-35322</td><td>09:51:45</td><td>进站</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=177d69c7-ac20-47ba-8b58-e5256f1cb237&amp;LineInfo=60(体育中心首末站北)\">60</a></td><td>体育中心首末站北</td><td>苏E-4F733</td><td>09:45:41</td><td>进站</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=77327e9b-9326-4fee-bbfa-e4ad289e0ea0&amp;LineInfo=68(新升新苑)\">68</a></td><td>新升新苑</td><td>苏E-90057</td><td>09:50:46</td><td>进站</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=F67551FF-D586-931B-CB6C-05CBF39BC869&amp;LineInfo=69(火车站北广场=&gt;胥香园首末站)\">69</a></td><td>火车站北广场=&gt;胥香园首末站</td><td>苏E-Q9609</td><td>09:46:53</td><td>10</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=7D0D5717-875A-88C0-1658-97E0E5FF2991&amp;LineInfo=69(火车站北广场=&gt;石公山首末站)\">69</a></td><td>火车站北广场=&gt;石公山首末站</td><td>苏E-R0447</td><td>09:50:38</td><td>2</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=7ABB4E89-A8E3-19DD-D60A-4DA723703BB4&amp;LineInfo=89(珠江社区服务中心停车场)\">89</a></td><td>珠江社区服务中心停车场</td><td>苏E-2F917</td><td>09:51:56</td><td>1</td></tr><tr><td><a href=\"APTSLine.aspx?LineGuid=57b0ff12-962b-45ae-9fe9-31458620e26b&amp;LineInfo=931(新庄立交)\">931</a></td><td>新庄立交</td><td>苏E-2L372</td><td>09:52:01</td><td>进站</td></tr></table></span>");
		return true;
	}

}
