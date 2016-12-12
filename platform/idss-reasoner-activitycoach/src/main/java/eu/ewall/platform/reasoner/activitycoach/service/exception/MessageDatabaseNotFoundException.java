package eu.ewall.platform.reasoner.activitycoach.service.exception;

/**
 * This exception is thrown when a message database could not be found.
 * 
 * @author Harm op den Akker (RRD)
 */
public class MessageDatabaseNotFoundException extends Exception {

	private static final long serialVersionUID = 8014488005200564310L;

	public MessageDatabaseNotFoundException(String message) {
		super(message);
	}

	public MessageDatabaseNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}