package eu.ewall.platform.idss.service.model.common;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.utils.json.DateTimeFromIsoDateTimeDeserializer;
import eu.ewall.platform.idss.utils.json.IsoDateTimeSerializer;
import eu.ewall.platform.idss.utils.json.JsonObject;

/**
 * This class can be used for a service request that wants to return an ISO
 * date/time string or null. Spring serializes a null response to an empty
 * document, which results in an error when deserializing. This class wraps
 * an object around the actual response.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DateTimeServiceResponse extends JsonObject {
	@JsonSerialize(using=IsoDateTimeSerializer.class)
	@JsonDeserialize(using=DateTimeFromIsoDateTimeDeserializer.class)
	private DateTime value = null;

	/**
	 * Constructs a response with value null.
	 */
	public DateTimeServiceResponse() {
	}
	
	/**
	 * Constructs a response with the specified value.
	 * 
	 * @param value the value or null
	 */
	public DateTimeServiceResponse(DateTime value) {
		this.value = value;
	}
	
	/**
	 * Returns the actual response.
	 * 
	 * @return the actual response
	 */
	public DateTime getValue() {
		return value;
	}

	/**
	 * Sets the actual response.
	 * 
	 * @param value the actual response
	 */
	public void setValue(DateTime value) {
		this.value = value;
	}
}
