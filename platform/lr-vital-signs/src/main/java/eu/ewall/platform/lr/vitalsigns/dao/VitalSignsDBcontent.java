package eu.ewall.platform.lr.vitalsigns.dao;

import java.util.Date;

public class VitalSignsDBcontent {
	
	private String _id;
	
	private String user;
	
	private Date dateTime;
	
	private String averInt; // the averaging interval
	
	private int counter; // the counter for the interval
	
	private double sbp;
	
	private double dbp;
	
	private double hr;
	
	private double hrv;
	
	private double os;
	
	
	
	public String get_Id() {
		return _id;
	}
	
	public String getUser() {
		return user;
	}
	
	public Date getDateTime() {
		return dateTime;
	}
	
	public String getAverInt() {
		return averInt;
	}
	
	public int getCounter() {
		return counter;
	}
	
	public double getSbp() {
		return sbp;
	}
	
	public double getDbp() {
		return dbp;
	}
	
	public double getHr() {
		return hr;
	}
	
	public double getHrv() {
		return hrv;
	}
	
	public double getOs() {
		return os;
	}
	
	
	
	public void set_Id(String id) {
		this._id = id;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public void setDateTime(Date dt) {
		this.dateTime = dt;
	}
	
	public void setAverInt(String intv) {
		this.averInt = intv;
	}
	
	public void setCounter(int cnt) {
		this.counter = cnt;
	}
	
	public void setSbp(double sbp) {
		this.sbp = sbp;
	}
	
	public void setDbp(double dbp) {
		this.dbp = dbp;
	}
	
	public void setHr(double hr) {
		this.hr = hr;
	}
	
	public void setHrv(double hrv) {
		this.hrv = hrv;
	}
	
	public void setOs(double os) {
		this.os = os;
	}

}
