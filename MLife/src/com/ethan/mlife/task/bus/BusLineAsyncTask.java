package com.ethan.mlife.task.bus;

import java.util.List;

import android.os.AsyncTask;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ethan.mlife.R;
import com.ethan.mlife.adapter.bus.BusLineAdapter;
import com.ethan.mlife.dao.MyBusFactory;
import com.ethan.mlife.entity.Line;

/**
 * 公交线路异步处理
 * 
 * @author Ethan
 * 
 */
public class BusLineAsyncTask extends AsyncTask<Void, Void, List<Line>> {

	/**
	 * 进度条
	 */
	LinearLayout waiFrame;
	/**
	 * 列表视图
	 */
	ListView lineListView;
	/**
	 * 搜索按钮
	 */
	Button btnLine;
	/**
	 * 查询条件
	 */
	Line line;
	/**
	 * 数据适配器
	 */
	BusLineAdapter busLineAdapter;

	/**
	 * @param waitFframe
	 *            进度条
	 * @param lineListView
	 *            列表视图
	 * @param btnLine
	 *            查询按钮
	 * @param line
	 *            查询条件
	 * @param busLineAdapter
	 *            数据适配器
	 */
	public BusLineAsyncTask(LinearLayout waitFrame, ListView lineListView,
			Button btnLine, Line line, BusLineAdapter busLineAdapter) {
		this.waiFrame = waitFrame;
		this.lineListView = lineListView;
		this.btnLine = btnLine;
		this.line = line;
		this.busLineAdapter = busLineAdapter;
	}

	@Override
	protected List<Line> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return MyBusFactory.getMyBus().getBusDao().getBusLine(line);
	}

	@Override
	protected void onPostExecute(List<Line> listLine) {
		// TODO Auto-generated method stub

		if (listLine == null || listLine.isEmpty())
			Toast.makeText(btnLine.getContext(),
					R.string.message_bus_query_failed, Toast.LENGTH_SHORT)
					.show();
		else {
			this.busLineAdapter.getDataSource().clear();
			this.busLineAdapter.getDataSource().addAll(listLine);
			this.busLineAdapter.initPageConfig();
			this.busLineAdapter.notifyDataSetChanged();
		}

		// 隐藏进度条
		waiFrame.setVisibility(FrameLayout.GONE);
		// 启用搜索按钮
		btnLine.setEnabled(true);
		// 启用列表
		lineListView.setEnabled(true);
	}
}
