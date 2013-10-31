package com.android.ringfly.entity.mapping;

public class Level {
	private String userid;
	private Integer seasonId;
	private Integer level;
	private boolean lock;
	private Integer stars;
	private double coin;
	private double magic;
	private String demoneacapenums;
	private String demondisappearnum;
	private String demondienum;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Integer getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(Integer seasonId) {
		this.seasonId = seasonId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public boolean getLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public Integer getStars() {
		return stars;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
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

	public String getDemoneacapenums() {
		return demoneacapenums;
	}

	public void setDemoneacapenums(String demoneacapenums) {
		this.demoneacapenums = demoneacapenums;
	}

	public String getDemondisappearnum() {
		return demondisappearnum;
	}

	public void setDemondisappearnum(String demondisappearnum) {
		this.demondisappearnum = demondisappearnum;
	}

	public String getDemondienum() {
		return demondienum;
	}

	public void setDemondienum(String demondienum) {
		this.demondienum = demondienum;
	}

	public void Init() {
		this.coin = 0;
		this.magic = 0;
		this.lock = true;
		this.stars = 0;
		demoneacapenums = "";
		demondisappearnum = "";
		demondienum = "";
	}

}
