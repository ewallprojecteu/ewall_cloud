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
	statement "Hallo [firstName]! Siehst so aus, als hättest du schon länger nicht mehr deine Gesundheitsinformationen angesehen."
	
	basicReply {
		replyId "A"
		statement "Zeige mir meinen Gesundheitsinformationen."
		action {
			actionType "openApplication"
			parameter "Healthcare Monitor"
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
	statement "Du hast zuletzt vor [daysAgoLastHealthInteraction] um [timeLastHealthInteraction] das Buch deiner Gersundheitsinformationen angesehen."
	
	basicReply {
		replyId "A"
		statement "Danke. Zeige mir meine Gesundheitsinformationen."
		action {
			actionType "openApplication"
			parameter "Healthcare Monitor"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
		action {
			actionType "postponeReminder"
			parameter "UseHealthApplication"
			parameter "24"
		}
	}

}