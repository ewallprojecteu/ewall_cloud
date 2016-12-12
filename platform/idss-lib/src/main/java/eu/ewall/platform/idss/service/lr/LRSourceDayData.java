package eu.ewall.platform.idss.service.lr;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.utils.json.SqlDateDeserializer;
import eu.ewall.platform.idss.utils.json.SqlDateSerializer;

/**
 * Source data for one day as input for a {@link WeekDayLifestyleReasoner
 * WeekDayLifestyleReasoner}. This abstract class only defines the user and
 * date fields. Subclasses should define additional fields.
 * 
 * <p>Instances of this class are stored in a local database of the lifestyle
 * reasoner. There should be at most one instance per day. Days without any
 * data may be omitted.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
public abstract class LRSourceDayData extends AbstractDatabaseObject {
	@DatabaseField(value=DatabaseType.STRING, index=true)
	private String user;

	@DatabaseField(value=DatabaseType.DATE, index=true)
	@JsonSerialize(using=SqlDateSerializer.class)
	@JsonDeserialize(using=SqlDateDeserializer.class)
	private LocalDate date;

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
	 * Returns the date.
	 * 
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date the date
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}
}
