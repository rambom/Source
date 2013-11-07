package com.ethan.mlife.activity.bus;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ethan.mlife.R;
import com.ethan.mlife.adapter.BusStationAdapter;
import com.ethan.mlife.common.Constants;
import com.ethan.mlife.entity.Station;
import com.ethan.mlife.task.bus.BusStationAsyncTask;

public class ListStationActivity extends Activity {
	/**
	 * 数据列表
	 */
	private ListView listView;
	/**
	 * 数据适配器
	 */
	private BusStationAdapter busStationAdapter;
	/**
	 * 进度条
	 */
	private LinearLayout waitLayout;
	/**
	 * 查询按钮
	 */
	private Button btnSearch;
	/**
	 * 查询输入框
	 */
	private EditText etSearch;
	/**
	 * 数据刷新标识
	 */
	private boolean isRefreshFoot = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homebusstation);
		busStationAdapter = new BusStationAdapter(this,
				new ArrayList<Station>());
		listView = (ListView) this.findViewById(R.id.listDataView);
		listView.setAdapter(busStationAdapter);
		waitLayout = (LinearLayout) findViewById(R.id.llDataLoadingWait);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		etSearch = (EditText) findViewById(R.id.etSearch);
		// 单击选择站台
		listView.setOnItemClickListener(getListClickListener());
		// 滑动分页事件
		listView.setOnScrollListener(onScrollListener);
		etSearch.setHint(R.string.mlife_bus_station_text_hint);
	}

	/**
	 * 查询站台列表数据
	 */
	public void btnSearch_Click(View v) {
		Station station = new Station();
		station.setName(etSearch.getText().toString().trim());
		if (!("".equalsIgnoreCase(station.getName()) || 0 == station.getName()
				.length())) {
			btnSearch.setEnabled(false);
			listView.setEnabled(false);
			waitLayout.setVisibility(LinearLayout.VISIBLE);

			new BusStationAsyncTask(waitLayout, listView, btnSearch, station,
					busStationAdapter).execute();
		}
	}

	protected OnScrollListener onScrollListener = new OnScrollListener() {

		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// 是否停止滑动并且处于底部
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& isRefreshFoot) {
				// 加载下一页数据
				busStationAdapter.nextPage();
			}
		}

		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// 是否滑动到底部
			if (firstVisibleItem + visibleItemCount == totalItemCount) {
				isRefreshFoot = true;
				view.setSelection(totalItemCount - 1);
			} else {
				isRefreshFoot = false;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode) {
		case RESULT_OK:
			break;
		default:
			break;
		}
	}

	protected OnItemClickListener getListClickListener() {
		return new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent();
				intent.setAction(getString(R.string.action_bus_station_detail));
				Bundle bundle = new Bundle();
				Station curStation = busStationAdapter.getItem(position);

				// 记录当前站台索引并传递到目标活动
				int intCurStation = 0;
				// 相同站台记录传递到目标活动
				ArrayList<Station> listStation = new ArrayList<Station>();
				// 按站台名称遍历记录
				for (int i = 0; i < busStationAdapter.getCount(); ++i) {
					Station station = busStationAdapter.getItem(i);

					if (curStation.getName()
							.equalsIgnoreCase(station.getName())) {
						if (curStation.getScode().equalsIgnoreCase(
								station.getScode())) {
							intCurStation = listStation.size();
						}
						listStation.add(station);
					}
				}
				bundle.putParcelableArrayList(Constants.BUNDLE_LIST_ITEM_NAME,
						listStation);
				bundle.putInt(Constants.BUNDLE_SELECTED_POSITION_NAME,
						intCurStation);
				intent.putExtras(bundle);
				startActivityForResult(intent, RESULT_OK);
			}
		};
	}
}
