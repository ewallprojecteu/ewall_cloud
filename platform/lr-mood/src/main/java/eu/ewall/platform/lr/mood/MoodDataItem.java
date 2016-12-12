package eu.ewall.platform.lr.mood;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MoodDataItem {

	@JsonProperty("from")
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime from;
	
	@JsonProperty("to")
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime to;
	
	@JsonProperty("valence")
	private float valence;

	@JsonProperty("arousal")
	private float arousal;

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

	public float getValence() {
		return valence;
	}

	public void setValence(float valence) {
		this.valence = valence;
	}

	public float getArousal() {
		return arousal;
	}

	public void setArousal(float arousal) {
		this.arousal = arousal;
	}
}
