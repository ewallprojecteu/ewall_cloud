package eu.ewall.fusioner.fitbit.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import eu.ewall.platform.idss.utils.json.JsonObject;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FitbitError extends JsonObject {
	private String errorType;
	private String fieldName;
	private String message;

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
