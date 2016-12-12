package eu.ewall.servicebrick.logicgames.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultId {

	private String resultId;
	
	@JsonCreator
	public ResultId(@JsonProperty("resultId") String resultId) {
		this.resultId = resultId;
	}

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

}
