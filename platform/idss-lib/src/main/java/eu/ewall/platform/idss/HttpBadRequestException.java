package eu.ewall.platform.idss;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is mapped to HTTP status code 400 (Bad Request) when thrown
 * in a Spring request.
 * 
 * @author Dennis Hofs (RRD)
 */
@ResponseStatus(value=HttpStatus.BAD_REQUEST)
public class HttpBadRequestException extends Exception {
	private static final long serialVersionUID = -3025961350244727511L;

	public HttpBadRequestException(String message) {
		super(message);
	}

	public HttpBadRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}
