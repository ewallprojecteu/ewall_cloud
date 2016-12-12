package eu.ewall.platform.idss.utils.http;

/**
 * This exception is thrown when a HTTP request results in an error response.
 * 
 * @author Dennis Hofs (RRD)
 */
public class HttpClientException extends Exception {
	private static final long serialVersionUID = 1L;

	private int statusCode;
	private String statusMessage;
	private String errorContent;

	/**
	 * Constructs a new HTTP client exception.
	 * 
	 * @param statusCode the HTTP status code
	 * @param statusMessage the HTTP status message
	 * @param errorContent the error string from the HTTP content
	 */
	public HttpClientException(int statusCode, String statusMessage,
			String errorContent) {
		super(statusCode + " " + statusMessage + ": " + errorContent);
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.errorContent = errorContent;
	}

	/**
	 * Returns the HTTP status code.
	 * 
	 * @return the HTTP status code
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Returns the HTTP status message.
	 * 
	 * @return the HTTP status message
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * Returns the error string from the HTTP content.
	 * 
	 * @return the error string from the HTTP content
	 */
	public String getErrorContent() {
		return errorContent;
	}
}
