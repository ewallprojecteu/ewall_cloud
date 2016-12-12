package eu.ewall.platform.lr.habits.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;

public class FunctioningActivityHistory {
	
	    private static final long serialVersionUID = 1L;
	    
		private String username;
		
		@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
		private DateTime from;
		@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
		private DateTime to;
		
		private Integer latestEvents;
		private List<FunctioningActivityEvent> functioningActivityEvents;
		
		public FunctioningActivityHistory() {
			functioningActivityEvents = new ArrayList<FunctioningActivityEvent>();
		}
		
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
		public Integer getLatestEvents() {
			return latestEvents;
		}
		public void setLatestEvents(Integer latestEvents) {
			this.latestEvents = latestEvents;
		}
		public List<FunctioningActivityEvent> getFunctioningActivityEvents() {
			return functioningActivityEvents;
		}
		public void setFunctioningActivityEvents(List<FunctioningActivityEvent> functioningActivityEvents) {
			this.functioningActivityEvents = functioningActivityEvents;
		}
		
}
