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
	statement "Goedemorgen [firstName]! Waar wilt u vandaag mee beginnen?"
	
	basicReply {
		replyId "A"
		statement "Laat me mijn agenda voor vandaag zien."
		action {
			actionType "openApplication"
			parameter "Calendar"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Laat zien hoe ik heb geslapen."
		action {
			actionType "openApplication"
			parameter "My Sleep"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
	}
}