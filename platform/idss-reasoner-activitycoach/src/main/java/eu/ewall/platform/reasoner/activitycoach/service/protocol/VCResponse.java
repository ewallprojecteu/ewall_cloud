package eu.ewall.platform.reasoner.activitycoach.service.protocol;

public class VCResponse {
	
	private String type;
	private String label;
	private String action;
	private String url;
	
	// ---------- Getters ---------- //
	
	public String getType() {
		return type;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getAction() {
		return action;
	}
	
	public String getUrl() {
		return url;
	}
	
	// ---------- Setters ---------- //
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
}
