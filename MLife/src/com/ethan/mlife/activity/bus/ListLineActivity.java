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
import com.ethan.mlife.adapter.BusLineAdapter;
import com.ethan.mlife.common.Constants;
import com.ethan.mlife.entity.Line;
import com.ethan.mlife.task.bus.BusLineAsyncTask;

public class ListLineActivity extends Activity implements OnScrollListener {
	/**
	 * 数据列表
	 */
	private ListView listView;
	/**
	 * 数据适配器
	 */
	private BusLineAdapter busLineAdapter;
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
		setContentView(R.layout.homebusline);
		busLineAdapter = new BusLineAdapter(this, new ArrayList<Line>());
		listView = (ListView) this.findViewById(R.id.listDataView);
		listView.setAdapter(busLineAdapter);
		// 分页加载进度条
		/*
		 * ProgressBar progressBar = new ProgressBar(this);
		 * progressBar.setVisibility(ProgressBar.GONE);
		 * listView.addFooterView(progressBar); // 添加页脚
		 */

		waitLayout = (LinearLayout) findViewById(R.id.llDataLoadingWait);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		etSearch = (EditText) findViewById(R.id.etSearch);
		// 单击选择线路
		listView.setOnItemClickListener(getListClickListener());
		// 滑动分页
		listView.setOnScrollListener(this);
		etSearch.setHint(R.string.mlife_bus_line_text_hint);
	}

	/**
	 * 查询线路列表
	 */
	public void btnSearch_Click(View v) {
		Line line = new Line();
		line.setLineNo(etSearch.getText().toString().trim());
		if (!(line.getLineNo().length() == 0 || "".equalsIgnoreCase(line
				.getLineNo()))) {
			btnSearch.setEnabled(false);
			listView.setEnabled(false);
			waitLayout.setVisibility(LinearLayout.VISIBLE);
			new BusLineAsyncTask(waitLayout, listView, btnSearch, line,
					busLineAdapter).execute();
		}
	}

	public void onScrollStateChanged(AbsListView paramAbsListView,
			int scrollState) {
		// 是否停止滑动并且处于底部
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && isRefreshFoot) {
			// 有页脚
			/*
			 * HeaderViewListAdapter listAdapter = (HeaderViewListAdapter)
			 * paramAbsListView .getAdapter(); adapter = (BusLineAdapter)
			 * listAdapter.getWrappedAdapter(); BusStationAdapter adapter =
			 * (BusStationAdapter) view.getAdapter();
			 */

			// 下一页
			this.busLineAdapter.nextPage();
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

	private OnItemClickListener getListClickListener() {
		return new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent();
				intent.setAction(getString(R.string.action_bus_line_detail));
				Bundle bundle = new Bundle();
				Line curLine = busLineAdapter.getItem(position);
				// 相同线路传递到目标活动
				ArrayList<Line> listLine = new ArrayList<Line>();
				// 记录当前选择线路索引并传递到目标活动
				int intCurLine = 0;
				// 按线路号遍历记录
				for (int i = 0; i < busLineAdapter.getCount(); ++i) {
					Line line = busLineAdapter.getItem(i);

					if (curLine.getLineNo().equalsIgnoreCase(line.getLineNo())) {
						if (curLine.getId().equalsIgnoreCase(line.getId())) {
							intCurLine = listLine.size();
						}
						listLine.add(line);
					}
				}
				bundle.putParcelableArrayList(Constants.BUNDLE_LIST_ITEM_NAME,
						listLine);
				bundle.putInt(Constants.BUNDLE_SELECTED_POSITION_NAME,
						intCurLine);
				intent.putExtras(bundle);
				startActivityForResult(intent, RESULT_OK);
			}
		};
	}
}
