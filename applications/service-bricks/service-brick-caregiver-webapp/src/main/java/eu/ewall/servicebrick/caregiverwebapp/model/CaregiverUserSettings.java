package eu.ewall.servicebrick.caregiverwebapp.model;

import java.io.Serializable;
import java.util.List;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="caregiverUserSettings")
public class CaregiverUserSettings implements Serializable {

	@Id
	private ObjectId id;
	
	private String caregiverUsername;
	private String primaryUserUsername;
	private boolean favourite;
	private DateTime lastViewed;
	
	
	@PersistenceConstructor
	public CaregiverUserSettings(String caregiverUsername, String primaryUserUsername) {
		this(caregiverUsername, primaryUserUsername, false, null, null);
	}

	
	public CaregiverUserSettings(String caregiverUsername, String primaryUserUsername, boolean favourite, DateTime lastViewed, List<Threshold> thresholds) {
		this.caregiverUsername = caregiverUsername;
		this.primaryUserUsername = primaryUserUsername;
		this.favourite = favourite;
		this.lastViewed = lastViewed;
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

	public boolean isFavourite() {
		return favourite;
	}

	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
	}

	public DateTime getLastViewed() {
		return lastViewed;
	}

	public void setLastViewed(DateTime lastViewed) {
		this.lastViewed = lastViewed;
	}

}
