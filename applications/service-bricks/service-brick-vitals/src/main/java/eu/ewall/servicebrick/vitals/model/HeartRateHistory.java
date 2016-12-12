package eu.ewall.servicebrick.vitals.model;

import java.util.List;

import org.joda.time.DateTime;

import eu.ewall.servicebrick.common.model.DataHistory;

public class HeartRateHistory extends DataHistory {

	private List<HeartRateSbMeasurement> heartRateMeasurements;
	
	public HeartRateHistory(String username, DateTime from, DateTime to, 
			List<HeartRateSbMeasurement> heartRateMeasurements) {
		super(username, null, from, to);
		this.heartRateMeasurements = heartRateMeasurements;
	}

	public List<HeartRateSbMeasurement> getHeartRateMeasurements() {
		return heartRateMeasurements;
	}

}
