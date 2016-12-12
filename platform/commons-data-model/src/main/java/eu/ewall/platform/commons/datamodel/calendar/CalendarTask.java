package eu.ewall.platform.commons.datamodel.calendar;

import java.util.Date;

/**
 * The Class CalendarTask.
 */
public class CalendarTask {

    /** The username. */
    private String username;

    /** The task. */
    private String task;

    /** The start date. */
    private Date startDate;

    /** The end date. */
    private Date endDate;

    /** The title. */
    private String title;

    /** The all day. */
    private boolean allDay;

    /** The id. */
    private String id;

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
	return username;
    }

    /**
     * Sets the username.
     *
     * @param username
     *            the new username
     */
    public void setUsername(String username) {
	this.username = username;
    }

    /**
     * Gets the task.
     *
     * @return the task
     */
    public String getTask() {
	task = toString();
	return task;
    }
    // public void setTask(String task) {
    // this.task = task;
    // }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * Sets the title.
     *
     * @param title
     *            the new title
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * Gets the all day.
     *
     * @return the all day
     */
    public boolean getAllDay() {
	return allDay;
    }

    /**
     * Sets the all day.
     *
     * @param allDay
     *            the new all day
     */
    public void setAllDay(boolean allDay) {
	this.allDay = allDay;
    }

    /**
     * Gets the start date.
     *
     * @return the start date
     */
    public Date getStartDate() {
	return startDate;
    }

    /**
     * Sets the start date.
     *
     * @param startDate
     *            the new start date
     */
    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    /**
     * Gets the end date.
     *
     * @return the end date
     */
    public Date getEndDate() {
	return endDate;
    }

    /**
     * Sets the end date.
     *
     * @param endDate
     *            the new end date
     */
    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
	StringBuilder sb = new StringBuilder();

	sb.append(getId()).append(",");
	sb.append(getTitle()).append(",");
	sb.append(getAllDay()).append(",");
	sb.append(getStartDate()).append(",");
	sb.append(getEndDate()).append(",");

	return sb.toString();
    }
}