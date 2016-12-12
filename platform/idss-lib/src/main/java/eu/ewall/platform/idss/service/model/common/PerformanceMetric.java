package eu.ewall.platform.idss.service.model.common;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.utils.json.SqlDateDeserializer;
import eu.ewall.platform.idss.utils.json.SqlDateSerializer;
import eu.ewall.platform.idss.utils.json.SqlTimeDeserializer;
import eu.ewall.platform.idss.utils.json.SqlTimeSerializer;

/**
 * A {@link PerformanceMetric} object contains information regarding an executed task
 * that can be logged to a database.
 * 
 * @author Harm op den Akker (RRD)
 */
public class PerformanceMetric extends AbstractDatabaseObject {
	
	@DatabaseField(value=DatabaseType.STRING)
	private String source;
	
	@DatabaseField(value=DatabaseType.DATE)
	@JsonSerialize(using=SqlDateSerializer.class)
	@JsonDeserialize(using=SqlDateDeserializer.class)
	private LocalDate taskEndDate;
	
	@DatabaseField(value=DatabaseType.TIME)
	@JsonSerialize(using=SqlTimeSerializer.class)
	@JsonDeserialize(using=SqlTimeDeserializer.class)
	private LocalTime taskEndTime;
	
	@DatabaseField(value=DatabaseType.LONG)
	private long taskExecutionTime;
	
	// ----- Optional parameters
	
	@DatabaseField(value=DatabaseType.STRING)
	private String username = null;
	
	@DatabaseField(value=DatabaseType.INT)
	private Integer repetitions = null;
	
	// ---------- CONSTRUCTORS ---------- //
	
	/**
	 * Creates an instance of a {@link PerformanceMetric} object, storing logging information
	 * about performance of a specific execution task.
	 * @param source a textual description of the task that was executed (free form description).
	 * @param taskEndDate the date at which the task was finished (as {@link LocalDate} object).
	 * @param taskEndtime the time at which the taks was finished (as {@link LocalTime} object).
	 * @param taskExecutionTime the duration of the executed task in milliseconds.
	 */
	public PerformanceMetric(String source, LocalDate taskEndDate, LocalTime taskEndTime, long taskExecutionTime) {
		this.source = source;
		this.taskEndDate = taskEndDate;
		this.taskEndTime = taskEndTime;
		this.taskExecutionTime = taskExecutionTime;
	}
	
	// ---------- GETTERS ---------- //
	
	/**
	 * Returns the free-form textual description of the source of this {@link PerformanceMetric}.
	 * @return the free-form textual description of the source of this {@link PerformanceMetric}.
	 */
	public String getSource() {
		return source;
	}
	
	/**
	 * Returns the date at which the task finished execution as {@link LocalDate} object.
	 * @return the date at which the task finished execution as {@link LocalDate} object.
	 */
	public LocalDate getTaskEndDate() {
		return taskEndDate;
	}
	
	/**
	 * Returns the time at which the task finished execution as {@link LocalTime} object.
	 * @return the time at which the task finished execution as {@link LocalTime} object.
	 */
	public LocalTime getTaskEndTime() {
		return taskEndTime;
	}
	
	/**
	 * Return the duration of the executed task in milliseconds.
	 * @return the duration of the executed task in milliseconds.
	 */
	public long getTaskExecutionTime() {
		return taskExecutionTime;
	}
	
	/**
	 * (OPTIONAL) Returns the username associated with the executed tasks.
	 * @return the username associated with the executed tasks.
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * (OPTIONAL) Returns the number of times the executed task was repeated.
	 * @return the number of times the executed task was repeated.
	 */
	public Integer getRepetitions() {
		return repetitions;
	}
	
	// ---------- SETTERS ---------- //
	
	/**
	 * Sets the free-form textual description of the source of this {@link PerformanceMetric}.
	 * @param source the free-form textual description of the source of this {@link PerformanceMetric}.
	 */
	public void setSource(String source) {
		this.source = source;
	}
	
	/**
	 * Sets the date at which the task finished execution as {@link LocalDate} object.
	 * @param taskEndDate the date at which the task finished execution as {@link LocalDate} object.
	 */
	public void setTaskEndDate(LocalDate taskEndDate) {
		this.taskEndDate = taskEndDate;
	}
	
	/**
	 * Sets the time at which the task finished execution as {@link LocalTime} object.
	 * @param taskEndTime the time at which the task finished execution as {@link LocalTime} object.
	 */
	public void setTaskEndTime(LocalTime taskEndTime) {
		this.taskEndTime = taskEndTime;
	}
	
	/**
	 * Sets the duration of the executed task in milliseconds.
	 * @param taskExecutionTime the duration of the executed task in milliseconds.
	 */
	public void setTaskExecutionTime(long taskExecutionTime) {
		this.taskExecutionTime = taskExecutionTime;
	}
	
	/**
	 * (OPTIONAL) Sets the username associated with the executed tasks.
	 * @param username the username associated with the executed tasks.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * (OPTIONAL) Sets the number of times the executed task was repeated.
	 * @param repetitions the number of times the executed task was repeated.
	 */
	public void setRepetitions(Integer repetitions) {
		this.repetitions = repetitions;
	}
	
}
