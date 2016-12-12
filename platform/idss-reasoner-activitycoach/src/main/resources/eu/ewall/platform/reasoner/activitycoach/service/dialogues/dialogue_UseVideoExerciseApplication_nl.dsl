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
	statement "Hallo [firstName]! Het lijkt er op dat u de Gymnastiek app al een tijdje niet meer gebruikt heeft."
	
	basicReply {
		replyId "A"
		statement "Open de Gymnastiek app."
		action {
			actionType "openApplication"
			parameter "Physical Trainer"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Wanneer heb ik voor het laatst gekeken?"
		nextStepId "LastCheckedTime"
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "De laatste keer dat u de Gymnastiek app gebruikt heeft was [daysAgoLastVideoTrainerInteraction], om [timeLastVideoTrainerInteraction]."
	
	basicReply {
		replyId "A"
		statement "Bedankt. Open de Gymnastiek app."
		action {
			actionType "openApplication"
			parameter "Physical Trainer"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
		action {
			actionType "postponeReminder"
			parameter "UseVideoExerciseTrainer"
			parameter "24"
		}
	}

}