package com.ethan.mlife.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ethan.mlife.R;
import com.ethan.mlife.entity.FavoriteBus;

public class FavoriteAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<FavoriteBus> listFavorite;
	private int layoutId = R.layout.viewbusfavorite;

	public FavoriteAdapter(Context context, List<FavoriteBus> listFavorite) {
		this.listFavorite = listFavorite;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return listFavorite.size();
	}

	public FavoriteBus getItem(int position) {
		// TODO Auto-generated method stub
		return listFavorite.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 获取数据源
	 * 
	 * @return
	 */
	public List<FavoriteBus> getDataSource() {
		return this.listFavorite;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		FavoriteAdapterView view;
		if (null == convertView) {
			convertView = inflater.inflate(layoutId, null);
			view = new FavoriteAdapterView();
			view.tvFavoriteName = (TextView) convertView
					.findViewById(R.id.tvFavoriteName);
			view.tvFavoriteDemo = (TextView) convertView
					.findViewById(R.id.tvFavoriteDemo);
			view.ibDelete = (ImageButton) convertView
					.findViewById(R.id.ibDeleteFavorite);
			convertView.setTag(view);
		} else {
			view = (FavoriteAdapterView) convertView.getTag();
		}
		FavoriteBus favorite = listFavorite.get(position);
		view.tvFavoriteName.setText(favorite.getFavoriteName());
		view.tvFavoriteDemo.setText(favorite.getDemo());
		view.ibDelete.setTag(position);
		return convertView;
	}

	private class FavoriteAdapterView {
		TextView tvFavoriteName;
		TextView tvFavoriteDemo;
		ImageButton ibDelete;
	}

}
