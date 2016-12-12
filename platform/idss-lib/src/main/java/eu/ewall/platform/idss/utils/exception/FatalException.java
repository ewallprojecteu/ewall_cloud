package eu.ewall.platform.idss.utils.exception;

/**
 * This exception indicates a fatal error that should cause a service to stop.
 * 
 * @author Dennis Hofs (RRD)
 */
public class FatalException extends Exception {
	private static final long serialVersionUID = 785341435736659111L;

	public FatalException(String message) {
		super(message);
	}

	public FatalException(String message, Throwable cause) {
		super(message, cause);
	}
}
