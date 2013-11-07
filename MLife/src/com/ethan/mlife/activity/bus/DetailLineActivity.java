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
import com.ethan.mlife.adapter.LineAdapter;
import com.ethan.mlife.common.Constants;
import com.ethan.mlife.common.FavoriteBusStyle;
import com.ethan.mlife.dao.MyBusFactory;
import com.ethan.mlife.entity.FavoriteBus;
import com.ethan.mlife.entity.Line;
import com.ethan.mlife.entity.Station;
import com.ethan.mlife.task.bus.LineAsyncTask;

public class DetailLineActivity extends DetailBase {

	/**
	 * 可切换线路
	 */
	private ArrayList<Line> listLine;

	@Override
	protected int getContentLayoutId() {
		return R.layout.homebuslinedetail;
	}

	@Override
	protected int getRootViewId() {
		return R.id.llBusLineDetail;
	}

	@Override
	protected void initActivity() {
		this.listLine = this.getIntent().getParcelableArrayListExtra(
				Constants.BUNDLE_LIST_ITEM_NAME);
		this.switchSize = listLine.size();
		listAdapter = new LineAdapter(this, new ArrayList<Station>());
		listViewItemClickListener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent();
				intent.setAction(getString(R.string.action_bus_station_detail));
				Bundle bundle = new Bundle();
				// 线路信息传递给目标
				ArrayList<Station> listStation = new ArrayList<Station>();
				listStation.add((Station) listAdapter.getItem(position));
				bundle.putParcelableArrayList(Constants.BUNDLE_LIST_ITEM_NAME,
						listStation);
				bundle.putInt(Constants.BUNDLE_SELECTED_POSITION_NAME, 0);
				intent.putExtras(bundle);
				startActivityForResult(intent, RESULT_OK);
			}
		};
	}

	@Override
	protected void loadBusData() {
		// Log.i("currSelectedPosition",
		// String.valueOf(this.curSelectedPosition));
		new LineAsyncTask(waitLayout, functionLayout, listView,
				listLine.get(curSelectedPosition), scrollPosition,
				(LineAdapter) listAdapter).execute();
	}

	@Override
	protected void favorite() {
		List<FavoriteBus> listFavorite = new ArrayList<FavoriteBus>();
		for (int i = 0; i < listLine.size(); i++) {
			Line line = listLine.get(i);
			FavoriteBus favorite = new FavoriteBus();
			favorite.setGuid(UUID.randomUUID().toString());
			favorite.setCityRegion(MyBusFactory.getMyBus().getRegion());
			favorite.setFavoriteName(line.getLineNo());
			favorite.setBusType(FavoriteBusStyle.Line.ordinal());
			favorite.setUrl(line.getUrlLink());
			favorite.setDemo(line.getDirection());
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
