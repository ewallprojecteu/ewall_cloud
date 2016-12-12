package eu.ewall.platform.lr.sleepmonitor.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.utils.json.SqlDateDeserializer;
import eu.ewall.platform.idss.utils.json.SqlDateSerializer;

/**
 * An instance of this class contains sleep parameters for one night. It should
 * define the following parameters:
 * 
 * <p><ul>
 * <li>{@link SleepParameter#PARAM_BED_OFF_TIME PARAM_BED_OFF_TIME}: time when
 * the user got out of bed, ISO date/time string</li>
 * <li>{@link SleepParameter#PARAM_BED_ON_TIME PARAM_BED_ON_TIME}: time when
 * the user went to bed, ISO date/time string</li>
 * <li>{@link SleepParameter#PARAM_FREQUENCY_WAKING_UP
 * PARAM_FREQUENCY_WAKING_UP}: number of times the user woke up</li>
 * <li>{@link SleepParameter#PARAM_SLEEP_EFFICIENCY PARAM_SLEEP_EFFICIENCY}:
 * sleep efficiency in percent</li>
 * <li>{@link SleepParameter#PARAM_TOTAL_SLEEP_TIME PARAM_TOTAL_SLEEP_TIME}:
 * total sleep time in minutes</li>
 * </ul></p>
 * 
 * @author Dennis Hofs (RRD)
 */
public class NightSleepData extends AbstractDatabaseObject {
	
	@DatabaseField(value=DatabaseType.STRING)
	private String user;
	
	@DatabaseField(value=DatabaseType.DATE)
	@JsonSerialize(using=SqlDateSerializer.class)
	@JsonDeserialize(using=SqlDateDeserializer.class)
	private LocalDate date;
	
	@DatabaseField(value=DatabaseType.MAP, elemType=DatabaseType.STRING)
	private Map<String,String> sleepParameters =
			new LinkedHashMap<String,String>();

	/**
	 * Returns the user name.
	 * 
	 * @return the user name
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user name.
	 * 
	 * @param user the user name
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Returns the date following the night for which this object contains
	 * sleep parameters.
	 * 
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Sets the date following the night for which this object contains sleep
	 * parameters.
	 * 
	 * @param date the date
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * Returns the sleep parameters. The keys should be one of the constants
	 * defined in {@link SleepParameter SleepParameter}.
	 * 
	 * @return the sleep parameters
	 */
	public Map<String,String> getSleepParameters() {
		return sleepParameters;
	}

	/**
	 * Sets the sleep parameters. The keys should be one of the constants
	 * defined in {@link SleepParameter SleepParameter}.
	 * 
	 * @param sleepParameters the sleep parameters
	 */
	public void setSleepParameters(Map<String,String> sleepParameters) {
		this.sleepParameters = sleepParameters;
	}
	
	/**
	 * Returns the value for the specified sleep parameter, which should be one
	 * of the constants defined in {@link SleepParameter SleepParameter}.
	 * 
	 * @param param the parameter
	 * @return the value
	 */
	public String getSleepParameter(String param) {
		return sleepParameters.get(param);
	}
	
	/**
	 * Adds a sleep parameter. The parameter should be one of the constants
	 * defined in {@link SleepParameter SleepParameter}.
	 * 
	 * @param param the parameter
	 * @param value the value
	 */
	public void putSleepParameter(String param, String value) {
		this.sleepParameters.put(param, value);
	}
}
