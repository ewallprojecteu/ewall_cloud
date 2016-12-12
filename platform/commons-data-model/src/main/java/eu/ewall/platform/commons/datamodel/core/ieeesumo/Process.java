package eu.ewall.platform.commons.datamodel.core.ieeesumo;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

/**
 * Intuitively, the class of things that happen and have temporal parts or
 * stages. Examples include extended events like a football match or a race,
 * actions like Pursuing and Reading, and biological processes. The formal
 * definition is: anything that lasts for a time but is not an Object. Note that
 * a Process may have participants 'inside' it which are Objects, such as the
 * players in a football match. In a 4D ontology, a Process is something whose
 * spatiotemporal extent is thought of as dividing into temporal stages roughly
 * perpendicular to the time-axis.
 * 
 * @author eandgrg
 */
public abstract class Process extends Physical {

	/**
	 * The Constructor.
	 */
	public Process() {

	}

}