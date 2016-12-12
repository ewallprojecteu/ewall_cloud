// [daysAgoLastActivityInteraction]
// [timeLastActivityInteraction]

dialogueTypeId "UseActivityApplication"
initialStepId "Initial"

// --------------------
// --- "Hello [firstName]! It looks like you haven't looked at your Activity book for some time."
// ---   o "Show me My Activity book."
// ---   o "When was the last time I looked at it?"
// ---   o "Goodbye."
// --------------------
step {
	stepId "Initial"
	statement "Ciao [firstName]! Sembra che non guardi il tuo libro Attività da un po'."
	
	basicReply {
		replyId "A"
		statement "Mostrami l'applicazione L'Attività."
		action {
			actionType "openApplication"
			parameter "Daily Physical Activity Monitoring"
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
			parameter "UseActivityApplication"
			parameter "24"
		}
	}
}

// --------------------
// --- "The last time you looked at your Activity book was [daysAgoLastActivityInteraction], at [timeLastActivityInteraction]."
// ---   o "Thanks. Show me My Activity book."
// ---   o "Goodbye."
// --------------------
step {
	stepId "LastCheckedTime"
	statement "L'ultima volta che hai visto il tuo libro Attività è stata [daysAgoLastActivityInteraction], alle [timeLastActivityInteraction]."
	
	basicReply {
		replyId "A"
		statement "Grazie. Mostrami l'applicazione L'Attività."
		action {
			actionType "openApplication"
			parameter "Daily Physical Activity Monitoring"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
		action {
			actionType "postponeReminder"
			parameter "UseActivityApplication"
			parameter "24"
		}
	}

}