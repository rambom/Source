package com.android.jetman.entity;

public class SvnUser {
	private String name;
	private String password;
	public static final String tblName="user";	//存储该对象的表明
	public static final String tblColName="name";	//第一列名
	public static final String tblColPassword="password";	//第二列名

	public SvnUser(String name, String pwd) {
		this.name = name;
		this.password = pwd;
	}
	public SvnUser()
	{		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
