package eu.ewall.fusioner.fitbit.api;

/**
 * This exception is thrown when Fitbit returns an oauth error for the access
 * token. This may happen because the access token needs to be refreshed.
 * 
 * @author Dennis Hofs (RRD)
 */
public class FitbitAccessTokenException extends Exception {
	private static final long serialVersionUID = 1L;

	public FitbitAccessTokenException(String message) {
		super(message);
	}

	public FitbitAccessTokenException(String message, Throwable cause) {
		super(message, cause);
	}
}
