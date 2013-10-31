package com.android.ringfly.entity.mapping;

public class User {
	private String userid;
	private String username;
	private String password;
	private boolean sound;
	private double coin;
	private double magic;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getSound() {
		return sound;
	}

	public void setSound(boolean sound) {
		this.sound = sound;
	}

	public double getCoin() {
		return coin;
	}

	public void setCoin(double coin) {
		this.coin = coin;
	}

	public double getMagic() {
		return magic;
	}

	public void setMagic(double magic) {
		this.magic = magic;
	}

}
