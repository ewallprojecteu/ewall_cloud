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
	statement "Good morning [firstName]! What would you like to start today with?"
	
	basicReply {
		replyId "A"
		statement "Show my calendar for today."
		action {
			actionType "openApplication"
			parameter "Calendar"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Show me how I slept."
		action {
			actionType "openApplication"
			parameter "My Sleep"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}