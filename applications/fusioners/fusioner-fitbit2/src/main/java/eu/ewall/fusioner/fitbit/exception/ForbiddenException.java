package eu.ewall.fusioner.fitbit.exception;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception results in a HTTP response with status 403 Forbidden. The
 * exception message (default "Forbidden") will be written to the response.
 * It is handled by the {@link ErrorController ErrorController}.
 * 
 * @author Dennis Hofs (RRD)
 */
@ResponseStatus(value=HttpStatus.FORBIDDEN)
public class ForbiddenException extends HttpException {
	private static final long serialVersionUID = 1L;

	public ForbiddenException() {
		super("Forbidden");
	}

	public ForbiddenException(String message) {
		super(message);
	}
}
