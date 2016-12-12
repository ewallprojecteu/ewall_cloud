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
	statement "Hej [firstName]! Det ser ud til, at du ikke har brugt videotræningsøvelserne i et stykke tid."
	
	basicReply {
		replyId "A"
		statement "Åben videotræningsøvelserne."
		action {
			actionType "openApplication"
			parameter "Physical Trainer"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Hvornår så du sidst på det?"
		nextStepId "LastCheckedTime"
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
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
	statement "Sidste gang, du brugte videotræningsøvelserne, var [daysAgoLastVideoTrainerInteraction], klokken [timeLastVideoTrainerInteraction]."
	
	basicReply {
		replyId "A"
		statement "Tak. Åben videotræningsøvelserne."
		action {
			actionType "openApplication"
			parameter "Physical Trainer"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
		action {
			actionType "postponeReminder"
			parameter "UseVideoExerciseTrainer"
			parameter "24"
		}
	}

}