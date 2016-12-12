package eu.ewall.platform.idss.service.model.common;

/**
 * This class can be used for a service request that wants to return null
 * sometimes. Spring serializes a null response to an empty document, which
 * results in an error when deserializing. This class wraps an object around
 * the actual response.
 * 
 * @author Dennis Hofs (RRD)
 *
 * @param <T> the actual response type
 */
public class ServiceResponse<T> {
	private T value = null;

	/**
	 * Constructs a response with value null.
	 */
	public ServiceResponse() {
	}
	
	/**
	 * Constructs a response with the specified value.
	 * 
	 * @param value the value or null
	 */
	public ServiceResponse(T value) {
		this.value = value;
	}
	
	/**
	 * Returns the actual response.
	 * 
	 * @return the actual response
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Sets the actual response.
	 * 
	 * @param value the actual response
	 */
	public void setValue(T value) {
		this.value = value;
	}
}
