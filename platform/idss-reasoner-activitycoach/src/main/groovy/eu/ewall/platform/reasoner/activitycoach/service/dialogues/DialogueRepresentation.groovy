package eu.ewall.platform.reasoner.activitycoach.service.dialogues

class DialogueRepresentation {
	String dialogueTypeId
	String initialStepId
	Map<String,DialogueStepRepresentation> steps = [:]
	
	def dialogueTypeId(dialogueTypeId) {
		this.dialogueTypeId = dialogueTypeId
		this
	}
	
	def initialStepId(initialStepId) {
		this.initialStepId = initialStepId
		this
	}
	
	def step(closure) {
		def step = new DialogueStepRepresentation()
		closure.delegate = step
		closure.resolveStrategy = Closure.DELEGATE_ONLY
		closure()
		steps[step.stepId] = step
		this
	}
	
	DialogueStepRepresentation getDialogueStepById(String stepId) {
		steps[stepId]
	}
	
	DialogueStepRepresentation getNextDialogueStep(DialogueStepRepresentation step, userAct) {
		def nextStepId = step.getNextStepId(userAct)
		nextStepId ? steps[nextStepId] : null
	}
}
