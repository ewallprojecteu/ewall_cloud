package eu.ewall.platform.commons.datamodel.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
/**
 * Impairments are concerned with abnormalities of body structure and appearance
 * and with organ or system function) resulting from any cause.
 * 
 * This class contains sensory, skeletal, language, memory categories of
 * impairments.
 * 
 * @author eandgrg
 */
public class HealthImpairments implements Serializable {

    // SENSORY RELATED IMPAIRMENTS
    /** The astigmatism. */
    private boolean astigmatism;

    /** The color blindness. */
    private boolean colorBlindness;

    /** The far sightedness. */
    private boolean farSightedness;

    /** The light sensitivity. */
    private boolean lightSensitivity;

    /** The short sightedness. */
    private boolean shortSightedness;

    /** The vision loss. */
    private Intensity visionLoss;

    /** The hear loss. */
    private Intensity hearLoss;

    // SKELETAL IMPAIRMENTS
    /** The neck motor impairment. */
    private Intensity neckMotorImpairment;

    /** The shoulder impairment. */
    private Intensity shoulderImpairment;

    /** The arm impairment. */
    private Intensity armImpairment;

    /** The wrist impairment. */
    private Intensity wristImpairment;

    /** The hip impairment. */
    private Intensity hipImpairment;

    /** The knee impairment. */
    private Intensity kneeImpairment;

    /** The leg impairment. */
    private Intensity legImpairment;

    /** The anckle impairment. */
    private Intensity anckleImpairment;

    /** The foot impairment. */
    private Intensity footImpairment;

    /** The foot problems list. */
    private List<FootProblemType> footProblemsList = new ArrayList<FootProblemType>();

    // LANGUAGE RELATED IMPAIRMENTS
    /** The dislexia. */
    private boolean dislexia;

    // MEMORY IMPAIRMENTS
    /** The amnesia. */
    private boolean amnesia;

    /**
     * The Constructor.
     */
    public HealthImpairments() {

    }

    /**
     * The Constructor.
     *
     * @param astigmatism
     *            the astigmatism
     * @param colorBlindness
     *            the color blindness
     * @param farSightedness
     *            the far sightedness
     * @param shortSightedness
     *            the short sightedness
     * @param lightSensitivity
     *            the light sensitivity
     */
    public HealthImpairments(boolean astigmatism, boolean colorBlindness, boolean farSightedness,
	    boolean shortSightedness, boolean lightSensitivity) {
	this.astigmatism = astigmatism;
	this.colorBlindness = colorBlindness;
	this.farSightedness = farSightedness;
	this.shortSightedness = shortSightedness;
	this.lightSensitivity = lightSensitivity;

    }

    /**
     * Instantiates a new health impairments.
     *
     * @param astigmatism
     *            the astigmatism
     * @param colorBlindness
     *            the color blindness
     * @param farSightedness
     *            the far sightedness
     * @param lightSensitivity
     *            the light sensitivity
     * @param shortSightedness
     *            the short sightedness
     * @param visionLoss
     *            the vision loss
     * @param hearLoss
     *            the hear loss
     * @param neckMotorImpairment
     *            the neck motor impairment
     * @param shoulderImpairment
     *            the shoulder impairment
     * @param armImpairment
     *            the arm impairment
     * @param wristImpairment
     *            the wrist impairment
     * @param hipImpairment
     *            the hip impairment
     * @param kneeImpairment
     *            the knee impairment
     * @param legImpairment
     *            the leg impairment
     * @param anckleImpairment
     *            the anckle impairment
     * @param footImpairment
     *            the foot impairment
     * @param footProblemsList
     *            the foot problems list
     * @param dislexia
     *            the dislexia
     * @param amnesia
     *            the amnesia
     */
    public HealthImpairments(boolean astigmatism, boolean colorBlindness, boolean farSightedness,
	    boolean lightSensitivity, boolean shortSightedness, Intensity visionLoss, Intensity hearLoss,
	    Intensity neckMotorImpairment, Intensity shoulderImpairment, Intensity armImpairment,
	    Intensity wristImpairment, Intensity hipImpairment, Intensity kneeImpairment, Intensity legImpairment,
	    Intensity anckleImpairment, Intensity footImpairment, List<FootProblemType> footProblemsList,
	    boolean dislexia, boolean amnesia) {
	this.astigmatism = astigmatism;
	this.colorBlindness = colorBlindness;
	this.farSightedness = farSightedness;
	this.lightSensitivity = lightSensitivity;
	this.shortSightedness = shortSightedness;
	this.visionLoss = visionLoss;
	this.hearLoss = hearLoss;
	this.neckMotorImpairment = neckMotorImpairment;
	this.shoulderImpairment = shoulderImpairment;
	this.armImpairment = armImpairment;
	this.wristImpairment = wristImpairment;
	this.hipImpairment = hipImpairment;
	this.kneeImpairment = kneeImpairment;
	this.legImpairment = legImpairment;
	this.anckleImpairment = anckleImpairment;
	this.footImpairment = footImpairment;
	this.footProblemsList = footProblemsList;
	this.dislexia = dislexia;
	this.amnesia = amnesia;
    }

    /**
     * Instantiates a new health impairments.
     *
     * @param astigmatism
     *            the astigmatism
     * @param colorBlindness
     *            the color blindness
     * @param farSightedness
     *            the far sightedness
     * @param lightSensitivity
     *            the light sensitivity
     * @param shortSightedness
     *            the short sightedness
     * @param visionLoss
     *            the vision loss
     * @param hearLoss
     *            the hear loss
     * @param neckMotorImpairment
     *            the neck motor impairment
     * @param shoulderImpairment
     *            the shoulder impairment
     * @param armImpairment
     *            the arm impairment
     * @param wristImpairment
     *            the wrist impairment
     * @param hipImpairment
     *            the hip impairment
     * @param kneeImpairment
     *            the knee impairment
     * @param legImpairment
     *            the leg impairment
     * @param anckleImpairment
     *            the anckle impairment
     * @param footImpairment
     *            the foot impairment
     * @param dislexia
     *            the dislexia
     * @param amnesia
     *            the amnesia
     */
    public HealthImpairments(boolean astigmatism, boolean colorBlindness, boolean farSightedness,
	    boolean lightSensitivity, boolean shortSightedness, Intensity visionLoss, Intensity hearLoss,
	    Intensity neckMotorImpairment, Intensity shoulderImpairment, Intensity armImpairment,
	    Intensity wristImpairment, Intensity hipImpairment, Intensity kneeImpairment, Intensity legImpairment,
	    Intensity anckleImpairment, Intensity footImpairment, boolean dislexia, boolean amnesia) {
	this.astigmatism = astigmatism;
	this.colorBlindness = colorBlindness;
	this.farSightedness = farSightedness;
	this.lightSensitivity = lightSensitivity;
	this.shortSightedness = shortSightedness;
	this.visionLoss = visionLoss;
	this.hearLoss = hearLoss;
	this.neckMotorImpairment = neckMotorImpairment;
	this.shoulderImpairment = shoulderImpairment;
	this.armImpairment = armImpairment;
	this.wristImpairment = wristImpairment;
	this.hipImpairment = hipImpairment;
	this.kneeImpairment = kneeImpairment;
	this.legImpairment = legImpairment;
	this.anckleImpairment = anckleImpairment;
	this.footImpairment = footImpairment;
	this.dislexia = dislexia;
	this.amnesia = amnesia;
    }

    /**
     * Checks if is astigmatism.
     *
     * @return true, if checks if is astigmatism
     */
    public boolean isAstigmatism() {
	return astigmatism;
    }

    /**
     * Sets the astigmatism.
     *
     * @param astigmatism
     *            the astigmatism
     */
    public void setAstigmatism(boolean astigmatism) {
	this.astigmatism = astigmatism;
    }

    /**
     * Checks if is color blindness.
     *
     * @return true, if checks if is color blindness
     */
    public boolean isColorBlindness() {
	return colorBlindness;
    }

    /**
     * Sets the color blindness.
     *
     * @param colorBlindness
     *            the color blindness
     */
    public void setColorBlindness(boolean colorBlindness) {
	this.colorBlindness = colorBlindness;
    }

    /**
     * Checks if is far sightedness.
     *
     * @return true, if checks if is far sightedness
     */
    public boolean isFarSightedness() {
	return farSightedness;
    }

    /**
     * Sets the far sightedness.
     *
     * @param farSightedness
     *            the far sightedness
     */
    public void setFarSightedness(boolean farSightedness) {
	this.farSightedness = farSightedness;
    }

    /**
     * Checks if is light sensitivity.
     *
     * @return true, if checks if is light sensitivity
     */
    public boolean isLightSensitivity() {
	return lightSensitivity;
    }

    /**
     * Sets the light sensitivity.
     *
     * @param lightSensitivity
     *            the light sensitivity
     */
    public void setLightSensitivity(boolean lightSensitivity) {
	this.lightSensitivity = lightSensitivity;
    }

    /**
     * Checks if is short sightedness.
     *
     * @return true, if checks if is short sightedness
     */
    public boolean isShortSightedness() {
	return shortSightedness;
    }

    /**
     * Sets the short sightedness.
     *
     * @param shortSightedness
     *            the short sightedness
     */
    public void setShortSightedness(boolean shortSightedness) {
	this.shortSightedness = shortSightedness;
    }

    /**
     * Gets the vision loss.
     *
     * @return the vision loss
     */
    public Intensity getVisionLoss() {
	return visionLoss;
    }

    /**
     * Sets the vision loss.
     *
     * @param visionLoss
     *            the new vision loss
     */
    public void setVisionLoss(Intensity visionLoss) {
	this.visionLoss = visionLoss;
    }

    /**
     * Gets the hear loss.
     *
     * @return the hear loss
     */
    public Intensity getHearLoss() {
	return hearLoss;
    }

    /**
     * Sets the hear loss.
     *
     * @param hearLoss
     *            the new hear loss
     */
    public void setHearLoss(Intensity hearLoss) {
	this.hearLoss = hearLoss;
    }

    /**
     * Gets the neck motor impairment.
     *
     * @return the neck motor impairment
     */
    public Intensity getNeckMotorImpairment() {
	return neckMotorImpairment;
    }

    /**
     * Sets the neck motor impairment.
     *
     * @param neckMotorImpairment
     *            the new neck motor impairment
     */
    public void setNeckMotorImpairment(Intensity neckMotorImpairment) {
	this.neckMotorImpairment = neckMotorImpairment;
    }

    /**
     * Gets the shoulder impairment.
     *
     * @return the shoulder impairment
     */
    public Intensity getShoulderImpairment() {
	return shoulderImpairment;
    }

    /**
     * Sets the shoulder impairment.
     *
     * @param shoulderImpairment
     *            the new shoulder impairment
     */
    public void setShoulderImpairment(Intensity shoulderImpairment) {
	this.shoulderImpairment = shoulderImpairment;
    }

    /**
     * Gets the arm impairment.
     *
     * @return the arm impairment
     */
    public Intensity getArmImpairment() {
	return armImpairment;
    }

    /**
     * Sets the arm impairment.
     *
     * @param armImpairment
     *            the new arm impairment
     */
    public void setArmImpairment(Intensity armImpairment) {
	this.armImpairment = armImpairment;
    }

    /**
     * Gets the wrist impairment.
     *
     * @return the wrist impairment
     */
    public Intensity getWristImpairment() {
	return wristImpairment;
    }

    /**
     * Sets the wrist impairment.
     *
     * @param wristImpairment
     *            the new wrist impairment
     */
    public void setWristImpairment(Intensity wristImpairment) {
	this.wristImpairment = wristImpairment;
    }

    /**
     * Gets the hip impairment.
     *
     * @return the hip impairment
     */
    public Intensity getHipImpairment() {
	return hipImpairment;
    }

    /**
     * Sets the hip impairment.
     *
     * @param hipImpairment
     *            the new hip impairment
     */
    public void setHipImpairment(Intensity hipImpairment) {
	this.hipImpairment = hipImpairment;
    }

    /**
     * Gets the knee impairment.
     *
     * @return the knee impairment
     */
    public Intensity getKneeImpairment() {
	return kneeImpairment;
    }

    /**
     * Sets the knee impairment.
     *
     * @param kneeImpairment
     *            the new knee impairment
     */
    public void setKneeImpairment(Intensity kneeImpairment) {
	this.kneeImpairment = kneeImpairment;
    }

    /**
     * Gets the leg impairment.
     *
     * @return the leg impairment
     */
    public Intensity getLegImpairment() {
	return legImpairment;
    }

    /**
     * Sets the leg impairment.
     *
     * @param legImpairment
     *            the new leg impairment
     */
    public void setLegImpairment(Intensity legImpairment) {
	this.legImpairment = legImpairment;
    }

    /**
     * Gets the anckle impairment.
     *
     * @return the anckle impairment
     */
    public Intensity getAnckleImpairment() {
	return anckleImpairment;
    }

    /**
     * Sets the anckle impairment.
     *
     * @param anckleImpairment
     *            the new anckle impairment
     */
    public void setAnckleImpairment(Intensity anckleImpairment) {
	this.anckleImpairment = anckleImpairment;
    }

    /**
     * Gets the foot impairment.
     *
     * @return the foot impairment
     */
    public Intensity getFootImpairment() {
	return footImpairment;
    }

    /**
     * Sets the foot impairment.
     *
     * @param footImpairment
     *            the new foot impairment
     */
    public void setFootImpairment(Intensity footImpairment) {
	this.footImpairment = footImpairment;
    }

    /**
     * Checks if is dislexia.
     *
     * @return true, if is dislexia
     */
    public boolean isDislexia() {
	return dislexia;
    }

    /**
     * Sets the dislexia.
     *
     * @param dislexia
     *            the new dislexia
     */
    public void setDislexia(boolean dislexia) {
	this.dislexia = dislexia;
    }

    /**
     * Checks if is amnesia.
     *
     * @return true, if is amnesia
     */
    public boolean isAmnesia() {
	return amnesia;
    }

    /**
     * Sets the amnesia.
     *
     * @param amnesia
     *            the new amnesia
     */
    public void setAmnesia(boolean amnesia) {
	this.amnesia = amnesia;
    }

    /**
     * Gets the foot problems list.
     *
     * @return the foot problems list
     */
    public List<FootProblemType> getFootProblemsList() {
	return footProblemsList;
    }

    /**
     * Sets the foot problems list.
     *
     * @param footProblemsList
     *            the new foot problems list
     */
    public void setFootProblemsList(List<FootProblemType> footProblemsList) {
	this.footProblemsList = footProblemsList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

	StringBuffer buffer = new StringBuffer();
	buffer.append("astigmatism: ");
	buffer.append(astigmatism);
	buffer.append("\n");
	buffer.append("colorBlindness: ");
	buffer.append(colorBlindness);
	buffer.append("\n");
	buffer.append("farSightedness: ");
	buffer.append(farSightedness);
	buffer.append("\n");
	buffer.append("shortSightedness: ");
	buffer.append(shortSightedness);
	buffer.append("\n");
	buffer.append("lightSensitivity: ");
	buffer.append(lightSensitivity);
	buffer.append("\n");
	buffer.append("visionLoss: ");
	buffer.append(visionLoss);
	buffer.append("\n");
	buffer.append("hearLoss: ");
	buffer.append(hearLoss);
	buffer.append("\n");
	buffer.append("neckMotorImpairment: ");
	buffer.append(neckMotorImpairment);
	buffer.append("\n");
	buffer.append("shoulderImpairment: ");
	buffer.append(shoulderImpairment);
	buffer.append("\n");
	buffer.append("armImpairment: ");
	buffer.append(armImpairment);
	buffer.append("\n");
	buffer.append("wristImpairment: ");
	buffer.append(wristImpairment);
	buffer.append("\n");
	buffer.append("hipImpairment: ");
	buffer.append(hipImpairment);
	buffer.append("\n");
	buffer.append("kneeImpairment: ");
	buffer.append(kneeImpairment);
	buffer.append("\n");
	buffer.append("legImpairment: ");
	buffer.append(legImpairment);
	buffer.append("\n");
	buffer.append("anckleImpairment: ");
	buffer.append(anckleImpairment);
	buffer.append("\n");
	buffer.append("footImpairment: ");
	buffer.append(footImpairment);
	buffer.append("\n");

	buffer.append("footProblemsList: ");
	if (footProblemsList != null)
	    for (FootProblemType fptype : footProblemsList)
		buffer.append(fptype.toString() + ", ");
	else
	    buffer.append("empty");
	buffer.append("\n");

	buffer.append("dislexia: ");
	buffer.append(dislexia);
	buffer.append("\n");
	buffer.append("amnesia: ");
	buffer.append(amnesia);
	buffer.append("\n");

	return buffer.toString();

    }

}