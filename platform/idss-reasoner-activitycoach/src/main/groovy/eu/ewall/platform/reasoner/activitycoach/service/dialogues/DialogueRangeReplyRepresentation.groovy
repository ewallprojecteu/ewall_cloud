package eu.ewall.platform.reasoner.activitycoach.service.dialogues

class DialogueRangeReplyRepresentation extends DialogueReplyRepresentation {
	int min = 0
	int max = 100
	
	def range(min, max) {
		this.min = min
		this.max = max
		this
	}
}
