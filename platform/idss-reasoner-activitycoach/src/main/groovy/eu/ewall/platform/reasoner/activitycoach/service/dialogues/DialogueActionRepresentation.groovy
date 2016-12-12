package eu.ewall.platform.reasoner.activitycoach.service.dialogues

class DialogueActionRepresentation {
	String actionType
	List<String> parameters = []
	String nextStepIdSuccess
	String nextStepIdFailure
	
	def actionType(actionType) {
		this.actionType = actionType
		this
	}
	
	def parameter(parameter) {
		parameters << parameter
		this
	}
	
	def nextStepIdSuccess(nextStepIdSuccess) {
		this.nextStepIdSuccess = nextStepIdSuccess
		this
	}
	
	def nextStepIdFailure(nextStepIdFailure) {
		this.nextStepIdFailure = nextStepIdFailure
		this
	}
}
