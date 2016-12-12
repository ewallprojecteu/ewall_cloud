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
	statement "Hej [firstName]! Det ser ud som om, du ikke har kigget i din aktivitetsbog i et stykke tid."
	
	basicReply {
		replyId "A"
		statement "Vis mig din aktivitetsbog."
		action {
			actionType "openApplication"
			parameter "Daily Physical Activity Monitoring"
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
	statement "Sidste gang, du kiggede i din aktivitetsbog, var [daysAgoLastActivityInteraction], klokken [timeLastActivityInteraction]."
	
	basicReply {
		replyId "A"
		statement "Tak. Vis mig din aktivitetsbog."
		action {
			actionType "openApplication"
			parameter "Daily Physical Activity Monitoring"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
		action {
			actionType "postponeReminder"
			parameter "UseActivityApplication"
			parameter "24"
		}
	}

}