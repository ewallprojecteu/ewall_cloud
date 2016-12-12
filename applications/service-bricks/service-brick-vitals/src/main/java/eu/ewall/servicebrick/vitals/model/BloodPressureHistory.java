package eu.ewall.servicebrick.vitals.model;

import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.model.DataHistory;

public class BloodPressureHistory extends DataHistory {

	private List<BloodPressureSbMeasurement> bloodPressureMeasurements;
	
	public BloodPressureHistory(String username, DateTime from, DateTime to,
			List<BloodPressureSbMeasurement> bloodPressureMeasurements) {
		super(username, null, from, to);
		this.bloodPressureMeasurements = bloodPressureMeasurements;
	}

	public List<BloodPressureSbMeasurement> getBloodPressureMeasurements() {
		return bloodPressureMeasurements;
	}

}
