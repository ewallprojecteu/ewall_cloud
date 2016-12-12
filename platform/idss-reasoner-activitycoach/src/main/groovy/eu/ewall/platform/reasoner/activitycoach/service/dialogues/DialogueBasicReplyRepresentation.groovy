package eu.ewall.platform.reasoner.activitycoach.service.dialogues

class DialogueBasicReplyRepresentation extends DialogueReplyRepresentation {
	
	/**
	 * Possible reply statements. The dialogue manager will pick a random statement from
	 * this list.
	 */
	List<String> statements = []

	/**
	 * The next step ID. This can be one of the following:
	 * 
	 * <p><ul>
	 * <li>String: a fixed step ID that is always the next step when this
	 * button is clicked.</li>
	 * <li>Closure: a closure that takes one parameter of type DialogueAct,
	 * which is the last user dialogue act, and returns a step ID or null. This
	 * can be used to return a dynamic step ID.</li>
	 * <li>null: if the dialogue ends when this button is clicked.</li>
	 * </ul></p>
	 */
	String nextStepId = null
	
	def statement(statement) {
		statements << statement
		this
	}
	
	def nextStepId(nextStepId) {
		this.nextStepId = nextStepId
		this
	}

}
