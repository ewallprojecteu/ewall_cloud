package eu.ewall.platform.reasoner.activitycoach.service.protocol;

/**
 * A message definition in the Virtual Coach communication protocol.
 * <br><br>
 * A {@link VCMessageContent} message contains the content for a Physical Activity
 * Motivational Message (PAMM) as a set of {@link VCStatement}s and the possible
 * replies that the user can give as a set of {@link VCResponse}s.
 * 
 * @author Harm op den Akker, RRD
 */
public class VCMessageContent extends VCMessage {
	
	private VCStatement[] statements;
	private VCResponse[] responses;
	
	// ---------- Getters ---------- //
	
	public VCStatement[] getStatements() {
		return statements;
	}
	
	public VCResponse[] getResponses() {
		return responses;
	}
	
	// ---------- Setters ---------- //
	
	public void setStatements(VCStatement[] statements) {
		this.statements = statements;
	}
	
	public void setResponses(VCResponse[] responses) {
		this.responses = responses;
	}

}
