package eu.ewall.servicebrick.dailyfunctioning.processor;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="rooms_data")
public class Rooms {
	
	@Id
	private ObjectId id;
	
	private DateTime timestamp;
	private String username;
	private int reasonedRoom, reasonedActivity;
	private int frontalDoorState;
	private boolean[] roomEvidence;
	private double[] roomWeights;
	private double[] activityWeights;
	private double[] pirAvg;
	private double[] tempAvg, tempStd, tempSq, humidityAvg, humidityStd, humiditySq;
	private boolean[] tempLargeVariation, humidityLargeVariation;
	
	
	public Rooms() {
		pirAvg = new double [8];
		roomEvidence = new boolean [8];
		roomWeights = new double [8];
		activityWeights = new double [12];
		tempAvg = new double [8];
		tempStd =  new double [8];
		tempSq=  new double [8];
		humidityAvg= new double [8]; 
		humidityStd= new double [8];
		humiditySq= new double [8];
		tempLargeVariation = new boolean [8];
		humidityLargeVariation = new boolean [8];
		
	}

	public boolean[] getRoomEvidence() {
		return roomEvidence;
	}

	public void setRoomEvidence(boolean[] roomEvidence) {
		this.roomEvidence = roomEvidence;
	}

	public double[] getRoomWeights() {
		return roomWeights;
	}

	public void setRoomWeights(double[] roomWeights) {
		this.roomWeights = roomWeights;
	}
	
	public double[] getActivityWeights() {
		return activityWeights;
	}

	public void setActivityWeights(double[] activityWeights) {
		this.activityWeights = activityWeights;
	}

	public double[] getPirAvg() {
		return pirAvg;
	}

	public void setPirAvg(double[] pirAvg) {
		this.pirAvg = pirAvg;
	}

	public double[] getTempAvg() {
		return tempAvg;
	}

	public void setTempAvg(double[] tempAvg) {
		this.tempAvg = tempAvg;
	}

	public double[] getTempStd() {
		return tempStd;
	}

	public void setTempStd(double[] tempStd) {
		this.tempStd = tempStd;
	}

	public double[] getTempSq() {
		return tempSq;
	}

	public void setTempSq(double[] tempSq) {
		this.tempSq = tempSq;
	}

	public double[] getHumidityAvg() {
		return humidityAvg;
	}

	public void setHumidityAvg(double[] humidityAvg) {
		this.humidityAvg = humidityAvg;
	}

	public double[] getHumidityStd() {
		return humidityStd;
	}

	public void setHumidityStd(double[] humidityStd) {
		this.humidityStd = humidityStd;
	}

	public double[] getHumiditySq() {
		return humiditySq;
	}

	public void setHumiditySq(double[] humiditySq) {
		this.humiditySq = humiditySq;
	}

	public boolean[] getTempLargeVariation() {
		return tempLargeVariation;
	}

	public void setTempLargeVariation(boolean[] tempLargeVariation) {
		this.tempLargeVariation = tempLargeVariation;
	}

	public boolean[] getHumidityLargeVariation() {
		return humidityLargeVariation;
	}

	public void setHumidityLargeVariation(boolean[] humidityLargeVariation) {
		this.humidityLargeVariation = humidityLargeVariation;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getReasonedRoom() {
		return reasonedRoom;
	}

	public void setResonedRoom(int reasonedRoom) {
		this.reasonedRoom = reasonedRoom;
	}
	
	public int getReasonedActivity() {
		return reasonedActivity;
	}

	public void setResonedActivity(int reasonedActivity) {
		this.reasonedActivity = reasonedActivity;
	}
	
	public int getFrontalDoorState() {
		return frontalDoorState;
	}

	public void setFrontalDoorState(int frontalDoorState) {
		this.frontalDoorState = frontalDoorState;
	}
}
