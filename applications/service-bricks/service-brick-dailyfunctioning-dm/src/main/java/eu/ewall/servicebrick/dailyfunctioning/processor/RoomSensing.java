package eu.ewall.servicebrick.dailyfunctioning.processor;

public class RoomSensing {
	private String roomName, userName;
	private double temp, humidity, tvPower;
	private int ng, co, faceNo;
	private boolean presence, doorOpen, bedPressure;
	
	public RoomSensing(String userName, String roomName) {
		this.userName = userName;
		this.roomName = roomName;
		temp = 21;
		humidity = 55;
		tvPower = 0;
		ng = 0;
		co = 0;
		faceNo = 0;
		presence = false;
		if (roomName.equals("livingroom"))
			doorOpen = false;
		else
			doorOpen = true;
		bedPressure = false;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public double getTvPower() {
		return tvPower;
	}

	public void setTvPower(double tvPower) {
		this.tvPower = tvPower;
	}

	public int getNg() {
		return ng;
	}

	public void setNg(int ng) {
		this.ng = ng;
	}

	public int getCo() {
		return co;
	}

	public void setCo(int co) {
		this.co = co;
	}

	public int getFaceNo() {
		return faceNo;
	}

	public void setFaceNo(int faceNo) {
		this.faceNo = faceNo;
	}

	public boolean isPresence() {
		return presence;
	}

	public void setPresence(boolean presence) {
		this.presence = presence;
	}

	public boolean isDoorOpen() {
		return doorOpen;
	}

	public void setDoorOpen(boolean doorOpen) {
		this.doorOpen = doorOpen;
	}

	public boolean isBedPressure() {
		return bedPressure;
	}

	public void setBedPressure(boolean bedPressure) {
		this.bedPressure = bedPressure;
	}
	
	
}
