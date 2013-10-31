package com.android.junit;

import android.os.Parcel;

/**
 * 站台信息
 * 
 * @author Ethan
 * 
 */
public class Line {
	/**
	 * 城市区号
	 */
	private String cityRegion;
	/**
	 * 线路编号
	 */
	private String lineNo;
	/**
	 * 唯一标识
	 */
	private String id;
	/**
	 * 方向
	 */
	private String direction;
	/**
	 * 车牌号
	 */
	private String veNumber;
	/**
	 * 更新时间
	 */
	private String updateTime;
	/**
	 * 站距
	 */
	private String spacing;
	/**
	 * 车辆到站
	 */
	private String veStation;
	/**
	 * 链接地址
	 */
	private String urlLink;
	/**
	 * 描述
	 */
	private String description;

	public Line() {
	}

	/**
	 * @param cityRegion
	 *            城市区号
	 * @param lineNo
	 *            线路编号
	 * @param id
	 *            唯一标识
	 * @param direction
	 *            方向
	 * @param veNumber
	 *            车牌号
	 * @param updateTime
	 *            更新时间
	 * @param spacing
	 *            站距
	 * @param urlLink
	 *            链接地址
	 * @param description
	 *            描述
	 */
	public Line(String cityRegion, String lineNo, String id, String direction,
			String veNumber, String updateTime, String spacing,
			String veStation, String urlLink, String description) {
		super();
		this.cityRegion = cityRegion;
		this.lineNo = lineNo;
		this.id = id;
		this.direction = direction;
		this.veNumber = veNumber;
		this.updateTime = updateTime;
		this.spacing = spacing;
		this.veStation = veStation;
		this.urlLink = urlLink;
		this.description = description;
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
	 * @return the lineNo
	 */
	public String getLineNo() {
		return lineNo;
	}

	/**
	 * @param lineNo
	 *            the lineNo to set
	 */
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}

	/**
	 * @return the veNumber
	 */
	public String getVeNumber() {
		return veNumber;
	}

	/**
	 * @param veNumber
	 *            the veNumber to set
	 */
	public void setVeNumber(String veNumber) {
		this.veNumber = veNumber;
	}

	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the spacing
	 */
	public String getSpacing() {
		return spacing;
	}

	/**
	 * @param spacing
	 *            the spacing to set
	 */
	public void setSpacing(String spacing) {
		this.spacing = spacing;
	}

	/**
	 * @return the veStation
	 */
	public String getVeStation() {
		return veStation;
	}

	/**
	 * @param veStation
	 *            the veStation to set
	 */
	public void setVeStation(String veStation) {
		this.veStation = veStation;
	}

	/**
	 * @return the urlLink
	 */
	public String getUrlLink() {
		return urlLink;
	}

	/**
	 * @param urlLink
	 *            the urlLink to set
	 */
	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<Line>");
		sb.append("\n");
		sb.append(String.format("<cityRegion>%s</cityRegion>", this.cityRegion));
		sb.append("\n");
		sb.append(String.format("<lineNo>%s</lineNo>", this.lineNo));
		sb.append("\n");
		sb.append(String.format("<id>%s</id>", this.id));
		sb.append("\n");
		sb.append(String.format("<direction>%s</direction>", this.direction));
		sb.append("\n");
		sb.append(String.format("<veNumber>%s</veNumber>", this.veNumber));
		sb.append("\n");
		sb.append(String.format("<updateTime>%s</updateTime>", this.updateTime));
		sb.append("\n");
		sb.append(String.format("<spacing>%s</spacing>", this.spacing));
		sb.append("\n");
		sb.append(String.format("<veStation>%s</veStation>", this.veStation));
		sb.append("\n");
		sb.append(String.format("<urlLink>%s</urlLink>", this.urlLink));
		sb.append("\n");
		sb.append(String.format("<description>%s</description>",
				this.description));
		sb.append("\n");
		sb.append("</Line>");
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#describeContents()
	 */
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(cityRegion);
		dest.writeString(lineNo);
		dest.writeString(id);
		dest.writeString(direction);
		dest.writeString(veNumber);
		dest.writeString(updateTime);
		dest.writeString(veStation);
		dest.writeString(spacing);
		dest.writeString(urlLink);
		dest.writeString(description);
	}
}