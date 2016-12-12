package eu.ewall.platform.idss.service.model.common.dialogue;

import java.util.List;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

/**
 * A {@link DialogueAction} is a representation of an action that should
 * be performed following the selection/choice of a {@link DialogueReply} from
 * the user. Any {@link DialogueReply} can have one or multiple {@link DialogueAction}s
 * attached to it.
 * 
 * @author Harm op den Akker (Roessingh Research and Development)
 */
public class DialogueAction extends AbstractDatabaseObject {

	@DatabaseField(value=DatabaseType.STRING)
	private String actionType;
	
	@DatabaseField(value=DatabaseType.LIST, elemType=DatabaseType.STRING)
	private List<String> parameters;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String nextStepIdSuccess;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String nextStepIdFailure;
	
	// ---------- Getters ---------- //
	
	/**
	 * Returns the type of this {@link DialogueAction}.
	 * @return the type of this {@link DialogueAction}.
	 */
	public String getActionType() {
		return actionType;
	}
	
	/**
	 * Returns the list of parameters, as {@link String}s, of this {@link DialogueAction}.
	 * @return the list of parameters, as {@link String}s, of this {@link DialogueAction}.
	 */
	public List<String> getParameters() {
		return parameters;
	}
	
	/**
	 * Returns what the next step of the dialogue should be in case the action executes successfully.
	 * @return what the next step of the dialogue should be in case the action executes successfully.
	 */
	public String getNextStepIdSuccess() {
		return nextStepIdSuccess;
	}
	
	/**
	 * Returns what the next step of the dialogue should be in case the action fails to execute.
	 * @return what the next step of the dialogue should be in case the action fails to execute.
	 */
	public String getNextStepIdFailure() {
		return nextStepIdFailure;
	}
	
	// ---------- Setters ---------- //
	
	/**
	 * Sets the type of this {@link DialogueAction}.
	 * @param actionType the type of this {@link DialogueAction}.
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
	/**
	 * Sets the list of parameters, as {@link String}s, of this {@link DialogueAction}.
	 * @param parameters the list of parameters, as {@link String}s, of this {@link DialogueAction}.
	 */
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Sets what the next step of the dialogue should be in case the action executes successfully.
	 * @param nextStepIdSuccess what the next step of the dialogue should be in case the action executes successfully.
	 */
	public void setNextStepIdSuccess(String nextStepIdSuccess) {
		this.nextStepIdSuccess = nextStepIdSuccess;
	}
	
	/**
	 * Sets what the next step of the dialogue should be in case the action fails to execute.
	 * @param nextStepIdFailure what the next step of the dialogue should be in case the action fails to execute.
	 */
	public void setNextStepIdFailure(String nextStepIdFailure) {
		this.nextStepIdFailure = nextStepIdFailure;
	}
}