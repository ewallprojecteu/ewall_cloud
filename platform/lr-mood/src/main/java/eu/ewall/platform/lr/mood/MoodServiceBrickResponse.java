package eu.ewall.platform.lr.mood;

import java.util.List;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MoodServiceBrickResponse {

	@JsonProperty("username")
	private String username;
	
	@JsonProperty("from")
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime from;
	
	@JsonProperty("to")
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime to;
	
	@JsonProperty("aggregation")
	private String aggregation;
	
	@JsonProperty("moodActivityEvents")
	private List<MoodDataItem> mood;

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

	public String getAggregation() {
		return aggregation;
	}

	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}

	public List<MoodDataItem> getMood() {
		return mood;
	}

	public void setMood(List<MoodDataItem> mood) {
		this.mood = mood;
	}
}
