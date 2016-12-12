package eu.ewall.servicebrick.vitals.model;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.model.SbMeasurement;

/**
 * An oxygen saturation measurement in the output format used by service bricks.
 */
public class OxygenSaturationSbMeasurement extends SbMeasurement {

	private int oxygenSaturation;
	
	public OxygenSaturationSbMeasurement(DateTime timestamp, int oxygenSaturation) {
		super(timestamp);
		this.oxygenSaturation = oxygenSaturation;
	}

	public int getOxygenSaturation() {
		return oxygenSaturation;
	}
	
}
