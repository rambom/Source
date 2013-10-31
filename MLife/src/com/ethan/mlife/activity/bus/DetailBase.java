package com.ethan.mlife.activity.bus;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ethan.mlife.MLifeApp;
import com.ethan.mlife.R;
import com.ethan.mlife.common.Constants;
import com.ethan.mlife.common.RefreshStyle;
import com.ethan.mlife.common.SwitchDirection;

public abstract class DetailBase extends Activity implements OnGestureListener {
	protected OnItemClickListener listViewItemClickListener;
	protected int scrollPosition;
	/**
	 * 可切换数量
	 */
	protected int switchSize = 1;
	/**
	 * 当前列表选择索引
	 */
	protected int curSelectedPosition = 0;

	/**
	 * 进度条
	 */
	protected LinearLayout waitLayout;
	/**
	 * 功能区
	 */
	protected LinearLayout functionLayout;
	/**
	 * 显示列表
	 */
	protected ListView listView;
	/**
	 * 数据适配器
	 */
	protected BaseAdapter listAdapter;
	/**
	 * 进度条显示文本
	 */
	private TextView tvLoadingData;

	/**
	 * 初始化活动
	 */
	protected abstract void initActivity();

	/**
	 * 加载数据
	 * 
	 * @param style
	 *            刷新方式
	 */
	protected abstract void loadBusData();

	/**
	 * 添加收藏
	 */
	protected abstract void favorite();

	/**
	 * 获取要显示的View
	 * 
	 * @return
	 */
	protected abstract int getContentLayoutId();

	/**
	 * 获取顶端视图ID
	 * 
	 * @return
	 */
	protected abstract int getRootViewId();

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MLifeApp.getContext().popActivity(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加activity至堆栈管理
		MLifeApp.getContext().pushActivity(this);
		this.setContentView(getContentLayoutId());
		this.gestureDetector = new GestureDetector(this);
		// 加载活动初始化操作
		initActivity();
		this.curSelectedPosition = this.getIntent().getIntExtra(
				Constants.BUNDLE_SELECTED_POSITION_NAME, 0);
		boolean blnIsFromFavorite = this.getIntent().getBooleanExtra(
				Constants.BUNDLE_IS_FROM_FAVORITE, false);
		waitLayout = (LinearLayout) this.findViewById(R.id.llDataLoadingWait);
		functionLayout = (LinearLayout) this
				.findViewById(R.id.llFunctionButton);

		if (this.switchSize <= 1) {
			((Button) this.findViewById(R.id.btnSwitch))
					.setVisibility(Button.GONE);
		}

		// 不重复收藏
		if (blnIsFromFavorite) {
			((Button) this.findViewById(R.id.btnCollect))
					.setVisibility(Button.GONE);
		}

		tvLoadingData = (TextView) this.findViewById(R.id.tvLoadingData);
		listView = (ListView) this.findViewById(R.id.listDataView);
		listView.setAdapter(listAdapter);
		this.listView.setOnItemClickListener(listViewItemClickListener);
		this.listView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent ev) {
				return gestureDetector.onTouchEvent(ev);
			}
		});
		// 滚动事件
		listView.setOnScrollListener(new OnScrollListener() {
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				scrollPosition = view.getFirstVisiblePosition();

			}

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					scrollPosition = view.getFirstVisiblePosition();
				}
			}
		});
		waitLayout.setVisibility(FrameLayout.VISIBLE);
		this.loadBusData();
	}

	/**
	 * 刷新
	 * 
	 * @param v
	 */
	public void btnRefresh_Click(View v) {
		setControlState(RefreshStyle.ManualRefresh);
		this.loadBusData();
	}

	/**
	 * 切换
	 * 
	 * @param v
	 */
	public void btnSwitch_Click(View v) {
		this.curSelectedPosition = this.curSelectedPosition < switchSize - 1 ? ++this.curSelectedPosition
				: 0;
		this.switchDirection(SwitchDirection.Normal);
	}

	/**
	 * 添加收藏
	 * 
	 * @param v
	 */
	public void btnCollect_Click(View v) {
		// 禁用功能按钮
		((Button) this.functionLayout.findViewById(R.id.btnRefresh))
				.setEnabled(false);
		((Button) this.functionLayout.findViewById(R.id.btnSwitch))
				.setEnabled(false);
		((Button) this.functionLayout.findViewById(R.id.btnCollect))
				.setEnabled(false);
		// 禁用列表
		listView.setEnabled(false);
		this.favorite();
		// 通知更新收藏列表
		ListFavoriteActivity.refreshFlag = true;
		// 启用功能按钮
		((Button) this.functionLayout.findViewById(R.id.btnRefresh))
				.setEnabled(true);
		((Button) this.functionLayout.findViewById(R.id.btnSwitch))
				.setEnabled(true);
		((Button) this.functionLayout.findViewById(R.id.btnCollect))
				.setEnabled(true);
		// 启用列表
		listView.setEnabled(true);
	}

	/**
	 * 设置控件状态
	 */
	private void setControlState(RefreshStyle refreshStyle) {
		switch (refreshStyle) {
		case ManualRefresh:
			tvLoadingData.setText(R.string.mlife_bus_refresh);
			break;
		case Switch:
		case Touch:
			tvLoadingData.setText(R.string.mlife_bus_switch);
			break;
		default:
			tvLoadingData.setText(R.string.mlife_bus_loading);
		}
		// 禁用功能按钮
		((Button) this.functionLayout.findViewById(R.id.btnRefresh))
				.setEnabled(false);
		((Button) this.functionLayout.findViewById(R.id.btnSwitch))
				.setEnabled(false);
		((Button) this.functionLayout.findViewById(R.id.btnCollect))
				.setEnabled(false);
		// 禁用列表
		listView.setEnabled(false);
		// 显示等待
		waitLayout.setVisibility(LinearLayout.VISIBLE);
	}

	/**
	 * 切换方向
	 */
	private void switchDirection(SwitchDirection direction) {
		View view = this.findViewById(this.getRootViewId());
		switch (direction) {
		case Left:
			view.setAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_in));
			view.setAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_out));
			break;
		case Right:
			view.setAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_in));
			view.setAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_out));
			break;
		default:
			view.setAnimation(AnimationUtils
					.loadAnimation(this, R.anim.fade_in));
			view.setAnimation(AnimationUtils
					.loadAnimation(this, R.anim.fade_in));
			break;
		}
		setControlState(RefreshStyle.Switch);
		// 切换线路不记录滚动条
		this.scrollPosition = 0;
		//this.functionLayout.setVisibility(LinearLayout.GONE);
		//this.listView.setVisibility(ListView.GONE);
		this.loadBusData();
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > Constants.FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > Constants.FLING_MIN_VELOCITY) {
			if (this.switchSize > 1) {
				if (this.curSelectedPosition < switchSize - 1) {
					++this.curSelectedPosition;
					switchDirection(SwitchDirection.Right);
				} else {
					Toast.makeText(this.getApplicationContext(),
							R.string.message_mlife_bus_no_more_switch,
							Toast.LENGTH_SHORT).show();
				}
			}
			return true;
		} else if (e2.getX() - e1.getX() > Constants.FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > Constants.FLING_MIN_VELOCITY) {
			if (this.switchSize > 1) {
				if (this.curSelectedPosition > 0) {
					--this.curSelectedPosition;
					switchDirection(SwitchDirection.Left);
				} else {
					Toast.makeText(this.getApplicationContext(),
							R.string.message_mlife_bus_no_more_switch,
							Toast.LENGTH_SHORT).show();
				}
			}
			return true;
		}
		return false;
	}

	private GestureDetector gestureDetector;

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		this.getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int intMenuId = item.getItemId();
		switch (intMenuId) {
		case R.id.menuAbout:
			showAbout();
			break;
		case R.id.menuExit:
			MLifeApp.getContext().exitApplication();
			break;
		case R.id.menuHelp:
			showHelp();
			break;
		case R.id.menuHome:
			MLifeApp.getContext().returnToActivity(MainActivity.class);
			break;
		}
		return true;
	}

	private void showAbout() {
		View linearLayout = this.getLayoutInflater().inflate(R.layout.about,
				(ViewGroup) findViewById(R.id.llAbout));
		Toast toast = new Toast(this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(linearLayout);
		toast.show();
	}

	private void showHelp() {
		View linearLayout = this.getLayoutInflater().inflate(R.layout.bushelp,
				(ViewGroup) findViewById(R.id.llHelp));
		TextView tvHelpContent = (TextView) linearLayout
				.findViewById(R.id.tvHelpContent);
		tvHelpContent.setText(R.string.message_mlife_bus_detail);
		Toast toast = new Toast(this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(linearLayout);
		toast.show();
	}
}
