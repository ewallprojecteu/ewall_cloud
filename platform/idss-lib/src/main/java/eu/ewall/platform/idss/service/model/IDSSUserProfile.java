package eu.ewall.platform.idss.service.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import eu.ewall.platform.idss.service.model.state.user.UserModel;
import eu.ewall.platform.idss.service.model.type.UnitSystem;

/**
 * User information that is needed for the IDSS services.
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
public class IDSSUserProfile {
	private DateTime updateTime;
	private String username;
	private String role;
	private String firstName;
	private String lastName;	
	private DateTimeZone timeZone;
	private List<Locale> locales;
	private List<String> applicationNames;
	private UnitSystem preferredUnitSystem;
	private Integer age = null;
	private List<String> diagnosis = new ArrayList<String>();
	private Object source;
	
	// ---------- Getters ---------- //
	
	/**
	 * Returns the time at which this {@link IDSSUserProfile} was created.
	 * @return the time at which this {@link IDSSUserProfile} was created.
	 */
	public DateTime getUpdateTime() {
		return updateTime;
	}
	
	/**
	 * Returns the user name.
	 * 
	 * @return the user name
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Returns the role of the user represented by this {@link IDSSUserProfile}.
	 * @return the role of the user represented by this {@link IDSSUserProfile}.
	 */
	public String getRole() {
		return role;
	}
	
	/**
	 * Returns the first name of the user.
	 * @return the first name of the user.
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Returns the last name of the user.
	 * @return the last name of the user.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns the time zone where the user resides. This should be based on a
	 * location (e.g. Europe/Amsterdam) rather than a GMT offset, because the
	 * former has a variable GMT offset for daylight saving time.
	 * 
	 * @return the time zone
	 */
	public DateTimeZone getTimeZone() {
		return timeZone;
	}

	/**
	 * Returns the locales of the user. There should be at least one locale.
	 * The first locale is the preferred one.
	 * 
	 * @return the locales
	 */
	public List<Locale> getLocales() {
		return locales;
	}
	
	/**
	 * Returns a list of names of applications available to this {@link IDSSUserProfile}.
	 * @return a list of names of applications available to this {@link IDSSUserProfile}.
	 */
	public List<String> getApplicationNames() {
		return applicationNames;
	}

	/**
	 * Returns the preferred {@link UnitSystem} for this user as either {@link UnitSystem#METRIC},
	 * {@link UnitSystem#IMPERIAL}, or {@link UnitSystem#STANDARD}.
	 * @return the preferred {@link UnitSystem} for this user.
	 */
	public UnitSystem getPreferredUnitSystem() {
		return preferredUnitSystem;
	}

	/**
	 * Returns the age. If the age is unknown, this method returns null
	 * (default).
	 * 
	 * @return the age or null
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * Returns the diagnosis list. A diagnosis can be one of:
	 * 
	 * <p><ul>
	 * <li>MCI</li>
	 * <li>COPD_GOLD1</li>
	 * <li>COPD_GOLD2</li>
	 * <li>COPD_GOLD3</li>
	 * <li>COPD_GOLD4</li>
	 * <li>FRAIL</li>
	 * <li>PRE_FRAIL</li>
	 * <li>OTHER</li>
	 * </ul></p>
	 * 
	 * @return the diagnosis list (can be empty)
	 */
	public List<String> getDiagnosis() {
		return diagnosis;
	}

	/**
	 * Returns a source object for this user profile. The class depends on the
	 * application. The source object may contain additional data that can be
	 * used for the application or the {@link UserModel UserModel}.
	 * 
	 * @return the source object
	 */
	public Object getSource() {
		return source;
	}	
	
	// ---------- Setters ---------- //
	
	/**
	 * Sets the time at which this {@link IDSSUserProfile} was created.
	 * @param updateTime the time at which this {@link IDSSUserProfile} was created.
	 */
	public void setUpdateTime(DateTime updateTime) {
		this.updateTime = updateTime;
	}
	
	/**
	 * Sets the user name.
	 * 
	 * @param username the user name
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Sets the role of the user represented by this {@link IDSSUserProfile}.
	 * @param role the role of the user represented by this {@link IDSSUserProfile}.
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	/**
	 * Sets the first name of the user.
	 * @param firstName the first name of the user.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Sets the last name of the user.
	 * @param lastName the last name of the user.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Sets the time zone where the user resides. This should be based on a
	 * location (e.g. Europe/Amsterdam) rather than a GMT offset, because the
	 * former has a variable GMT offset for daylight saving time.
	 * 
	 * @param timeZone the time zone
	 */
	public void setTimeZone(DateTimeZone timeZone) {
		this.timeZone = timeZone;
	}
	
	/**
	 * Sets the locales of the user. There should be at least one locale. The
	 * first locale is the preferred one.
	 * 
	 * @param locales the locales
	 */
	public void setLocales(List<Locale> locales) {
		this.locales = locales;
	}
	
	/**
	 * Sets a list of names of applications available to this {@link IDSSUserProfile}.
	 * @param applicationNames a list of names of applications available to this {@link IDSSUserProfile}.
	 */
	public void setApplicationNames(List<String> applicationNames) {
		this.applicationNames = applicationNames;
	}
	
	/**
	 * Sets the preferred {@link UnitSystem} for this user as either {@link UnitSystem#METRIC},
	 * {@link UnitSystem#IMPERIAL}, or {@link UnitSystem#STANDARD}.
	 * @param preferredUnitSystem the preferred {@link UnitSystem} for this user.
	 */
	public void setPreferredUnitSystem(UnitSystem preferredUnitSystem) {
		this.preferredUnitSystem = preferredUnitSystem;
	}

	/**
	 * Sets the age. If the age is unknown, this can be set to null (default).
	 * 
	 * @param age the age or null
	 */
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * Sets the diagnosis list. See {@link #getDiagnosis() getDiagnosis() for
	 * possible values.
	 * 
	 * @param diagnosis the diagnosis list (can be empty)
	 */
	public void setDiagnosis(List<String> diagnosis) {
		this.diagnosis = diagnosis;
	}
	
	/**
	 * Sets a source object for this user profile. The class depends on the
	 * application. The source object may contain additional data that can be
	 * used for the application or the {@link UserModel UserModel}.
	 * 
	 * @param source the source object
	 */
	public void setSource(Object source) {
		this.source = source;
	}
}
