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
	statement "Ciao [firstName]! Sembra che non dai uno sguardo all'Allenatore Esercizi Video da un po'."
	
	basicReply {
		replyId "A"
		statement "Apri l'Allenatore Esercizi Video."
		action {
			actionType "openApplication"
			parameter "Physical Trainer"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Quando è stata l'ultima volta che l'hai consultata?"
		nextStepId "LastCheckedTime"
	}
	
	basicReply {
		replyId "BYE"
		statement "Arriverderci."
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
	statement "L'ultima volta che hai usato l'Allenatore Esercizi Video è stata [daysAgoLastVideoTrainerInteraction], alle [timeLastVideoTrainerInteraction]."
	
	basicReply {
		replyId "A"
		statement "Grazie. Apri l'Allenatore Esercizi Video."
		action {
			actionType "openApplication"
			parameter "Physical Trainer"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arriverderci."
		action {
			actionType "postponeReminder"
			parameter "UseVideoExerciseTrainer"
			parameter "24"
		}
	}

}