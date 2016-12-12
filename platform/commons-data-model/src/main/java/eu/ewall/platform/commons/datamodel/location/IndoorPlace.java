package eu.ewall.platform.commons.datamodel.location;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * @author eandgrg
 */
public class IndoorPlace extends Location {

    /** The indoorPlaceArea. */
    private IndoorPlaceArea indoorPlaceArea;

    /** The poor light. */
    boolean poorLight;

    /** The slippery floor. */
    boolean slipperyFloor;

    /** The loose carpet. */
    boolean looseCarpet;

    /** The stairs exist. */
    boolean stairsExist;

    /**
     * The Constructor.
     */
    public IndoorPlace() {

    }

    /**
     * Instantiates a new indoor place.
     *
     * @param indoorPlace
     *            the indoor place
     */
    public IndoorPlace(IndoorPlace indoorPlace) {
	this.name = indoorPlace.getName();
	this.indoorPlaceArea = indoorPlace.getIndoorPlaceArea();
	this.timestamp = indoorPlace.getTimestamp();
    }

    /**
     * The Constructor.
     *
     * @param name
     *            the name
     * @param indoorPlaceArea
     *            the indoor place area
     * @param timestamp
     *            the timestamp
     */
    public IndoorPlace(String name, IndoorPlaceArea indoorPlaceArea, long timestamp) {
	this.name = name;
	this.indoorPlaceArea = indoorPlaceArea;
	this.timestamp = timestamp;
    }

    /**
     * Instantiates a new indoor place.
     *
     * @param name
     *            the name
     * @param indoorPlaceArea
     *            the indoor place area
     * @param timestamp
     *            the timestamp
     * @param poorLight
     *            the poor light
     * @param slipperyFloor
     *            the slippery floor
     * @param looseCarpet
     *            the loose carpet
     * @param stairsExist
     *            the stairs exist
     */
    public IndoorPlace(String name, IndoorPlaceArea indoorPlaceArea, long timestamp, boolean poorLight,
	    boolean slipperyFloor, boolean looseCarpet, boolean stairsExist) {
	this.name = name;
	this.indoorPlaceArea = indoorPlaceArea;
	this.timestamp = timestamp;
	this.indoorPlaceArea = indoorPlaceArea;
	this.poorLight = poorLight;
	this.slipperyFloor = slipperyFloor;
	this.looseCarpet = looseCarpet;
	this.stairsExist = stairsExist;
    }

    /**
     * Gets the indoor place area.
     *
     * @return the indoorPlaceArea
     */
    public IndoorPlaceArea getIndoorPlaceArea() {
	return indoorPlaceArea;
    }

    /**
     * Sets the indoor place area.
     *
     * @param indoorPlaceArea
     *            the indoorPlaceArea to set
     */
    public void setIndoorPlaceArea(IndoorPlaceArea indoorPlaceArea) {
	this.indoorPlaceArea = indoorPlaceArea;
    }

    /**
     * Checks if is poor light.
     *
     * @return true, if is poor light
     */
    public boolean isPoorLight() {
	return poorLight;
    }

    /**
     * Sets the poor light.
     *
     * @param poorLight
     *            the new poor light
     */
    public void setPoorLight(boolean poorLight) {
	this.poorLight = poorLight;
    }

    /**
     * Checks if is slippery floor.
     *
     * @return true, if is slippery floor
     */
    public boolean isSlipperyFloor() {
	return slipperyFloor;
    }

    /**
     * Sets the slippery floor.
     *
     * @param slipperyFloor
     *            the new slippery floor
     */
    public void setSlipperyFloor(boolean slipperyFloor) {
	this.slipperyFloor = slipperyFloor;
    }

    /**
     * Checks if is loose carpet.
     *
     * @return true, if is loose carpet
     */
    public boolean isLooseCarpet() {
	return looseCarpet;
    }

    /**
     * Sets the loose carpet.
     *
     * @param looseCarpet
     *            the new loose carpet
     */
    public void setLooseCarpet(boolean looseCarpet) {
	this.looseCarpet = looseCarpet;
    }

    /**
     * Checks if is stairs exist.
     *
     * @return true, if is stairs exist
     */
    public boolean isStairsExist() {
	return stairsExist;
    }

    /**
     * Sets the stairs exist.
     *
     * @param stairsExist
     *            the new stairs exist
     */
    public void setStairsExist(boolean stairsExist) {
	this.stairsExist = stairsExist;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

	StringBuffer buffer = new StringBuffer();
	buffer.append("------IndoorPlace start------\n");
	buffer.append("name: ");
	buffer.append(name);
	buffer.append("\n");
	buffer.append("timestamp: ");
	buffer.append(timestamp);
	buffer.append("\n");
	buffer.append("indoorPlaceArea: ");
	buffer.append(indoorPlaceArea);
	buffer.append("\n");
	buffer.append("poorLight: ");
	buffer.append(poorLight);
	buffer.append("\n");
	buffer.append("slipperyFloor: ");
	buffer.append(slipperyFloor);
	buffer.append("\n");
	buffer.append("looseCarpet: ");
	buffer.append(looseCarpet);
	buffer.append("\n");
	buffer.append("stairsExist: ");
	buffer.append(stairsExist);
	buffer.append("\n");
	buffer.append("------IndoorPlace end------\n");
	return buffer.toString();
    }

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    @JsonIgnore
    public long getTimestamp() {
	return timestamp;
    }

}