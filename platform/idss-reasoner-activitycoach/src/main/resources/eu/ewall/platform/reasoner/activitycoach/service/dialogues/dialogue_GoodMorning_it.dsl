dialogueTypeId "GoodMorning"
initialStepId "Initial"

// --------------------
// --- "Good morning [firstName]! What would you like to start today with?"
// ---   o "Show my calendar for today."
// ---   o "Show me how I slept."
// ---   o "Goodbye."
// --------------------
step {
	stepId "Initial"
	statement "Buon giorno [firstName]! Con cosa vorresti partire oggi?"
	
	basicReply {
		replyId "A"
		statement "Mostrami il calendario di oggi."
		action {
			actionType "openApplication"
			parameter "Calendar"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Mostrami come ho dormito."
		action {
			actionType "openApplication"
			parameter "My Sleep"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
	}
}