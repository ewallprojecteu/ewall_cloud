// [daysAgoLastSleepInteraction]
// [timeLastSleepInteraction]

dialogueTypeId "UseSleepApplication"
initialStepId "Initial"

// --------------------
// --- "Hello [firstName]! It looks like you haven't looked at the My Sleep book for some time."
// ---   o "Show me My Sleep application."
// ---   o "When was the last time I looked at it?"
// ---   o "Goodbye."
// --------------------
step {
	stepId "Initial"
	statement "Hallo [firstName]! Siehst so aus, als hättest du schon länger nicht mehr das Buch deiner Schlafinformationen angesehen."
	
	basicReply {
		replyId "A"
		statement "Ziege mir die Funktion Schlaf."
		action {
			actionType "openApplication"
			parameter "Sleep Monitoring"
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
			parameter "UseSleepApplication"
			parameter "24"
		}
	}
}

// --------------------
// --- "The last time you looked at the My Sleep book was [daysAgoLastSleepInteraction], at [timeLastSleepInteraction]."
// ---   o "Thanks. Show me My Sleep application."
// ---   o "Goodbye."
// --------------------
step {
	stepId "LastCheckedTime"
	statement "Du hast zuletzt vor [daysAgoLastSleepInteraction] um [timeLastSleepInteraction] Uhrdas Buch deiner Schlafinformationen angesehen."
	
	basicReply {
		replyId "A"
		statement "Danke. Zeige mir meine Schlafinformationen."
		action {
			actionType "openApplication"
			parameter "Sleep Monitoring"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
		action {
			actionType "postponeReminder"
			parameter "UseSleepApplication"
			parameter "24"
		}
	}

}