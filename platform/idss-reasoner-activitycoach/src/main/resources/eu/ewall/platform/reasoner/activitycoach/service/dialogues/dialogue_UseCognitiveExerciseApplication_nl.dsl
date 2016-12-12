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
	statement "Hallo [firstName]! Het lijkt er op dat u al een tijdje geen eWALL spelletjes heeft gespeeld."
	
	basicReply {
		replyId "A"
		statement "Laat me de spelletjes app zien."
		action {
			actionType "openApplication"
			parameter "chessboard-button"
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
	statement "De laatste keer dat u een van de eWall spelletjes hebt gespeeld was [daysAgoLastDomoticsInteraction], om [timeLastDomoticsInteraction]."
	
	basicReply {
		replyId "A"
		statement "Bedankt. Laat me de spelletjes app zien."
		action {
			actionType "openApplication"
			parameter "chessboard-button"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
		action {
			actionType "postponeReminder"
			parameter "UseCognitiveExerciseApplication"
			parameter "24"
		}
	}

}