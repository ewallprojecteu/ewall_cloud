// ----- Message Database for default Language 'en' (English)

// ----- Dynamic representation variables usable:
// -----    ${physActState.currentActivitySteps} - Current level of physical activity in steps
// -----    ${physActState.currentActivityBurnedCalories} - Current level of physical activity in burned calories
// -----    ${physActState.currentActivityKilometers} - Current level of physical activity in kilometers
// -----    ${physActState.currentGoalSteps} - Goal for today in steps
// -----    ${physActState.currentGoalBurnedCalories} - Goal for today in burned calories
// -----    ${physActState.currentGoalKilometers} - Goal for today in kilometers traveled
// -----	${physActState.stepsFromGoal} - Absolute difference between current activity and goal
// -----	${physActState.goalPercentageSteps} - Percentage of step goal achieved

// -------------------- Greeting Representations -------------------- //

greetingRepresentations {
	
	greeting {
		id "greeting_da_1"
		withFirstName false
		message "Hej."
		message "Hej!"
	}
	
	greeting {
		id "greeting_da_2"
		withFirstName true
		message "Hej ${userModel.firstName}."
		message "Hej ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_da_3"
		withFirstName false
		dayPart "morning"
		message "Godmorgen."
		message "Godmorgen!"
	}
	
	greeting {
		id "greeting_da_4"
		withFirstName true
		dayPart "morning"
		message "Godmorgen ${userModel.firstName}."
		message "Godmorgen ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_da_5"
		withFirstName false
		dayPart "afternoon"
		message "Goddag."
		message "Goddag!"
	}
	
	greeting {
		id "greeting_da_6"
		withFirstName true
		dayPart "afternoon"
		message "Goddag ${userModel.firstName}."
		message "Goddag ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_da_7"
		withFirstName false
		dayPart "evening"
		message "Godaften."
		message "Godaften!"
	}
	
	greeting {
		id "greeting_da_8"
		withFirstName true
		dayPart "evening"
		message "Godaften ${userModel.firstName}."
		message "Godaften ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_da_9"
		withFirstName false
		dayPart "night"
		message "Godnat."
		message "Godnat!"
	}
	
	greeting {
		id "greeting_da_10"
		withFirstName true
		dayPart "night"
		message "Godnat ${userModel.firstName}."
		message "Godnat ${userModel.firstName}!"
	}
}

// -------------------- Feedback Representations -------------------- //

feedbackRepresentations {
	
	feedback {
		id "feedback_da_1"
		withGoalSetting false
		activityUnit "steps"
		message "Du gik ${physActState.currentActivitySteps} skridt."
		message "Du har gået ${physActState.currentActivitySteps} skridt."
	}
	
	feedback {
		id "feedback_da_2"
		withGoalSetting true
		underGoal true
		activityUnit "steps"
		message "Du gik ${physActState.currentActivitySteps} ud af ${physActState.currentGoalSteps} skridt."
		message "Du har gået ${physActState.currentActivitySteps} ud af ${physActState.currentGoalSteps} skridt."
		message "Du er ${physActState.stepsFromGoal} skridt fra at nå dit mål."
		message "Du er nu ${physActState.stepsFromGoal} skridt fra at nå dit mål."
		message "I øjeblikket er du ${physActState.stepsFromGoal} skridt fra at nå dit mål."
		message "Nu er du ${physActState.stepsFromGoal} skridt fra at nå dit mål."
		message "Du er ${physActState.stepsFromGoal} skridt fra dit mål."
		message "Du er nu ${physActState.stepsFromGoal} skridt fra dit mål."
		message "I øjeblikket er du ${physActState.stepsFromGoal} skridt fra dit mål."
		message "Nu er du ${physActState.stepsFromGoal} skridt fra dit mål."
	}
	
	feedback {
		id "feedback_da_3"
		withGoalSetting true
		overGoal true
		activityUnit "steps"
		message "Du er ${physActState.stepsFromGoal} skridt over dit mål."
		message "Du har overgået dit mål med ${physActState.stepsFromGoal} skridt."
		message "Du har gået ${physActState.stepsFromGoal} skridt over dit mål."
	}
	
	feedback {
		id "feedback_da_4"
		withGoalSetting true
		activityUnit "steps"
		message "Dit nuværende aktivitetsniveau er ${physActState.currentActivitySteps} skridt. Dit mål er ${physActState.currentGoalSteps} skridt."
		message "Du har nu gået ${physActState.currentActivitySteps} skridt. Dit mål er ${physActState.currentGoalSteps} skridt."
		message "Du gik ${physActState.currentActivitySteps} skridt. Dit mål er ${physActState.currentGoalSteps} skridt."
		message "Dit nuværende aktivitetsniveau ligger på ${physActState.currentActivitySteps} skridt. Dit nuværende mål er ${physActState.currentGoalSteps} skridt."
		message "Nu har du gået ${physActState.currentActivitySteps} skridt. Dit nuværende mål er ${physActState.currentGoalSteps} skridt."
		message "Du gik ${physActState.currentActivitySteps} skridt. Dit nuværende mål er ${physActState.currentGoalSteps} skridt."		
	}
	
	feedback {
		id "feedback_da_5"
		withGoalSetting false
		activityUnit "distance"
		message "Du har gået ${physActState.currentActivityKilometers} kilometer i dag."
		message "I dag har du gået ${physActState.currentActivityKilometers} kilometer."
		message "Du har gået ${physActState.currentActivityKilometers} kilometer."
		message "I alt har du gået ${physActState.currentActivityKilometers} kilometer."
		message "Indtil nu har du gået ${physActState.currentActivityKilometers} kilometer."
		message "Indtil videre har du gået ${physActState.currentActivityKilometers} kilometer i dag."
		message "I dag har du gået ${physActState.currentActivityKilometers} kilometers indtil videre."
	}
	
	feedback {
		id "feedback_da_6"
		withGoalSetting true
		underGoal true
		activityUnit "distance"
		message "Du gik ${physActState.currentActivityKilometers} ud af ${physActState.currentGoalKilometers} kilometer."
		message "Du har gået ${physActState.currentActivityKilometers} ud af ${physActState.currentGoalKilometers} kilometer."
		message "Du er ${physActState.kilometersFromGoal} kilometer fra at nå dit mål."
		message "Du er nu ${physActState.kilometersFromGoal} kilometer fra at nå dit mål."
		message "I øjeblikket er du ${physActState.kilometersFromGoal} kilometer fra at nå dit mål."
		message "Du er ${physActState.kilometersFromGoal} kilometer fra dit mål."
		message "Du er nu ${physActState.kilometersFromGoal} kilometer fra dit mål."
		message "Du er i øjeblikket ${physActState.kilometersFromGoal} kilometer fra dit mål."
		message "Du er nu ${physActState.kilometersFromGoal} kilometer fra dit mål."		
	}
	
	feedback {
		id "feedback_da_7"
		withGoalSetting true
		overGoal true
		activityUnit "distance"
		message "Du er ${physActState.kilometersFromGoal} kilometer over dit mål."
		message "Du har overgået dit mål med ${physActState.kilometersFromGoal} kilometer."
		message "Du har gået ${physActState.kilometersFromGoal} kilometer mere end dit mål."
	}
	
	feedback {
		id "feedback_da_8"
		withGoalSetting true
		activityUnit "distance"
		message "Dit nuværende aktivitetsniveau er ${physActState.currentActivityKilometers} kilometer. Dit mål er ${physActState.currentGoalKilometers} kilometer."
		message "Du har nu gået ${physActState.currentActivityKilometers} kilometer. Dit mål er ${physActState.currentGoalKilometers} kilometer."
		message "Du gik ${physActState.currentActivityKilometers} kilometer. Dit mål er ${physActState.currentGoalKilometers} kilometer."
		message "I øjeblikket er dit aktivitetsniveau ${physActState.currentActivityKilometers} kilometer. Dit mål er ${physActState.currentGoalKilometers} kilometer."
		message "Du har nu gået ${physActState.currentActivityKilometers} kilometer. I øjeblikket er dit mål ${physActState.currentGoalKilometers} kilometer."
		message "Du gik ${physActState.currentActivityKilometers} kilometer. Dit mål er i øjeblikket ${physActState.currentGoalKilometers} kilometer."
	}
}

// -------------------- Reinforcement Representations -------------------- //

reinforcementRepresentations {
	
	reinforcement {
		id "reinforcement_da_1"
		reinforcementIntention "neutral"
		message "Tillykke!"
		message "Godt gået!"
		message "Du gjorde det!"
		message "Rigtig godt!"
		message "Flot gået!"
		message "Fantastisk!"
	}
	
	reinforcement {
		id "reinforcement_da_2"
		reinforcementIntention "encourage"
		closeToGoal false
		message "Ikke giv op!"
		message "Ikke giv op nu!"
		message "Bliv ved!"
	}
	
	reinforcement {
		id "reinforcement_da_3"
		reinforcementIntention "encourage"
		closeToGoal true
		message "Du er der næsten!"
		message "Ikke giv op nu! Du er der næsten!"
		message "Bare lidt mere nu!"
		message "Bare nogle få skridt mere!"
	}
	
	reinforcement {
		id "reinforcement_da_4"
		reinforcementIntention "discourage"
		closeToGoal false
		message "Tag det roligt!"
		message "Giv dig selv en pause!"
	}
	
	reinforcement {
		id "reinforcement_da_5"
		reinforcementIntention "discourage"
		closeToGoal true
		message "Bare slap lidt af."
		message "Du laver lidt for meget."
		message "Du laver en anelse for meget."
		message "Du er lidt for aktiv."
	}
	
}

// -------------------- Argument Representations -------------------- //

argumentRepresentations {

}

// -------------------- Lifestyle Suggestion Representations -------------------- //

lifestyleSuggestionRepresentations {

}

// -------------------- Activity Suggestion Representations -------------------- //

activitySuggestionRepresentations {

}

// -------------------- Synchronize Sensor Representations -------------------- //

synchronizeSensorRepresentations {

	synchronizeSensor {
		id "synchronizeSensor_da_1"
		message "Det ser ud til, at du ikke har synkroniseret din aktivitetssensor endnu i dag."
		message "Dine data om din fysiske aktivitet er ikke tilgængelige. Har du synkronisret din sensor for nylig?"
		message "Jeg kan ikke finde dine data om din fysiske aktivitet. Prøv at synkronisere din sensor."
		message "Jeg kan ikke finde dine data om din fysiske aktivitet. Har du synkroniseret din sensor i dag?"
	}

}