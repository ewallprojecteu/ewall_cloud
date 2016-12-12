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
	statement "Hallo [firstName]! Siehst so aus, als hättest du schon länger nicht mehr eWALL Spiele gespielt."
	
	basicReply {
		replyId "A"
		statement "Zeige mir die Spielauswahl."
		action {
			actionType "openApplication"
			parameter "chessboard-button"
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
	statement "Du hast zuletzt vor [daysAgoLastCognitiveTrainerInteraction] um [timeLastCognitiveTrainerInteraction] Uhr ein Spiel gespielt."
	
	basicReply {
		replyId "A"
		statement "Danke. Zeige mir die Spielauswahl."
		action {
			actionType "openApplication"
			parameter "chessboard-button"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
		action {
			actionType "postponeReminder"
			parameter "UseCognitiveExerciseApplication"
			parameter "24"
		}
	}

}