package eu.ewall.servicebrick.common.model;

import java.util.HashMap;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="sensorsUpdates")
public class SensorsUpdates {
	
	@Id
	private ObjectId id;

	private String username;
	
	private HashMap<String, String> lastHumidityUpdates = new HashMap<String, String>();
	private HashMap<String, String> lastPirMovementUpdates = new HashMap<String, String>();
	private HashMap<String, String> lastMattressUpdates = new HashMap<String, String>();
	private HashMap<String, String> lastIlluminanceUpdates = new HashMap<String, String>();
	private HashMap<String, String> lastTemperatureUpdates = new HashMap<String, String>();
	private HashMap<String, String> lastDoorUpdates = new HashMap<String, String>();
	private HashMap<String, String> lastGasLpgUpdates = new HashMap<String, String>();
	private HashMap<String, String> lastGasNgUpdates = new HashMap<String, String>();
	private HashMap<String, String> lastGasCoUpdates = new HashMap<String, String>();
	private HashMap<String, String> lastVisualSensingUpdates = new HashMap<String, String>();
	
	private DateTime lastHumidityUpdateTimestamp;
	private DateTime lastPirMovementUpdateTimestamp;
	private DateTime lastMattressUpdateTimestamp;
	private DateTime lastIlluminanceUpdateTimestamp;
	private DateTime lastTemperatureUpdateTimestamp;
	private DateTime lastDoorUpdateTimestamp;
	private DateTime lastGasLpgUpdateTimestamp;
	private DateTime lastGasNgUpdateTimestamp;
	private DateTime lastGasCoUpdateTimestamp;
	private DateTime lastVisualSensingUpdateTimestamp;
	
	public SensorsUpdates() {
		// TODO Auto-generated constructor stub
	}
	
	
	public SensorsUpdates(String username, DateTime lastGeneralUpdate) {
		this.username = username;
		this.lastHumidityUpdateTimestamp = lastGeneralUpdate;
		this.lastPirMovementUpdateTimestamp = lastGeneralUpdate;
		this.lastMattressUpdateTimestamp = lastGeneralUpdate;
		this.lastIlluminanceUpdateTimestamp = lastGeneralUpdate;
		this.lastTemperatureUpdateTimestamp = lastGeneralUpdate;
		this.lastDoorUpdateTimestamp = lastGeneralUpdate;
		this.lastGasLpgUpdateTimestamp = lastGeneralUpdate;
		this.lastGasNgUpdateTimestamp = lastGeneralUpdate;
		this.lastGasCoUpdateTimestamp = lastGeneralUpdate;
		this.lastVisualSensingUpdateTimestamp = lastGeneralUpdate;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public HashMap<String, String> getLastHumidityUpdates() {
		return lastHumidityUpdates;
	}


	public void setLastHumidityUpdates(HashMap<String, String> lastHumidityUpdates) {
		this.lastHumidityUpdates = lastHumidityUpdates;
	}


	public HashMap<String, String> getLastPirMovementUpdates() {
		return lastPirMovementUpdates;
	}


	public void setLastPirMovementUpdates(
			HashMap<String, String> lastPirMovementUpdates) {
		this.lastPirMovementUpdates = lastPirMovementUpdates;
	}


	public HashMap<String, String> getLastMattressUpdates() {
		return lastMattressUpdates;
	}


	public void setLastMattressUpdates(HashMap<String, String> lastMattressUpdates) {
		this.lastMattressUpdates = lastMattressUpdates;
	}


	public HashMap<String, String> getLastIlluminanceUpdates() {
		return lastIlluminanceUpdates;
	}


	public void setLastIlluminanceUpdates(
			HashMap<String, String> lastIlluminanceUpdates) {
		this.lastIlluminanceUpdates = lastIlluminanceUpdates;
	}


	public HashMap<String, String> getLastTemperatureUpdates() {
		return lastTemperatureUpdates;
	}


	public void setLastTemperatureUpdates(
			HashMap<String, String> lastTemperatureUpdates) {
		this.lastTemperatureUpdates = lastTemperatureUpdates;
	}


	public HashMap<String, String> getLastDoorUpdates() {
		return lastDoorUpdates;
	}


	public void setLastDoorUpdates(
			HashMap<String, String> lastDoorUpdates) {
		this.lastDoorUpdates = lastDoorUpdates;
	}


	public DateTime getLastHumidityUpdateTimestamp() {
		return lastHumidityUpdateTimestamp;
	}


	public void setLastHumidityUpdateTimestamp(DateTime lastHumidityUpdateTimestamp) {
		this.lastHumidityUpdateTimestamp = lastHumidityUpdateTimestamp;
	}


	public DateTime getLastPirMovementUpdateTimestamp() {
		return lastPirMovementUpdateTimestamp;
	}


	public void setLastPirMovementUpdateTimestamp(
			DateTime lastPirMovementUpdateTimestamp) {
		this.lastPirMovementUpdateTimestamp = lastPirMovementUpdateTimestamp;
	}


	public DateTime getLastMattressUpdateTimestamp() {
		return lastMattressUpdateTimestamp;
	}


	public void setLastMattressUpdateTimestamp(DateTime lastMattressUpdateTimestamp) {
		this.lastMattressUpdateTimestamp = lastMattressUpdateTimestamp;
	}


	public DateTime getLastIlluminanceUpdateTimestamp() {
		return lastIlluminanceUpdateTimestamp;
	}


	public void setLastIlluminanceUpdateTimestamp(
			DateTime lastIlluminanceUpdateTimestamp) {
		this.lastIlluminanceUpdateTimestamp = lastIlluminanceUpdateTimestamp;
	}


	public DateTime getLastTemperatureUpdateTimestamp() {
		return lastTemperatureUpdateTimestamp;
	}


	public void setLastTemperatureUpdateTimestamp(
			DateTime lastTemperatureUpdateTimestamp) {
		this.lastTemperatureUpdateTimestamp = lastTemperatureUpdateTimestamp;
	}


	public DateTime getLastDoorUpdateTimestamp() {
		return lastDoorUpdateTimestamp;
	}


	public void setLastDoorUpdateTimestamp(
			DateTime lastDoorUpdateTimestamp) {
		this.lastDoorUpdateTimestamp = lastDoorUpdateTimestamp;
	}


	public HashMap<String, String> getLastGasLpgUpdates() {
		return lastGasLpgUpdates;
	}


	public void setLastGasLpgUpdates(HashMap<String, String> lastGasLpgUpdates) {
		this.lastGasLpgUpdates = lastGasLpgUpdates;
	}


	public DateTime getLastGasLpgUpdateTimestamp() {
		return lastGasLpgUpdateTimestamp;
	}


	public void setLastGasLpgUpdateTimestamp(DateTime lastGasLpgUpdateTimestamp) {
		this.lastGasLpgUpdateTimestamp = lastGasLpgUpdateTimestamp;
	}


	public DateTime getLastGasNgUpdateTimestamp() {
		return lastGasNgUpdateTimestamp;
	}


	public void setLastGasNgUpdateTimestamp(DateTime lastGasNgUpdateTimestamp) {
		this.lastGasNgUpdateTimestamp = lastGasNgUpdateTimestamp;
	}


	public HashMap<String, String> getLastGasNgUpdates() {
		return lastGasNgUpdates;
	}


	public void setLastGasNgUpdates(HashMap<String, String> lastGasNgUpdates) {
		this.lastGasNgUpdates = lastGasNgUpdates;
	}


	public HashMap<String, String> getLastGasCoUpdates() {
		return lastGasCoUpdates;
	}


	public void setLastGasCoUpdates(HashMap<String, String> lastGasCoUpdates) {
		this.lastGasCoUpdates = lastGasCoUpdates;
	}


	public HashMap<String, String> getLastVisualSensingUpdates() {
		return lastVisualSensingUpdates;
	}


	public void setLastVisualSensingUpdates(HashMap<String, String> lastVisualSensingUpdates) {
		this.lastVisualSensingUpdates = lastVisualSensingUpdates;
	}
	
	public DateTime getLastGasCoUpdateTimestamp() {
		return lastGasCoUpdateTimestamp;
	}


	public void setLastGasCoUpdateTimestamp(DateTime lastGasCoUpdateTimestamp) {
		this.lastGasCoUpdateTimestamp = lastGasCoUpdateTimestamp;
	}
	

	public DateTime getLastVisualSensingUpdateTimestamp() {
		return lastVisualSensingUpdateTimestamp;
	}


	public void setLastVisualSensingUpdateTimestamp(DateTime lastVisualSensingUpdateTimestamp) {
		this.lastVisualSensingUpdateTimestamp = lastVisualSensingUpdateTimestamp;
	}
}
