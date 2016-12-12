package eu.ewall.platform.reasoner.sleep;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Inactivity {
	
	@JsonProperty("timestamp")
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime timestamp;
	
	@JsonProperty("inactive")
	private boolean inactive;
	
	public DateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}
	public boolean isInactive() {
		return inactive;
	}
	public void setInactive(boolean inactive) {
		this.inactive = inactive;
	}
	
}
