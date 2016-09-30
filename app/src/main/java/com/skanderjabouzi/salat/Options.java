package com.skanderjabouzi.salat;

public class Options {

	private long id;
	private int method;
	private int asr;
	private int hijri;
	private int higherLatitude;
	private int adhan;
	private int autoLocation;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public int getAsr() {
		return asr;
	}

	public void setAsr(int asr) {
		this.asr = asr;
	}

	public int getHijri() {
		return hijri;
	}

	public void setHijri(int hijri) {
		this.hijri = hijri;
	}

	public int getHigherLatitude() {
		return higherLatitude;
	}

	public void setHigherLatitude(int higherLatitude) {
		this.higherLatitude = higherLatitude;
	}

	public int getAdhan() {
		return adhan;
	}

	public void setAdhan(int adhan) {
		this.adhan = adhan;
	}
	
	public int getAutoLocation() {
		return autoLocation;
	}

	public void setAutoLocation(int autoLocation) {
		this.autoLocation = autoLocation;
	}

}
