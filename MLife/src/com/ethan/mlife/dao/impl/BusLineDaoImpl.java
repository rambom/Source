package com.ethan.mlife.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ethan.mlife.dao.AbstractBaseDao;
import com.ethan.mlife.dao.IBusLineDao;
import com.ethan.mlife.entity.Line;
import com.ethan.mlife.util.StringUtil;

public class BusLineDaoImpl extends AbstractBaseDao<Line> implements
		IBusLineDao {

	public BusLineDaoImpl() {
		this.TABLE_NAME = "bus_line_his";
	}

	public List<Line> query(Line example, String groupby, String having,
			String orderby) {
		// TODO Auto-generated method stub
		List<Line> listLine = new ArrayList<Line>();

		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		// 查询条件
		StringBuilder sbWhere = new StringBuilder();
		// 绑定参数
		String[] strArguments = this.getWhereArguments(example, sbWhere);
		Cursor cursor = db.query(this.TABLE_NAME, null, sbWhere.toString(),
				strArguments, groupby, having, orderby);

		while (cursor.moveToNext()) {
			listLine.add(new Line(cursor.getString(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3), cursor
							.getString(4), cursor.getString(5), cursor
							.getString(6), cursor.getString(7), cursor
							.getString(8), cursor.getString(9)));
		}
		cursor.close();
		db.close();
		return listLine;
	}

	public long insert(Line value) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();

		long lEffectCount = db.insert(this.TABLE_NAME, null,
				this.getContentValues(value));
		db.close();
		return lEffectCount;
	}

	public long update(Line example, Line value) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		// 查询条件
		StringBuilder sbWhere = new StringBuilder();
		// 绑定参数
		String[] strArguments = this.getWhereArguments(example, sbWhere);

		long lEffectCount = db.update(this.TABLE_NAME,
				this.getContentValues(value), sbWhere.toString(), strArguments);
		db.close();
		return lEffectCount;
	}

	public long delete(Line example) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		// 查询条件
		StringBuilder sbWhere = new StringBuilder();
		// 绑定参数
		String[] strArguments = this.getWhereArguments(example, sbWhere);

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
	protected String[] getWhereArguments(Line example, StringBuilder sbWhere) {
		// TODO Auto-generated method stub
		List<String> listArgs = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(example.getCityRegion())) {
			sbWhere.append("and city_region=? ");
			listArgs.add(example.getCityRegion());
		}
		if (!StringUtil.isNullOrEmpty(example.getLineNo())) {
			sbWhere.append("and line_No=? ");
			listArgs.add(example.getLineNo());
		}
		if (!StringUtil.isNullOrEmpty(example.getId())) {
			sbWhere.append("and id=? ");
			listArgs.add(example.getId());
		}
		if (!StringUtil.isNullOrEmpty(example.getDirection())) {
			sbWhere.append("and direction=? ");
			listArgs.add(example.getDirection());
		}
		if (!StringUtil.isNullOrEmpty(example.getVeNumber())) {
			sbWhere.append("and ve_number=? ");
			listArgs.add(example.getVeNumber());
		}
		if (!StringUtil.isNullOrEmpty(example.getUpdateTime())) {
			sbWhere.append("and update_time=? ");
			listArgs.add(example.getUpdateTime());
		}
		if (!StringUtil.isNullOrEmpty(example.getSpacing())) {
			sbWhere.append("and spacing=? ");
			listArgs.add(example.getSpacing());
		}
		if (!StringUtil.isNullOrEmpty(example.getVeStation())) {
			sbWhere.append("and ve_station=? ");
			listArgs.add(example.getVeStation());
		}
		if (!StringUtil.isNullOrEmpty(example.getUrlLink())) {
			sbWhere.append("and url_link=? ");
			listArgs.add(example.getUrlLink());
		}
		if (!StringUtil.isNullOrEmpty(example.getDescription())) {
			sbWhere.append("and description=? ");
			listArgs.add(example.getDescription());
		}
		sbWhere.delete(0, 3);
		return listArgs.toArray(new String[] {});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.mlife.dao.AbstractBaseDao#getContentValues(java.lang.Object)
	 */
	@Override
	protected ContentValues getContentValues(Line example) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();

		if (!StringUtil.isNullOrEmpty(example.getCityRegion())) {
			cv.put("city_region", example.getCityRegion());
		}
		if (!StringUtil.isNullOrEmpty(example.getLineNo())) {
			cv.put("line_no", example.getLineNo());
		}
		if (!StringUtil.isNullOrEmpty(example.getId())) {
			cv.put("id", example.getId());
		}
		if (!StringUtil.isNullOrEmpty(example.getDirection())) {
			cv.put("direction", example.getDirection());
		}
		if (!StringUtil.isNullOrEmpty(example.getVeNumber())) {
			cv.put("ve_number", example.getVeNumber());
		}
		if (!StringUtil.isNullOrEmpty(example.getUpdateTime())) {
			cv.put("update_time", example.getUpdateTime());
		}
		if (!StringUtil.isNullOrEmpty(example.getSpacing())) {
			cv.put("spacing", example.getSpacing());
		}
		if (!StringUtil.isNullOrEmpty(example.getVeStation())) {
			cv.put("ve_station", example.getVeStation());
		}
		if (!StringUtil.isNullOrEmpty(example.getUrlLink())) {
			cv.put("url_link", example.getUrlLink());
		}
		if (!StringUtil.isNullOrEmpty(example.getDescription())) {
			cv.put("description", example.getDescription());
		}
		return cv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.mlife.dao.IBaseDao#queryWithSql(java.lang.String)
	 */
	public List<Line> queryWithSql(String sqlWhere) {
		// TODO Auto-generated method stub
		List<Line> listLine = new ArrayList<Line>();

		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(String.format("select * from %s where %s",
				this.TABLE_NAME, sqlWhere), null);
		while (cursor.moveToNext()) {
			listLine.add(new Line(cursor.getString(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3), cursor
							.getString(4), cursor.getString(5), cursor
							.getString(6), cursor.getString(7), cursor
							.getString(8), cursor.getString(9)));
		}
		cursor.close();
		db.close();
		return listLine;
	}

}
