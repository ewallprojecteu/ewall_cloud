package eu.ewall.platform.commons.datamodel.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * @author eandgrg
 */
public class HealthMedications implements Serializable {

	/** The medications list. */
	private List<String> medicationsList;

	/**
	 * The Constructor.
	 */
	public HealthMedications() {
		this.medicationsList = new ArrayList<String>();
	}

	/**
	 * Gets the medications list.
	 *
	 * @return the medications list
	 */
	public List<String> getMedicationsList() {
		return medicationsList;
	}

	/**
	 * Adds the medication to medicatios list.
	 *
	 * @param medicationName
	 *            the medication name
	 * @return true, if adds the medication to medicatios list
	 */
	public boolean addMedicationToMedicatiosList(String medicationName) {
		return medicationsList.add(medicationName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		List<String> slist = new ArrayList<String>(medicationsList);
		StringBuilder rString = new StringBuilder();
		rString.append("medicationsList: ");
		int counter = slist.size();
		for (String each : slist) {
			counter--;
			if (counter > 0)
				rString.append(each.toString()).append(", ");
			else
				rString.append(each.toString());
		}

		return rString.toString();

	}
}