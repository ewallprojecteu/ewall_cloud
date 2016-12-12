package eu.ewall.fusioner.fitbit.exception;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception results in a HTTP response with status 401 Unauthorized. The
 * exception message (default "Unauthorized") will be written to the response.
 * It is handled by the {@link ErrorController ErrorController}.
 * 
 * @author Dennis Hofs (RRD)
 */
@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends HttpException {
	private static final long serialVersionUID = 1L;

	public UnauthorizedException() {
		super("Unauthorized");
	}

	public UnauthorizedException(String message) {
		super(message);
	}
}
