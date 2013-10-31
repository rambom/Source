package com.ethan.mlife.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "bus_db";
	private final static int DATABASE_VERSION = 1;
	private static Map<String, String> createTableSql = new HashMap<String, String>();

	static {
		// 公交收藏
		createTableSql
				.put("favorite_bus",
						"create table %s (GUID varchar(32),CITY_REGION varchar(10),FAVORITE_NAME varchar(50),BUS_TYPE integer,URL varchar(250),DEMO varchar(250),VISIBILITY integer,CLICK_COUNT integer,INSERT_TIME varchar(50),UPDATE_TIME varchar(50))");
		// 线路查询历史
		createTableSql
				.put("bus_line_his",
						"create table %s (CITY_REGION varchar(10),LINE_NO varchar(50),ID varchar(50),DIRECTION varchar(100),VE_NUMBER varchar(50),UPDATE_TIME varchar(50),SPACING varchar(10),VE_STATION varchar(50),URL_LINK varchar(250),DESCRIPTION varchar(250))");
		// 站台查询历史
		createTableSql
				.put("bus_station_his",
						"create table %s (CITY_REGION varchar(10),NAME varchar(50),ID varchar(50),SCODE varchar(10),DISTRICT varchar(50),STREET varchar(50),AREA varchar(50),DIRECTION varchar(100),VE_NUMBER varchar(50),PASS_TIME varchar(50),URL_LINK varchar(250),DESCRIPTION varchar(250))");
	}

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Iterator<Entry<String, String>> iterator = createTableSql.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			db.execSQL(String.format(entry.getValue(), entry.getKey()));
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Iterator<Entry<String, String>> iterator = createTableSql.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			db.execSQL(String.format("drop table if exists %s", entry.getKey()));
		}
		onCreate(db);
	}
}
