package eu.ewall.platform.commons.datamodel.location;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * @author eandgrg
 */
public class OutdoorPlace extends Location {

	/** The outdoor place area. */
	private OutdoorPlaceArea outdoorPlaceArea;

	/**
	 * The Constructor.
	 */
	public OutdoorPlace() {

	}

	/**
	 * The Constructor.
	 *
	 * @param name
	 *            the name
	 * @param outdoorPlaceArea
	 *            the outdoor place area
	 * @param timestamp
	 *            the timestamp
	 */
	public OutdoorPlace(String name, OutdoorPlaceArea outdoorPlaceArea,
			long timestamp) {
		this.name = name;
		this.outdoorPlaceArea = outdoorPlaceArea;
		this.timestamp = timestamp;
	}

	/**
	 * Gets the outdoor place area.
	 *
	 * @return the outdoorPlaceArea
	 */
	public OutdoorPlaceArea getOutdoorPlaceArea() {
		return outdoorPlaceArea;
	}

	/**
	 * Sets the outdoor place area.
	 *
	 * @param outdoorPlaceArea
	 *            the outdoorPlaceArea to set
	 */
	public void setOutdoorPlaceArea(OutdoorPlaceArea outdoorPlaceArea) {
		this.outdoorPlaceArea = outdoorPlaceArea;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("------OutoorPlace start------\n");
		buffer.append("name: ");
		buffer.append(name);
		buffer.append("\n");
		buffer.append("timestamp: ");
		buffer.append(timestamp);
		buffer.append("\n");
		buffer.append("outdoorPlaceArea: ");
		buffer.append(outdoorPlaceArea);
		buffer.append("\n");
		buffer.append("------OutoorPlace end------\n");
		return buffer.toString();
	}
}