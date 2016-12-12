package eu.ewall.fusioner.fitbit.api;

/**
 * This exception is thrown when you use an invalid authorization code
 * to get an access token.
 * 
 * @author Dennis Hofs (RRD)
 */
public class FitbitAuthCodeException extends Exception {
	private static final long serialVersionUID = 1L;

	public FitbitAuthCodeException(String message) {
		super(message);
	}

	public FitbitAuthCodeException(String message, Throwable cause) {
		super(message, cause);
	}
}
