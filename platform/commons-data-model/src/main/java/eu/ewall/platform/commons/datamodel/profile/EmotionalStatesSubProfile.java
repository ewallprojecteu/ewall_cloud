package eu.ewall.platform.commons.datamodel.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * The Class of Attributes that denote emotional states of Organisms.
 * 
 * @author eandgrg
 */
public class EmotionalStatesSubProfile extends UserSubProfile {

	/** The emotional state categories set. */
	private Set<EmotionalStateCategory> emotionalStateCategoriesSet;

	/**
	 * The Constructor.
	 */
	public EmotionalStatesSubProfile() {
		this.emotionalStateCategoriesSet = new TreeSet<EmotionalStateCategory>();

	}

	/**
	 * Gets the emotional state categories set.
	 *
	 * @return the emotionalStateCategoriesSet
	 */
	public Set<EmotionalStateCategory> getEmotionalStateCategoriesSet() {
		return emotionalStateCategoriesSet;
	}

	/**
	 * Adds the emotional state to emotional state categories set.
	 *
	 * @param emotionalStateToAdd
	 *            the emotional state to add
	 * @return true, if adds the emotional state to emotional state categories
	 *         set
	 */
	public boolean addEmotionalStateToEmotionalStateCategoriesSet(
			EmotionalStateCategory emotionalStateToAdd) {
		return emotionalStateCategoriesSet.add(emotionalStateToAdd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		List<EmotionalStateCategory> slist = new ArrayList<EmotionalStateCategory>(
				emotionalStateCategoriesSet);
		StringBuilder rString = new StringBuilder();
		rString.append("emotionalStateCategories: ");
		int counter = slist.size();
		for (EmotionalStateCategory each : slist) {
			counter--;
			if (counter > 0)
				rString.append(each.toString()).append(", ");
			else
				rString.append(each.toString());
		}
		rString.append("\n");
		return rString.toString();

	}

}