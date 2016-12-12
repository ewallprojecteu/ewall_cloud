package eu.ewall.platform.reasoner.sleep;

import java.util.List;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InbedDataResponse {
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("from")
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime from;
	
	@JsonProperty("to")
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime to;
	
	@JsonProperty("inbedEvents")
	private List<Inbed> inbedEvents;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public DateTime getFrom() {
		return from;
	}
	public void setFrom(DateTime from) {
		this.from = from;
	}
	public DateTime getTo() {
		return to;
	}
	public void setTo(DateTime to) {
		this.to = to;
	}
	public List<Inbed> getInbedEvents() {
		return inbedEvents;
	}
	public void setInbedEvents(List<Inbed> inbedEvents) {
		this.inbedEvents = inbedEvents;
	}
}
