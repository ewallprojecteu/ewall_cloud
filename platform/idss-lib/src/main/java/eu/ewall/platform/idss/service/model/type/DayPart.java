package eu.ewall.platform.idss.service.model.type;

/**
 * The possible parts of the day (morning, afternoon, evening and night).
 * 
 * @author Harm op den Akker (RRD).
 */
public enum DayPart {
	MORNING,
	AFTERNOON,
	EVENING,
	NIGHT;
	
	@Override
	public String toString(){
		switch(this) {
			case MORNING :
				return "morning";
			case AFTERNOON :
				return "afternoon";
			case EVENING :
				return "evening";
			case NIGHT :
				return "night";		 
		}
		return null;
	}

}
