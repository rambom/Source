package com.ethan.mlife.task.bus;

import java.util.List;

import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ethan.mlife.R;
import com.ethan.mlife.adapter.LineAdapter;
import com.ethan.mlife.dao.MyBusFactory;
import com.ethan.mlife.entity.Line;
import com.ethan.mlife.entity.Station;

/**
 * 线路信息异步处理任务
 * 
 * @author Ethan
 * 
 */
public class LineAsyncTask extends AsyncTask<Void, Void, List<Station>> {
	/**
	 * 进度条
	 */
	protected LinearLayout waitFrame;
	/**
	 * 功能区
	 */
	protected LinearLayout functionLayout;
	/**
	 * 显示列表
	 */
	protected ListView listView;

	/**
	 * 选择项位置
	 */
	protected int selectedItemPosition;
	/**
	 * 查询条件
	 */
	private Line line;
	/**
	 * 数据适配器
	 */
	private LineAdapter lineAdapter;

	/**
	 * @param waitFframe
	 *            进度条
	 * @param functionLayout
	 *            功能按钮区域
	 * @param listView
	 *            显示列表
	 * @param line
	 *            请求地址
	 * @param selectedItemPosition
	 *            滚动条位置
	 * @param lineAdapter
	 *            数据适配器
	 */
	public LineAsyncTask(LinearLayout waitFrame, LinearLayout functionLayout,
			ListView listView, Line line, int selectedItemPosition,
			LineAdapter lineAdapter) {
		this.waitFrame = waitFrame;
		this.functionLayout = functionLayout;
		this.listView = listView;
		this.line = line;
		this.selectedItemPosition = selectedItemPosition;
		this.lineAdapter = lineAdapter;
	}

	@Override
	protected List<Station> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return MyBusFactory.getMyBus().getBusDao().getLineStation(line);
	}

	@Override
	protected void onPostExecute(List<Station> listStation) {
		// TODO Auto-generated method stub

		if (null == listStation || listStation.isEmpty()) {
			Toast toast = Toast.makeText(functionLayout.getContext(),
					R.string.message_bus_query_failed, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} else {
			this.lineAdapter.getDataSource().clear();
			this.lineAdapter.getDataSource().addAll(listStation);
			this.lineAdapter.notifyDataSetChanged();
			// 还原滚动条位置
			listView.setSelection(this.selectedItemPosition);
			functionLayout.setVisibility(LinearLayout.VISIBLE);
		}
		// 隐藏进度条
		waitFrame.setVisibility(FrameLayout.GONE);
		// 启用listView
		listView.setEnabled(true);
		listView.setVisibility(ListView.VISIBLE);
		// 启用功能按钮
		((Button) this.functionLayout.findViewById(R.id.btnRefresh))
				.setEnabled(true);
		((Button) this.functionLayout.findViewById(R.id.btnSwitch))
				.setEnabled(true);
		((Button) this.functionLayout.findViewById(R.id.btnCollect))
				.setEnabled(true);
	}
}
