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
		id "greeting_en_1"
		withFirstName false
		message "Hello."
		message "Hello!"
		message "Hi."
		message "Hi!"
	}
	
	greeting {
		id "greeting_en_2"
		withFirstName true
		message "Hello ${userModel.firstName}."
		message "Hello ${userModel.firstName}!"
		message "Hi ${userModel.firstName}."
		message "Hi ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_en_3"
		withFirstName false
		dayPart "morning"
		message "Good morning."
		message "Good morning!"
	}
	
	greeting {
		id "greeting_en_4"
		withFirstName true
		dayPart "morning"
		message "Good morning ${userModel.firstName}."
		message "Good morning ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_en_5"
		withFirstName false
		dayPart "afternoon"
		message "Good afternoon."
		message "Good afternoon!"
	}
	
	greeting {
		id "greeting_en_6"
		withFirstName true
		dayPart "afternoon"
		message "Good afternoon ${userModel.firstName}."
		message "Good afternoon ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_en_7"
		withFirstName false
		dayPart "evening"
		message "Good evening."
		message "Good evening!"
	}
	
	greeting {
		id "greeting_en_8"
		withFirstName true
		dayPart "evening"
		message "Good evening ${userModel.firstName}."
		message "Good evening ${userModel.firstName}!"
	}
	
	greeting {
		id "greeting_en_9"
		withFirstName false
		dayPart "night"
		message "Good night."
		message "Good night!"
	}
		
	greeting {
		id "greeting_en_10"
		withFirstName true
		dayPart "night"
		message "Good night ${userModel.firstName}."
		message "Good night ${userModel.firstName}!"
	}	
}

// -------------------- Feedback Representations -------------------- //

feedbackRepresentations {
	
	feedback {
		id "feedback_en_1"
		withGoalSetting false
		activityUnit "steps"
		message "You did ${physActState.currentActivitySteps} steps."
		message "You have taken ${physActState.currentActivitySteps} steps."
	}
	
	feedback {
		id "feedback_en_2"
		withGoalSetting true
		underGoal true
		activityUnit "steps"
		message "You did ${physActState.currentActivitySteps} out of ${physActState.currentGoalSteps} steps."
		message "You haven taken ${physActState.currentActivitySteps} out of ${physActState.currentGoalSteps} steps."
		message "You are ${physActState.stepsFromGoal} steps away from reaching your goal."
		message "You are now ${physActState.stepsFromGoal} steps away from reaching your goal."
		message "You are at the moment ${physActState.stepsFromGoal} steps away from reaching your goal."
		message "You are currently ${physActState.stepsFromGoal} steps away from reaching your goal."
		message "You are ${physActState.stepsFromGoal} steps from reaching your goal."
		message "You are now ${physActState.stepsFromGoal} steps from reaching your goal."
		message "You are at the moment ${physActState.stepsFromGoal} steps from reaching your goal."
		message "You are currently ${physActState.stepsFromGoal} steps from reaching your goal."
	}
	
	feedback {
		id "feedback_en_3"
		withGoalSetting true
		overGoal true
		activityUnit "steps"
		message "You are ${physActState.stepsFromGoal} steps over your goal."
		message "You have surpassed your goal by ${physActState.stepsFromGoal} steps."
		message "You have done ${physActState.stepsFromGoal} more steps than your goal."
	}
	
	feedback {
		id "feedback_en_4"
		withGoalSetting true
		activityUnit "steps"
		message "Your current activity level is ${physActState.currentActivitySteps} steps, your goal is ${physActState.currentGoalSteps} steps."
		message "You have currently done ${physActState.currentActivitySteps} steps, your goal is ${physActState.currentGoalSteps} steps."
		message "You did ${physActState.currentActivitySteps} steps, your goal is ${physActState.currentGoalSteps} steps."
		message "Your current activity level is ${physActState.currentActivitySteps} steps, your current goal is ${physActState.currentGoalSteps} steps."
		message "You have currently done ${physActState.currentActivitySteps} steps, your current goal is ${physActState.currentGoalSteps} steps."
		message "You did ${physActState.currentActivitySteps} steps, your current goal is ${physActState.currentGoalSteps} steps."
	}
	
	feedback {
		id "feedback_en_5"
		withGoalSetting false
		activityUnit "distance"
		message "You have walked ${physActState.currentActivityKilometers} kilometers today."
		message "Today you have walked ${physActState.currentActivityKilometers} kilometers."
		message "You have been walking ${physActState.currentActivityKilometers} kilometers."
		message "You have walked a total of ${physActState.currentActivityKilometers} kilometers."
		message "So far you have walked ${physActState.currentActivityKilometers} kilometers."
		message "So far, today, you have walked ${physActState.currentActivityKilometers} kilometers."
		message "Today you have walked ${physActState.currentActivityKilometers} kilometers so far."
	}
	
	feedback {
		id "feedback_en_6"
		withGoalSetting true
		underGoal true
		activityUnit "distance"
		message "You did ${physActState.currentActivityKilometers} out of ${physActState.currentGoalKilometers} kilometers."
		message "You have walked ${physActState.currentActivityKilometers} out of ${physActState.currentGoalKilometers} kilometers."
		message "You are ${physActState.kilometersFromGoal} kilometers away from reaching your goal."
		message "You are now ${physActState.kilometersFromGoal} kilometers away from reaching your goal."
		message "You are at the moment ${physActState.kilometersFromGoal} kilometers away from reaching your goal."
		message "You are currently ${physActState.kilometersFromGoal} kilometers away from reaching your goal."
		message "You are ${physActState.kilometersFromGoal} kilometers from reaching your goal."
		message "You are now ${physActState.kilometersFromGoal} kilometers from reaching your goal."
		message "You are at the moment ${physActState.kilometersFromGoal} kilometers from reaching your goal."
		message "You are currently ${physActState.kilometersFromGoal} kilometers from reaching your goal."
	}
	
	feedback {
		id "feedback_en_7"
		withGoalSetting true
		overGoal true
		activityUnit "distance"
		message "You are ${physActState.kilometersFromGoal} kilometers over your goal."
		message "You have surpassed your goal by ${physActState.kilometersFromGoal} kilometers."
		message "You have walked ${physActState.kilometersFromGoal} more kilometers than your goal."
	}
	
	feedback {
		id "feedback_en_8"
		withGoalSetting true
		activityUnit "distance"
		message "Your current activity level is ${physActState.currentActivityKilometers} kilometers, your goal is ${physActState.currentGoalKilometers} kilometers."
		message "You have currently done ${physActState.currentActivityKilometers} kilometers, your goal is ${physActState.currentGoalKilometers} kilometers."
		message "You did ${physActState.currentActivityKilometers} kilometers, your goal is ${physActState.currentGoalKilometers} kilometers."
		message "Your current activity level is ${physActState.currentActivityKilometers} kilometers, your current goal is ${physActState.currentGoalKilometers} kilometers."
		message "You have currently done ${physActState.currentActivityKilometers} kilometers, your current goal is ${physActState.currentGoalKilometers} kilometers."
		message "You did ${physActState.currentActivityKilometers} kilometers, your current goal is ${physActState.currentGoalKilometers} kilometers."
	}

}

// -------------------- Reinforcement Representations -------------------- //

reinforcementRepresentations {
	
	reinforcement {
		id "reinforcement_en_1"
		reinforcementIntention "neutral"
		message "Congratulations!"
		message "Nice job!"
		message "You nailed it!"
		message "Well done!"
		message "Great job!"
		message "Excellent!"
	}
	
	reinforcement {
		id "reinforcement_en_2"
		reinforcementIntention "encourage"
		closeToGoal false
		message "Don't give up!"
		message "Don't give up now!"
		message "Keep going!"
	}
	
	reinforcement {
		id "reinforcement_en_3"
		reinforcementIntention "encourage"
		closeToGoal true
		message "You are almost there!"
		message "Don't give up now! You are almost there!"
		message "Just a little bit more now!"
		message "Just a few more steps!"
	}
	
	reinforcement {
		id "reinforcement_en_4"
		reinforcementIntention "discourage"
		closeToGoal false
		message "Take it easy!"
		message "Give yourself some rest!"
	}
	
	reinforcement {
		id "reinforcement_en_5"
		reinforcementIntention "discourage"
		closeToGoal true
		message "Just relax a little bit."
		message "You're doing a little too much."
		message "You're doing just a bit too much."
		message "You're just a little too active."
	}
	
}

// -------------------- Argument Representations -------------------- //

argumentRepresentations {

	argument {
		id "argument_en_1"
		goalIntention "increase"
		message "An increase in physical activity can make you feel more energetic."
		message "Increasing your physical activity can make you feel more energetic."
	}

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
		id "synchronizeSensor_en_1"
		message "It looks like you didn't synchronize your activity sensor yet today."
		message "It seems that you haven't synchronized your activity sensor yet today."
		message "Your physical activity data is not available, did you synchronize the sensor recently?"
		message "I can't find your physical activity data, try to synchronize your sensor."
		message "I can't find your physical activity data, did you synchronize your sensor today?"
	}

}
