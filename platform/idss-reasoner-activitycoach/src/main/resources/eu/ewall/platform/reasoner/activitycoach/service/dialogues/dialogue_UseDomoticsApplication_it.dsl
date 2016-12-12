// [daysAgoLastDomoticsInteraction]
// [timeLastDomoticsInteraction]

dialogueTypeId "UseDomoticsApplication"
initialStepId "Initial"

// --------------------
// --- "Hello [firstName]! It looks like you haven't used the Domotics application for some time."
// ---   o "Show me the Domotics application."
// ---   o "When was the last time I looked at it?"
// ---   o "Goodbye."
// --------------------
step {
	stepId "Initial"
	statement "Ciao [firstName]! Sembra che non usi l'applicazione Domotica da un po'."
	
	basicReply {
		replyId "A"
		statement "Mostrami l'applicazione Domotica."
		action {
			actionType "openApplication"
			parameter "Domotics"
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
			parameter "UseDomoticsApplication"
			parameter "24"
		}
	}
}

// --------------------
// --- "The last time you looked at the Domotics application was [daysAgoLastDomoticsInteraction], at [timeLastDomoticsInteraction]."
// ---   o "Thanks. Show me the Domotics application."
// ---   o "Goodbye."
// --------------------
step {
	stepId "LastCheckedTime"
	statement "L'ultima volta che hai visto l'applicazione Domotica è stata [daysAgoLastDomoticsInteraction], alle [timeLastDomoticsInteraction]."
	
	basicReply {
		replyId "A"
		statement "Grazie. Mostrami l'applicazione Domotica."
		action {
			actionType "openApplication"
			parameter "Domotics"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
		action {
			actionType "postponeReminder"
			parameter "UseDomoticsApplication"
			parameter "24"
		}
	}

}