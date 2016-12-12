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
	statement "Ciao [firstName]! Sembra che non giochi con i Giochi di eWALL da un po'."
	
	basicReply {
		replyId "A"
		statement "Mostrami l'applicazione Giochi."
		action {
			actionType "openApplication"
			parameter "chessboard-button"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Quando è stata l'ultima volta che l'hai consultata?"
		nextStepId "LastCheckedTime"
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "L'ultima volta che hai giocato con i Giochi eWALL è stata [daysAgoLastCognitiveTrainerInteraction], alle [timeLastCognitiveTrainerInteraction]."
	
	basicReply {
		replyId "A"
		statement "Grazie. Mostrami l'applicazione Giochi."
		action {
			actionType "openApplication"
			parameter "chessboard-button"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
		action {
			actionType "postponeReminder"
			parameter "UseCognitiveExerciseApplication"
			parameter "24"
		}
	}

}