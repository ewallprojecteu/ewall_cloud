package eu.ewall.platform.commons.datamodel.profile;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * @author eandgrg
 */
public class HealthSubProfile extends UserSubProfile {

    /** The health diagnosis type: ARI, COPD, MCI... */
    private List<HealthDiagnosisType> healthDiagnosisList = new ArrayList<HealthDiagnosisType>();

    /** The health measurements. */
    private HealthMeasurements healthMeasurements;

    /** The healthImpairments. */
    private HealthImpairments healthImpairments;

    /** The medications. */
    private HealthMedications takesMedications;

    /**
     * Hypertension (HTN or HT), also known as high blood pressure is a common
     * condition in which the long-term force of the blood against your artery
     * walls is high enough that it may eventually cause health problems, such
     * as heart disease
     */
    boolean hypertension;

    /**
     * Dizziness is a term used to describe a range of sensations, such as
     * feeling faint, woozy, weak or unsteady. Dizziness that creates the false
     * sense that you or your surroundings are spinning or moving is called
     * vertigo.
     */
    boolean dizziness;

    /**
     * The Constructor.
     */
    public HealthSubProfile() {

    }

    /**
     * Instantiates a new health sub profile.
     *
     * @param healthMeasurements
     *            the health measurements
     * @param healthImpairments
     *            the health impairments
     * @param takesMedications
     *            the takes medications
     * @param healthDiagnosisList
     *            the health diagnosis list
     */
    public HealthSubProfile(HealthMeasurements healthMeasurements, HealthImpairments healthImpairments,
	    HealthMedications takesMedications, List<HealthDiagnosisType> healthDiagnosisList) {
	this.healthImpairments = healthImpairments;
	this.takesMedications = takesMedications;
	this.healthMeasurements = healthMeasurements;
	this.healthDiagnosisList = healthDiagnosisList;

    }

    /**
     * Instantiates a new health sub profile.
     *
     * @param healthDiagnosisList
     *            the health diagnosis list
     * @param healthMeasurements
     *            the health measurements
     * @param healthImpairments
     *            the health impairments
     * @param takesMedications
     *            the takes medications
     * @param hypertension
     *            the hypertension
     * @param dizziness
     *            the dizziness
     */
    public HealthSubProfile(List<HealthDiagnosisType> healthDiagnosisList, HealthMeasurements healthMeasurements,
	    HealthImpairments healthImpairments, HealthMedications takesMedications, boolean hypertension,
	    boolean dizziness) {
	this.healthDiagnosisList = healthDiagnosisList;
	this.healthMeasurements = healthMeasurements;
	this.healthImpairments = healthImpairments;
	this.takesMedications = takesMedications;
	this.hypertension = hypertension;
	this.dizziness = dizziness;
    }

    /**
     * Gets the health diagnosis type.
     *
     * @return the health diagnosis type
     */
    public List<HealthDiagnosisType> getHealthDiagnosisType() {
	return healthDiagnosisList;
    }

    /**
     * Sets the health diagnosis type.
     *
     * @param healthDiagnosisList
     *            the new health diagnosis type
     */
    public void setHealthDiagnosisType(List<HealthDiagnosisType> healthDiagnosisList) {
	this.healthDiagnosisList = healthDiagnosisList;
    }

    /**
     * Gets the health measurements.
     *
     * @return the checks for health measurements
     */
    public HealthMeasurements getHealthMeasurements() {
	return healthMeasurements;
    }

    /**
     * Sets the health measurements.
     *
     * @param healthMeasurements
     *            the checks for health measurements
     */
    public void setHealthMeasurements(HealthMeasurements healthMeasurements) {
	this.healthMeasurements = healthMeasurements;
    }

    /**
     * Gets the healthImpairments.
     *
     * @return the checks for healthImpairments
     */
    public HealthImpairments getHealthImpairments() {
	return healthImpairments;
    }

    /**
     * Sets the healthImpairments.
     *
     * @param hasImpairments
     *            the healthImpairments
     */
    public void setHealthImpairments(HealthImpairments hasImpairments) {
	this.healthImpairments = hasImpairments;
    }

    /**
     * Gets the medications.
     *
     * @return the medications
     */
    public HealthMedications getTakesMedications() {
	return takesMedications;
    }

    /**
     * Sets the takes medications.
     *
     * @param takesMedications
     *            the takes medications
     */
    public void setTakesMedications(HealthMedications takesMedications) {
	this.takesMedications = takesMedications;
    }

    /**
     * Gets the health diagnosis list.
     *
     * @return the health diagnosis list
     */
    public List<HealthDiagnosisType> getHealthDiagnosisList() {
	return healthDiagnosisList;
    }

    /**
     * Sets the health diagnosis list.
     *
     * @param healthDiagnosisList
     *            the new health diagnosis list
     */
    public void setHealthDiagnosisList(List<HealthDiagnosisType> healthDiagnosisList) {
	this.healthDiagnosisList = healthDiagnosisList;
    }

    /**
     * Checks if is hypertension.
     *
     * @return true, if is hypertension
     */
    public boolean isHypertension() {
	return hypertension;
    }

    /**
     * Sets the hypertension.
     *
     * @param hypertension
     *            the new hypertension
     */
    public void setHypertension(boolean hypertension) {
	this.hypertension = hypertension;
    }

    /**
     * Checks if is dizziness.
     *
     * @return true, if is dizziness
     */
    public boolean isDizziness() {
	return dizziness;
    }

    /**
     * Sets the dizziness.
     *
     * @param dizziness
     *            the new dizziness
     */
    public void setDizziness(boolean dizziness) {
	this.dizziness = dizziness;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

	StringBuffer buffer = new StringBuffer();
	buffer.append("******HealthSubProfile start******\n");

	buffer.append("healthDiagnosisList: ");
	if (healthDiagnosisList != null)
	    for (HealthDiagnosisType hdtype : healthDiagnosisList)
		buffer.append(hdtype.toString() + ", ");
	else
	    buffer.append("empty");
	buffer.append("\n");

	buffer.append("\n---healthMeasurements start---\n");
	if (healthMeasurements != null)
	    buffer.append(healthMeasurements.toString());
	else
	    buffer.append("empty");
	buffer.append("\n");
	buffer.append("---healthMeasurements end---\n");

	buffer.append("---healthImpairments start---\n");
	if (healthImpairments != null)
	    buffer.append(healthImpairments.toString());
	else
	    buffer.append("empty");
	buffer.append("\n");
	buffer.append("---healthImpairments end---\n");

	buffer.append("---takesMedications start---\n");
	if (takesMedications != null)
	    buffer.append(takesMedications.toString());
	else
	    buffer.append("empty");
	buffer.append("\n");
	buffer.append("---takesMedications end---\n");

	buffer.append("has hypertension: " + hypertension + "\n");

	buffer.append("has dizziness: " + dizziness + "\n");

	buffer.append("******HealthSubProfile end******\n");

	return buffer.toString();
    }

}