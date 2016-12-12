package eu.ewall.platform.notificationmanager;

public class NotificationContentFeedback {
	private String type;
	private String label;
	private String min;
	private String max;
	private String url;
	
	public NotificationContentFeedback() {
	}
	
	public NotificationContentFeedback(String type, String label, String min, String max, String url) {
		this.type = type;
		this.label = label;
		this.min = min;
		this.max = max;
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}
	
	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
