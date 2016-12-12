package eu.ewall.platform.idss.service.exception;

/**
 * This exception is thrown when a request is made for a user that doesn't
 * exist.
 * 
 * @author Dennis Hofs (RRD)
 */
public class UserNotFoundException extends Exception {
	private static final long serialVersionUID = 2950362487865369715L;

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
