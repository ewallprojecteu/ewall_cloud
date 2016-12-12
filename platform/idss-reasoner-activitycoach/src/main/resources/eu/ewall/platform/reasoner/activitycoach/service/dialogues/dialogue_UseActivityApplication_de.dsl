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
	statement "Hallo [firstName]! Siehst so aus, als hättest du schon länger nicht mehr diene Aktivitätsübersicht angesehen."
	
	basicReply {
		replyId "A"
		statement "Zeige mir meine Aktivitätsübersicht."
		action {
			actionType "openApplication"
			parameter "Daily Physical Activity Monitoring"
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
	statement "Du hast zuletzt vor [daysAgoLastActivityInteraction] um [timeLastActivityInteraction] Uhr das Buch deiner Aktivitätsübersicht angesehen."
	
	basicReply {
		replyId "A"
		statement "Danke. Zeige mir meine Aktivitätsübersicht."
		action {
			actionType "openApplication"
			parameter "Daily Physical Activity Monitoring"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
		action {
			actionType "postponeReminder"
			parameter "UseActivityApplication"
			parameter "24"
		}
	}

}