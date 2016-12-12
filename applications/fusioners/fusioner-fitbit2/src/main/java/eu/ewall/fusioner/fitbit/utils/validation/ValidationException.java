package eu.ewall.fusioner.fitbit.utils.validation;

/**
 * This exception is thrown when a validation fails.
 * 
 * @author Dennis Hofs (RRD)
 */
public class ValidationException extends Exception {
	private static final long serialVersionUID = 1L;

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
