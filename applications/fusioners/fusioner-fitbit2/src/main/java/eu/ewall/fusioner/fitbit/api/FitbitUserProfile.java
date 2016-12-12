package eu.ewall.fusioner.fitbit.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import eu.ewall.platform.idss.utils.json.JsonObject;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FitbitUserProfile extends JsonObject {
	private String timezone = null;

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
}
