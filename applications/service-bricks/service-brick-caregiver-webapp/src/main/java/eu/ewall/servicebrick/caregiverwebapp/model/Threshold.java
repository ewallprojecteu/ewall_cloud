package eu.ewall.servicebrick.caregiverwebapp.model;

public class Threshold {
	
	private String name;
	//Min and Max values for the bar visualization
	private double rangeMin;
	private double rangeMax;
	//Minimum target value, null if there is no lower bound in the threshold. 
	//Equals 'defaultTargetMin' if defaults are active, otherwise overrides it
	//The application must use this value
	private double targetMin;
	private double defaultTargetMin;
	//Same concept as 'min'
	private double targetMax;
	private double defaultTargetMax;
	//Visible in the dashboard (checkbox)
	private boolean visible=true;
	private boolean defaultVisible=true;
	//Priority for this parameter, affects notifications (NONE, LOW, MEDIUM, HIGH)
	private Priority priority = Priority.NONE;
	private Priority defaultPriority = Priority.NONE;
	//Define if defaults must be overridden
	private boolean overrideDefault=false;
	private String linkedEventName;
	
	public enum Priority {
		NONE, LOW, MEDIUM, HIGH
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRangeMin() {
		return rangeMin;
	}

	public void setRangeMin(double rangeMin) {
		this.rangeMin = rangeMin;
	}

	public double getRangeMax() {
		return rangeMax;
	}

	public void setRangeMax(double rangeMax) {
		this.rangeMax = rangeMax;
	}

	public double getTargetMin() {
		return targetMin;
	}

	public void setTargetMin(double targetMin) {
		this.targetMin = targetMin;
	}

	public double getDefaultTargetMin() {
		return defaultTargetMin;
	}

	public void setDefaultTargetMin(double defaultTargetMin) {
		this.defaultTargetMin = defaultTargetMin;
	}

	public double getTargetMax() {
		return targetMax;
	}

	public void setTargetMax(double targetMax) {
		this.targetMax = targetMax;
	}

	public double getDefaultTargetMax() {
		return defaultTargetMax;
	}

	public void setDefaultTargetMax(double defaultTargetMax) {
		this.defaultTargetMax = defaultTargetMax;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public boolean isOverrideDefault() {
		return overrideDefault;
	}

	public void setOverrideDefault(boolean overrideDefault) {
		this.overrideDefault = overrideDefault;
	}
	
	public boolean equals(Object o){
	    if(o == null)
	    	return false;
	    
	    if(!(o instanceof Threshold)) 
	    	return false;

	    Threshold other = (Threshold) o;
	    if(!this.name.equals(other.name))
	    	return false;

	    return true;
	  }

	public String getLinkedEventName() {
		return linkedEventName;
	}

	public void setLinkedEventName(String linkedEventName) {
		this.linkedEventName = linkedEventName;
	}

	public Priority getDefaultPriority() {
		return defaultPriority;
	}

	public void setDefaultPriority(Priority defaultPriority) {
		this.defaultPriority = defaultPriority;
	}

	public boolean isDefaultVisible() {
		return defaultVisible;
	}

	public void setDefaultVisible(boolean defaultVisible) {
		this.defaultVisible = defaultVisible;
	}

}


