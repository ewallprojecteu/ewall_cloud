package eu.ewall.platform.idss.dao;

/**
 * This exception is thrown when any database error occurs.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DatabaseException extends Exception {
	private static final long serialVersionUID = 1205649302013465454L;

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}
}
