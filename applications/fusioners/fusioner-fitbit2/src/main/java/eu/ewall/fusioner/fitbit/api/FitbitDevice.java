package eu.ewall.fusioner.fitbit.api;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.fusioner.fitbit.utils.json.LocalDateTimeDeserializer;
import eu.ewall.fusioner.fitbit.utils.json.LocalDateTimeSerializer;
import eu.ewall.platform.idss.utils.json.JsonObject;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FitbitDevice extends JsonObject {
	private String battery;
	private String deviceVersion;
	private String id;
	@JsonSerialize(using=LocalDateTimeSerializer.class)
	@JsonDeserialize(using=LocalDateTimeDeserializer.class)
	private LocalDateTime lastSyncTime;
	private String type;

	public String getBattery() {
		return battery;
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}

	public String getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getLastSyncTime() {
		return lastSyncTime;
	}

	public void setLastSyncTime(LocalDateTime lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
