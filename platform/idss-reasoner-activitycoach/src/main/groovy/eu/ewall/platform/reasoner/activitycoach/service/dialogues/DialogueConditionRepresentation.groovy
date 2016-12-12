package eu.ewall.platform.reasoner.activitycoach.service.dialogues

import java.util.List;

class DialogueConditionRepresentation {
	String conditionType
	List<String> parameters = []
	
	def conditionType(conditionType) {
		this.conditionType = conditionType
		this
	}
	
	def parameter(parameter) {
		parameters << parameter
		this
	}
}
