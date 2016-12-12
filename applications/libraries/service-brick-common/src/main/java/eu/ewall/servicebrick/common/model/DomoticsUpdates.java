package eu.ewall.servicebrick.common.model;



import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="domoticsUpdates")
public class DomoticsUpdates {
	
	@Id
	private ObjectId id;

	private String username;
	
	private String location;
	private DateTime lastHumidityUpdateTimestampLivingroom;
	private DateTime lastHumidityUpdateTimestampKitchen;
	private DateTime lastHumidityUpdateTimestampBedroom;
	private DateTime lastHumidityUpdateTimestampBathroom;
	private DateTime lastIlluminanceUpdateTimestampLivingroom;
	private DateTime lastIlluminanceUpdateTimestampKitchen;
	private DateTime lastIlluminanceUpdateTimestampBedroom;
	private DateTime lastIlluminanceUpdateTimestampBathroom;
	private DateTime lastTemperatureUpdateTimestampLivingroom;
	private DateTime lastTemperatureUpdateTimestampKitchen;
	private DateTime lastTemperatureUpdateTimestampBedroom;
	private DateTime lastTemperatureUpdateTimestampBathroom;
	
	private double lastTemperatureLivingroom;
	private double lastTemperatureKitchen;
	private double lastTemperatureBedroom;
	private double lastTemperatureBathroom;
	private double lastHumidityLivingroom;
	private double lastHumidityKitchen;
	private double lastHumidityBedroom;
	private double lastHumidityBathroom;
	private double lastIlluminanceLivingroom;
	private double lastIlluminanceKitchen;
	private double lastIlluminanceBedroom;
	private double lastIlluminanceBathroom;
	


	
	public DomoticsUpdates() {
		// TODO Auto-generated constructor stub
	}
	
	
	public DomoticsUpdates(String username, DateTime lastGeneralUpdate) {
		this.username = username;
		this.lastHumidityUpdateTimestampLivingroom = lastGeneralUpdate;
		this.lastIlluminanceUpdateTimestampLivingroom = lastGeneralUpdate;
		this.lastTemperatureUpdateTimestampLivingroom = lastGeneralUpdate;
		this.lastHumidityUpdateTimestampKitchen = lastGeneralUpdate;
		this.lastIlluminanceUpdateTimestampKitchen = lastGeneralUpdate;
		this.lastTemperatureUpdateTimestampKitchen = lastGeneralUpdate;
		this.lastHumidityUpdateTimestampBedroom = lastGeneralUpdate;
		this.lastIlluminanceUpdateTimestampBedroom = lastGeneralUpdate;
		this.lastTemperatureUpdateTimestampBedroom = lastGeneralUpdate;
		this.lastHumidityUpdateTimestampBathroom = lastGeneralUpdate;
		this.lastIlluminanceUpdateTimestampBathroom = lastGeneralUpdate;
		this.lastTemperatureUpdateTimestampBathroom = lastGeneralUpdate;
	
	}
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public DateTime getLastHumidityUpdateTimestampLivingroom() {
		return lastHumidityUpdateTimestampLivingroom;
	}


	public void setLastHumidityUpdateTimestampLivingroom(DateTime lastHumidityUpdateTimestampLivingroom) {
		this.lastHumidityUpdateTimestampLivingroom = lastHumidityUpdateTimestampLivingroom;
	}


	public DateTime getLastHumidityUpdateTimestampKitchen() {
		return lastHumidityUpdateTimestampKitchen;
	}


	public void setLastHumidityUpdateTimestampKitchen(DateTime lastHumidityUpdateTimestampKitchen) {
		this.lastHumidityUpdateTimestampKitchen = lastHumidityUpdateTimestampKitchen;
	}

	public DateTime getLastHumidityUpdateTimestampBedroom() {
		return lastHumidityUpdateTimestampBedroom;
	}


	public void setLastHumidityUpdateTimestampBedroom(DateTime lastHumidityUpdateTimestampBedroom) {
		this.lastHumidityUpdateTimestampBedroom = lastHumidityUpdateTimestampBedroom;
	}

	public DateTime getLastHumidityUpdateTimestampBathroom() {
		return lastHumidityUpdateTimestampBathroom;
	}


	public void setLastHumidityUpdateTimestampBathroom(DateTime lastHumidityUpdateTimestampBathroom) {
		this.lastHumidityUpdateTimestampBathroom = lastHumidityUpdateTimestampBathroom;
	}
	
	public DateTime getLastIlluminanceUpdateTimestampLivingroom() {
		return lastIlluminanceUpdateTimestampLivingroom;
	}


	public void setLastIlluminanceUpdateTimestampLivingroom(
			DateTime lastIlluminanceUpdateTimestampLivingroom) {
		this.lastIlluminanceUpdateTimestampLivingroom = lastIlluminanceUpdateTimestampLivingroom;
	}


	public DateTime getLastIlluminanceUpdateTimestampKitchen() {
		return lastIlluminanceUpdateTimestampKitchen;
	}


	public void setLastIlluminanceUpdateTimestampKitchen(
			DateTime lastIlluminanceUpdateTimestampKitchen) {
		this.lastIlluminanceUpdateTimestampKitchen = lastIlluminanceUpdateTimestampKitchen;
	}
	
	public DateTime getLastIlluminanceUpdateTimestampBedroom() {
		return lastIlluminanceUpdateTimestampBedroom;
	}


	public void setLastIlluminanceUpdateTimestampBedroom(
			DateTime lastIlluminanceUpdateTimestampBedroom) {
		this.lastIlluminanceUpdateTimestampBedroom = lastIlluminanceUpdateTimestampBedroom;
	}
	
	public DateTime getLastIlluminanceUpdateTimestampBathroom() {
		return lastIlluminanceUpdateTimestampBathroom;
	}


	public void setLastIlluminanceUpdateTimestampBathroom(
			DateTime lastIlluminanceUpdateTimestampBathroom) {
		this.lastIlluminanceUpdateTimestampBathroom = lastIlluminanceUpdateTimestampBathroom;
	}
	
	public DateTime getLastTemperatureUpdateTimestampLivingroom() {
		return lastTemperatureUpdateTimestampLivingroom;
	}


	public void setLastTemperatureUpdateTimestampLivingroom(
			DateTime lastTemperatureUpdateTimestampLivingroom) {
		this.lastTemperatureUpdateTimestampLivingroom = lastTemperatureUpdateTimestampLivingroom;
	}

	public DateTime getLastTemperatureUpdateTimestampKitchen() {
		return lastTemperatureUpdateTimestampKitchen;
	}


	public void setLastTemperatureUpdateTimestampKitchen(
			DateTime lastTemperatureUpdateTimestampKitchen) {
		this.lastTemperatureUpdateTimestampKitchen = lastTemperatureUpdateTimestampKitchen;
	}
	
	public DateTime getLastTemperatureUpdateTimestampBedroom() {
		return lastTemperatureUpdateTimestampBedroom;
	}


	public void setLastTemperatureUpdateTimestampBedroom(
			DateTime lastTemperatureUpdateTimestampBedroom) {
		this.lastTemperatureUpdateTimestampBedroom = lastTemperatureUpdateTimestampBedroom;
	}
	
	public DateTime getLastTemperatureUpdateTimestampBathroom() {
		return lastTemperatureUpdateTimestampBathroom;
	}


	public void setLastTemperatureUpdateTimestampBathroom(
			DateTime lastTemperatureUpdateTimestampBathroom) {
		this.lastTemperatureUpdateTimestampBathroom = lastTemperatureUpdateTimestampBathroom;
	}

	public double getLastTemperatureLivingroom() {
		return lastTemperatureLivingroom;
	}


	public void setLastTemperatureLivingroom(
			double lastTemperatureLivingroom) {
		this.lastTemperatureLivingroom = lastTemperatureLivingroom;
	}
	
	public double getLastTemperatureKitchen() {
		return lastTemperatureKitchen;
	}


	public void setLastTemperatureKitchen(
			double lastTemperatureKitchen) {
		this.lastTemperatureKitchen = lastTemperatureKitchen;
	}
	
	public double getLastTemperatureBedroom() {
		return lastTemperatureBedroom;
	}


	public void setLastTemperatureBedroom(
			double lastTemperatureBedroom) {
		this.lastTemperatureBedroom = lastTemperatureBedroom;
	}
	
	public double getLastTemperatureBathroom() {
		return lastTemperatureBathroom;
	}


	public void setLastTemperatureBathroom(
			double lastTemperatureBathroom) {
		this.lastTemperatureBathroom = lastTemperatureBathroom;
	}
	
	
	public double getLastHumidityLivingroom() {
		return lastHumidityLivingroom;
	}


	public void setLastHumidityLivingroom(
			double lastHumidityLivingroom) {
		this.lastHumidityLivingroom = lastHumidityLivingroom;
	}
	
	public double getLastHumidityKitchen() {
		return lastHumidityKitchen;
	}


	public void setLastHumidityKitchen(
			double lastHumidityKitchen) {
		this.lastHumidityKitchen = lastHumidityKitchen;
	}
	
	public double getLastHumidityBedroom() {
		return lastHumidityBedroom;
	}


	public void setLastHumidityBedroom(
			double lastHumidityBedroom) {
		this.lastHumidityBedroom = lastHumidityBedroom;
	}
	
	public double getLastHumidityBathroom() {
		return lastHumidityBathroom;
	}


	public void setLastHumidityBathroom(
			double lastHumidityBathroom) {
		this.lastHumidityBathroom = lastHumidityBathroom;
	}
	

	public double getLastIlluminanceLivingroom() {
		return lastIlluminanceLivingroom;
	}


	public void setLastIlluminanceLivingroom(
			double lastIlluminanceLivingroom) {
		this.lastIlluminanceLivingroom = lastIlluminanceLivingroom;
	}
	
	public double getLastIlluminanceKitchen() {
		return lastIlluminanceKitchen;
	}


	public void setLastIlluminanceKitchen(
			double lastIlluminanceKitchen) {
		this.lastIlluminanceKitchen = lastIlluminanceKitchen;
	}
	
	public double getLastIlluminanceBedroom() {
		return lastIlluminanceBedroom;
	}


	public void setLastIlluminanceBedroom(
			double lastIlluminanceBedroom) {
		this.lastIlluminanceBedroom = lastIlluminanceBedroom;
	}
	
	public double getLastIlluminanceBathroom() {
		return lastIlluminanceBathroom;
	}


	public void setLastIlluminanceBathroom(
			double lastvBathroom) {
		this.lastIlluminanceBathroom = lastIlluminanceBathroom;
	}
	
}
