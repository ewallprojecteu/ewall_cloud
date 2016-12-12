package eu.ewall.platform.idss.service.model.common.dialogue;

import java.util.List;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

public class DialogueCondition extends AbstractDatabaseObject{

	@DatabaseField(value=DatabaseType.STRING)
	private String conditionType;
	
	@DatabaseField(value=DatabaseType.LIST, elemType=DatabaseType.STRING)
	private List<String> parameters;
	
	// ---------- Getters ---------- //
	
	/**
	 * Returns the type of this {@link DialogueCondition}.
	 * @return the type of this {@link DialogueCondition}.
	 */
	public String getConditionType() {
		return conditionType;
	}
	
	/**
	 * Returns the list of parameters, as {@link String}s, of this {@link DialogueCondition}.
	 * @return the list of parameters, as {@link String}s, of this {@link DialogueCondition}.
	 */
	public List<String> getParameters() {
		return parameters;
	}
	
	// ---------- Setters ---------- //
	
	/**
	 * Sets the type of this {@link DialogueCondition}.
	 * @param conditionType the type of this {@link DialogueCondition}.
	 */
	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}
	
	/**
	 * Sets the list of parameters, as {@link String}s, of this {@link DialogueCondition}.
	 * @param parameters the list of parameters, as {@link String}s, of this {@link DialogueCondition}.
	 */
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}	
	
}
