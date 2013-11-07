package com.ethan.mlife.activity.bus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.ethan.mlife.R;
import com.ethan.mlife.adapter.StationAdapter;
import com.ethan.mlife.common.Constants;
import com.ethan.mlife.common.FavoriteBusStyle;
import com.ethan.mlife.dao.MyBusFactory;
import com.ethan.mlife.entity.FavoriteBus;
import com.ethan.mlife.entity.Line;
import com.ethan.mlife.entity.Station;
import com.ethan.mlife.task.StationAsyncTask;
import com.ethan.mlife.util.StringUtil;

public class DetailStationActivity extends DetailBase {
	/**
	 * 可切换站台
	 */
	private ArrayList<Station> listStation;

	@Override
	protected int getContentLayoutId() {
		return R.layout.homebusstationdetail;
	}

	@Override
	protected int getRootViewId() {
		return R.id.llBusStationDetail;
	}

	@Override
	protected void initActivity() {
		this.listStation = this.getIntent().getParcelableArrayListExtra(
				Constants.BUNDLE_LIST_ITEM_NAME);
		this.switchSize = listStation.size();
		listAdapter = new StationAdapter(this, new ArrayList<Line>());

		listViewItemClickListener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent();
				intent.setAction(getString(R.string.action_bus_line_detail));
				Bundle bundle = new Bundle();
				// 站台信息传递给目标
				ArrayList<Line> listLine = new ArrayList<Line>();
				listLine.add((Line) listAdapter.getItem(position));

				bundle.putParcelableArrayList(Constants.BUNDLE_LIST_ITEM_NAME,
						listLine);
				bundle.putInt(Constants.BUNDLE_SELECTED_POSITION_NAME, 0);
				intent.putExtras(bundle);
				startActivityForResult(intent, RESULT_OK);
			}
		};
	}

	@Override
	protected void loadBusData() {
		new StationAsyncTask(waitLayout, functionLayout, listView,
				listStation.get(curSelectedPosition), scrollPosition,
				(StationAdapter) listAdapter).execute();
	}

	@Override
	protected void favorite() {
		List<FavoriteBus> listFavorite = new ArrayList<FavoriteBus>();
		for (int i = 0; i < listStation.size(); i++) {
			Station station = listStation.get(i);
			FavoriteBus favorite = new FavoriteBus();
			favorite.setGuid(UUID.randomUUID().toString());
			favorite.setCityRegion(MyBusFactory.getMyBus().getRegion());
			favorite.setFavoriteName(station.getName());
			favorite.setBusType(FavoriteBusStyle.Station.ordinal());
			favorite.setUrl(station.getUrlLink());

			if (!StringUtil.isNullOrEmpty(station.getStreet())
					&& !StringUtil.isNullOrEmpty(station.getDirection())) {
				// 从线路跳转到站台时没有方向信息
				favorite.setDemo(String.format("%s:%s", station.getStreet(),
						station.getDirection()));
			}
			if (this.curSelectedPosition == i)
				favorite.setVisibility(FavoriteBus.VISIBLE);
			else
				favorite.setVisibility(FavoriteBus.HIDDEN);
			favorite.setClickCount(0);
			favorite.setInsertTime(new Date());
			favorite.setUpdateTime(new Date());

			listFavorite.add(favorite);
		}
		if (!listFavorite.isEmpty() && listFavorite.size() > 0) {
			MyBusFactory.getMyBus().getBusDao().saveToFavorite(listFavorite);
			Toast.makeText(this, R.string.message_mlife_bus_favorite,
					Toast.LENGTH_SHORT).show();
		}
	}
}
