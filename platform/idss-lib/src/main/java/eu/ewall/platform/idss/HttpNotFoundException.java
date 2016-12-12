package eu.ewall.platform.idss;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is mapped to HTTP status code 404 (Not Found) when thrown in
 * a Spring request.
 * 
 * @author Dennis Hofs (RRD)
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class HttpNotFoundException extends Exception {
	private static final long serialVersionUID = -3025961350244727511L;

	public HttpNotFoundException(String message) {
		super(message);
	}

	public HttpNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
