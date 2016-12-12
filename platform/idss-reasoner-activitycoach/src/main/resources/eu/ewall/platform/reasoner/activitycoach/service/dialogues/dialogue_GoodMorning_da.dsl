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
	statement "Godmorgen [firstName]! Hvordan vil du gerne starte dagen?"
	
	basicReply {
		replyId "A"
		statement "Vis mig min kalender for i dag."
		action {
			actionType "openApplication"
			parameter "Calendar"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Vis mig hvordan jeg har sovet."
		action {
			actionType "openApplication"
			parameter "My Sleep"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
	}
}