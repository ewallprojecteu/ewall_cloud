package eu.ewall.platform.idss.response.ewall.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherCondition {

	@JsonProperty("id")
	private String conditionId;
	
	@JsonProperty("main")
	private String conditionCategory;
	
	@JsonProperty("description")
	private String conditionDescription;
	
	@JsonProperty("icon")
	private String conditionIconId;
	
	// ----- GETTERS: -----
	
	public String getConditionId() {
		return conditionId;
	}
	
	public String getConditionCategory() {
		return conditionCategory;
	}
	
	public String getConditionDescription() {
		return conditionDescription;
	}
	
	public String getConditionIconId() {
		return conditionIconId;
	}
	
	// ----- SETTERS: -----
	
	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}
	
	public void setConditionCategory(String conditionCategory) {
		this.conditionCategory = conditionCategory;
	}
	
	public void setConditionDescription(String conditionDescription) {
		this.conditionDescription = conditionDescription;
	}
	
	public void setConditionIconId(String conditionIconId) {
		this.conditionIconId = conditionIconId;
	}
	
}
