package eu.ewall.platform.reasoner.activitycoach.service.dialogues

import java.util.List;

abstract class DialogueReplyRepresentation {
	String replyId
	
	/**
	 * List of actions that should be performed when this button is clicked.
	 */
	List<DialogueActionRepresentation> actions = []
	
	/**
	 * List of conditions that should be met before this reply becomes available.
	 */
	List<DialogueConditionRepresentation> conditions = []
	
	
	def replyId(replyId) {
		this.replyId = replyId
		this
	}
	
	def action(closure) {
		def action = new DialogueActionRepresentation()
		closure.delegate = action
		closure.resolveStrategy = Closure.DELEGATE_ONLY
		closure()
		actions << action
		this
	}
	
	def condition(closure) {
		def condition = new DialogueConditionRepresentation()
		closure.delegate = condition
		closure.resolveStrategy = Closure.DELEGATE_ONLY
		closure()
		conditions << condition
		this
	}
}
