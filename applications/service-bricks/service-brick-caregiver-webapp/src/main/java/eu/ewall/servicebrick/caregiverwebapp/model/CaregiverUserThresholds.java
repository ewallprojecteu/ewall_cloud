package eu.ewall.servicebrick.caregiverwebapp.model;

import java.io.Serializable;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


@Document(collection="caregiverUserThresholds")
public class CaregiverUserThresholds implements Serializable {

	@Id
	private ObjectId id;
	
	private String caregiverUsername;
	private String primaryUserUsername;
	private List<Threshold> thresholds;
	
	
	@PersistenceConstructor
	@JsonCreator
	public CaregiverUserThresholds(@JsonProperty("caregiverUsername") String caregiverUsername, @JsonProperty("primaryUserUsername") String primaryUserUsername) {
		this(caregiverUsername, primaryUserUsername, null);
	}

	
	public CaregiverUserThresholds(String caregiverUsername, String primaryUserUsername, List<Threshold> thresholds) {
		this.caregiverUsername = caregiverUsername;
		this.primaryUserUsername = primaryUserUsername;
		this.thresholds = thresholds;
	}

	public String getCaregiverUsername() {
		return caregiverUsername;
	}

	public void setCaregiverUsername(String caregiverUsername) {
		this.caregiverUsername = caregiverUsername;
	}

	public String getPrimaryUserUsername() {
		return primaryUserUsername;
	}

	public void setPrimaryUserUsername(String primaryUserUsername) {
		this.primaryUserUsername = primaryUserUsername;
	}

	public List<Threshold> getThresholds() {
		return thresholds;
	}

	public void setThresholds(List<Threshold> thresholds) {
		this.thresholds = thresholds;
	}
	
	public Threshold getThreshold(String name){
		for(Threshold threshold : thresholds){
			if(threshold.getName().equals(name)){
				return(threshold);
			}
		}
		return null;
	}
	
	public Threshold getThresholdByLinkedEventName(String eventName){
		for(Threshold threshold : thresholds){
			if(threshold.getLinkedEventName().equals(eventName)){
				return(threshold);
			}
		}
		return null;
	}


	public Threshold getThresholdByName(String name) {
		for(Threshold threshold : thresholds){
			if(threshold.getName().equals(name)){
				return(threshold);
			}
		}
		return null;
	}
	
}
