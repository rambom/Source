package com.android.jetman.dbAccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table AddressBookOfDcjet(name varchar(10),sex varchar(1),phone varchar(10),mobile varchar(11),entphone varchar(10),email varchar(40))");
		db.execSQL("create table user(name varchar(20),password varchar(20))");
		System.out.println("a databaseCreated");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("databaseUpdated");
	}
}
