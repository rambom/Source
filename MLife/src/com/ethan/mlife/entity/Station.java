package com.ethan.mlife.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 公交站点
 * 
 * @author Ethan
 * 
 */
public class Station implements Parcelable {
	/**
	 * 城市区号
	 */
	private String cityRegion;
	/**
	 * 站台名称
	 */
	private String name;
	/**
	 * 站台ID
	 */
	private String id;
	/**
	 * 站台编号
	 */
	private String scode;
	/**
	 * 行政区域
	 */
	private String district;
	/**
	 * 所在道路
	 */
	private String street;
	/**
	 * 所在路段
	 */
	private String area;
	/**
	 * 方向
	 */
	private String direction;
	/**
	 * 车牌号
	 */
	private String veNumber;
	/**
	 * 通过时间
	 */
	private String passTime;
	/**
	 * 连接地址
	 */
	private String urlLink;
	/**
	 * 描述
	 */
	private String description;

	public Station() {
	}

	/**
	 * @param cityRegion
	 *            城市区号
	 * @param name
	 *            站台名
	 * @param id
	 *            唯一标识
	 * @param district
	 *            行政区域
	 * @param street
	 *            街道
	 * @param area
	 *            路段
	 * @param direction
	 *            行驶方向
	 * @param veNumber
	 *            车牌号
	 * @param passTime
	 *            通过时间
	 * @param urlLink
	 *            链接地址
	 * @param description
	 *            描述
	 */
	public Station(String cityRegion, String name, String id, String scode,
			String district, String street, String area, String direction,
			String veNumber, String passTime, String urlLink, String description) {
		super();
		this.cityRegion = cityRegion;
		this.name = name;
		this.id = id;
		this.scode = scode;
		this.district = district;
		this.street = street;
		this.area = area;
		this.direction = direction;
		this.veNumber = veNumber;
		this.passTime = passTime;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the id
	 */
	public String getScode() {
		return scode;
	}

	/**
	 * @param scode
	 *            the id to set
	 */
	public void setScode(String scode) {
		this.scode = scode;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district
	 *            the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(String area) {
		this.area = area;
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
	 * @return the passTime
	 */
	public String getPassTime() {
		return passTime;
	}

	/**
	 * @param passTime
	 *            the passTime to set
	 */
	public void setPassTime(String passTime) {
		this.passTime = passTime;
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
		sb.append("<Station>");
		sb.append("\n");
		sb.append(String.format("<cityRegion>%s</cityRegion>", this.cityRegion));
		sb.append("\n");
		sb.append(String.format("<id>%s</id>", this.id));
		sb.append("\n");
		sb.append(String.format("<scode>%s</scode>", this.scode));
		sb.append("\n");
		sb.append(String.format("<name>%s</name>", this.name));
		sb.append("\n");
		sb.append(String.format("<district>%s</district>", this.district));
		sb.append("\n");
		sb.append(String.format("<street>%s</street>", this.street));
		sb.append("\n");
		sb.append(String.format("<area>%s</area>", this.area));
		sb.append("\n");
		sb.append(String.format("<direction>%s</direction>", this.direction));
		sb.append("\n");
		sb.append(String.format("<veNumber>%s</veNumber>", this.veNumber));
		sb.append("\n");
		sb.append(String.format("<passTime>%s</passTime>", this.passTime));
		sb.append("\n");
		sb.append(String.format("<urlLink>%s</urlLink>", this.urlLink));
		sb.append("\n");
		sb.append(String.format("<description>%s</description>",
				this.description));
		sb.append("\n");
		sb.append("</Station>");
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
		dest.writeString(name);
		dest.writeString(id);
		dest.writeString(scode);
		dest.writeString(district);
		dest.writeString(street);
		dest.writeString(area);
		dest.writeString(direction);
		dest.writeString(veNumber);
		dest.writeString(passTime);
		dest.writeString(urlLink);
		dest.writeString(description);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Creator() {
		public Station createFromParcel(Parcel source) {
			return new Station(source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readString());
		}

		public Station[] newArray(int size) {
			return new Station[size];
		}
	};
}