/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.profile;

import java.io.Serializable;

/**
 * User SubProfile to be used as a parent for different User SubProfile
 * implementations.
 * 
 * @author eandgrg
 */
public abstract class UserSubProfile implements Serializable {

	/**
	 * Default serial version uid, will be inherited in all (sub)classes.
	 */
	private static final long serialVersionUID = 2796600682433012073L;

}