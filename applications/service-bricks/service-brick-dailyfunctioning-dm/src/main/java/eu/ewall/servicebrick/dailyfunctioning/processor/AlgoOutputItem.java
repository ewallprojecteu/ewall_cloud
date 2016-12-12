package eu.ewall.servicebrick.dailyfunctioning.processor;

import org.joda.time.DateTime;


public class AlgoOutputItem  implements Cloneable{
	
	private String location;
	private String activity;
	private boolean inBed;
	private DateTime timestamp;
	
	public AlgoOutputItem() {
		// TODO Auto-generated constructor stub
	}
	
	public AlgoOutputItem(DateTime timestamp, String location, String activity, boolean inBed) {
		this.timestamp = timestamp;
		this.location = location;
		this.activity = activity;
		this.inBed = inBed;
	}
		
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public boolean isInBed() {
		return inBed;
	}
	public void setInBed(boolean inBed) {
		this.inBed = inBed;
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
	    return super.clone();
	}


	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}
			
}
