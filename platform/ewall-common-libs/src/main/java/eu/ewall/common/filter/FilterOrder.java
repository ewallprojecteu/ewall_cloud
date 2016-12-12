package eu.ewall.common.filter;

/**
 * Defines eWall common filters ordering.
 */
public class FilterOrder {

	/** The Constant CORS. */
	// Make sure CORS filtering always happens first
	public static final int CORS = 0;
	
	/** The Constant JWT. */
	public static final int JWT = 1;
	
}
