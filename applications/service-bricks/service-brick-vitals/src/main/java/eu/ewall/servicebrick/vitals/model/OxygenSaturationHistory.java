package eu.ewall.servicebrick.vitals.model;

import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.model.DataHistory;

public class OxygenSaturationHistory extends DataHistory {

	private List<OxygenSaturationSbMeasurement> oxygenSaturationMeasurements;
	
	public OxygenSaturationHistory(String username, DateTime from, DateTime to, 
			List<OxygenSaturationSbMeasurement> oxygenSaturationMeasurements) {
		super(username, null, from, to);
		this.oxygenSaturationMeasurements = oxygenSaturationMeasurements;
	}

	public List<OxygenSaturationSbMeasurement> getOxygenSaturationMeasurements() {
		return oxygenSaturationMeasurements;
	}

}
