package com.ethan.mlife.dao;

import com.ethan.mlife.MLifeApp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class AbstractBaseDao<T> {
	// 操作的表名
	protected String TABLE_NAME;

	// sqlite instance
	protected final SQLiteOpenHelper sqliteHelper = MLifeApp.getSqliteHelper();

	/**
	 * 生成过滤条件
	 * 
	 * @param example
	 * @param sbWhere
	 * @return
	 */
	protected abstract String[] getWhereArguments(T example,
			StringBuilder sbWhere);

	/**
	 * 生成要修改的键值对
	 * 
	 * @param example
	 * @return
	 */
	protected abstract ContentValues getContentValues(T example);

}
