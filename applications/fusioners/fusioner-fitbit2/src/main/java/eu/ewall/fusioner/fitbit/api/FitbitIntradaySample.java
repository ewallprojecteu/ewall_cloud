package eu.ewall.fusioner.fitbit.api;

import org.joda.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.utils.json.JsonObject;
import eu.ewall.platform.idss.utils.json.SqlTimeDeserializer;
import eu.ewall.platform.idss.utils.json.SqlTimeSerializer;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FitbitIntradaySample extends JsonObject {
	@JsonSerialize(using=SqlTimeSerializer.class)
	@JsonDeserialize(using=SqlTimeDeserializer.class)
	private LocalTime time;
	
	private float value;

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}
}
