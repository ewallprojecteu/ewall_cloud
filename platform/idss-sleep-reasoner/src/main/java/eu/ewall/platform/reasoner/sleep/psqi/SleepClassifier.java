package eu.ewall.platform.reasoner.sleep.psqi;


import eu.ewall.platform.reasoner.sleep.Inactivity;
import eu.ewall.platform.reasoner.sleep.Inbed;
import eu.ewall.platform.reasoner.sleep.InbedDataResponse;
import eu.ewall.platform.reasoner.sleep.InactivityDataResponse;

public class SleepClassifier extends PSQI{
	private InbedDataResponse inbedData;
	private InactivityDataResponse inactivityData;
	
	public SleepClassifier(InbedDataResponse inbedData,
			InactivityDataResponse inactivityData) {
		super();
		this.inbedData = inbedData;
		this.inactivityData = inactivityData;
		
		//Dummy logic here
		
		if(inbedData.getInbedEvents().isEmpty() || inactivityData.getInactivityEvents().isEmpty()) {
			Q1 = 0;
			Q2 = 0;
			Q3 = 0;
			Q4 = 0;
		} else {
			AsleepDetection();
		}
		
		
		//when Asleep is created, we automatically create a psqi instance with 
		//the calculated quality indexes 
				
		
	}
	
	private void AsleepDetection() {
		//get first inbed == true get last inbed == false
		Inbed firstInbed = new Inbed();
		Inbed lastOffbed = new Inbed();
		for(Inbed inbed : inbedData.getInbedEvents()) {
			if(inbed.isInBed()) {
				firstInbed = inbed;
			} else if(!inbed.isInBed()) {
				lastOffbed = inbed;
			} else {
				continue;
			}
			
			System.out.println(inbed.getTimestamp()+"  "+inbed.isInBed());
		}
		Inactivity firstInbedInactive = new Inactivity();
		
		for(Inactivity inactive : inactivityData.getInactivityEvents()) {
			if(inactive.isInactive()) {
				if(inactive.getTimestamp().isAfter(firstInbed.getTimestamp())) {
					firstInbedInactive = inactive;
				}
			}
		}
		
		Q1 = firstInbed.getTimestamp().getMillis()/1000;
		Q2 = (int) (firstInbedInactive.getTimestamp().getMillis()-firstInbed.getTimestamp().getMillis())/(1000*60);
		Q3 = lastOffbed.getTimestamp().getMillis()/1000;
		Q4 = (int) (firstInbedInactive.getTimestamp().getMillis()-lastOffbed.getTimestamp().getMillis())/(1000*60*60);
		
		
	}
	
}
