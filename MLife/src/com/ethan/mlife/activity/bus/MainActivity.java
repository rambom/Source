package com.ethan.mlife.activity.bus;

import android.app.ActivityGroup;
import android.content.Intent;
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
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ethan.mlife.MLifeApp;
import com.ethan.mlife.R;
import com.ethan.mlife.common.Constants;

/**
 * @author Ethan
 * 
 */
/**
 * @author Ethan
 * 
 */
public class MainActivity extends ActivityGroup implements OnGestureListener,
		OnTouchListener {
	private FrameLayout container;
	private LinearLayout favorite;
	private LinearLayout line;
	private LinearLayout station;
	private GestureDetector gestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加activity至堆栈管理
		MLifeApp.getContext().pushActivity(this);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.homebus);
		container = (FrameLayout) this.findViewById(R.id.llBusContainer);
		favorite = (LinearLayout) this.findViewById(R.id.llFavoriteBus);
		line = (LinearLayout) this.findViewById(R.id.llQueryBusLine);
		station = (LinearLayout) this.findViewById(R.id.llSearchBusStation);

		gestureDetector = new GestureDetector(this);
		container.setOnTouchListener(this);
		container.setLongClickable(true);
		favoriteBus_Click(favorite);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ActivityGroup#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MLifeApp.getContext().popActivity(this);
	}

	public void favoriteBus_Click(View v) {
		container.removeAllViews();
		v.setBackgroundResource(R.drawable.tab_two_highlight);
		line.setBackgroundResource(R.drawable.tab_one_normal);
		station.setBackgroundResource(R.drawable.tab_one_normal);
		Intent intent = new Intent();
		intent.setClass(this, ListFavoriteActivity.class);
		container.addView(this.getLocalActivityManager()
				.startActivity(String.valueOf(R.id.llBusFavorite), intent)
				.getDecorView());
	}

	public void queryBusLine_Click(View v) {
		container.removeAllViews();
		v.setBackgroundResource(R.drawable.tab_two_highlight);
		favorite.setBackgroundResource(R.drawable.tab_one_normal);
		station.setBackgroundResource(R.drawable.tab_one_normal);
		Intent intent = new Intent();
		intent.setClass(this, ListLineActivity.class);
		container.addView(this.getLocalActivityManager()
				.startActivity(String.valueOf(R.id.llBusLine), intent)
				.getDecorView());
	}

	public void searchBusStation_Click(View v) {
		container.removeAllViews();
		v.setBackgroundResource(R.drawable.tab_two_highlight);
		favorite.setBackgroundResource(R.drawable.tab_one_normal);
		line.setBackgroundResource(R.drawable.tab_one_normal);
		Intent intent = new Intent();
		intent.setClass(this, ListStationActivity.class);
		container.addView(this.getLocalActivityManager()
				.startActivity(String.valueOf(R.id.llBusStation), intent)
				.getDecorView());
	}

	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		// Log.i("onTouch", "MainActivity");
		return gestureDetector.onTouchEvent(arg1);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// Log.i("dispatchTouchEvent", "MainActivity");
		if (this.gestureDetector.onTouchEvent(ev))
			return true;
		else
			return super.dispatchTouchEvent(ev);
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		// Log.i("onFling", "BusActivity");
		String strId = this.getLocalActivityManager().getCurrentId();
		if (e1.getX() - e2.getX() > Constants.FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > Constants.FLING_MIN_VELOCITY) {
			if (!strId.equals(R.id.llBusStation)) {
				if (strId.equals(String.valueOf(R.id.llBusFavorite))) {
					this.setAnimationRight();
					this.queryBusLine_Click(line);
				} else if (strId.equals(String.valueOf(R.id.llBusLine))) {
					this.setAnimationRight();
					this.searchBusStation_Click(station);
				}
			}
		} else if (e2.getX() - e1.getX() > Constants.FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > Constants.FLING_MIN_VELOCITY) {
			if (!strId.equals(R.id.llBusFavorite)) {
				if (strId.equals(String.valueOf(R.id.llBusLine))) {
					this.setAnimationLeft();
					this.favoriteBus_Click(favorite);
				} else if (strId.equals(String.valueOf(R.id.llBusStation))) {
					this.setAnimationLeft();
					this.queryBusLine_Click(line);
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 左滑动效果
	 */
	private void setAnimationLeft() {
		this.container.setAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_right_in));
		this.container.setAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_right_out));

	}

	/**
	 * 右滑动效果
	 */
	private void setAnimationRight() {
		this.container.setAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_left_in));
		this.container.setAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_left_out));
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

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
			this.finish();
			break;
		}
		return true;
	}

	/**
	 * 显示系统信息
	 */
	private void showAbout() {
		View linearLayout = this.getLayoutInflater().inflate(R.layout.about,
				(ViewGroup) findViewById(R.id.llAbout));
		Toast toast = new Toast(this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(linearLayout);
		toast.show();
	}

	/**
	 * 显示帮助
	 */
	private void showHelp() {
		View linearLayout = this.getLayoutInflater().inflate(R.layout.bushelp,
				(ViewGroup) findViewById(R.id.llHelp));
		TextView tvHelpContent = (TextView) linearLayout
				.findViewById(R.id.tvHelpContent);
		tvHelpContent.setText(R.string.message_mlife_bus_help);
		Toast toast = new Toast(this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(linearLayout);
		toast.show();
	}
}
