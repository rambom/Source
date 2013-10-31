package com.android.jetman;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.jetman.adapter.addressLvAdapter;
import com.android.jetman.common.Constants;
import com.android.jetman.data.dao.Address;
import com.android.jetman.dbAccess.DataBaseHelper;
import com.android.jetman.entity.AddressNode;
import com.android.jetman.entity.SvnUser;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class JetmanActivity extends ListActivity {
	private Button BtnUserCfg = null;
	private Button BtnOnLineSearch = null;
	private Button BtnOffLineSearch = null;
	private Button BtnUpdateDb = null;
	private EditText TxtWhere = null;
	private String strUser;
	private String strPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		setContentView(R.layout.homeaddress);
		getViews();
		bindBtnListener();
		DataBaseHelper dh = new DataBaseHelper(JetmanActivity.this, "myDb",
				null, 1);
		final SQLiteDatabase db = dh.getWritableDatabase();
		Cursor cursor = db.query(SvnUser.tblName, new String[] {
				SvnUser.tblColName, SvnUser.tblColPassword }, null, null, null,
				null, null);
		if (!cursor.moveToNext()) {
			Toast.makeText(JetmanApp.getContext(), "您还没有配置SVN用户名密码",
					Toast.LENGTH_SHORT).show();
		}
		if (db.isOpen())
			db.close();
	}

	// @Override
	// protected void onListItemClick(ListView l, View v, int position, long id)
	// {
	// super.onListItemClick(l, v, position, id);
	// Toast.makeText(MLifeApp.getContext(), id + "|" + position,
	// Toast.LENGTH_SHORT).show();
	// }

	private void getViews() {
		BtnUserCfg = (Button) findViewById(R.id.BtnUserCfg);
		BtnOnLineSearch = (Button) findViewById(R.id.BtnOnLineSearch);
		BtnOffLineSearch = (Button) findViewById(R.id.BtnOffLineSearch);
		BtnUpdateDb = (Button) findViewById(R.id.BtnUpdateDb);
		TxtWhere = (EditText) findViewById(R.id.txtWhere);
	}

	private void bindBtnListener() {
		BtnUserCfg.setOnClickListener(new BtnUserCfgListener());
		BtnOnLineSearch.setOnClickListener(new BtnOnLineSearchListener());
		BtnOffLineSearch.setOnClickListener(new BtnOffLineSearchListener());
		BtnUpdateDb.setOnClickListener(new BtnUpdateDbListener());
	}

	/**
	 * 用户配置
	 * 
	 * @author Administrator
	 * 
	 */
	class BtnUserCfgListener implements OnClickListener {
		public void onClick(View v) {

			DataBaseHelper dh = new DataBaseHelper(JetmanActivity.this,
					"myDb", null, 1);
			final SQLiteDatabase db = dh.getWritableDatabase();
			Cursor cursor = db.query(SvnUser.tblName, new String[] {
					SvnUser.tblColName, SvnUser.tblColPassword }, null, null,
					null, null, null);
			SvnUser svnUser = new SvnUser();
			while (cursor.moveToNext()) {
				svnUser.setName(cursor.getString(cursor
						.getColumnIndex(SvnUser.tblColName)));
				svnUser.setPassword(cursor.getString(cursor
						.getColumnIndex(SvnUser.tblColPassword)));
				break;
			}
			LayoutInflater inflater = (LayoutInflater) getApplicationContext()
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			final View view = inflater.inflate(R.layout.usercfgdialog, null);
			final EditText editSvnUser = (EditText) view
					.findViewById(R.id.EditSvnUser);
			final EditText editSvnPwd = (EditText) view
					.findViewById(R.id.EditSvnPwd);
			editSvnUser.setText("");
			editSvnPwd.setText("");
			if (null != svnUser) {
				editSvnUser.setText(svnUser.getName());
				editSvnPwd.setText(svnUser.getPassword());
			}
			AlertDialog.Builder builder2 = new AlertDialog.Builder(
					JetmanActivity.this);
			builder2.setView(view);
			builder2.setTitle("用户配置")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// 读取配置
									if (null != editSvnUser.getText()
											&& !"".equals(editSvnUser.getText())
											&& null != editSvnPwd.getText()
											&& !"".equals(editSvnPwd.getText()
													.toString())) {
										// 全删
										db.delete(SvnUser.tblName, "1", null);
										ContentValues values = new ContentValues();
										values.put(SvnUser.tblColName,
												editSvnUser.getText()
														.toString());
										values.put(SvnUser.tblColPassword,
												editSvnPwd.getText().toString());
										// 更新数据库用户表
										db.insert(SvnUser.tblName, null, values);
										dialog.cancel();
									}
								}
							}).create().show();
		}
	}

	/**
	 * 在线查询
	 * 
	 * @author Administrator
	 * 
	 */
	class BtnOnLineSearchListener implements OnClickListener {
		private String strName;
		private String strPwd;

		public void onClick(View v) {
			DataBaseHelper dh = new DataBaseHelper(JetmanActivity.this,
					"myDb", null, 1);
			final SQLiteDatabase db = dh.getWritableDatabase();
			Cursor cursor = db.query(SvnUser.tblName, new String[] {
					SvnUser.tblColName, SvnUser.tblColPassword }, null, null,
					null, null, null);
			if (!cursor.moveToNext()) {
				Toast.makeText(JetmanApp.getContext(), "您还没有配置SVN用户名密码",
						Toast.LENGTH_SHORT).show();
				return;
			}

			strName = cursor.getString(cursor
					.getColumnIndex(SvnUser.tblColName));
			strPwd = cursor.getString(cursor
					.getColumnIndex(SvnUser.tblColPassword));

			String strWhere = TxtWhere.getText().toString();
			if (null == strWhere || "".equals(strWhere))
				return;
			List<AddressNode> addressNodeList = new ArrayList<AddressNode>();
			Address address = new Address();
			addressNodeList = address.searchNodes(strWhere, strName, strPwd);
			if (null == addressNodeList) {
				return;
			}
			bindNodeListData(addressNodeList);
			if (db.isOpen())
				db.close();
		}

	}

	/**
	 * 离线查询
	 * 
	 * @author Administrator
	 * 
	 */
	class BtnOffLineSearchListener implements OnClickListener {
		public void onClick(View v) {
			String strWhere = TxtWhere.getText().toString();
			if (null == strWhere || "".equals(strWhere))
				return;
			strWhere = "%" + strWhere + "%";
			DataBaseHelper dh = new DataBaseHelper(JetmanApp.getContext(),
					"myDb", null, 1);
			SQLiteDatabase db = dh.getReadableDatabase();
			String strSelection = MessageFormat.format(
					Constants.SEARCHTEMPLATE, Constants.COLUMNNAME,
					Constants.COLUMNSEX, Constants.COLUMNPHONE,
					Constants.COLUMNMOBLIE, Constants.COLUMNENTPHONE,
					Constants.COLUMNEMAIL);
			Cursor cursor = db.query("AddressBookOfDcjet", new String[] {
					Constants.COLUMNNAME, Constants.COLUMNSEX,
					Constants.COLUMNPHONE, Constants.COLUMNMOBLIE,
					Constants.COLUMNENTPHONE, Constants.COLUMNEMAIL },
					strSelection, new String[] { strWhere, strWhere, strWhere,
							strWhere, strWhere, strWhere }, null, null, null);
			bindCursorData(cursor);
			if (db.isOpen())
				db.close();
		}
	}

	private void bindNodeListData(List<AddressNode> addressNodeList) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (AddressNode node : addressNodeList) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(Constants.COLUMNNAME, node.getName());
			map.put(Constants.COLUMNSEX, node.getSex());
			map.put(Constants.COLUMNPHONE, node.getPhone());
			map.put(Constants.COLUMNMOBLIE, node.getMobile());
			map.put(Constants.COLUMNENTPHONE, node.getEntphone());
			map.put(Constants.COLUMNEMAIL, node.getEmail());
			list.add(map);
		}
		setList(list);
	}

	private void bindCursorData(Cursor cursor) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		while (cursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(Constants.COLUMNNAME, cursor.getString(cursor
					.getColumnIndex(Constants.COLUMNNAME)));
			map.put(Constants.COLUMNSEX, cursor.getString(cursor
					.getColumnIndex(Constants.COLUMNSEX)));
			map.put(Constants.COLUMNPHONE, cursor.getString(cursor
					.getColumnIndex(Constants.COLUMNPHONE)));
			map.put(Constants.COLUMNMOBLIE, cursor.getString(cursor
					.getColumnIndex(Constants.COLUMNMOBLIE)));
			map.put(Constants.COLUMNENTPHONE, cursor.getString(cursor
					.getColumnIndex(Constants.COLUMNENTPHONE)));
			map.put(Constants.COLUMNEMAIL, cursor.getString(cursor
					.getColumnIndex(Constants.COLUMNEMAIL)));
			list.add(map);
		}
		/*
		 * SimpleAdapter listAdapter = new SimpleAdapter(this, list,
		 * R.layout.addresslist, new String[] { Constants.COLUMNNAME,
		 * Constants.COLUMNSEX, Constants.COLUMNPHONE, Constants.COLUMNMOBLIE,
		 * Constants.COLUMNENTPHONE, Constants.COLUMNEMAIL }, new int[] {
		 * R.id.TxtName, R.id.TxtSex, R.id.TxtPhone, R.id.TxtMobile,
		 * R.id.TxtEntPhone, R.id.TxtEmail });
		 */
		setList(list);
	}

	// 设置listview数据适配器
	private void setList(ArrayList<HashMap<String, String>> list) {
		if (null != list && list.size() != 0) {
			addressLvAdapter listItemAdapter = new addressLvAdapter(this, list,
					R.layout.addresslist, new String[] { Constants.COLUMNNAME,
							Constants.COLUMNPHONE, Constants.COLUMNMOBLIE,
							Constants.COLUMNENTPHONE, Constants.COLUMNEMAIL },
					new int[] { R.id.TxtName, R.id.TxtPhone, R.id.TxtMobile,
							R.id.TxtEntPhone, R.id.TxtEmail });
			setListAdapter(listItemAdapter);
		}
		String strInfo = null;
		switch (list.size()) {
		case 0:
			strInfo = "No result found!";
			break;
		case 1:
			strInfo = "1 result found!";
			break;
		default:
			strInfo = list.size() + " results found!";
			break;
		}
		Toast.makeText(JetmanApp.getContext(), strInfo, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 更新数据库
	 * 
	 * @author Administrator
	 * 
	 */
	class BtnUpdateDbListener implements OnClickListener {
		public void onClick(View v) {
			Toast.makeText(JetmanApp.getContext(), "正在更新，请耐心等待!",
					Toast.LENGTH_SHORT).show();
			Thread updateDbThread = new Thread(updateThread);
			updateDbThread.start();
		}

	}

	Handler handler = new Handler() {
		/**
		 * 更新表数据
		 */
		@Override
		public void handleMessage(Message msg) {
			DataBaseHelper dataBaseHelper = new DataBaseHelper(
					JetmanActivity.this, "myDb", null, 1);
			final SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
			Cursor cursor = db.query(SvnUser.tblName, new String[] {
					SvnUser.tblColName, SvnUser.tblColPassword }, null, null,
					null, null, null);
			if (!cursor.moveToNext()) {
				Toast.makeText(JetmanApp.getContext(), "您还没有配置SVN用户名密码",
						Toast.LENGTH_SHORT).show();
				return;
			}

			String strName = cursor.getString(cursor
					.getColumnIndex(SvnUser.tblColName));
			String strPwd = cursor.getString(cursor
					.getColumnIndex(SvnUser.tblColPassword));

			Address address = new Address();
			List<AddressNode> addressNodeList = address.getAllAddressNodes(
					strName, strPwd);
			if (null == addressNodeList) {
				return;
			}
			db.beginTransaction();
			// 全删
			db.delete("AddressBookOfDcjet", "1", null);
			// 全插
			for (AddressNode node : addressNodeList) {
				ContentValues values = new ContentValues();
				values.put(Constants.COLUMNNAME, node.getName());
				values.put(Constants.COLUMNSEX, node.getSex());
				values.put(Constants.COLUMNPHONE, node.getPhone());
				values.put(Constants.COLUMNMOBLIE, node.getMobile());
				values.put(Constants.COLUMNENTPHONE, node.getEntphone());
				values.put(Constants.COLUMNEMAIL, node.getEmail());
				db.insert("AddressBookOfDcjet", null, values);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			Toast.makeText(JetmanApp.getContext(), "本地数据更新完毕",
					Toast.LENGTH_SHORT).show();
			if (db.isOpen())
				db.close();
		}

	};
	// 更新数据库线程
	Runnable updateThread = new Runnable() {
		public void run() {
			Message msg = handler.obtainMessage();
			msg.sendToTarget();
		}
	};
}
