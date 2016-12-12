package eu.ewall.platform.commons.datamodel.profile;

import java.util.ArrayList;
import java.util.List;

import eu.ewall.platform.commons.datamodel.activity.Activity;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * @author eandgrg
 */
public class ActivitiesSubProfile extends UserSubProfile {

	/** The activities. */
	private List<Activity> activities;

	/**
	 * The Constructor.
	 */
	public ActivitiesSubProfile() {

		this.activities = new ArrayList<Activity>();

	}

	/**
	 * Gets the activities.
	 *
	 * @return the activities
	 */
	public List<Activity> getActivities() {
		return activities;
	}

	/**
	 * Adds the activity to activities sub profile.
	 *
	 * @param activityToAdd
	 *            the activity to add
	 * @return true, if adds the activity to activities sub profile
	 */
	public boolean addActivityToActivitiesSubProfile(Activity activityToAdd) {
		return activities.add(activityToAdd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		List<Activity> slist = new ArrayList<Activity>(activities);
		StringBuilder rString = new StringBuilder();
		rString.append("******ActivitiesSubProfile start******\n");
		for (Activity each : slist) {
			rString.append(each.toString()).append("\n");
		}
		rString.append("******ActivitiesSubProfile end******\n");
		return rString.toString();

	}
}