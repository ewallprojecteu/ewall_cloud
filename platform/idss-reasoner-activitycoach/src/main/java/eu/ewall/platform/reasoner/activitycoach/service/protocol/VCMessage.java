package eu.ewall.platform.reasoner.activitycoach.service.protocol;

/**
 * Generic, abstract Virtual Coach Message in the Virtual Coach communication
 * protocol. The content/payload of specific communication messages are defined
 * in the classes that extend this class.
 * 
 * @author Harm op den Akker, RRD
 */
public abstract class VCMessage {
	
	private String type;
	private String subType;
	
	// ---------- Constructors ---------- //
	
	/**
	 * Creates an empty instance of a {@link VCMessage}.
	 */
	public VCMessage() { }
	
	/**
	 * Creates an instance of a {@link VCMessage} with given type and sub-type.
	 * @param type the top-level type of this {@link VCMessage}.
	 * @param subType the sub-type of this {@link VCMessage}.
	 */
	public VCMessage(String type, String subType) {
		this.type = type;
		this.subType = subType;
	}
	
	// ---------- Getters ----------- //
	
	/**
	 * Returns the top-level type of this {@link VCMessage}.
	 * @return the top-level type of this {@link VCMessage}.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Returns the sub-type of this {@link VCMessage}.
	 * @return the sub-type of this {@link VCMessage}.
	 */
	public String getSubType() {
		return subType;
	}
	
	// ---------- Setters ---------- //
	
	/**
	 * Sets the top-level type of this {@link VCMessage}.
	 * @param type the top-level type of this {@link VCMessage}.
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Sets the sub-type of this {@link VCMessage}.
	 * @param subType the sub-type of this {@link VCMessage}.
	 */
	public void setSubType(String subType) {
		this.subType = subType;
	}
}
