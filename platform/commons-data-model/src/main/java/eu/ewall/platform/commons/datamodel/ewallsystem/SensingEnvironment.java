/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.ewallsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.ewall.platform.commons.datamodel.core.ieeesumo.Entity;
import eu.ewall.platform.commons.datamodel.location.IndoorPlace;

/**
 * The Class SensingEnvironment.
 *
 * @author emirmos
 */
public class SensingEnvironment extends Entity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 262435773885514211L;

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(SensingEnvironment.class);

	/** The name. */
	protected String name;

	/** The status. */
	protected RegistrationStatus registrationStatus = RegistrationStatus.NOT_REGISTERED;

	/** The list of indoor places/rooms in the sensing environment. */
	protected List<IndoorPlace> indoorPlaces = new ArrayList<IndoorPlace>();

	/** The point of contact. */
	protected PointOfContact pointOfContact;

	/** Primary user username of this sensing environment. */
	protected String primaryUser;

	/** The (unique) region name. */
	private String regionName;

	/** The enabled. */
	private boolean enabled;

	/**
	 * Instantiates a new sensing environment.
	 */
	public SensingEnvironment() {
	}

	/**
	 * Instantiates a new sensing environment.
	 *
	 * @param name
	 *            the name
	 */
	public SensingEnvironment(String name) {
		this.name = name;
	}

	/**
	 * Instantiates a new sensing environment.
	 *
	 * @param uuid
	 *            the uuid
	 */
	public SensingEnvironment(UUID uuid) {
		super(uuid);
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the registration status.
	 *
	 * @return the registrationStatus
	 */
	public RegistrationStatus getRegistrationStatus() {
		return registrationStatus;
	}

	/**
	 * Sets the registration status.
	 *
	 * @param registrationStatus
	 *            the registrationStatus to set
	 */
	public void setRegistrationStatus(RegistrationStatus registrationStatus) {
		this.registrationStatus = registrationStatus;
	}

	/**
	 * Gets the point of contact.
	 *
	 * @return the pointOfContact
	 */
	public PointOfContact getPointOfContact() {
		return pointOfContact;
	}

	/**
	 * Sets the point of contact.
	 *
	 * @param pointOfContact
	 *            the pointOfContact to set
	 */
	public void setPointOfContact(PointOfContact pointOfContact) {
		this.pointOfContact = pointOfContact;
	}

	/**
	 * Gets the primary user.
	 *
	 * @return the primaryUser
	 */
	public String getPrimaryUser() {
		return primaryUser;
	}

	/**
	 * Sets the primary user.
	 *
	 * @param primaryUser
	 *            the primaryUser to set
	 */
	public void setPrimaryUser(String primaryUser) {
		this.primaryUser = primaryUser;
	}

	/**
	 * Gets the region name.
	 *
	 * @return the regionName
	 */
	public String getRegionName() {
		return regionName;
	}

	/**
	 * Sets the region name.
	 *
	 * @param regionName
	 *            the regionName to set
	 */
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets the enabled.
	 *
	 * @param enabled
	 *            the new enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Gets the uuid.
	 *
	 * @return unique identifier
	 */
	public UUID getUuid() {
		UUID uuid = null;
		try {
			uuid = UUID.fromString(super.getObjectId());
		} catch (Exception e) {
			LOG.error("trying to convert string id to uuid. Error: "
					+ e.getMessage());
		}
		return uuid;
	}

	/**
	 * Sets the uuid.
	 *
	 * @param uuid
	 *            the new uuid
	 */
	public void setUuid(UUID uuid) {
		super.setObjectId(uuid.toString());
	}

	/**
	 * Gets the indoor places.
	 *
	 * @return the indoorPlaces
	 */
	public List<IndoorPlace> getIndoorPlaces() {
		return indoorPlaces;
	}

	/**
	 * Sets the indoor places.
	 *
	 * @param indoorPlaces
	 *            the indoorPlaces to set
	 */
	public void setIndoorPlaces(List<IndoorPlace> indoorPlaces) {
		this.indoorPlaces = indoorPlaces;
	}

	/**
	 * Adds the indoor place.
	 *
	 * @param indoorPlace
	 *            the indoor place
	 */
	public void addIndoorPlace(IndoorPlace indoorPlace) {
		this.indoorPlaces.add(new IndoorPlace(indoorPlace));
	}

	/**
	 * Gets the indoor place names.
	 *
	 * @return the indoor place names
	 */
	@JsonIgnore
	public List<String> getIndoorPlaceNames() {
		List<String> indoorPlacesNames = new ArrayList<String>();
		for (IndoorPlace indoorPlace : this.indoorPlaces) {
			indoorPlacesNames.add(indoorPlace.getName());
		}
		return indoorPlacesNames;
	}

}
