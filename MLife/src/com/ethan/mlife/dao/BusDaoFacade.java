package com.ethan.mlife.dao;

import java.util.List;

import com.ethan.mlife.entity.FavoriteBus;
import com.ethan.mlife.entity.Line;
import com.ethan.mlife.entity.Station;

/**
 * 公交数据接口
 * 
 * @author Ethan
 * 
 */
public interface BusDaoFacade {
	/**
	 * 获取线路列表
	 * 
	 * @param line
	 * @return
	 */
	List<Line> getBusLine(Line line);

	/**
	 * 获取站台列表
	 * 
	 * @param station
	 * @return
	 */
	List<Station> getBusStation(Station station);

	/**
	 * 获取线路站台信息
	 * 
	 * @param line
	 * @return
	 */
	List<Station> getLineStation(Line line);

	/**
	 * 获取站台线路信息
	 * 
	 * @param station
	 * @return
	 */
	List<Line> getStationLine(Station station);

	/**
	 * 获取收藏公交
	 * 
	 * @return
	 */
	List<FavoriteBus> getFavoriteBus();

	/**
	 * 添加收藏
	 * 
	 * @param listFavorite
	 * @return
	 */
	boolean saveToFavorite(List<FavoriteBus> listFavorite);

	/**
	 * 获取可切换的收藏
	 * 
	 * @param favorite
	 * @return
	 */
	List<FavoriteBus> getSwitchFavoriteBus(FavoriteBus favorite);

	/**
	 * 更新收藏信息
	 * 
	 * @param favoriteGuid
	 * @param favorite
	 * @return
	 */
	boolean updateFavoriteBus(FavoriteBus where, FavoriteBus favorite);

	/**
	 * 删除收藏信息
	 * 
	 * @param favorite
	 * @return
	 */
	boolean deleteFavoriteBus(FavoriteBus favorite);
}
