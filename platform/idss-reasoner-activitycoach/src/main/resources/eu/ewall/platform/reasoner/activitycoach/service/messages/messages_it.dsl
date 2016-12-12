// ----- Message Database for Language 'it' (Italian)

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
		id "greeting_it_1"
		withFirstName false
		message "Salve."
		message "Salve!"
		message "Ciao."
		message "Ciao!"
	}
	
	greeting {
		id "greeting_it_2"
		withFirstName true
		message "Salve ${userModel.firstName}."
		message "Salve ${userModel.firstName}!"
		message "Ciao ${userModel.firstName}."
		message "Ciao ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_it_3"
		withFirstName false
		dayPart "morning"
		message "Buon giorno."
		message "Buon giorno!"
	}
	
	greeting {
		id "greeting_it_4"
		withFirstName true
		dayPart "morning"
		message "Buon giorno ${userModel.firstName}."
		message "Buon giorno ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_it_5"
		withFirstName false
		dayPart "afternoon"
		message "Buon pomeriggio."
		message "Buon pomeriggio!"
	}
	
	greeting {
		id "greeting_it_6"
		withFirstName true
		dayPart "afternoon"
		message "Buon pomeriggio ${userModel.firstName}."
		message "Buon pomeriggio ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_it_7"
		withFirstName false
		dayPart "evening"
		message "Buona sera."
		message "Buona sera!"
	}
	
	greeting {
		id "greeting_it_8"
		withFirstName true
		dayPart "evening"
		message "Buona sera ${userModel.firstName}."
		message "Buona sera ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_it_9"
		withFirstName false
		dayPart "night"
		message "Buona notte."
		message "Buona notte!"
	}
	
	greeting {
		id "greeting_it_10"
		withFirstName true
		dayPart "night"
		message "Buona notte ${userModel.firstName}."
		message "Buona notte ${userModel.firstName}!"
	}
}

// -------------------- Feedback Representations -------------------- //

feedbackRepresentations {
	
	feedback {
		id "feedback_it_1"
		withGoalSetting false
		activityUnit "steps"
		message "Hai fatto ${physActState.currentActivitySteps} passi."
		message "Hai compiuto ${physActState.currentActivitySteps} passi."
	}

	feedback {
		id "feedback_it_2"
		withGoalSetting true
		underGoal true
		activityUnit "steps"
		message "Hai fatto ${physActState.currentActivitySteps} passi su ${physActState.currentGoalSteps}."
		message "Hai fatto ${physActState.currentActivitySteps} passi su ${physActState.currentGoalSteps}."
		message "Sei a ${physActState.stepsFromGoal} passi dal tuo obiettivo."
		message "Adesso sei a ${physActState.stepsFromGoal} passi dal tuo obiettivo."
		message "Al momento sei a ${physActState.stepsFromGoal} passi dal tuo obiettivo."
		message "In questo momento sei a ${physActState.stepsFromGoal} passi dal tuo obiettivo."
		message "Ti mancano ${physActState.stepsFromGoal} passi per raggiungere il tuo obiettivo."
		message "Adesso ti mancano ${physActState.stepsFromGoal} passi per raggiungere il tuo obiettivo."
		message "Al momento ti mancano ${physActState.stepsFromGoal} passi per raggiungere il tuo obiettivo."
		message "In questo momento ti mancano ${physActState.stepsFromGoal} passi per raggiungere il tuo obiettivo."
	}
	
	feedback {
		id "feedback_it_3"
		withGoalSetting true
		overGoal true
		activityUnit "steps"
		message "Sei a ${physActState.stepsFromGoal} passi oltre il tuo obiettivo."
		message "Hai sorpassato il tuo obiettivo di ${physActState.stepsFromGoal} passi."
		message "Hai fatto ${physActState.stepsFromGoal} passi in più del tuo obiettivo."
	}
	
	feedback {
		id "feedback_it_4"
		withGoalSetting true
		activityUnit "steps"
		message "Al momento il tuo livello di attività è di ${physActState.currentActivitySteps} passi, il tuo obiettivo è di ${physActState.currentGoalSteps} passi."
		message "Al momento hai fatto ${physActState.currentActivitySteps} passi, il tuo obiettivo è di ${physActState.currentGoalSteps} passi."
		message "Hai fatto ${physActState.currentActivitySteps} passi, il tuo obiettivo è di ${physActState.currentGoalSteps} passi."
		message "Il tuo livello di attività è adesso di ${physActState.currentActivitySteps} passi, il tuo obiettivo attuale è di ${physActState.currentGoalSteps} passi."
		message "Al momento hai fatto ${physActState.currentActivitySteps} passi, il tuo obiettivo attuale è di ${physActState.currentGoalSteps} passi."
		message "Hai fatto ${physActState.currentActivitySteps} passi, il tuo obiettivo attuale è di ${physActState.currentGoalSteps} passi."
	}
	
	feedback {
		id "feedback_it_5"
		withGoalSetting false
		activityUnit "distance"
		message "Hai camminato per ${physActState.currentActivityKilometers} chilometri oggi."
		message "Oggi hai camminato per ${physActState.currentActivityKilometers} chilometri."
		message "Hai percorso ${physActState.currentActivityKilometers} chilometri oggi."
		message "Hai percorso un totale di ${physActState.currentActivityKilometers} chilometri oggi."
		message "Finora hai camminato per ${physActState.currentActivityKilometers} chilometri."
		message "Finora, oggi, hai camminato per ${physActState.currentActivityKilometers} chilometri."
		message "Oggi hai camminato per ${physActState.currentActivityKilometers} chilometri fino ad adesso."
	}
	
	feedback {
		id "feedback_it_6"
		withGoalSetting true
		underGoal true
		activityUnit "distance"
		message "Hai percorso ${physActState.currentActivityKilometers} chilometri su ${physActState.currentGoalKilometers}."
		message "Hai camminato per ${physActState.currentActivityKilometers} chilometri su ${physActState.currentGoalKilometers}."
		message "Sei a ${physActState.kilometersFromGoal} chilometri dal tuo obiettivo."
		message "Adesso sei a ${physActState.kilometersFromGoal} chilometri dal tuo obiettivo."
		message "In questo momento sei a ${physActState.kilometersFromGoal} chilometri dal tuo obiettivo."
		message "Attualmente sei a ${physActState.kilometersFromGoal} chilometri dal tuo obiettivo."
		message "Sei a ${physActState.kilometersFromGoal} chilometri dal raggiungere il tuo obiettivo."
		message "Adesso sei a ${physActState.kilometersFromGoal} chilometri dal raggiungere il tuo obiettivo."
		message "In questo momento sei a ${physActState.kilometersFromGoal} chilometri dal raggiungere il tuo obiettivo."
		message "Attualmente sei a ${physActState.kilometersFromGoal} chilometri dal raggiungere il tuo obiettivo."
	}
	
	feedback {
		id "feedback_it_7"
		withGoalSetting true
		overGoal true
		activityUnit "distance"
		message "Sei a ${physActState.kilometersFromGoal} chilometri oltre il tuo obiettivo."
		message "Hai sorpassato il tuo obiettivo di ${physActState.kilometersFromGoal} chilometri."
		message "Hai camminato per ${physActState.kilometersFromGoal} chilometri in più rispetto al tuo obiettivo."
	}
	
	feedback {
		id "feedback_it_8"
		withGoalSetting true
		activityUnit "distance"
		message "Al momento il tuo livello di attività è di ${physActState.currentActivityKilometers} chilometri, il tuo obiettivo è di ${physActState.currentGoalKilometers} chilometri."
		message "Al momento hai percorso ${physActState.currentActivityKilometers} chilometri, il tuo obiettivo è di ${physActState.currentGoalKilometers} chilometri."
		message "Hai fatto ${physActState.currentActivityKilometers} chilometri, il tuo obiettivo è di ${physActState.currentGoalKilometers} chilometri."
		message "Il tuo livello di attività attuale è di ${physActState.currentActivityKilometers} chilometri, il tuo obiettivo attuale è di ${physActState.currentGoalKilometers} chilometri."
		message "Attualmente hai fatto ${physActState.currentActivityKilometers} chilometri, il tuo obiettivo attuale è di ${physActState.currentGoalKilometers} chilometri."
		message "Hai fatto ${physActState.currentActivityKilometers} chilometri, il tuo obiettivo attuale è di ${physActState.currentGoalKilometers} chilometri."
	}
}

// -------------------- Reinforcement Representations -------------------- //

reinforcementRepresentations {
	
	reinforcement {
		id "reinforcement_it_1"
		reinforcementIntention "neutral"
		message "Complimenti!"
		message "Bel lavoro!"
		message "Ce l'hai fatta!"
		message "Ben fatto!"
		message "Ottimo lavoro!"
		message "Eccellente!"
	}
	
	reinforcement {
		id "reinforcement_it_2"
		reinforcementIntention "encourage"
		closeToGoal false
		message "Non arrenderti!"
		message "Non arrenderti proprio adesso!"
		message "Continua a provarci!"
	}
	
	reinforcement {
		id "reinforcement_it_3"
		reinforcementIntention "encourage"
		closeToGoal true
		message "Ce l'hai quasi fatta!"
		message "Non arrenderti ora, ce l'hai quasi fatta!"
		message "Solo un ultimo sforzo!"
		message "Manca veramente poco!"
	}
	
	reinforcement {
		id "reinforcement_it_4"
		reinforcementIntention "discourage"
		closeToGoal false
		message "Prenditela comoda!"
		message "Riposati un po'!"
	}
	
	reinforcement {
		id "reinforcement_it_5"
		reinforcementIntention "discourage"
		closeToGoal true
		message "Rilassati un po'."
		message "Stai facendo un po' troppo."
		message "Ti stai impegnando un po' troppo."
		message "Sei un po' troppo attivo."
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
		id "synchronizeSensor_it_1"
		message "Sembra oggi che tu non abbia ancora sincronizzato il sensore di attività."
		message "Sembra che tu non abbia ancora sincronizzato il sensore di attività oggi."
		message "I tuoi dati sull'attività fisica non sono disponibili, hai sincronizzato il sensore recentemente?"
		message "Non riesco a trovare i tuoi dati sull'attività fisica, prova a sincronizzare il sensore."
		message "Non riesco a trovare i tuoi dati sull'attività fisica, oggi hai sincronizzato il sensore?"
	}

}
