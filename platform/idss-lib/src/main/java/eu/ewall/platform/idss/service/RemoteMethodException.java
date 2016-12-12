package eu.ewall.platform.idss.service;

/**
 * This exception is thrown when a remote method returns an error.
 * 
 * @author Dennis Hofs (RRD)
 */
public class RemoteMethodException extends Exception {
	private static final long serialVersionUID = -1636030058645684127L;
	
	public RemoteMethodException(String message) {
		super(message);
	}

	public RemoteMethodException(String message, Throwable cause) {
		super(message, cause);
	}
}
