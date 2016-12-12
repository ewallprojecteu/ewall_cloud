package eu.ewall.platform.reasoner.activitycoach.service.protocol;

/**
 * A message definition in the Virtual Coach communication protocol.
 * <br><br>
 * A {@link VCMessageAvailable} message indicates that the Virtual Coach
 * has something to say to the user. The specifics of what type of 
 * communication is available depends on the message's subtype ({@link VCMessage#getType()}).
 * 
 * @author Harm op den Akker, RRD
 */
public class VCMessageAvailable extends VCMessage {
	
	private String callbackURL;
	private String content;
	
	// ---------- Constructors ---------- //
	
	/**
	 * Creates an empty instance of a {@link VCMessageAvailable} object.
	 */
	public VCMessageAvailable() { }
	
	/**
	 * Creates an instance of a {@link VCMessageAvailable} object with the given 
	 * type and sub-type values.
	 * @param type the top-level type of this {@link VCMessage}.
	 * @param subType the sub-type of this {@link VCMessage}.
	 */
	public VCMessageAvailable(String type, String subType) {
		super(type,subType);
	}
	
	/**
	 * Creates an instance of a {@link VCMessageAvailable} object with given type,
	 * sub-type, callbackURL, expirationTime and content values.
	 * @param type the top-level type of this {@link VCMessage}.
	 * @param subType the sub-type of this {@link VCMessage}.
	 * @param callbackURL the URL {@link String} that should be called as a response to this message.
	 * @param expirationTime a {@link String} representation (defined in {@link VCMessageConstants#DATE_TIME_FORMAT}) of the 
	 * expiration time for this 'available' message.
	 * @param content a readable description that can be displayed to the user to describe the contents of 
	 * this 'available' message.
	 */
	public VCMessageAvailable(String type, String subType, String callbackURL, String content) {
		super(type,subType);
		this.callbackURL = callbackURL;
		this.content = content;
	}
	
	// ---------- Getters ---------- //
	
	/**
	 * Returns the URL {@link String} that should be called as a response to this message.
	 * @return the URL {@link String} that should be called as a response to this message.
	 */
	public String getCallbackURL() {
		return callbackURL;
	}
	
	/**
	 * Returns a readable description that can be displayed to the user to describe the contents of 
	 * this 'available' message.
	 * @return a readable description that can be displayed to the user to describe the contents of 
	 * this 'available' message.
	 */
	public String getContent() {
		return content;
	}
	
	// ---------- Setters ---------- //
	
	/**
	 * Sets the URL {@link String} that should be called as a response to this message.
	 * @param callbackURL the URL {@link String} that should be called as a response to this message.
	 */
	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}
	
	/**
	 * Sets a readable description that can be displayed to the user to describe the contents of 
	 * this 'available' message.
	 * @param content a readable description that can be displayed to the user to describe the contents of 
	 * this 'available' message.
	 */
	public void setContent(String content) {
		this.content = content;
	}	
}
