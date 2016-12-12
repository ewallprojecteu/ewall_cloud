package eu.ewall.platform.idss.service.exception;

/**
 * 
 * @author Harm op den Akker, RRD
 */
public class DialogueNotFoundException extends Exception{

	private static final long serialVersionUID = -2125277349871311610L;

	public DialogueNotFoundException(String message) {
		super(message);
	}

	public DialogueNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
