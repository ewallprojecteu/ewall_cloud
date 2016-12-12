package eu.ewall.fusioner.fitbit.exception;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception results in a HTTP response with status 400 Bad Request. The
 * exception message (default "Bad Request") will be written to the response.
 * It is handled by the {@link ErrorController ErrorController}.
 * 
 * @author Dennis Hofs (RRD)
 */
@ResponseStatus(value=HttpStatus.BAD_REQUEST)
public class BadRequestException extends HttpException {
	private static final long serialVersionUID = 1L;
	
	public BadRequestException() {
		super("Bad Request");
	}

	public BadRequestException(String message) {
		super(message);
	}
}
