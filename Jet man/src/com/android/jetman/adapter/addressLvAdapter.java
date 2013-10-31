package com.android.jetman.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.graphics.Color;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jetman.R;
import com.android.jetman.entity.AddressNode;

public class addressLvAdapter extends BaseAdapter {

	private class buttonViewHolder {
		TextView TxtName;
		TextView TxtPhone;
		TextView TxtMobile;
		TextView TxtEntPhone;
		TextView TxtEmail;
		ImageView btnSave;
		LinearLayout rowPhone;
		LinearLayout rowEntPhone;
		LinearLayout rowMobile;
		LinearLayout rowEmail;
		View choiceView;
	}

	private ArrayList<HashMap<String, String>> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;
	private String[] keyString;
	private int[] valueViewID;
	private buttonViewHolder holder;

	public addressLvAdapter(Context context,
			ArrayList<HashMap<String, String>> appList, int resource,
			String[] from, int[] to) {
		mContext = context;
		mAppList = appList;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		keyString = new String[from.length];
		valueViewID = new int[to.length];
		System.arraycopy(from, 0, keyString, 0, from.length);
		System.arraycopy(to, 0, valueViewID, 0, to.length);
	}

	public int getCount() {
		return mAppList.size();
	}

	public Object getItem(int position) {
		return mAppList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void removeItem(int position) {
		mAppList.remove(position);
		this.notifyDataSetChanged();
	}

	synchronized protected View getAddressListView() {
		LayoutInflater factory = LayoutInflater.from(mContext);
		return factory.inflate(R.layout.addresslist, null);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if ( convertView != null) {
			holder = (buttonViewHolder) convertView.getTag();
		} else {
			convertView = mInflater.inflate(R.layout.addresslist, null);
			// convertView=getAddressListView();
			holder = new buttonViewHolder();
			holder.TxtName = (TextView) convertView
					.findViewById(valueViewID[0]);
			holder.TxtPhone = (TextView) convertView
					.findViewById(valueViewID[1]);
			holder.TxtMobile = (TextView) convertView
					.findViewById(valueViewID[2]);
			holder.TxtEntPhone = (TextView) convertView
					.findViewById(valueViewID[3]);
			holder.TxtEmail = (TextView) convertView
					.findViewById(valueViewID[4]);
			holder.btnSave = (ImageView) convertView
					.findViewById(R.id.imageViewSave);
			holder.rowPhone = (LinearLayout) convertView
					.findViewById(R.id.rowPhone);
			holder.rowEntPhone = (LinearLayout) convertView
					.findViewById(R.id.rowEntPhone);
			holder.rowMobile = (LinearLayout) convertView
					.findViewById(R.id.rowMobile);
			holder.rowEmail = (LinearLayout) convertView
					.findViewById(R.id.rowEmail);
			View view = mInflater.inflate(R.layout.choiceview, null);
			holder.choiceView = view;
			convertView.setTag(holder);
		}
		
		HashMap<String, String> appInfo = mAppList.get(position);
		if (appInfo != null) {
			String strName = (String) appInfo.get(keyString[0]);
			String strPhone = (String) appInfo.get(keyString[1]);
			String strMobile = (String) appInfo.get(keyString[2]);
			String strEntPhone = (String) appInfo.get(keyString[3]);
			String strEmail = (String) appInfo.get(keyString[4]);
			holder.TxtName.setText(strName);
			if ("".equals(strPhone))
				holder.rowPhone.setVisibility(LinearLayout.GONE);
			else
			{
				holder.TxtPhone.setText(strPhone);
				holder.rowPhone.setVisibility(LinearLayout.VISIBLE);
			}

			holder.TxtMobile.setText(strMobile);

			if ("".equals(strEntPhone))
				holder.rowEntPhone.setVisibility(LinearLayout.GONE);
			else
			{
				holder.TxtEntPhone.setText(strEntPhone);
				holder.rowEntPhone.setVisibility(LinearLayout.VISIBLE);
			}

			if ("".equals(strEmail))
				holder.rowEmail.setVisibility(LinearLayout.GONE);
			else
			{
				holder.TxtEmail.setText(strEmail);
				holder.rowEmail.setVisibility(LinearLayout.VISIBLE);
			}

			holder.rowMobile
					.setOnClickListener(new TxtMobileListener(position));
			holder.rowEntPhone.setOnClickListener(new TxtEntPhoneListener(
					position));
			holder.rowEmail.setOnClickListener(new TxtEmailListener(position));
			holder.rowPhone.setOnClickListener(new TxtPhoneListener(position));
			holder.btnSave.setOnClickListener(new btnSaveListener(position,
					strName, strMobile, strEmail, strEntPhone));
		}
		return convertView;
	}

	// 保存数据至手机通讯录
	class btnSaveListener implements OnClickListener {
		private int position;
		private String strName;
		private String strMobile;
		private String strEmail;
		private String strEntPhone;

		btnSaveListener(int pos, String name, String mobile, String email,
				String entphone) {
			position = pos;
			strName = name;
			strMobile = mobile;
			strEmail = email;
			strEntPhone = entphone;
		}

		public void onClick(View v) {
			AddressNode node = getNodeInfo(position);
			int vid = v.getId();
			if (vid == holder.btnSave.getId()) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
				dialog.setTitle("请确认！")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setMessage("保存至手机通讯录")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										boolean blnOk = SavaToContact(strName,
												strMobile, strEmail,
												strEntPhone);
										String strRes = "";
										if (blnOk) {
											strRes = "保存成功！";
										} else {
											strRes = "保存失败！";
										}
										Toast.makeText(mContext, strRes,
												Toast.LENGTH_SHORT).show();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();// 取消弹出框
									}
								}).create().show();
			}
		}
	}

	// 保存数据库数据到手机本地通讯录
	private boolean SavaToContact(String name, String mobile, String email,
			String entphone) {
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex = ops.size();
		ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
				.withValue(RawContacts.ACCOUNT_TYPE, null)
				.withValue(RawContacts.ACCOUNT_NAME, null).build());
		// 文档位置：reference/android/provider/ContactsContract.Data.html
		ops.add(ContentProviderOperation
				.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID,
						rawContactInsertIndex)
				.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.GIVEN_NAME, name).build());
		// 更新手机号码：Data.RAW_CONTACT_ID 获取上一条语句插入联系人时产生的 ID
		if (null != entphone && !"".equals(entphone))
			ops.add(ContentProviderOperation
					.newInsert(
							android.provider.ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(Data.RAW_CONTACT_ID,
							rawContactInsertIndex)
					.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
					.withValue(Phone.NUMBER, entphone)
					// "data1"
					.withValue(Phone.TYPE, Phone.TYPE_WORK_MOBILE)
					.withValue(Phone.LABEL, "单位").build());
		if (null != mobile && !"".equals(mobile))
			ops.add(ContentProviderOperation
					.newInsert(
							android.provider.ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(Data.RAW_CONTACT_ID,
							rawContactInsertIndex)
					.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
					.withValue(Phone.NUMBER, mobile)
					// "data1"
					.withValue(Phone.TYPE, Phone.TYPE_MOBILE)
					.withValue(Phone.LABEL, "手机号").build());
		if (null != email && !"".equals(email))
			ops.add(ContentProviderOperation
					.newInsert(
							android.provider.ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(Data.RAW_CONTACT_ID,
							rawContactInsertIndex)
					.withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
					.withValue(Email.DATA, email)
					.withValue(Email.TYPE, Email.TYPE_WORK).build());

		// 批量插入 -- 在同一个事务当中
		ContentProviderResult[] results = null;
		try {
			results = mContext.getContentResolver().applyBatch(
					ContactsContract.AUTHORITY, ops);
		} catch (RemoteException e) {
			e.printStackTrace();
			Toast.makeText(mContext, "保存", Toast.LENGTH_SHORT).show();
			return false;
		} catch (OperationApplicationException e) {
			e.printStackTrace();
			Toast.makeText(mContext, "保存", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	// 发邮件事件
	class TxtEmailListener implements OnClickListener {
		private int position;

		TxtEmailListener(int pos) {
			position = pos;
		}

		public void onClick(View v) {
			AddressNode node = getNodeInfo(position);
			int vid = v.getId();
			if (vid == holder.rowEmail.getId()) {
				Uri uri = Uri.parse("mailto:" + node.getEmail());
				Intent it = new Intent(Intent.ACTION_SENDTO, uri);
				mContext.startActivity(it);
			}
		}
	}

	// 企业小号
	class TxtEntPhoneListener implements OnClickListener {
		private int position;

		TxtEntPhoneListener(int pos) {
			position = pos;
		}

		public void onClick(View v) {
			final AddressNode node = getNodeInfo(position);
			int vid = v.getId();
			if (vid == holder.rowEntPhone.getId()) {

				LinearLayout viewContent = new LinearLayout(mContext);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				viewContent.setLayoutParams(params);

				ImageView btnCall = new ImageView(mContext);
				LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
				btnParams.gravity = Gravity.CENTER;
				btnCall.setImageResource(R.drawable.phone);
				btnCall.setLayoutParams(btnParams);
				viewContent.addView(btnCall);

				ImageView btnMessage = new ImageView(mContext);
				btnMessage.setImageResource(R.drawable.chat);
				btnMessage.setLayoutParams(btnParams);
				viewContent.addView(btnMessage);

				//title
				FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				
				FrameLayout viewTitle = new FrameLayout(mContext);
				viewTitle.setLayoutParams(params2);

				TextView title = new TextView(mContext);
				title.setTextSize(28);
				title.setTextColor(Color.rgb(255, 255, 255));

				FrameLayout.LayoutParams titleParams = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				
				titleParams.gravity = Gravity.CENTER;
				title.setLayoutParams(titleParams);
				title.setGravity(Gravity.CENTER);
				viewTitle.addView(title);

				ImageView cancel = new ImageView(mContext);
				FrameLayout.LayoutParams cancelParams = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				cancelParams.gravity = Gravity.RIGHT;
				cancel.setLayoutParams(cancelParams);
				cancel.setImageResource(R.drawable.cancel);
				
				viewTitle.addView(cancel);

				title.setText(node.getEntphone());
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				final AlertDialog dialog = builder.setCustomTitle(viewTitle)
						.setView(viewContent).create();
				dialog.show();
				btnCall.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dialog.cancel();
						makeDirectCall(node.getEntphone());
					}
				});
				btnMessage.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dialog.cancel();
						sendMessage(node.getEntphone());
					}
				});
				cancel.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dialog.cancel();
					}
				});
			}
		}
	}

	// 座机号码点击事件
	class TxtPhoneListener implements OnClickListener {
		private int position;

		TxtPhoneListener(int pos) {
			position = pos;
		}

		public void onClick(View v) {
			final AddressNode node = getNodeInfo(position);
			int vid = v.getId();
			if (vid == holder.rowPhone.getId()) {
				makeCall("0512" + node.getPhone());
			}
		}
	}

	// 手机号码点击事件
	class TxtMobileListener implements OnClickListener {
		private int position;
		private AlertDialog alertDialog;

		TxtMobileListener(int pos) {
			position = pos;
		}

		public void onClick(View v) {
			final AddressNode node = getNodeInfo(position);
			int vid = v.getId();
			if (vid == holder.rowMobile.getId()) {
				
				LinearLayout viewContent = new LinearLayout(mContext);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				viewContent.setLayoutParams(params);

				ImageView btnCall = new ImageView(mContext);
				LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
				btnParams.gravity = Gravity.CENTER;
				btnCall.setImageResource(R.drawable.phone);
				btnCall.setLayoutParams(btnParams);
				viewContent.addView(btnCall);

				ImageView btnMessage = new ImageView(mContext);
				btnMessage.setImageResource(R.drawable.chat);
				btnMessage.setLayoutParams(btnParams);
				viewContent.addView(btnMessage);

				//title
				FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				
				FrameLayout viewTitle = new FrameLayout(mContext);
				viewTitle.setLayoutParams(params2);

				TextView title = new TextView(mContext);
				title.setTextSize(28);
				title.setTextColor(Color.rgb(255, 255, 255));

				FrameLayout.LayoutParams titleParams = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				
				titleParams.gravity = Gravity.CENTER;
				title.setLayoutParams(titleParams);
				title.setGravity(Gravity.CENTER);
				viewTitle.addView(title);

				ImageView cancel = new ImageView(mContext);
				FrameLayout.LayoutParams cancelParams = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				cancelParams.gravity = Gravity.RIGHT;
				cancel.setLayoutParams(cancelParams);
				cancel.setImageResource(R.drawable.cancel);
				
				viewTitle.addView(cancel);
				
				title.setText(node.getMobile());

				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				alertDialog = builder.setCustomTitle(viewTitle)
						.setView(viewContent).create();
				alertDialog.show();
				btnCall.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						alertDialog.cancel();
						makeDirectCall(node.getMobile());
					}
				});
				btnMessage.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						alertDialog.cancel();
						sendMessage(node.getMobile());
					}
				});
				cancel.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						alertDialog.cancel();
					}
				});
			}
		}
	}

	private void makeCall(String strNum) {
		Uri uri = Uri.parse("tel:" + strNum);
		Intent it = new Intent(Intent.ACTION_DIAL, uri);
		mContext.startActivity(it);
	}

	private void makeDirectCall(String strNum) {
		Uri uri = Uri.parse("tel:" + strNum);
		Intent it = new Intent(Intent.ACTION_CALL, uri);
		mContext.startActivity(it);
	}

	private void sendMessage(String strNum) {
		Uri uri = Uri.parse("smsto:" + strNum);
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		mContext.startActivity(it);
	}

	// 根据position获取节点通讯信息
	private AddressNode getNodeInfo(int position) {
		AddressNode node = new AddressNode();
		HashMap<String, String> appInfo = mAppList.get(position);
		if (appInfo == null)
			return null;
		node.setName((String) appInfo.get(keyString[0]));
		node.setPhone((String) appInfo.get(keyString[1]));
		node.setMobile((String) appInfo.get(keyString[2]));
		node.setEntphone((String) appInfo.get(keyString[3]));
		node.setEmail((String) appInfo.get(keyString[4]));
		return node;

	}
}
