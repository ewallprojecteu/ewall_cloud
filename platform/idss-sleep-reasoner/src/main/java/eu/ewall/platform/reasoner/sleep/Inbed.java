package eu.ewall.platform.reasoner.sleep;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Inbed {
	
	@JsonProperty("timestamp")
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime timestamp;
	
	@JsonProperty("inBed")
	private boolean inBed;
	
	public DateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}
	public boolean isInBed() {
		return inBed;
	}
	public void setInBed(boolean inBed) {
		this.inBed = inBed;
	}
	
}
