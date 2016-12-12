// ----- Message Database for Language 'nl' (Dutch)

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
		id "greeting_nl_1"
		withFirstName false
		message "Hallo."
		message "Hallo!"
		message "Hoi."
		message "Hoi!"
	}
	
	greeting {
		id "greeting_nl_2"
		withFirstName true
		message "Hallo ${userModel.firstName}."
		message "Hallo ${userModel.firstName}!"
		message "Hoi ${userModel.firstName}."
		message "Hoi ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_nl_3"
		withFirstName false
		dayPart "morning"
		message "Goedemorgen."
		message "Goedemorgen!"
	}
	
	greeting {
		id "greeting_nl_4"
		withFirstName true
		dayPart "morning"
		message "Goedemorgen ${userModel.firstName}."
		message "Goedemorgen ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_nl_5"
		withFirstName false
		dayPart "afternoon"
		message "Goedemiddag."
		message "Goedemiddag!"
	}
	
	greeting {
		id "greeting_nl_6"
		withFirstName true
		dayPart "afternoon"
		message "Goedemiddag ${userModel.firstName}."
		message "Goedemiddag ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_nl_7"
		withFirstName false
		dayPart "evening"
		message "Goedenavond."
		message "Goedenavond!"
	}
	
	greeting {
		id "greeting_nl_8"
		withFirstName true
		dayPart "evening"
		message "Goedenavond ${userModel.firstName}."
		message "Goedenavond ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_nl_9"
		withFirstName false
		dayPart "night"
		message "Goedenacht."
		message "Goedenacht!"
	}
		
	greeting {
		id "greeting_nl_10"
		withFirstName true
		dayPart "night"
		message "Goedenacht ${userModel.firstName}."
		message "Goedenacht ${userModel.firstName}!"
	}
}

// -------------------- Feedback Representations -------------------- //

feedbackRepresentations {
	
	feedback {
		id "feedback_nl_1"
		withGoalSetting false
		activityUnit "steps"
		message "Je hebt ${physActState.currentActivitySteps} stappen gezet."
		message "Tot nu toe heb je ${physActState.currentActivitySteps} stappen gezet."
		message "Je hebt tot nu toe ${physActState.currentActivitySteps} stappen gezet."
	}

	feedback {
		id "feedback_nl_2"
		withGoalSetting true
		underGoal true
		activityUnit "steps"
		message "Je hebt ${physActState.currentActivitySteps} van de ${physActState.currentGoalSteps} stappen gezet."
		message "Je hebt tot nu toe ${physActState.currentActivitySteps} van de ${physActState.currentGoalSteps} stappen gezet."
		message "Tot nu toe heb je ${physActState.currentActivitySteps} van de ${physActState.currentGoalSteps} stappen gezet."
		message "Je bent ${physActState.stepsFromGoal} stappen verwijderd van je doel."
		message "Nog ${physActState.stepsFromGoal} stappen te gaan om je doel te bereiken."
		message "Nog ${physActState.stepsFromGoal} stappen te gaan om je doel te bereiken!"
		message "Je moet nog ${physActState.stepsFromGoal} stappen zetten om je doel te bereiken."
		message "Je bent nu ${physActState.stepsFromGoal} verwijderd van je doel."
		message "Op dit moment heb je ${physActState.currentActivitySteps} van de ${physActState.currentGoalSteps} stappen gezet."
		message "Op dit moment ben je ${physActState.stepsFromGoal} stappen verwijderd van je doel."		
	}
	
	feedback {
		id "feedback_nl_3"
		withGoalSetting true
		overGoal true
		activityUnit "steps"
		message "Je bent ${physActState.stepsFromGoal} voorbij je doel."
		message "Je bent je doel met ${physActState.stepsFromGoal} stappen voorbij gegaan."
		message "Je hebt ${physActState.stepsFromGoal} meer stappen gedaan dan je doel."
	}
	
	feedback {
		id "feedback_nl_4"
		withGoalSetting true
		activityUnit "steps"
		message "Je huidige activiteitenniveau is ${physActState.currentActivitySteps} stappen, je doel is ${physActState.currentGoalSteps} stappen."
		message "Op dit moment heb je ${physActState.currentActivitySteps} stappen gezet, je doel is ${physActState.currentGoalSteps} stappen."
		message "Je hebt ${physActState.currentActivitySteps} stappen gezet, je doel is ${physActState.currentGoalSteps} stappen."
		message "Je huidige aantal stappen is ${physActState.currentActivitySteps}, je huidige doel is ${physActState.currentGoalSteps} stappen."
		message "Je hebt op dit moment ${physActState.currentActivitySteps} stappen gezet, je huidige doel is ${physActState.currentGoalSteps} stappen."
		message "Je hebt op dit moment ${physActState.currentActivitySteps} stappen gezet, je doel is ${physActState.currentGoalSteps} stappen."
	}
	
	feedback {
		id "feedback_nl_5"
		withGoalSetting false
		activityUnit "distance"
		message "Je hebt vandaag ${physActState.currentActivityKilometers} kilometer gelopen."
		message "Vandaag heb je ${physActState.currentActivityKilometers} kilometer gelopen."
		message "Tot nu toe heb je vandaag ${physActState.currentActivityKilometers} kilometer gelopen."
		message "Je hebt vandaag ${physActState.currentActivityKilometers} kilometer afgelegd."
		message "Tot nu toe heb je ${physActState.currentActivityKilometers} kilometer gelopen."
		message "Vandaag staat je totale aantal kilometers op ${physActState.currentActivityKilometers}."
		message "De kilometer teller staat vandaag op ${physActState.currentActivityKilometers}."
	}
	
	feedback {
		id "feedback_nl_6"
		withGoalSetting true
		underGoal true
		activityUnit "distance"
		message "Je hebt ${physActState.currentActivityKilometers} van de ${physActState.currentGoalKilometers} kilometers gedaan."
		message "Je hebt ${physActState.currentActivityKilometers} van de ${physActState.currentGoalKilometers} kilometers gelopen."
		message "Je bent ${physActState.kilometersFromGoal} kilometer verwijderd van het bereiken van je doel."
		message "Je bent nu ${physActState.kilometersFromGoal} kilometer verwijderd van het bereiken van je doel."
		message "Op dit moment ben je ${physActState.kilometersFromGoal} kilometer verwijderd van je doel."
		message "Je bent nu ${physActState.kilometersFromGoal} kilometer verwijderd van je doel."
	}
	
	feedback {
		id "feedback_nl_7"
		withGoalSetting true
		overGoal true
		activityUnit "distance"
		message "Je bent ${physActState.kilometersFromGoal} kilometer voorbij je doel."
		message "Je bent je doel met ${physActState.kilometersFromGoal} kilometer voorbij gestreven."
		message "Je hebt ${physActState.kilometersFromGoal} meer gelopen dan je doel."
		message "Je hebt ${physActState.kilometersFromGoal} meer gedaan je doel."
	}
	
	feedback {
		id "feedback_nl_8"
		withGoalSetting true
		activityUnit "distance"
		message "Je huidige activiteiten niveau staat op ${physActState.currentActivityKilometers} kilometers, je doel is ${physActState.currentGoalKilometers} kilometers."
		message "Je hebt op dit moment ${physActState.currentActivityKilometers} kilometer gelopen, je doel is ${physActState.currentGoalKilometers} kilometer."
		message "Je hebt ${physActState.currentActivityKilometers} kilometer gelopen, je doel is ${physActState.currentGoalKilometers} kilometer."
		message "Je hebt ${physActState.currentActivityKilometers} kilometer gelopen, je huidige doel is ${physActState.currentGoalKilometers} kilometer."
		message "Je hebt op dit moment ${physActState.currentActivityKilometers} kilometer gelopen, je huidge doel is ${physActState.currentGoalKilometers} kilometer."
		message "Op dit moment is je doel ${physActState.currentGoalKilometers} kilometer, en je hebt ${physActState.currentActivityKilometers} kilometer gelopen."
	}
}

// -------------------- Reinforcement Representations -------------------- //

reinforcementRepresentations {
	
	reinforcement {
		id "reinforcement_nl_1"
		reinforcementIntention "neutral"
		message "Gefeliciteerd!"
		message "Goed gedaan!"
		message "Goed werk!"
		message "Heel mooi!"
		message "Uitstekend!"
		message "Perfect!"
	}
	
	reinforcement {
		id "reinforcement_nl_2"
		reinforcementIntention "encourage"
		closeToGoal false
		message "Geef het niet op!"
		message "Nu niet opgeven!"
		message "Ga door!"
	}
	
	reinforcement {
		id "reinforcement_nl_3"
		reinforcementIntention "encourage"
		closeToGoal true
		message "Je bent er bijna!"
		message "Geeft het niet op. Je bent er bijna!"
		message "Nog een klein stukje verder!"
		message "Nog een paar stapjes!"
	}
	
	reinforcement {
		id "reinforcement_nl_4"
		reinforcementIntention "discourage"
		closeToGoal false
		message "Doe het rustig aan!"
		message "Neem een beetje rust!"
	}
	
	reinforcement {
		id "reinforcement_nl_5"
		reinforcementIntention "discourage"
		closeToGoal true
		message "Probeer een beetje rustig aan te doen."
		message "Je bent iets te actief."
		message "Je doet een klein beetje te veel."
		message "Je bent een beetje te actief."
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
		id "synchronizeSensor_nl_1"
		message "Het lijkt erop dat u vandaag uw activiteiten sensor nog niet gesynchroniseerd heeft."
		message "Het ziet er naar uit dat u uw activiteiten sensor vandaag nog niet hebt gesynchroniseerd."
		message "Uw activiteiten data is niet beschikbaar, heeft u recentelijk de sensor gesynchroniseerd?"
		message "Ik kan uw fysieke activiteiten data niet vinden, probeer uw sensor to synchroniseren."
		message "Ik kan uw fysieke activiteiten data niet vinden, heeft u uw activiteiten sensor gesynchroniseerd vandaag?"
	}

}
