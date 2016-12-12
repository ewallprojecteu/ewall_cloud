package eu.ewall.servicebrick.dailyfunctioning.model;

import java.io.Serializable;
import java.util.List;
import eu.ewall.servicebrick.common.model.SensorsUpdates;

public class DomoticsHistory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;
	private SensorsUpdates domoticsEvent;
	
	public DomoticsHistory(String username, SensorsUpdates domoticsEvent){
		this.username = username;
		this.domoticsEvent = domoticsEvent;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public SensorsUpdates getDomoticsEvent() {
		return domoticsEvent;
	}

	public void setDomoticsEvent(SensorsUpdates domoticsEvent) {
		this.domoticsEvent = domoticsEvent;
	}	
}
