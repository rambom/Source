package com.ethan.mlife.activity.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ethan.mlife.R;
import com.ethan.mlife.adapter.FavoriteAdapter;
import com.ethan.mlife.common.Constants;
import com.ethan.mlife.common.FavoriteBusStyle;
import com.ethan.mlife.dao.MyBusFactory;
import com.ethan.mlife.entity.FavoriteBus;
import com.ethan.mlife.entity.Line;
import com.ethan.mlife.entity.Station;
import com.ethan.mlife.task.FavoriteAsyncTask;

public class ListFavoriteActivity extends Activity {
	/**
	 * 进度条
	 */
	protected LinearLayout waitLayout;
	private ListView listFavorite;
	private LinearLayout llFavoriteMessage = null;
	public static boolean refreshFlag = false;

	/**
	 * 数据适配器
	 */
	private FavoriteAdapter favoriteAdapter;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			favoriteAdapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

		if (refreshFlag) {// 需要更新收藏列表
			new FavoriteAsyncTask(waitLayout, listFavorite, llFavoriteMessage,
					favoriteAdapter).execute();
			refreshFlag = false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.homebusfavorite);
		favoriteAdapter = new FavoriteAdapter(this,
				new ArrayList<FavoriteBus>());
		listFavorite = (ListView) this.findViewById(R.id.listDataView);
		listFavorite.setAdapter(favoriteAdapter);
		listFavorite.setOnItemClickListener(favoriteBusClickListener);
		llFavoriteMessage = (LinearLayout) this
				.findViewById(R.id.llFavoriteMessage);
		waitLayout = (LinearLayout) findViewById(R.id.llDataLoadingWait);
		waitLayout.setVisibility(LinearLayout.GONE);
		new FavoriteAsyncTask(waitLayout, listFavorite, llFavoriteMessage,
				favoriteAdapter).execute();
	}

	public void deleteFavorite_Click(View v) {
		int intPosition = Integer.parseInt(v.getTag().toString());
		String strGuid = favoriteAdapter.getItem(intPosition).getGuid();
		this.favoriteAdapter.getDataSource().remove(intPosition);
		this.favoriteAdapter.notifyDataSetChanged();
		if (this.favoriteAdapter.getCount() <= 0) {
			this.llFavoriteMessage.setVisibility(LinearLayout.VISIBLE);
		}

		FavoriteBus favorite = new FavoriteBus();
		favorite.setGuid(strGuid);
		// 从数据库中删除记录
		MyBusFactory.getMyBus().getBusDao().deleteFavoriteBus(favorite);

	}

	private OnItemClickListener favoriteBusClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			FavoriteBus favorite = (FavoriteBus) favoriteAdapter
					.getItem(position);
			favorite.setClickCount(favorite.getClickCount() + 1);

			// 异步刷新listView
			new Thread() {
				@Override
				public void run() {
					try {
						Collections.sort(favoriteAdapter.getDataSource(),
								new Comparator<FavoriteBus>() {
									public int compare(FavoriteBus object1,
											FavoriteBus object2) {
										// 按点击数排序
										return object2
												.getClickCount()
												.compareTo(
														object1.getClickCount());
									}
								});
						// 暂停1秒,不立即显示排序变化
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
					}
					handler.sendEmptyMessage(0);
				}
			}.start();

			// 更新点击次数
			FavoriteBus where = new FavoriteBus();
			where.setGuid(favorite.getGuid());
			where.setCityRegion(favorite.getCityRegion());
			where.setFavoriteName(favorite.getFavoriteName());
			where.setBusType(favorite.getBusType());

			FavoriteBus set = new FavoriteBus();
			set.setClickCount(favorite.getClickCount());
			set.setUpdateTime(new Date());
			MyBusFactory.getMyBus().getBusDao().updateFavoriteBus(where, set);
			switch (FavoriteBusStyle.values()[favorite.getBusType()]) {
			case Line:
				goToLineDetail(favorite);
				break;
			case Station:
				goToStationDetail(favorite);
				break;
			}
		}
	};

	/**
	 * 查看线路详情
	 * 
	 * @param favorite
	 */
	private void goToLineDetail(FavoriteBus favorite) {
		Intent intent = new Intent();
		intent.setAction(getString(R.string.action_bus_line_detail));
		Bundle bundle = new Bundle();
		// 相同线路传递到目标活动
		ArrayList<Line> listLine = new ArrayList<Line>();
		List<FavoriteBus> listFavorite = MyBusFactory.getMyBus().getBusDao()
				.getSwitchFavoriteBus(favorite);
		// 记录当前选择线路索引并传递到目标活动
		int intCurLine = 0;

		// 按线路号遍历记录
		for (int i = 0; i < listFavorite.size(); ++i) {
			FavoriteBus fav = listFavorite.get(i);

			if (favorite.getGuid().equals(fav.getGuid()))
				intCurLine = i;

			Line line = new Line();
			line.setId(fav.getGuid());
			line.setCityRegion(fav.getCityRegion());
			line.setLineNo(fav.getFavoriteName());
			line.setUrlLink(fav.getUrl());
			line.setDirection(fav.getDemo());
			listLine.add(line);
		}

		if (listLine.isEmpty() && listLine.size() == 0) {
			Line line = new Line();
			line.setId(favorite.getGuid());
			line.setCityRegion(favorite.getCityRegion());
			line.setLineNo(favorite.getFavoriteName());
			line.setUrlLink(favorite.getUrl());
			line.setDirection(favorite.getDemo());
			listLine.add(line);
		}
		bundle.putParcelableArrayList(Constants.BUNDLE_LIST_ITEM_NAME, listLine);
		bundle.putInt(Constants.BUNDLE_SELECTED_POSITION_NAME, intCurLine);
		bundle.putBoolean(Constants.BUNDLE_IS_FROM_FAVORITE, true);
		intent.putExtras(bundle);
		startActivityForResult(intent, RESULT_OK);
	}

	/**
	 * 查看站台详情
	 * 
	 * @param favorite
	 */
	private void goToStationDetail(FavoriteBus favorite) {
		Intent intent = new Intent();
		intent.setAction(getString(R.string.action_bus_station_detail));
		Bundle bundle = new Bundle();

		// 记录当前站台索引并传递到目标活动
		int intCurStation = 0;
		// 相同站台记录传递到目标活动
		ArrayList<Station> listStation = new ArrayList<Station>();
		List<FavoriteBus> listFavorite = MyBusFactory.getMyBus().getBusDao()
				.getSwitchFavoriteBus(favorite);

		// 按站台名称遍历记录
		for (int i = 0; i < listFavorite.size(); i++) {
			FavoriteBus fav = listFavorite.get(i);
			if (fav.getGuid().equals(favorite.getGuid())) {
				intCurStation = i;
			}
			Station station = new Station();
			station.setId(fav.getGuid());
			station.setCityRegion(fav.getCityRegion());
			station.setName(fav.getFavoriteName());
			station.setUrlLink(fav.getUrl());
			station.setDescription(fav.getDemo());
			listStation.add(station);
		}
		if (listStation.isEmpty() && listStation.size() == 0) {
			Station station = new Station();
			station.setId(favorite.getGuid());
			station.setCityRegion(favorite.getCityRegion());
			station.setName(favorite.getFavoriteName());
			station.setUrlLink(favorite.getUrl());
			station.setDescription(favorite.getDemo());
			listStation.add(station);
		}

		bundle.putParcelableArrayList(Constants.BUNDLE_LIST_ITEM_NAME,
				listStation);
		bundle.putInt(Constants.BUNDLE_SELECTED_POSITION_NAME, intCurStation);
		bundle.putBoolean(Constants.BUNDLE_IS_FROM_FAVORITE, true);
		intent.putExtras(bundle);
		startActivityForResult(intent, RESULT_OK);
	}
}
