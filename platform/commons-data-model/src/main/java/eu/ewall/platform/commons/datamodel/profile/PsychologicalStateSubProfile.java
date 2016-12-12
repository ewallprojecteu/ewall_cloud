/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.profile;

/**
 * The Class PsychologicalStateSubProfile.
 *
 * @author eandgrg
 * @author Harm op den Akker 
 */
public class PsychologicalStateSubProfile extends UserSubProfile {

	/** The physical activity stage of change. */
	PsychologicalStageOfChangeType physicalActivityStageOfChange;
	/** The self-efficacy for daily physical activity, between 0 and 1. */
	double physicalActivitySelfEfficacy = 0;

	/** The smoking cessation stage of change. */
	PsychologicalStageOfChangeType smokingCessationStageOfChange;

	/** The smoking cessation self efficacy. */
	double smokingCessationSelfEfficacy = 0;

	/** The diet behavior stage of change. */
	PsychologicalStageOfChangeType dietBehaviorStageOfChange;

	/** The diet behavior self efficacy. */
	double dietBehaviorSelfEfficacy = 0;

	/** The alcohol use stage of change. */
	PsychologicalStageOfChangeType alcoholUseStageOfChange;

	/** The alcohol use self efficacy. */
	double alcoholUseSelfEfficacy = 0;

	/**
	 * The Constructor.
	 */
	public PsychologicalStateSubProfile() {

	}

	/**
	 * Instantiates a new psychological state sub profile.
	 *
	 * @param physicalActivityStageOfChange
	 *            the physical activity stage of change
	 * @param smokingCessationStageOfChange
	 *            the smoking cessation stage of change
	 * @param dietBehaviorStageOfChange
	 *            the diet behavior stage of change
	 * @param alcoholUseStageOfChange
	 *            the alcohol use stage of change
	 */
	public PsychologicalStateSubProfile(
			PsychologicalStageOfChangeType physicalActivityStageOfChange,
			PsychologicalStageOfChangeType smokingCessationStageOfChange,
			PsychologicalStageOfChangeType dietBehaviorStageOfChange,
			PsychologicalStageOfChangeType alcoholUseStageOfChange) {
		super();
		this.physicalActivityStageOfChange = physicalActivityStageOfChange;
		this.smokingCessationStageOfChange = smokingCessationStageOfChange;
		this.dietBehaviorStageOfChange = dietBehaviorStageOfChange;
		this.alcoholUseStageOfChange = alcoholUseStageOfChange;
	}

	/**
	 * Instantiates a new psychological state sub profile.
	 *
	 * @param physicalActivityStageOfChange
	 *            the physical activity stage of change
	 * @param physicalActivitySelfEfficacy
	 *            the physical activity self efficacy
	 * @param smokingCessationStageOfChange
	 *            the smoking cessation stage of change
	 * @param smokingCessationSelfEfficacy
	 *            the smoking cessation self efficacy
	 * @param dietBehaviorStageOfChange
	 *            the diet behavior stage of change
	 * @param dietBehaviorSelfEfficacy
	 *            the diet behavior self efficacy
	 * @param alcoholUseStageOfChange
	 *            the alcohol use stage of change
	 * @param alcoholUseSelfEfficacy
	 *            the alcohol use self efficacy
	 */
	public PsychologicalStateSubProfile(
			PsychologicalStageOfChangeType physicalActivityStageOfChange,
			double physicalActivitySelfEfficacy,
			PsychologicalStageOfChangeType smokingCessationStageOfChange,
			double smokingCessationSelfEfficacy,
			PsychologicalStageOfChangeType dietBehaviorStageOfChange,
			double dietBehaviorSelfEfficacy,
			PsychologicalStageOfChangeType alcoholUseStageOfChange,
			double alcoholUseSelfEfficacy) {
		super();
		this.physicalActivityStageOfChange = physicalActivityStageOfChange;
		this.physicalActivitySelfEfficacy = physicalActivitySelfEfficacy;
		this.smokingCessationStageOfChange = smokingCessationStageOfChange;
		this.smokingCessationSelfEfficacy = smokingCessationSelfEfficacy;
		this.dietBehaviorStageOfChange = dietBehaviorStageOfChange;
		this.dietBehaviorSelfEfficacy = dietBehaviorSelfEfficacy;
		this.alcoholUseStageOfChange = alcoholUseStageOfChange;
		this.alcoholUseSelfEfficacy = alcoholUseSelfEfficacy;
	}

	/**
	 * Gets the physical activity stage of change.
	 *
	 * @return the physical activity stage of change
	 */
	public PsychologicalStageOfChangeType getPhysicalActivityStageOfChange() {
		return physicalActivityStageOfChange;
	}

	/**
	 * Sets the physical activity stage of change.
	 *
	 * @param physicalActivityStageOfChange
	 *            the new physical activity stage of change
	 */
	public void setPhysicalActivityStageOfChange(
			PsychologicalStageOfChangeType physicalActivityStageOfChange) {
		this.physicalActivityStageOfChange = physicalActivityStageOfChange;
	}

	/**
	 * Gets the physical activity self efficacy.
	 *
	 * @return the physical activity self efficacy
	 */
	public double getPhysicalActivitySelfEfficacy() {
		return physicalActivitySelfEfficacy;
	}

	/**
	 * Sets the physical activity self efficacy.
	 *
	 * @param physicalActivitySelfEfficacy
	 *            the new physical activity self efficacy
	 */
	public void setPhysicalActivitySelfEfficacy(
			double physicalActivitySelfEfficacy) {
		this.physicalActivitySelfEfficacy = physicalActivitySelfEfficacy;
	}

	/**
	 * Gets the smoking cessation stage of change.
	 *
	 * @return the smoking cessation stage of change
	 */
	public PsychologicalStageOfChangeType getSmokingCessationStageOfChange() {
		return smokingCessationStageOfChange;
	}

	/**
	 * Sets the smoking cessation stage of change.
	 *
	 * @param smokingCessationStageOfChange
	 *            the new smoking cessation stage of change
	 */
	public void setSmokingCessationStageOfChange(
			PsychologicalStageOfChangeType smokingCessationStageOfChange) {
		this.smokingCessationStageOfChange = smokingCessationStageOfChange;
	}

	/**
	 * Gets the smoking cessation self efficacy.
	 *
	 * @return the smoking cessation self efficacy
	 */
	public double getSmokingCessationSelfEfficacy() {
		return smokingCessationSelfEfficacy;
	}

	/**
	 * Sets the smoking cessation self efficacy.
	 *
	 * @param smokingCessationSelfEfficacy
	 *            the new smoking cessation self efficacy
	 */
	public void setSmokingCessationSelfEfficacy(
			double smokingCessationSelfEfficacy) {
		this.smokingCessationSelfEfficacy = smokingCessationSelfEfficacy;
	}

	/**
	 * Gets the diet behavior stage of change.
	 *
	 * @return the diet behavior stage of change
	 */
	public PsychologicalStageOfChangeType getDietBehaviorStageOfChange() {
		return dietBehaviorStageOfChange;
	}

	/**
	 * Sets the diet behavior stage of change.
	 *
	 * @param dietBehaviorStageOfChange
	 *            the new diet behavior stage of change
	 */
	public void setDietBehaviorStageOfChange(
			PsychologicalStageOfChangeType dietBehaviorStageOfChange) {
		this.dietBehaviorStageOfChange = dietBehaviorStageOfChange;
	}

	/**
	 * Gets the diet behavior self efficacy.
	 *
	 * @return the diet behavior self efficacy
	 */
	public double getDietBehaviorSelfEfficacy() {
		return dietBehaviorSelfEfficacy;
	}

	/**
	 * Sets the diet behavior self efficacy.
	 *
	 * @param dietBehaviorSelfEfficacy
	 *            the new diet behavior self efficacy
	 */
	public void setDietBehaviorSelfEfficacy(double dietBehaviorSelfEfficacy) {
		this.dietBehaviorSelfEfficacy = dietBehaviorSelfEfficacy;
	}

	/**
	 * Gets the alcohol use stage of change.
	 *
	 * @return the alcohol use stage of change
	 */
	public PsychologicalStageOfChangeType getAlcoholUseStageOfChange() {
		return alcoholUseStageOfChange;
	}

	/**
	 * Sets the alcohol use stage of change.
	 *
	 * @param alcoholUseStageOfChange
	 *            the new alcohol use stage of change
	 */
	public void setAlcoholUseStageOfChange(
			PsychologicalStageOfChangeType alcoholUseStageOfChange) {
		this.alcoholUseStageOfChange = alcoholUseStageOfChange;
	}

	/**
	 * Gets the alcohol use self efficacy.
	 *
	 * @return the alcohol use self efficacy
	 */
	public double getAlcoholUseSelfEfficacy() {
		return alcoholUseSelfEfficacy;
	}

	/**
	 * Sets the alcohol use self efficacy.
	 *
	 * @param alcoholUseSelfEfficacy
	 *            the new alcohol use self efficacy
	 */
	public void setAlcoholUseSelfEfficacy(double alcoholUseSelfEfficacy) {
		this.alcoholUseSelfEfficacy = alcoholUseSelfEfficacy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder rString = new StringBuilder();
		rString.append("******PsychologicalStateSubProfile start******\n");
		rString.append("physicalActivityStageOfChange="
				+ physicalActivityStageOfChange + "\n");
		rString.append("physicalActivitySelfEfficacy="
				+ physicalActivitySelfEfficacy + "\n");

		rString.append("smokingCessationStageOfChange="
				+ smokingCessationStageOfChange + "\n");
		rString.append("smokingCessationSelfEfficacy="
				+ smokingCessationSelfEfficacy + "\n");

		rString.append("dietBehaviorStageOfChange=" + dietBehaviorStageOfChange
				+ "\n");
		rString.append("dietBehaviorSelfEfficacy=" + dietBehaviorSelfEfficacy
				+ "\n");

		rString.append("alcoholUseStageOfChange=" + alcoholUseStageOfChange
				+ "\n");
		rString.append("alcoholUseSelfEfficacy =" + alcoholUseSelfEfficacy
				+ "\n");
		rString.append("******PsychologicalStateSubProfile end******\n");
		return rString.toString();

	}
}