package com.ethan.mlife.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ethan.mlife.dao.AbstractBaseDao;
import com.ethan.mlife.dao.IFavoriteBusDao;
import com.ethan.mlife.entity.FavoriteBus;
import com.ethan.mlife.util.DateUtil;
import com.ethan.mlife.util.StringUtil;

public class FavoriteBusDaoImpl extends AbstractBaseDao<FavoriteBus> implements
		IFavoriteBusDao {

	public FavoriteBusDaoImpl() {
		this.TABLE_NAME = "favorite_bus";
	}

	public List<FavoriteBus> query(FavoriteBus favoriteBus, String groupby,
			String having, String orderby) {
		List<FavoriteBus> listFavoriteBus = new ArrayList<FavoriteBus>();

		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		// 查询条件
		StringBuilder sbWhere = new StringBuilder();
		// 绑定参数
		String[] strArguments = this.getWhereArguments(favoriteBus, sbWhere);
		Cursor cursor = db.query(this.TABLE_NAME, null, sbWhere.toString(),
				strArguments, groupby, having, orderby);

		while (cursor.moveToNext()) {
			listFavoriteBus.add(new FavoriteBus(cursor.getString(0), cursor
					.getString(1), cursor.getString(2), cursor.getInt(3),
					cursor.getString(4), cursor.getString(5), cursor.getInt(6),
					cursor.getInt(7), DateUtil.convertStringToDate(cursor
							.getString(8)), DateUtil.convertStringToDate(cursor
							.getString(9))));
		}
		cursor.close();
		db.close();
		return listFavoriteBus;
	}

	public long insert(FavoriteBus favoriteBus) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();

		long lEffectCount = db.insert(this.TABLE_NAME, null,
				this.getContentValues(favoriteBus));
		db.close();
		return lEffectCount;
	}

	public long update(FavoriteBus where, FavoriteBus set) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		// 查询条件
		StringBuilder sbWhere = new StringBuilder();
		// 绑定参数
		String[] strArguments = this.getWhereArguments(where, sbWhere);

		long lEffectCount = db.update(this.TABLE_NAME,
				this.getContentValues(set), sbWhere.toString(), strArguments);
		db.close();
		return lEffectCount;
	}

	public long delete(FavoriteBus favoriteBus) {
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		// 查询条件
		StringBuilder sbWhere = new StringBuilder();
		// 绑定参数
		String[] strArguments = this.getWhereArguments(favoriteBus, sbWhere);

		long lEffectCount = db.delete(this.TABLE_NAME, sbWhere.toString(),
				strArguments);
		db.close();
		return lEffectCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.mlife.dao.AbstractBaseDao#getWhereArguments(java.lang.Object,
	 * java.lang.StringBuilder)
	 */
	@Override
	protected String[] getWhereArguments(FavoriteBus example,
			StringBuilder sbWhere) {
		List<String> listArgs = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(example.getGuid())) {
			sbWhere.append("and guid=? ");
			listArgs.add(example.getGuid());
		}
		if (!StringUtil.isNullOrEmpty(example.getCityRegion())) {
			sbWhere.append("and city_region=? ");
			listArgs.add(example.getCityRegion());
		}
		if (!StringUtil.isNullOrEmpty(example.getFavoriteName())) {
			sbWhere.append("and favorite_name=? ");
			listArgs.add(example.getFavoriteName());
		}
		if (!StringUtil.isNullOrEmpty(example.getBusType())) {
			sbWhere.append("and bus_type=? ");
			listArgs.add(example.getBusType().toString());
		}
		if (!StringUtil.isNullOrEmpty(example.getUrl())) {
			sbWhere.append("and url=? ");
			listArgs.add(example.getUrl());
		}
		if (!StringUtil.isNullOrEmpty(example.getDemo())) {
			sbWhere.append("and demo=? ");
			listArgs.add(example.getDemo());
		}
		if (!StringUtil.isNullOrEmpty(example.getVisibility())) {
			sbWhere.append("and visibility=? ");
			listArgs.add(example.getVisibility().toString());
		}
		if (!StringUtil.isNullOrEmpty(example.getClickCount())) {
			sbWhere.append("and click_count=? ");
			listArgs.add(example.getClickCount().toString());
		}
		if (!StringUtil.isNullOrEmpty(DateUtil.convertDateToString(example
				.getInsertTime()))) {
			sbWhere.append("and insert_time=? ");
			listArgs.add(DateUtil.convertDateToString(example.getInsertTime()));
		}
		if (!StringUtil.isNullOrEmpty(DateUtil.convertDateToString(example
				.getUpdateTime()))) {
			sbWhere.append("and update_time=? ");
			listArgs.add(DateUtil.convertDateToString(example.getUpdateTime()));
		}
		sbWhere.delete(0, 3);
		return listArgs.toArray(new String[] {});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.mlife.dao.AbstractBaseDao#getWhereArguments(java.lang.Object,
	 * java.lang.StringBuilder)
	 */
	@Override
	protected ContentValues getContentValues(FavoriteBus example) {

		ContentValues cv = new ContentValues();

		if (!StringUtil.isNullOrEmpty(example.getGuid())) {
			cv.put("guid", example.getGuid());
		}

		if (!StringUtil.isNullOrEmpty(example.getCityRegion())) {
			cv.put("city_region", example.getCityRegion());
		}

		if (!StringUtil.isNullOrEmpty(example.getFavoriteName())) {
			cv.put("favorite_name", example.getFavoriteName());
		}

		if (!StringUtil.isNullOrEmpty(example.getBusType())) {
			cv.put("bus_type", example.getBusType());
		}

		if (!StringUtil.isNullOrEmpty(example.getUrl())) {
			cv.put("url", example.getUrl());
		}

		if (!StringUtil.isNullOrEmpty(example.getDemo())) {
			cv.put("demo", example.getDemo());
		}

		if (!StringUtil.isNullOrEmpty(example.getVisibility())) {
			cv.put("visibility", example.getVisibility());
		}

		if (!StringUtil.isNullOrEmpty(example.getClickCount())) {
			cv.put("click_count", example.getClickCount());
		}

		if (!StringUtil.isNullOrEmpty(DateUtil.convertDateToString(example
				.getInsertTime()))) {
			cv.put("insert_time",
					DateUtil.convertDateToString(example.getInsertTime()));
		}
		if (!StringUtil.isNullOrEmpty(DateUtil.convertDateToString(example
				.getUpdateTime()))) {
			cv.put("update_time",
					DateUtil.convertDateToString(example.getUpdateTime()));
		}
		return cv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.mlife.dao.IBaseDao#queryWithSql(java.lang.String)
	 */
	public List<FavoriteBus> queryWithSql(String sqlWhere) {
		// TODO Auto-generated method stub
		List<FavoriteBus> listFavoriteBus = new ArrayList<FavoriteBus>();

		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(String.format("select * from %s where %s",
				this.TABLE_NAME, sqlWhere), null);

		while (cursor.moveToNext()) {
			listFavoriteBus.add(new FavoriteBus(cursor.getString(0), cursor
					.getString(1), cursor.getString(2), cursor.getInt(3),
					cursor.getString(4), cursor.getString(5), cursor.getInt(6),
					cursor.getInt(7), DateUtil.convertStringToDate(cursor
							.getString(8)), DateUtil.convertStringToDate(cursor
							.getString(9))));
		}
		cursor.close();
		db.close();
		return listFavoriteBus;
	}
}
