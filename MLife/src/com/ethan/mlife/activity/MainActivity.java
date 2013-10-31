package com.ethan.mlife.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ethan.mlife.MLifeApp;
import com.ethan.mlife.R;

public class MainActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加activity至堆栈管理
		MLifeApp.getContext().pushActivity(this);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.homemlife, new String[] { "title", "img" }, new int[] {
						R.id.title, R.id.img });
		setListAdapter(adapter);
	}

	/**
	 * 加载菜单列表
	 * 
	 * @return
	 */
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", this.getString(R.string.menu_mlife_address));
		map.put("img", R.drawable.ic_contacts);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", this.getString(R.string.menu_mlife_bus));
		map.put("img", R.drawable.ic_bus);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", this.getString(R.string.menu_mlife_tax));
		map.put("img", R.drawable.ic_tax);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", this.getString(R.string.menu_mlife_train));
		map.put("img", R.drawable.ic_train);
		list.add(map);

		return list;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		Intent intent = new Intent();
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		switch (position) {
		case 1:
			intent.setAction(this.getString(R.string.action_menu_mlife_bus));
			this.startActivityForResult(intent, RESULT_OK);
			break;
		default:
			Toast.makeText(this.getApplicationContext(),
					R.string.message_mlife_list_item_click, Toast.LENGTH_SHORT)
					.show();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode) {
		case RESULT_OK:
			break;
		default:
			break;
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MLifeApp.getContext().popActivity(this);
	}
}
