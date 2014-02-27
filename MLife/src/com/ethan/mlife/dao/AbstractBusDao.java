package com.ethan.mlife.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.ethan.mlife.dao.impl.BusLineDaoImpl;
import com.ethan.mlife.dao.impl.BusStationDaoImpl;
import com.ethan.mlife.dao.impl.FavoriteBusDaoImpl;
import com.ethan.mlife.entity.FavoriteBus;
import com.ethan.mlife.entity.Line;
import com.ethan.mlife.entity.Station;

public abstract class AbstractBusDao implements BusDaoFacade {

	protected final String searchBusLinePostParamName = "ctl00$MainContent$LineName";
	protected final String searchBusStationPostParamName = "ctl00$MainContent$StandName";
	/**
	 * 鍏氦鍒楄〃鏁版嵁琛�
	 */
	protected final String busMainContentRow = "#MainContent_DATA table tr";
	/**
	 * 鍏氦鏌ヨ鏍圭珯鐐�
	 */
	protected final String busBaseUrl = "http://www.szjt.gov.cn/apts/";
	/**
	 * 绾胯矾鏌ヨURL
	 */
	protected final String busLineUrl = "http://www.szjt.gov.cn/apts/APTSLine.aspx";
	/**
	 * 绔欏彴鏌ヨURL
	 */
	protected final String busStationUrl = "http://www.szjt.gov.cn/apts/default.aspx";

	/**
	 * 绾胯矾鏌ヨpost鍙傛暟
	 */
	protected Map<String, String> busLinePostParams = new HashMap<String, String>() {
		{
			put("__EVENTVALIDATION",
					"/wEWAwLeub7XBwL88Oh8AqX89aoK1GKT3VlKUTd/xyQgZexCetMuo/i/LRDnisAyha1YxN0=");
			put("__VIEWSTATE",
					"/wEPDwUJNDk3MjU2MjgyD2QWAmYPZBYCAgMPZBYCAgEPZBYCAgYPDxYCHgdWaXNpYmxlaGRkZLSbkOWJhbw7r9tBdPn33bPCSlJcKXww5ounfGoyhKl3");
			put("ctl00$MainContent$SearchLine", "");
		}
	};

	/**
	 * 绔欏彴鏌ヨpost鍙傛暟
	 */
	protected Map<String, String> busStationPostParams = new HashMap<String, String>() {
		{
			put("__EVENTVALIDATION",
					"/wEWBQKWw5ntCwLq+uyKCAKkmJj/DwL0+sTIDgLl5vKEDljsKhZ4wQv+QX4Mur5a1YA5Wv4LH4UmUVklzGAJcHPX");
			put("__VIEWSTATE",
					"/wEPDwULLTE5ODM5MjcxNzlkZMRDQaX2utUjbGHOIUP3iptC9gAY2I3YLCpB16qioLAI");
			put("ctl00$MainContent$SearchCode", "");
		}
	};
	/**
	 * 鍏氦鏀惰棌鎿嶄綔
	 */
	protected IFavoriteBusDao favoriteBusDao;
	/**
	 * 鍏氦绾胯矾鍘嗗彶鎿嶄綔
	 */
	protected IBusLineDao busLineDao;
	/**
	 * 鍏氦绔欏彴鍘嗗彶鎿嶄綔
	 */
	protected IBusStationDao busStationDao;
	/**
	 * url璇锋眰缂栫爜
	 */
	protected String urlEncode = "utf-8";

	protected AbstractBusDao() {
		favoriteBusDao = new FavoriteBusDaoImpl();
		busLineDao = new BusLineDaoImpl();
		busStationDao = new BusStationDaoImpl();
	}

	public abstract List<Line> getBusLine(Line line);

	public abstract List<Station> getBusStation(Station station);

	public abstract List<Station> getLineStation(Line line);

	public abstract List<Line> getStationLine(Station station);

	public List<FavoriteBus> getFavoriteBus() {
		// TODO Auto-generated method stub
		try {
			FavoriteBus query = new FavoriteBus();
			query.setCityRegion(MyBusFactory.getMyBus().getRegion());
			query.setVisibility(FavoriteBus.VISIBLE);
			return this.favoriteBusDao.query(query, null, null,
					"click_count desc,update_time desc");
		} catch (Exception ex) {
			Log.e(AbstractBusDao.this.toString(), ex.toString());
		}
		return new ArrayList<FavoriteBus>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.mlife.dao.BusDaoFacade#saveToFavorite(java.util.List)
	 */
	public boolean saveToFavorite(List<FavoriteBus> listFavorite) {
		// TODO Auto-generated method stub
		boolean blnReturn = false;
		try {
			if (null != listFavorite && !listFavorite.isEmpty()
					&& listFavorite.size() > 0) {
				FavoriteBus query = new FavoriteBus();
				query.setCityRegion(MyBusFactory.getMyBus().getRegion());
				query.setFavoriteName(listFavorite.get(0).getFavoriteName());
				query.setBusType(listFavorite.get(0).getBusType());
				// 鍒犻櫎宸插瓨鍦ㄧ殑鏁版嵁
				this.favoriteBusDao.delete(query);
			}

			for (FavoriteBus favorite : listFavorite) {
				favorite.setCityRegion(MyBusFactory.getMyBus().getRegion());
				// 娣诲姞鏀惰棌
				this.favoriteBusDao.insert(favorite);
				blnReturn = true;
			}
		} catch (Exception ex) {
			Log.e(AbstractBusDao.this.toString(), ex.toString());
		}
		return blnReturn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.mlife.dao.BusDaoFacade#getSwitchFavoriteBus(com.android.mlife
	 * .entity.FavoriteBus)
	 */
	public List<FavoriteBus> getSwitchFavoriteBus(FavoriteBus favorite) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		try {
			FavoriteBus query = new FavoriteBus();
			query.setCityRegion(favorite.getCityRegion());
			query.setFavoriteName(favorite.getFavoriteName());
			query.setBusType(favorite.getBusType());
			// 鎵惧埌鐩存帴杩斿洖
			return this.favoriteBusDao.query(query, null, null,
					"visibility desc");
		} catch (Exception ex) {
			Log.e(AbstractBusDao.this.toString(), ex.toString());
		}
		return new ArrayList<FavoriteBus>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.mlife.dao.BusDaoFacade#updateFavoriteBus(java.lang.String,
	 * com.android.mlife.entity.FavoriteBus)
	 */
	public boolean updateFavoriteBus(FavoriteBus where, FavoriteBus set) {
		// TODO Auto-generated method stub
		boolean blnReturn = false;
		try {
			this.favoriteBusDao.update(where, set);
			blnReturn = true;
		} catch (Exception ex) {
			Log.e(AbstractBusDao.this.toString(), ex.toString());
		}
		return blnReturn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.mlife.dao.BusDaoFacade#deleteFavoriteBus(com.android.mlife
	 * .entity.FavoriteBus)
	 */
	public boolean deleteFavoriteBus(FavoriteBus favorite) {
		// TODO Auto-generated method stub
		boolean blnReturn = false;
		try {
			this.favoriteBusDao.delete(favorite);
			blnReturn = true;
		} catch (Exception ex) {
			Log.e(AbstractBusDao.this.toString(), ex.toString());
		}
		return blnReturn;
	}

	/**
	 * 鍒犻櫎绾胯矾鍘嗗彶
	 * 
	 * @param line
	 * @return
	 */
	protected boolean deleteBusLine(Line line) {
		boolean blnReturn = false;
		try {
			this.busLineDao.delete(line);
			blnReturn = true;
		} catch (Exception ex) {
			Log.e(AbstractBusDao.this.toString(), ex.toString());
		}
		return blnReturn;
	}

	/**
	 * 鍒犻櫎绔欏彴鍘嗗彶
	 * 
	 * @param station
	 * @return
	 */
	protected boolean deleteBusStation(Station station) {
		boolean blnReturn = false;
		try {
			this.busStationDao.delete(station);
			blnReturn = true;
		} catch (Exception ex) {
			Log.e(AbstractBusDao.this.toString(), ex.toString());
		}
		return blnReturn;
	}

	/**
	 * 淇濆瓨绾胯矾鏌ヨ鍘嗗彶
	 * 
	 * @param line
	 * @return
	 */
	protected boolean saveBusLine(Line line) {
		boolean blnReturn = false;
		try {
			Line example = new Line();
			example.setCityRegion(MyBusFactory.getMyBus().getRegion());
			example.setId(line.getId());
			example.setLineNo(line.getLineNo());
			this.busLineDao.delete(example);
			line.setCityRegion(MyBusFactory.getMyBus().getRegion());
			this.busLineDao.insert(line);
			blnReturn = true;
		} catch (Exception ex) {
			Log.e(AbstractBusDao.this.toString(), ex.toString());
		}
		return blnReturn;
	}

	/**
	 * 淇濆瓨绔欏彴鏌ヨ鍘嗗彶
	 * 
	 * @param station
	 * @return
	 */
	protected boolean saveBusStation(Station station) {
		boolean blnReturn = false;
		try {
			Station example = new Station();
			example.setCityRegion(MyBusFactory.getMyBus().getRegion());
			example.setName(station.getName());
			example.setScode(station.getScode());
			this.busStationDao.delete(example);
			station.setCityRegion(MyBusFactory.getMyBus().getRegion());
			this.busStationDao.insert(station);
			blnReturn = true;
		} catch (Exception ex) {
			Log.e(AbstractBusDao.this.toString(), ex.toString());
		}
		return blnReturn;
	}

	protected List<Line> queryLineFromHis(Line line) {
		try {
			String strSql = String
					.format("city_Region='%s' and line_no like '%%%s%%' order by line_no",
							MyBusFactory.getMyBus().getRegion(),
							line.getLineNo(), line.getLineNo());
			return this.busLineDao.queryWithSql(strSql);
		} catch (Exception ex) {
			return new ArrayList<Line>();
		}
	}

	protected List<Station> queryStationFromHis(Station station) {
		try {
			String strSql = String.format(
					"city_Region='%s' and name like '%%%s%%' order by name",
					MyBusFactory.getMyBus().getRegion(), station.getName());
			return this.busStationDao.queryWithSql(strSql);
		} catch (Exception ex) {
			return new ArrayList<Station>();
		}
	}
}
