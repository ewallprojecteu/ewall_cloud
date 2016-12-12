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
	statement "Hallo [firstName]! Siehst so aus, als hättest du schon länger nicht mehr die den Hausstatus angesehen."
	
	basicReply {
		replyId "A"
		statement "Zeige mir den Hausstatus."
		action {
			actionType "openApplication"
			parameter "Domotics"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Wann habe ich das zuletzt angesehen?"
		nextStepId "LastCheckedTime"
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
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
	statement "Du hast zuletzt vor [daysAgoLastDomoticsInteraction] um [timeLastDomoticsInteraction] Uhr den Hausstatus angesehen."
	
	basicReply {
		replyId "A"
		statement "Danke. Zeige mir den Hausstatus."
		action {
			actionType "openApplication"
			parameter "Domotics"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
		action {
			actionType "postponeReminder"
			parameter "UseDomoticsApplication"
			parameter "24"
		}
	}

}