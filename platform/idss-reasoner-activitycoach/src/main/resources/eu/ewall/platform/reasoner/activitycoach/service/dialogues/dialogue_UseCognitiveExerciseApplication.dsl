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
	statement "Hello [firstName]! It looks like you haven't played any eWALL Games for some time."
	
	basicReply {
		replyId "A"
		statement "Show me the Game Board application."
		action {
			actionType "openApplication"
			parameter "chessboard-button"
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
	statement "The last time you played any of the eWALL Games was [daysAgoLastCognitiveTrainerInteraction], at [timeLastCognitiveTrainerInteraction]."
	
	basicReply {
		replyId "A"
		statement "Thanks. Show me the Game Board application."
		action {
			actionType "openApplication"
			parameter "chessboard-button"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
		action {
			actionType "postponeReminder"
			parameter "UseCognitiveExerciseApplication"
			parameter "24"
		}
	}

}