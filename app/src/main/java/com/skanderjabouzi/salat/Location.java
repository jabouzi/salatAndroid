package com.skanderjabouzi.salat;

import java.io.Serializable;

public class Location implements Serializable{

	private long id;
	private float latitude;
	private float longitude;
	private String country;
	private String city;
	private float timezone;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getTimezone() {
		return timezone;
	}

	public void setTimezone(float timezone) {
		this.timezone = timezone;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	// Will be used by the ArrayAdapter in the ListView
	//@Override
	//public String toString() {
		//return comment;
	//}
}
