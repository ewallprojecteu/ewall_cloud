package eu.ewall.platform.commons.datamodel.profile;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * @author eandgrg
 */
public enum UserRole {
	/** The system itself. */
	SYSTEM,
	/** The system administrator. */
	ADMINISTRATOR,
	/** The Region administrator. */
	REGION_ADMINISTRATOR,
	/** Primary (end) user. */
	PRIMARY_USER,
	/** The informal caregiver. */
	INFORMAL_CAREGIVER,
	/** The formal caregiver. */
	FORMAL_CAREGIVER

}