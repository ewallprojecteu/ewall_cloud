package eu.ewall.platform.commons.datamodel.profile;

/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * @author eandgrg, emirmos
 */
public enum HealthDiagnosisType {

	/** Mild cognitive impairment. */
	MCI,

	/**
	 * Chronic obstructive pulmonary disease (COPD) diagnosis based on Global
	 * Initiative for Chronic Obstructive Lung Disease (GOLD) classification.
	 * Chronic obstructive pulmonary disease GOLD 1
	 */
	COPD_GOLD1,

	/**  Chronic obstructive pulmonary disease GOLD 2. */
	COPD_GOLD2,

	/**  Chronic obstructive pulmonary disease GOLD 3. */
	COPD_GOLD3,

	/**  Chronic obstructive pulmonary disease GOLD 4. */
	COPD_GOLD4,

	/** The frail. */
	FRAIL,

	/** The pre frail. */
	PRE_FRAIL,

	/** Diagnosis unknown. */
	UNKNOWN,

	/** Some other diagnosis. */
	OTHER

}