package eu.ewall.platform.idss.service.model.common;

/**
 * Response type for a request for the last date when data has been updated by
 * a service. This is normally implemented by a method that returns a {@link
 * DataUpdated DataUpdated} object or null. This class wraps around that result
 * to ensure that the request never returns null.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DataUpdatedResponse extends ServiceResponse<DataUpdated> {
	public DataUpdatedResponse() {
	}

	public DataUpdatedResponse(DataUpdated value) {
		setValue(value);
	}
}
