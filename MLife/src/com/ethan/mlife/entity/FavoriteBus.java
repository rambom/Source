package com.ethan.mlife.entity;

import java.util.Date;

public class FavoriteBus {

	public final static int VISIBLE = 1;
	public final static int HIDDEN = 0;

	/**
	 * 收藏主键
	 */
	private String guid;
	/**
	 * 城市区号
	 */
	private String cityRegion;
	/**
	 * 收藏名称
	 */
	private String favoriteName;
	/**
	 * 收藏类型
	 */
	private Integer busType;
	/**
	 * 访问地址
	 */
	private String url;
	/**
	 * 描述备注
	 */
	private String demo;
	/**
	 * 是否显示
	 */
	private Integer visibility;
	/**
	 * 点击次数
	 */
	private Integer clickCount;
	/**
	 * 插入时间
	 */
	private Date insertTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

	public FavoriteBus() {
	}

	/**
	 * @param guid
	 *            收藏主键
	 * @param cityRegion
	 *            城市区号
	 * @param favoriteName
	 *            收藏名称
	 * @param busType
	 *            收藏类型
	 * @param url
	 *            访问地址
	 * @param demo
	 *            描述备注
	 * @param visibility
	 *            是否显示
	 * @param clickCount
	 *            点击次数
	 * @param insertTime
	 *            插入时间
	 * @param updateTime
	 *            更新时间
	 */
	public FavoriteBus(String guid, String cityRegion, String favoriteName,
			Integer busType, String url, String demo, Integer visibility,
			Integer clickCount, Date insertTime, Date updateTime) {
		this.guid = guid;
		this.cityRegion = cityRegion;
		this.favoriteName = favoriteName;
		this.busType = busType;
		this.url = url;
		this.demo = demo;
		this.visibility = visibility;
		this.clickCount = clickCount;
		this.insertTime = insertTime;
		this.updateTime = updateTime;
	}

	/**
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * @param guid
	 *            the guid to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * @return the cityRegion
	 */
	public String getCityRegion() {
		return cityRegion;
	}

	/**
	 * @param cityRegion
	 *            the cityRegion to set
	 */
	public void setCityRegion(String cityRegion) {
		this.cityRegion = cityRegion;
	}

	/**
	 * @return the favoriteName
	 */
	public String getFavoriteName() {
		return favoriteName;
	}

	/**
	 * @param favoriteName
	 *            the favoriteName to set
	 */
	public void setFavoriteName(String favoriteName) {
		this.favoriteName = favoriteName;
	}

	/**
	 * @return the busType
	 */
	public Integer getBusType() {
		return busType;
	}

	/**
	 * @param busType
	 *            the busType to set
	 */
	public void setBusType(Integer busType) {
		this.busType = busType;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the demo
	 */
	public String getDemo() {
		return demo;
	}

	/**
	 * @param demo
	 *            the demo to set
	 */
	public void setDemo(String demo) {
		this.demo = demo;
	}

	/**
	 * @return the visibility
	 */
	public Integer getVisibility() {
		return visibility;
	}

	/**
	 * @param visibility
	 *            the visibility to set
	 */
	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}

	/**
	 * @return the clickCount
	 */
	public Integer getClickCount() {
		return clickCount;
	}

	/**
	 * @param clickCount
	 *            the clickCount to set
	 */
	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}

	/**
	 * @return the insertTime
	 */
	public Date getInsertTime() {
		return insertTime;
	}

	/**
	 * @param insertTime
	 *            the insertTime to set
	 */
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<FavoriteBus>");
		sb.append("\n");
		sb.append(String.format("<guid>%s</guid>", this.guid));
		sb.append("\n");
		sb.append(String.format("<cityRegion>%s</cityRegion>", this.cityRegion));
		sb.append("\n");
		sb.append(String.format("<favoriteName>%s</favoriteName>",
				this.favoriteName));
		sb.append("\n");
		sb.append(String.format("<busType>%s</busType>", this.busType));
		sb.append("\n");
		sb.append(String.format("<url>%s</url>", this.url));
		sb.append("\n");
		sb.append(String.format("<demo>%s</demo>", this.demo));
		sb.append("\n");
		sb.append(String.format("<visibility>%s</visibility>", this.visibility));
		sb.append("\n");
		sb.append(String.format("<clickCount>%s</clickCount>", this.clickCount));
		sb.append("\n");
		sb.append(String.format("<insertTime>%s</insertTime>", this.insertTime));
		sb.append("\n");
		sb.append(String.format("<updateTime>%s</updateTime>", this.updateTime));
		sb.append("\n");
		sb.append("</FavoriteBus>");
		return sb.toString();
	}
}
