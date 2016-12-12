package eu.ewall.fusioner.fitbit.exception;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Base class for exceptions that result in a HTTP error response. Subclasses
 * should be annotated with {@link ResponseStatus ResponseStatus}. They are
 * handled by {@link ErrorController ErrorController}.
 * 
 * @author Dennis Hofs (RRD)
 */
public abstract class HttpException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new HTTP exception with default error code 0.
	 * 
	 * @param message the error message
	 */
	public HttpException(String message) {
		super(message);
	}
}
