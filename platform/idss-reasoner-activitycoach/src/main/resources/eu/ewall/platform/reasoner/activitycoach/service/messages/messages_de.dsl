// ----- Message Database for Language 'de-at' (German/Austria)

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
		id "greeting_de_1"
		withFirstName false
		message "Hallo."
		message "Hallo!"
	}
	
	greeting {
		id "greeting_de_2"
		withFirstName true
		message "Hallo ${userModel.firstName}."
		message "Hallo ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_de_3"
		withFirstName false
		dayPart "morning"
		message "Guten Morgen."
		message "Guten Morgen!"
	}
	
	greeting {
		id "greeting_de_4"
		withFirstName true
		dayPart "morning"
		message "Guten Morgen ${userModel.firstName}."
		message "Guten Morgen ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_de_5"
		withFirstName false
		dayPart "afternoon"
		message "Guten Tag."
		message "Guten Tag!"
	}
	
	greeting {
		id "greeting_de_6"
		withFirstName true
		dayPart "afternoon"
		message "Guten Tag ${userModel.firstName}."
		message "Guten Tag ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_de_7"
		withFirstName false
		dayPart "evening"
		message "Guten Abend."
		message "Guten Abend!"
	}
	
	greeting {
		id "greeting_de_8"
		withFirstName true
		dayPart "evening"
		message "Guten Abend ${userModel.firstName}."
		message "Guten Abend ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_de_9"
		withFirstName false
		dayPart "night"
		message "Gute Nacht."
		message "Gute Nacht!"
	}
	
	greeting {
		id "greeting_de_10"
		withFirstName true
		dayPart "night"
		message "Gute Nacht ${userModel.firstName}."
		message "Gute Nacht ${userModel.firstName}!"
	}	
}

// -------------------- Feedback Representations -------------------- //

feedbackRepresentations {
	
	feedback {
		id "feedback_de_1"
		withGoalSetting false
		activityUnit "steps"
		message "Du hast ${physActState.currentActivitySteps} Schritte gemacht."
	}
	
	feedback {
		id "feedback_de_2"
		withGoalSetting true
		underGoal true
		activityUnit "steps"
		message "Du hast ${physActState.currentActivitySteps} von ${physActState.currentGoalSteps} geplanten Schritte gemacht."
		message "Du bist ${physActState.stepsFromGoal} Schritte von deinem geplanten Ziel entfernt."
		message "Du bist nun ${physActState.stepsFromGoal} Schritte von deinem geplanten Ziel entfernt."
		message "Du bist im Moment ${physActState.stepsFromGoal} Schritte von deinem geplanten Ziel entfernt."
		message "Du bist momentan ${physActState.stepsFromGoal} Schritte von deinem geplanten Ziel entfernt."
		message "In ${physActState.stepsFromGoal} Schritten hast du dein Ziel erreicht."
		message "Du bist nun ${physActState.stepsFromGoal} Schritte von deinem geplanten Ziel entfernt."
		message "Du bist im Moment ${physActState.stepsFromGoal} Schritte von deinem geplanten Ziel entfernt."
		message "Du bist momentan ${physActState.stepsFromGoal} Schritte von deinem geplanten Ziel entfernt."
	}
	
	feedback {
		id "feedback_de_3"
		withGoalSetting true
		overGoal true
		activityUnit "steps"
		message "Du bist ${physActState.stepsFromGoal} Schritte über deinem Ziel."
		message "Du hast dein Ziel um ${physActState.stepsFromGoal} Schritte übertroffen."
	}
	
	feedback {
		id "feedback_de_4"
		withGoalSetting true
		activityUnit "steps"
		message "Du hast momentan schon ${physActState.currentActivitySteps} Schnitte gemacht. Dein Ziel sind ${physActState.currentGoalSteps} Schritte."
		message "Derzeiotig hast du schon ${physActState.currentActivitySteps} Schnitte gemacht. Dein Ziel sind ${physActState.currentGoalSteps} Schritte."
		message "Du hast ${physActState.currentActivitySteps} Schnitte gemacht. Dein Ziel sind ${physActState.currentGoalSteps} Schritte."
		message "Zum Zeitpunkt hast du schon ${physActState.currentActivitySteps} Schnitte gemacht. Dein derzeitiges Ziel sind ${physActState.currentGoalSteps} Schritte."
		message "Du hast ${physActState.currentActivitySteps} Schnitte gemacht. Dein derzeitiges Ziel sind ${physActState.currentGoalSteps} Schritte."
		message "Du hast nun schon ${physActState.currentActivitySteps} Schnitte gemacht. Dein derzeitiges Ziel sind ${physActState.currentGoalSteps} Schritte."
	}
	
	feedback {
		id "feedback_de_5"
		withGoalSetting false
		activityUnit "distance"
		message "Du bist heute ${physActState.currentActivityKilometers} Kilometer gegangen."
		message "Heute bist du ${physActState.currentActivityKilometers} Kilometer gegangen."
		message "Insgesamt bist du schon ${physActState.currentActivityKilometers} Kilometer gegangen."
		message "Bis jetzt bist du schon ${physActState.currentActivityKilometers} Kilometer gegangen."
	}
	
	feedback {
		id "feedback_de_6"
		withGoalSetting true
		underGoal true
		activityUnit "distance"
		message "Du bist ${physActState.currentActivityKilometers} von den geplanten ${physActState.currentGoalKilometers} Kilometern gegangen."
		message "Du bist ${physActState.kilometersFromGoal} Kilometer von deinem Ziel entfernt."
		message "Du bist nun ${physActState.kilometersFromGoal} Kilometer von deinem Ziel entfernt."
		message "Du bist im Moment ${physActState.kilometersFromGoal} Kilometer von deinem geplanten Ziel entfernt."
		message "Du bist momentan ${physActState.kilometersFromGoal} Kilometer von deinem Ziel entfernt."
	}
	
	feedback {
		id "feedback_de_7"
		withGoalSetting true
		overGoal true
		activityUnit "distance"
		message "Du bist ${physActState.kilometersFromGoal} Kilometer über deinem Ziel."
		message "Du hast dein Ziel um ${physActState.kilometersFromGoal} Kilometer übertroffen."
		message "Du bist ${physActState.kilometersFromGoal} Kilometer mehr gegangen als geplant."
	}
	
	feedback {
		id "feedback_de_8"
		withGoalSetting true
		activityUnit "distance"
		message "Du bist momentan schon ${physActState.currentActivityKilometers} Kilometer gegangen. Dein Ziel sind ${physActState.currentGoalKilometers} Kilometer."
	}
	
}

// -------------------- Reinforcement Representations -------------------- //

reinforcementRepresentations {
	
	reinforcement {
		id "reinforcement_de_1"
		reinforcementIntention "neutral"
		message "Gratulation!"
		message "Gute Arbeit!"
		message "Spitze!"
		message "Gut gemacht!"
		message "Super!"
		message "Excellent!"
	}
	
	reinforcement {
		id "reinforcement_de_2"
		reinforcementIntention "encourage"
		closeToGoal false
		message "Gib nicht auf!"
		message "Jetzt nicht aufgeben!"
		message "Gib alles!"
	}
	
	reinforcement {
		id "reinforcement_de_3"
		reinforcementIntention "encourage"
		closeToGoal true
		message "Du hast es fast geschafft!"
		message "Jetzt nicht aufgeben! Du hast es fast geschafft!"
		message "Nur noch ein klein bisschen mehr!"
		message "Nur ein paar mehr Schritte!"
	}
	
	reinforcement {
		id "reinforcement_de_4"
		reinforcementIntention "discourage"
		closeToGoal false
		message "Nimm es leicht!"
		message "Mach eine Pause!"
	}
	
	reinforcement {
		id "reinforcement_de_5"
		reinforcementIntention "discourage"
		closeToGoal true
		message "Ruhe dich kurz aus."
		message "Du gibst ein bisschen zu viel Gas."
		message "Du machst ein bisschen zu viel."
		message "Du bist ein bisschen zu sehr aktiv."
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
		id "synchronizeSensor_de_1"
		message "Es sieht so aus als hättest du den Aktivitätssensor noch nicht sychnronisiert heute."
		message "Dein Aktivitätssensor wurde heute allem Anschein nach noch nicht synchronisiert."
		message "Deine Aktivitätsdaten sich nicht zugänglich. Hast du deine Aktivitätsdaten in letzter Zeit synchronisiert?"
		message "Ich kann deine Aktivitätsdaten nicht finden. Versuche deinen Sensor zu synchronisieren."
		message "Ich kann deine Aktivitätsdaten nicht finden. Hast du deine Aktivitätsdaten heute schon synchronisiert?"
	}

}