package eu.ewall.platform.idss.utils.exception;

/**
 * This exception is thrown when some content has an invalid format for a
 * parser.
 * 
 * @author Dennis Hofs (RRD)
 */
public class ParseException extends Exception {
	private static final long serialVersionUID = 2827411288488428951L;

	public ParseException(String message) {
		super(message);
	}

	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
