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
	statement "Guten Morgen [firstName]! Womit möchtest du heute starten?"
	
	basicReply {
		replyId "A"
		statement "Zeige meinen heutigen Kalender."
		action {
			actionType "openApplication"
			parameter "Calendar"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Zeige mir wie gut ich geschlafen habe."
		action {
			actionType "openApplication"
			parameter "My Sleep"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
	}
}