package eu.ewall.platform.reasoner.activitycoach;

import com.fasterxml.jackson.annotation.JsonInclude;

public class NotificationContentFeedback {
	private String type;
	private String url;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String label = null;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer min = null;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer max = null;
	
	public NotificationContentFeedback() {
	}
	
	public static NotificationContentFeedback createButton(String label,
			String url) {
		NotificationContentFeedback result = new NotificationContentFeedback();
		result.type = "button";
		result.label = label;
		result.url = url;
		return result;
	}
	
	public static NotificationContentFeedback createRange(int min, int max,
			String url) {
		NotificationContentFeedback result = new NotificationContentFeedback();
		result.type = "range";
		result.min = min;
		result.max = max;
		result.url = url;
		return result;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}
}
