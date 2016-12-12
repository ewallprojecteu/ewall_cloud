package eu.ewall.platform.reasoner.activitycoach.service.protocol;

public class VCMessageError extends VCMessage {
	
	private String userFriendlyMessage;
	
	// ---------- Constructors ---------- //
	
	public VCMessageError(String subType) {
		super(VCMessageConstants.MESSAGE_TYPE_ERROR,subType);
	}
	
	public VCMessageError(String subType, String userFriendlyMessage) {
		super(VCMessageConstants.MESSAGE_TYPE_ERROR,subType);
		this.userFriendlyMessage = userFriendlyMessage;
	}
	
	// ---------- Getters ---------- //
	
	public String getUserFriendlyMessage() {
		return userFriendlyMessage;
	}
	
	// ---------- Setters ---------- //
	
	public void setUserFriendlyMessage(String userFriendlyMessage) {
		this.userFriendlyMessage = userFriendlyMessage;
	}
	

}
