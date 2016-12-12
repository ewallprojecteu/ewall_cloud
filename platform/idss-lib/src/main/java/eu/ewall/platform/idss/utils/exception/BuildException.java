package eu.ewall.platform.idss.utils.exception;

/**
 * This exception is thrown if an error occurs while building an object through
 * Java reflection.
 * 
 * @author Dennis Hofs (RRD)
 */
public class BuildException extends Exception {
	private static final long serialVersionUID = 6927169045026491569L;

	public BuildException(String message) {
		super(message);
	}

	public BuildException(String message, Throwable cause) {
		super(message, cause);
	}
}
