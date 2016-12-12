package eu.ewall.platform.commons.datamodel.measure;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * The Class OxygenSaturationMeasurement.
 * 
 * @author emirmos
 */
public class OxygenSaturationMeasurement extends Measurement {

	private int oxygenSaturation;

	/**
	 * Instantiates a new oxygen saturation measurement.
	 */
	public OxygenSaturationMeasurement() {
		super();
	}

	/**
	 * Instantiates a new oxygen saturation measurement.
	 *
	 * @param oxygenSaturation
	 *            the oxygen saturation
	 * @param timestamp
	 *            the timestamp
	 */
	public OxygenSaturationMeasurement(int oxygenSaturation, long timestamp) {
		super(ConstantQuantityMeasureType.OXYGEN_SATURATION, timestamp);
		this.oxygenSaturation = oxygenSaturation;
	}

	/**
	 * @return the oxygenSaturation
	 */
	public int getOxygenSaturation() {
		return oxygenSaturation;
	}

	/**
	 * @param oxygenSaturation
	 *            the oxygenSaturation to set
	 */
	public void setOxygenSaturation(int oxygenSaturation) {
		this.oxygenSaturation = oxygenSaturation;
	}

}
