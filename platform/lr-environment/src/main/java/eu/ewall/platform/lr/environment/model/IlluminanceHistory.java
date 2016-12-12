package eu.ewall.platform.lr.environment.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;

public class IlluminanceHistory {
		private String username;
		private String location;
		
		@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
		private DateTime from;
		@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
		private DateTime to;
		
		private Integer latestEvents;
		private List<IlluminanceEvent> illuminanceEvents;
		
		public IlluminanceHistory() {
			illuminanceEvents = new ArrayList<IlluminanceEvent>();
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
		public Integer getLatestEvents() {
			return latestEvents;
		}
		public void setLatestEvents(Integer latestEvents) {
			this.latestEvents = latestEvents;
		}
		public List<IlluminanceEvent> getIlluminanceEvents() {
			return illuminanceEvents;
		}
		public void setIlluminanceEvents(List<IlluminanceEvent> illuminanceEvents) {
			this.illuminanceEvents = illuminanceEvents;
		}
		
}
