package eu.ewall.platform.idss.service.model.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.utils.json.SqlDateDeserializer;
import eu.ewall.platform.idss.utils.json.SqlDateSerializer;

import java.util.List;

import org.joda.time.LocalDate;

/**
 * A sleep week pattern as calculated by the lifestyle reasoner for sleep
 * monitoring. It contains a pattern for each week day. Days without data are
 * not included.
 * 
 * @author Dennis Hofs (RRD)
 */
public class LRSleepWeekPattern {
	private String user;
	private List<LRSleepDayPattern> days;
	
	@JsonSerialize(using=SqlDateSerializer.class)
	@JsonDeserialize(using=SqlDateDeserializer.class)
	private LocalDate updated;

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
	 * Returns the pattern for each week day. Days without data are not
	 * included.
	 * 
	 * @return the week day patterns
	 */
	public List<LRSleepDayPattern> getDays() {
		return days;
	}

	/**
	 * Sets the pattern for each week day. Days without data should not be
	 * included.
	 * 
	 * @param days the week day patterns
	 */
	public void setDays(List<LRSleepDayPattern> days) {
		this.days = days;
	}

	/**
	 * Returns the date following the night until which sleep data has been
	 * processed to calculate this week pattern. This may be null if no data
	 * is available.
	 * 
	 * @return the date until which sleep data has been processed or null
	 */
	public LocalDate getUpdated() {
		return updated;
	}

	/**
	 * Sets the date following the night until which sleep data has been
	 * processed to calculate this week pattern. This may be null if no data
	 * is available.
	 * 
	 * @param updated the date until which sleep data has been processed or
	 * null
	 */
	public void setUpdated(LocalDate updated) {
		this.updated = updated;
	}
}
