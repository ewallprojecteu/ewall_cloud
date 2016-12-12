package eu.ewall.fusioner.fitbit.exception;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception results in a HTTP response with status 500 Internal Server
 * Error. The exception message (default "Internal Server Error") will be
 * written to the response. It is handled by the {@link ErrorController
 * ErrorController}.
 * 
 * @author Dennis Hofs (RRD)
 */
@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends HttpException {
	private static final long serialVersionUID = 1L;

	public InternalServerErrorException() {
		super("Internal Server Error");
	}

	public InternalServerErrorException(String message) {
		super(message);
	}
}
