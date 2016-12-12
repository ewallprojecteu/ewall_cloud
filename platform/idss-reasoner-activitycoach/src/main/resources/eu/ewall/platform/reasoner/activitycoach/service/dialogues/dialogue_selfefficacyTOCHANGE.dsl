dialogueTypeId 'SelfEfficacy'

firstStep {
	id 'SelfEfficacy.Initial'
	statement "Do you have some time to talk about your physical activity?"
	statement "Can you answer a question about your physical activity?"
	statement "Do you mind answering a question about your physical activity?"
	
	button {
		id 'SelfEfficacy.Initial.1'
		text 'Yes'
		nextStepId 'SelfEfficacy.Question1'
	}
	
	button {
		id 'SelfEfficacy.Initial.2'
		text 'No'
	}
}

step {
	id 'SelfEfficacy.Question1'
	statement "On a scale of 0-100 how confident are you that you would be able to exercise when you are lacking of time?"
	
	range {
		id 'SelfEfficacy.Question1.Range'
		range 0, 100
	}
	
	button {
		id 'SelfEfficacy.Question1.Button'
		text 'OK'
		nextStepId 'SelfEfficacy.Question2'
	}
}

step {
	id 'SelfEfficacy.Question2'
	statement "On a scale of 0-100 how confident are you that you would be able to exercise when you lack energy?"
	
	range {
		id 'SelfEfficacy.Question2.Range'
		range 0, 100
	}
	
	button {
		id 'SelfEfficacy.Question2.Button'
		text 'OK'
		nextStepId 'SelfEfficacy.Question3'
	}
}

step {
	id 'SelfEfficacy.Question3'
	statement "On a scale of 0-100 how confident are you that you would be able to exercise when you feel discomfort?"
	
	range {
		id 'SelfEfficacy.Question3.Range'
		range 0, 100
	}
	
	button {
		id 'SelfEfficacy.Question3.Button'
		text 'OK'
		nextStepId 'SelfEfficacy.Question4'
	}
}

step {
	id 'SelfEfficacy.Question4'
	statement "On a scale of 0-100 how confident are you that you would be able to exercise when the weather is not good (for example rain or snow)?"
	
	range {
		id 'SelfEfficacy.Question4.Range'
		range 0, 100
	}
	
	button {
		id 'SelfEfficacy.Question4.Button'
		text 'OK'
		nextStepId 'SelfEfficacy.Question5'
	}
}

step {
	id 'SelfEfficacy.Question5'
	statement "On a scale of 0-100 how confident are you that you would be able to include exercise in your daily routine?"
	
	range {
		id 'SelfEfficacy.Question5.Range'
		range 0, 100
	}
	
	button {
		id 'SelfEfficacy.Question5.Button'
		text 'OK'
		nextStepId 'SelfEfficacy.Question6'
	}
}

step {
	id 'SelfEfficacy.Question6'
	statement "On a scale of 0-100 how confident are you that you would be able to consistently exercise three times per week?"
	
	range {
		id 'SelfEfficacy.Question6.Range'
		range 0, 100
	}
	
	button {
		id 'SelfEfficacy.Question6.Button'
		text 'OK'
		nextStepId 'SelfEfficacy.Question7'
	}
}

step {
	id 'SelfEfficacy.Question7'
	statement "On a scale of 0-100 how confident are you that you would be able to arrange your schedule to include regular exercise?"
	
	range {
		id 'SelfEfficacy.Question7.Range'
		range 0, 100
	}
	
	button {
		id 'SelfEfficacy.Question7.Button'
		text 'OK'
		nextStepId 'SelfEfficacy.Question8'
	}
}

step {
	id 'SelfEfficacy.Question8'
	statement "On a scale of 0-100 how confident are you that you would be able to exercise when you have a lot of things to do?"
	
	range {
		id 'SelfEfficacy.Question8.Range'
		range 0, 100
	}
	
	button {
		id 'SelfEfficacy.Question8.Button'
		text 'OK'
		nextStepId 'Final.SelfEfficacy'
		action { user, dialogueActs ->
			writeSelfEfficacy user, dialogueActs
		}
	}
}

step {
	id 'Final.SelfEfficacy'
	statement "Thank you!"
	statement "Thank you very much!"
	statement "Thank you for your time!"
	statement "Thank you so much!"
	
	button {
		id 'Final.SelfEfficacy.1'
		text 'OK'
	}
}
