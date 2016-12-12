package eu.ewall.platform.reasoner.activitycoach.service.dialogues

class DialogueStepRepresentation {
	String stepId
	List<String> statements = []
	List<DialogueReplyRepresentation> replies = []
	
	def stepId(stepId) {
		this.stepId = stepId
		this
	}
	
	def statement(statement) {
		statements << statement
		this
	}
	
	def basicReply(closure) {
		def basicReply = new DialogueBasicReplyRepresentation()
		closure.delegate = basicReply
		closure.resolveStrategy = Closure.DELEGATE_ONLY
		closure()
		replies << basicReply
		this
	}
	
	def rangeReply(closure) {
		def rangeReply = new DialogueRangeReplyRepresentation()
		closure.delegate = rangeReply
		closure.resolveStrategy = Closure.DELEGATE_ONLY
		closure()
		replies << rangeReply
		this
	}
	
	DialogueReplyRepresentation getReply(String replyId) {
		replies.find {
			it.replyId == replyId
		}
	}
	
	String getNextStepId(userAct) {
		def buttonData = userAct.userData.find {
			it.widgetType == 'button'
		}
		if (buttonData == null)
			return null
		def button = replies.find {
			it.replyId == buttonData.widgetId
		}
		button.getNextStepId(userAct)
	}
}
