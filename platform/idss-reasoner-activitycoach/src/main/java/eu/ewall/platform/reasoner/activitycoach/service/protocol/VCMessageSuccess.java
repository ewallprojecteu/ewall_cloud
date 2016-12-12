package eu.ewall.platform.reasoner.activitycoach.service.protocol;

public class VCMessageSuccess extends VCMessage {
	
	public VCMessageSuccess(String subType) {
		super(VCMessageConstants.MESSAGE_TYPE_SUCCESS,subType);
	}

}
