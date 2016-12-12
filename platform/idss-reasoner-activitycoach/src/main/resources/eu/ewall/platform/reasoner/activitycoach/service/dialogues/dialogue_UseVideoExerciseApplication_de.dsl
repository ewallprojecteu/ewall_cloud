// [daysAgoLastVideoTrainerInteraction]
// [timeLastVideoTrainerInteraction]

dialogueTypeId "UseVideoExerciseApplication"
initialStepId "Initial"

// --------------------
// --- "Hello [firstName]! It looks like you haven't used the Video Exercise Trainer for some time."
// ---   o "Open the Video Exercise Trainer."
// ---   o "When was the last time I looked at it?"
// ---   o "Goodbye."
// --------------------
step {
	stepId "Initial"
	statement "Hallo [firstName]! Siehst so aus, als hättest du schon länger nicht mehr die Übungen des Video Trainers gemacht."
	
	basicReply {
		replyId "A"
		statement "Öffne den Video Trainer."
		action {
			actionType "openApplication"
			parameter "Physical Trainer"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Wann habe ich das zuletzt angesehen?"
		nextStepId "LastCheckedTime"
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
		action {
			actionType "postponeReminder"
			parameter "UseVideoExerciseTrainer"
			parameter "24"
		}
	}
}

// --------------------
// --- "The last time you used the Video Exercise Trainer was [daysAgoLastVideoTrainerInteraction], at [timeLastVideoTrainerInteraction]."
// ---   o "Thanks. Open the Video Exercise Trainer."
// ---   o "Goodbye."
// --------------------
step {
	stepId "LastCheckedTime"
	statement "Du hast zuletzt vor [daysAgoLastVideoTrainerInteraction] um [timeLastVideoTrainerInteraction] Uhr den Video Trainer verwendet."
	
	basicReply {
		replyId "A"
		statement "Danke. Öffne den Video Trainier."
		action {
			actionType "openApplication"
			parameter "Physical Trainer"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
		action {
			actionType "postponeReminder"
			parameter "UseVideoExerciseTrainer"
			parameter "24"
		}
	}

}