// [daysAgoLastCognitiveTrainerInteraction]
// [timeLastCognitiveTrainerInteraction]

dialogueTypeId "UseCognitiveExerciseApplication"
initialStepId "Initial"

// --------------------
// --- "Hello [firstName]! It looks like you haven't played any eWALL Games for some time."
// ---   o "Show me the Game Board application."
// ---   o "When was the last time I looked at it?"
// ---   o "Goodbye."
// --------------------
step {
	stepId "Initial"
	statement "Hej [firstName]! Det ser ud som om, du ikke har spillet et eWALL spil i et stykke tid."
	
	basicReply {
		replyId "A"
		statement "Vis mig eWALL spillene."
		action {
			actionType "openApplication"
			parameter "chessboard-button"
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
			parameter "UseCognitiveExerciseApplication"
			parameter "24"
		}
	}
}

// --------------------
// --- "The last time you played any of the eWALL Games was [daysAgoLastCognitiveTrainerInteraction], at [timeLastCognitiveTrainerInteraction]."
// ---   o "Thanks. Show me the Game Board application."
// ---   o "Goodbye."
// --------------------
step {
	stepId "LastCheckedTime"
	statement "Sidste gang, du spillede et eWALL spil, var [daysAgoLastCognitiveTrainerInteraction], klokken [timeLastCognitiveTrainerInteraction]."
	
	basicReply {
		replyId "A"
		statement "Tak. Vis mig eWALL spillene."
		action {
			actionType "openApplication"
			parameter "chessboard-button"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
		action {
			actionType "postponeReminder"
			parameter "UseCognitiveExerciseApplication"
			parameter "24"
		}
	}

}