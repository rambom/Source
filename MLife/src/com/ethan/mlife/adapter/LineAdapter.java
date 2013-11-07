package com.ethan.mlife.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ethan.mlife.R;
import com.ethan.mlife.entity.Station;

public class LineAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Station> listStation;
	private int layoutId = R.layout.viewbuslinedetail;

	public LineAdapter(Context context, List<Station> listStation) {
		this.listStation = listStation;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return listStation.size();
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
	 * 返回数据源
	 * 
	 * @return
	 */
	public List<Station> getDataSource() {
		return this.listStation;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LineAdapterView view;
		if (null == convertView) {
			convertView = inflater.inflate(layoutId, null);
			view = new LineAdapterView();
			view.tvStationName = (TextView) convertView
					.findViewById(R.id.tvStationName);
			view.tvPassTime = (TextView) convertView
					.findViewById(R.id.tvPassTime);
			view.tvVehicleNo = (TextView) convertView
					.findViewById(R.id.tvVehicleNo);
			convertView.setTag(view);
		} else {
			view = (LineAdapterView) convertView.getTag();
		}
		Station station = listStation.get(position);
		view.tvStationName.setText(station.getName());
		view.tvPassTime.setText(station.getPassTime());
		view.tvVehicleNo.setText(station.getVeNumber());
		return convertView;
	}

	private class LineAdapterView {
		TextView tvStationName;
		TextView tvPassTime;
		TextView tvVehicleNo;
	}

}
