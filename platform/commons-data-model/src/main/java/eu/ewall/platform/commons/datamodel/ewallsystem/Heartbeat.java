package eu.ewall.platform.commons.datamodel.ewallsystem;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import eu.ewall.platform.commons.datamodel.core.ieeesumo.Entity;

// TODO: Auto-generated Javadoc
/**
 * The Class Heartbeat.
 *
 * @author emirmos
 */
/**
 * @author eandgrg
 *
 */
public class Heartbeat extends Entity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2850264883116966781L;

	/** The log. */
	private static Logger LOG = LoggerFactory.getLogger(Heartbeat.class);

	/** The name. */
	private String username;

	/** The ip. */
	private String ip;

	/** The update frequency secs. */
	private int updateFrequencySecs;

	/** The date. */
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date date;

	/**
	 * Instantiates a new sensing heartbeat.
	 */
	public Heartbeat() {
	}

	/**
	 * Instantiates a new heartbeat.
	 *
	 * @param username
	 *            the username
	 * @param ip
	 *            the ip
	 * @param updateFrequencySecs
	 *            the update frequency secs
	 */
	public Heartbeat(String username, String ip, int updateFrequencySecs) {
		this.username = username;
		this.ip = ip;
		this.updateFrequencySecs = updateFrequencySecs;
		this.date = new Date();
	}

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
	 * Gets the ip.
	 *
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Sets the ip.
	 *
	 * @param ip
	 *            the new ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date
	 *            the new date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Gets the update frequency secs.
	 *
	 * @return the update frequency secs
	 */
	public int getUpdateFrequencySecs() {
		return updateFrequencySecs;
	}

	/**
	 * Sets the update frequency secs.
	 *
	 * @param updateFrequencySecs
	 *            the new update frequency secs
	 */
	public void setUpdateFrequencySecs(int updateFrequencySecs) {
		this.updateFrequencySecs = updateFrequencySecs;
	}

}
