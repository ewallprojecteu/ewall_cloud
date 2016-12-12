package eu.ewall.platform.reasoner.activitycoach.service.exception;

/**
 * This exception is thrown when a request is made for a message that doesn't
 * exist.
 * 
 * @author Dennis Hofs
 */
public class MessageNotFoundException extends Exception {
	private static final long serialVersionUID = 7449747287069603334L;

	public MessageNotFoundException(String message) {
		super(message);
	}

	public MessageNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
