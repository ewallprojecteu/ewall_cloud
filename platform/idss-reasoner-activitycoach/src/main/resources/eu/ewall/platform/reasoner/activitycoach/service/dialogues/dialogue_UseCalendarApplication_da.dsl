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
	statement "Hej [firstName]! Det ser ud til, at du ikke har kigget i din kalender i et stykke tid."
	
	basicReply {
		replyId "A"
		statement "Vis mig kalenderen."
		action {
			actionType "openApplication"
			parameter "Calendar"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Hvornår så du sidst på det?"
		nextStepId "LastCheckedTime"
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
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
	statement "Sidste gang, du kiggede i din kalender, var [daysAgoLastCalendarInteraction], klokken [timeLastCalendarInteraction]."
	
	basicReply {
		replyId "A"
		statement "Tak. Vis mig kalenderen."
		action {
			actionType "openApplication"
			parameter "Calendar"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
		action {
			actionType "postponeReminder"
			parameter "UseCalendarApplication"
			parameter "24"
		}
	}

}