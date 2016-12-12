// [daysAgoLastCalendarInteraction]
// [timeLastCalendarInteraction]

dialogueTypeId "UseCalendarApplication"
initialStepId "Initial"

// --------------------
// --- "Hello [firstName]! It looks like you haven't looked at your Calendar for some time."
// ---   o "Show me the Calendar application."
// ---   o "When was the last time I looked at it?"
// ---   o "Goodbye."
// --------------------
step {
	stepId "Initial"
	statement "Ciao [firstName]! Sembra che non guardi il tuo Calendario da un po'."
	
	basicReply {
		replyId "A"
		statement "Mostrami l'applicazione Calendario."
		action {
			actionType "openApplication"
			parameter "Calendar"
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
			parameter "UseCalendarApplication"
			parameter "24"
		}
	}
}

// --------------------
// --- "The last time you looked at your Calendar was [daysAgoLastCalendarInteraction], at [timeLastCalendarInteraction]."
// ---   o "Thanks. Show me the Calendar application."
// ---   o "Goodbye."
// --------------------
step {
	stepId "LastCheckedTime"
	statement "L'ultima volta che hai visto il Caledario è stata [daysAgoLastCalendarInteraction], alle [timeLastCalendarInteraction]."
	
	basicReply {
		replyId "A"
		statement "Grazie. Mostrami l'applicazione Calendario."
		action {
			actionType "openApplication"
			parameter "Calendar"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
		action {
			actionType "postponeReminder"
			parameter "UseCalendarApplication"
			parameter "24"
		}
	}

}