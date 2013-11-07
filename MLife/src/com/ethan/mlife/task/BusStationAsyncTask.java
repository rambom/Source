package com.ethan.mlife.task;

import java.util.List;

import android.os.AsyncTask;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ethan.mlife.R;
import com.ethan.mlife.adapter.BusStationAdapter;
import com.ethan.mlife.dao.MyBusFactory;
import com.ethan.mlife.entity.Station;

/**
 * 公交站台异步处理
 * 
 * @author Ethan
 * 
 */
public class BusStationAsyncTask extends AsyncTask<Void, Void, List<Station>> {

	/**
	 * 进度条
	 */
	LinearLayout waitFrame;
	/**
	 * 列表视图
	 */
	ListView stationListView;
	/**
	 * 查询按钮
	 */
	Button btnStation;
	/**
	 * 查询条件
	 */
	Station station;
	/**
	 * 数据适配器
	 */
	BusStationAdapter busStationAdapter;

	/**
	 * @param waitFframe
	 *            进度条
	 * @param lineListView
	 *            列表视图
	 * @param btnLine
	 *            查询按钮
	 * @param station
	 *            查询条件
	 * @param busStationAdapter
	 *            数据适配器
	 */
	public BusStationAsyncTask(LinearLayout waitFrame,
			ListView stationListView, Button btnStation, Station station,
			BusStationAdapter busStationAdapter) {
		this.waitFrame = waitFrame;
		this.stationListView = stationListView;
		this.btnStation = btnStation;
		this.station = station;
		this.busStationAdapter = busStationAdapter;
	}

	@Override
	protected List<Station> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return MyBusFactory.getMyBus().getBusDao().getBusStation(station);
	}

	@Override
	protected void onPostExecute(List<Station> listStation) {
		// TODO Auto-generated method stub

		if (null == listStation || listStation.isEmpty())
			Toast.makeText(btnStation.getContext(),
					R.string.message_bus_query_failed, Toast.LENGTH_SHORT)
					.show();
		else {
			this.busStationAdapter.getDataSource().clear();
			this.busStationAdapter.getDataSource().addAll(listStation);
			this.busStationAdapter.initPageConfig();
			this.busStationAdapter.notifyDataSetChanged();
		}
		// 隐藏进度条
		waitFrame.setVisibility(FrameLayout.GONE);
		// 启用搜索按钮
		btnStation.setEnabled(true);
		// 启用列表
		stationListView.setEnabled(true);
	}
}
