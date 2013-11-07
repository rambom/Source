package com.ethan.mlife.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ethan.mlife.R;
import com.ethan.mlife.entity.Line;

public class BusLineAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Line> listLine;
	private int layoutId = R.layout.viewbusline;;
	/**
	 * 页大小
	 */
	private int pageSize = 10;
	/**
	 * 显示记录数
	 */
	private int count = 0;

	public BusLineAdapter(Context context, List<Line> listLine) {
		this.listLine = listLine;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.initPageConfig();
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return this.count;
	}

	public Line getItem(int position) {
		// TODO Auto-generated method stub
		return listLine.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 返回数据源
	 * 
	 * @return
	 */
	public List<Line> getDataSource() {
		return this.listLine;
	}

	/**
	 * 加载分页配置
	 */
	public void initPageConfig() {
		if (pageSize > listLine.size()) {
			// 不够1页直接返回总条数
			this.count = listLine.size();
		} else {
			// 第1页
			this.count = pageSize;
		}
	}

	/**
	 * 翻页
	 */
	public void nextPage() {
		if (this.count < this.listLine.size()) {
			this.count = count + pageSize <= listLine.size() ? count + pageSize
					: listLine.size();
			this.notifyDataSetChanged();
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		BusLineAdapterView view;
		if (null == convertView) {
			convertView = inflater.inflate(layoutId, null);
			view = new BusLineAdapterView();
			view.tvLineName = (TextView) convertView
					.findViewById(R.id.tvLineName);
			view.tvLineDirection = (TextView) convertView
					.findViewById(R.id.tvLineDirection);
			convertView.setTag(view);
		} else {
			view = (BusLineAdapterView) convertView.getTag();
		}
		Line line = listLine.get(position);
		view.tvLineName.setText(line.getLineNo());
		view.tvLineDirection.setText(line.getDirection());

		return convertView;
	}

	private class BusLineAdapterView {
		TextView tvLineName;
		TextView tvLineDirection;
	}
}
