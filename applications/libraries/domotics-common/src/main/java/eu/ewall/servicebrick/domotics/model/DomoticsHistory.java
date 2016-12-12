package eu.ewall.servicebrick.domotics.model;

import java.io.Serializable;
import java.util.List;

public class DomoticsHistory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;
	private List<DomoticsEvent> domoticsEvent;
	
	public DomoticsHistory(String username, List<DomoticsEvent> domoticsEvent){
		this.username = username;
		this.domoticsEvent = domoticsEvent;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<DomoticsEvent> getDomoticsEvent() {
		return domoticsEvent;
	}

	public void setDomoticsEvent(List<DomoticsEvent> domoticsEvent) {
		this.domoticsEvent = domoticsEvent;
	}	
}
