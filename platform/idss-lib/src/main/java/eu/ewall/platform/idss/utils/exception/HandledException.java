package eu.ewall.platform.idss.utils.exception;

/**
 * This exception can be thrown to inform the caller that an error occurred,
 * but that has already been handled, for example by writing an error log.
 *
 * @author Dennis Hofs (RRD)
 */
public class HandledException extends Exception {
	private static final long serialVersionUID = -293367753343564260L;
}
