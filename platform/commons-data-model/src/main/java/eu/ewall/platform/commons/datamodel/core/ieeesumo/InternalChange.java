package eu.ewall.platform.commons.datamodel.core.ieeesumo;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * Processes which involve altering an internal property of an Object, e.g. the
 * shape of the Object, its coloring, its structure, etc. Processes that are not
 * instances of this class include changes that only affect the relationship to
 * other objects, e.g. changes in spatial or temporal location.
 * 
 * @author eandgrg
 */
public abstract class InternalChange extends Process {

	/**
	 * The Constructor.
	 */
	public InternalChange() {

	}

}