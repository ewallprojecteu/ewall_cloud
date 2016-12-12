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
	statement "Hallo [firstName]! Siehst so aus, als hättest du schon länger nicht mehr deinen Kalender geöffnet."
	
	basicReply {
		replyId "A"
		statement "Zeige mir meinen Kalender."
		action {
			actionType "openApplication"
			parameter "Calendar"
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
	statement "Du hast zuletzt vor [daysAgoLastCalendarInteraction] um [timeLastCalendarInteraction] Uhr den Kalender geöffnet."
	
	basicReply {
		replyId "A"
		statement "Danke. Zeige mir den Kalender."
		action {
			actionType "openApplication"
			parameter "Calendar"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
		action {
			actionType "postponeReminder"
			parameter "UseCalendarApplication"
			parameter "24"
		}
	}

}