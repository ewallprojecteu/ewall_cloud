// [daysAgoLastHealthInteraction]
// [timeLastHealthInteraction]

dialogueTypeId "UseHealthApplication"
initialStepId "Initial"

// --------------------
// --- "Hello [firstName]! It looks like you haven't looked at the My Health book for some time."
// ---   o "Show me My Health book."
// ---   o "When was the last time I looked at it?"
// ---   o "Goodbye."
// --------------------
step {
	stepId "Initial"
	statement "Ciao [firstName]! Sembra che non guardi il tuo libro La Salute da un po'."
	
	basicReply {
		replyId "A"
		statement "Mostrami il libro La Salute."
		action {
			actionType "openApplication"
			parameter "Healthcare Monitor"
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
			parameter "UseHealthApplication"
			parameter "24"
		}
	}
}

// --------------------
// --- "The last time you looked at the My Health book was [daysAgoLastHealthInteraction], at [timeLastHealthInteraction]."
// ---   o "Thanks. Show me My Health book."
// ---   o "Goodbye."
// --------------------
step {
	stepId "LastCheckedTime"
	statement "L'ultima volta che hai visto l tuo libro La Salute è stata [daysAgoLastHealthInteraction], alle [timeLastHealthInteraction]."
	
	basicReply {
		replyId "A"
		statement "Grazie. Mostrami il libro La Salute."
		action {
			actionType "openApplication"
			parameter "Healthcare Monitor"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
		action {
			actionType "postponeReminder"
			parameter "UseHealthApplication"
			parameter "24"
		}
	}

}