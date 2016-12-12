package eu.ewall.platform.idss.service.exception;

/**
 * 
 * @author Harm op den Akker, RRD
 */
public class InvalidDialogueReplyException extends Exception{

	private static final long serialVersionUID = -8918107658808556144L;

	public InvalidDialogueReplyException(String message) {
		super(message);
	}

	public InvalidDialogueReplyException(String message, Throwable cause) {
		super(message, cause);
	}
}
