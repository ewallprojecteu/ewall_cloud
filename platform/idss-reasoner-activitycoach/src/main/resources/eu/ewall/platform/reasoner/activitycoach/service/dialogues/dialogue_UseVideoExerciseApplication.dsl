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
	statement "Hello [firstName]! It looks like you haven't used the Video Exercise Trainer for some time."
	
	basicReply {
		replyId "A"
		statement "Open the Video Exercise Trainer."
		action {
			actionType "openApplication"
			parameter "Physical Trainer"
		}
	}
	
	basicReply {
		replyId "B"
		statement "When was the last time I looked at it?"
		nextStepId "LastCheckedTime"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
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
	statement "The last time you used the Video Exercise Trainer was [daysAgoLastVideoTrainerInteraction], at [timeLastVideoTrainerInteraction]."
	
	basicReply {
		replyId "A"
		statement "Thanks. Open the Video Exercise Trainer."
		action {
			actionType "openApplication"
			parameter "Physical Trainer"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
		action {
			actionType "postponeReminder"
			parameter "UseVideoExerciseTrainer"
			parameter "24"
		}
	}

}