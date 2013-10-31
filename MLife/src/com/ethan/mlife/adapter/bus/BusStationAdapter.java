package com.ethan.mlife.adapter.bus;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ethan.mlife.R;
import com.ethan.mlife.entity.Station;

public class BusStationAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Station> listStation;
	private int layoutId = R.layout.viewbusstation;
	/**
	 * 页大小
	 */
	private int pageSize = 10;
	/**
	 * 显示记录数
	 */
	private int count = 0;

	public BusStationAdapter(Context context, List<Station> listStation) {
		this.listStation = listStation;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.initPageConfig();
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return this.count;
	}

	public Station getItem(int position) {
		// TODO Auto-generated method stub
		return listStation.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 添加数据源
	 * 
	 * @return
	 */
	public List<Station> getDataSource() {
		return this.listStation;

	}

	/**
	 * 加载分页配置
	 */
	public void initPageConfig() {
		if (pageSize > listStation.size()) {
			// 不够1页直接返回总条数
			this.count = listStation.size();
		} else {
			// 第1页
			this.count = pageSize;
		}
	}

	/**
	 * 翻页
	 */
	public void nextPage() {
		if (this.count < this.listStation.size()) {
			this.count = count + pageSize <= listStation.size() ? count
					+ pageSize : listStation.size();
			this.notifyDataSetChanged();
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		BusStationAdapterView view;
		if (null == convertView) {
			convertView = inflater.inflate(layoutId, null);
			view = new BusStationAdapterView();
			view.tvStationName = (TextView) convertView
					.findViewById(R.id.tvStationName);
			view.tvStationDirection = (TextView) convertView
					.findViewById(R.id.tvStationDirection);
			view.tvStationDescription = (TextView) convertView
					.findViewById(R.id.tvStationDescription);
			convertView.setTag(view);
		} else {
			view = (BusStationAdapterView) convertView.getTag();
		}
		Station station = listStation.get(position);
		view.tvStationName.setText(station.getName());
		view.tvStationDirection.setText(station.getDirection());
		view.tvStationDescription.setText(String.format("%s%s",
				station.getDistrict(), station.getStreet()));
		return convertView;
	}

	private class BusStationAdapterView {
		TextView tvStationName;
		TextView tvStationDirection;
		TextView tvStationDescription;
	}

}
