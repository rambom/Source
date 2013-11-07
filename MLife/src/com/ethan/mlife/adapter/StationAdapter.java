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

public class StationAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Line> listLine;
	private int layoutId = R.layout.viewbusstationdetail;

	public StationAdapter(Context context, List<Line> listLine) {
		this.listLine = listLine;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return listLine.size();
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

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		StationAdapterView view;
		if (null == convertView) {
			convertView = inflater.inflate(layoutId, null);
			view = new StationAdapterView();
			view.tvStationLineId = (TextView) convertView
					.findViewById(R.id.tvStationLineId);
			view.tvStationSpacing = (TextView) convertView
					.findViewById(R.id.tvStationSpacing);
			view.tvVehicleNo = (TextView) convertView
					.findViewById(R.id.tvVehicleNo);
			view.tvLineDirection = (TextView) convertView
					.findViewById(R.id.tvLineDirection);
			convertView.setTag(view);
		} else {
			view = (StationAdapterView) convertView.getTag();
		}
		Line line = listLine.get(position);
		view.tvStationLineId.setText(line.getLineNo());
		view.tvStationSpacing.setText(line.getSpacing());
		view.tvVehicleNo.setText(line.getVeNumber());
		view.tvLineDirection.setText(line.getDirection());
		return convertView;
	}

	private class StationAdapterView {
		TextView tvStationLineId;
		TextView tvStationSpacing;
		TextView tvVehicleNo;
		TextView tvLineDirection;
	}
}
