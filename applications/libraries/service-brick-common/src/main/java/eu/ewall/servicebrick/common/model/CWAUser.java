package eu.ewall.servicebrick.common.model;

import org.joda.time.DateTime;

import eu.ewall.platform.commons.datamodel.profile.User;

public class CWAUser extends User{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean favourite;
	DateTime lastViewed;

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
