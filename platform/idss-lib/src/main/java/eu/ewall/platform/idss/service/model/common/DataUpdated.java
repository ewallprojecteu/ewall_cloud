package eu.ewall.platform.idss.service.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

import eu.ewall.platform.idss.utils.json.SqlDateDeserializer;
import eu.ewall.platform.idss.utils.json.SqlDateSerializer;

import org.joda.time.LocalDate;

/**
 * This class defines until what date a certain type of data has been updated.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DataUpdated extends AbstractDatabaseObject {
	@JsonIgnore
	private String id;
	
	@DatabaseField(value= DatabaseType.STRING)
	private String user;
	
	@DatabaseField(value=DatabaseType.DATE)
	@JsonSerialize(using=SqlDateSerializer.class)
	@JsonDeserialize(using=SqlDateDeserializer.class)
	private LocalDate date;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

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
	 * Returns the date until which the data has been updated (inclusive).
	 * 
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Sets the date until which the data has been updated (inclusive).
	 * 
	 * @param date the date
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}
}
