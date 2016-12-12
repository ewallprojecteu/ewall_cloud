package eu.ewall.platform.notificationmanager;

public class Statements {
	
	private String text;
	private String type;
	
	public Statements() {
	}
	
	public Statements(String text, String type) 
	{
		this.text = text;
		this.type = type;
	}
	
	public String getText() {
		return text;
	}
	
	public String getType() {
		return type;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setType(String type) {
		this.type = type;
	}

}
