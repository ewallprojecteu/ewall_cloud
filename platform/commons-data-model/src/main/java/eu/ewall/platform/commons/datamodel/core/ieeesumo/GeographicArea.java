package eu.ewall.platform.commons.datamodel.core.ieeesumo;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * A geographic location, generally having definite boundaries. Note that this
 * differs from its immediate superclass Region in that a GeographicArea is a
 * three-dimensional Region of the earth. Accordingly, all astronomical objects
 * other than earth and all one-dimensional and two-dimensional Regions are not
 * classed under GeographicArea.
 * 
 * @author eandgrg
 */
public abstract class GeographicArea extends Region {

	/**
	 * The Constructor.
	 */
	public GeographicArea() {

	}

}