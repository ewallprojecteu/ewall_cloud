package eu.ewall.fusioner.fitbit.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import eu.ewall.platform.idss.utils.json.JsonObject;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FitbitErrorResponse extends JsonObject {
	private List<FitbitError> errors;

	public List<FitbitError> getErrors() {
		return errors;
	}

	public void setErrors(List<FitbitError> errors) {
		this.errors = errors;
	}
}
