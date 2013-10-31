package com.ethan.mlife.task.bus;

import java.util.List;

import android.os.AsyncTask;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ethan.mlife.adapter.bus.FavoriteAdapter;
import com.ethan.mlife.dao.MyBusFactory;
import com.ethan.mlife.entity.FavoriteBus;

/**
 * 我收藏的公交异步处理
 * 
 * @author Ethan
 * 
 */
public class FavoriteAsyncTask extends AsyncTask<Void, Void, List<FavoriteBus>> {

	/**
	 * 进度条
	 */
	LinearLayout waitFrame;
	/**
	 * 列表视图
	 */
	ListView favoriteListView;

	/**
	 * 消息提示框
	 */
	LinearLayout llMessageFrame;
	/**
	 * 数据适配器
	 */
	private FavoriteAdapter favoriteAdapter;

	/**
	 * @param waitFframe
	 *            进度条
	 * @param favoriteListView
	 *            列表视图
	 * @param llMessageFrame
	 *            消息提示框
	 * @param favoriteAdapter
	 *            数据适配器
	 */
	public FavoriteAsyncTask(LinearLayout waitFrame, ListView favoriteListView,
			LinearLayout llMessageFrame, FavoriteAdapter favoriteAdapter) {
		this.waitFrame = waitFrame;
		this.favoriteListView = favoriteListView;
		this.llMessageFrame = llMessageFrame;
		this.favoriteAdapter = favoriteAdapter;
	}

	@Override
	protected List<FavoriteBus> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return MyBusFactory.getMyBus().getBusDao().getFavoriteBus();
	}

	@Override
	protected void onPostExecute(List<FavoriteBus> listFavorite) {
		// TODO Auto-generated method stub

		if (null == listFavorite || listFavorite.isEmpty())
			llMessageFrame.setVisibility(LinearLayout.VISIBLE);
		else {
			llMessageFrame.setVisibility(LinearLayout.GONE);
			this.favoriteAdapter.getDataSource().clear();
			this.favoriteAdapter.getDataSource().addAll(listFavorite);
			this.favoriteAdapter.notifyDataSetChanged();
		}
		// 隐藏进度条
		waitFrame.setVisibility(FrameLayout.GONE);
		// 启用列表
		favoriteListView.setEnabled(true);
	}
}
