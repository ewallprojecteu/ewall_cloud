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
	statement "Hej [firstName]! Det ser ud som om, du ikke har kigget i din sundhedsbog i et stykke tid."
	
	basicReply {
		replyId "A"
		statement "Vis mig din sundhedsbog."
		action {
			actionType "openApplication"
			parameter "Healthcare Monitor"
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
	statement "Sidste gang, du kiggede i din sundhedsbog, var [daysAgoLastHealthInteraction], klokken [timeLastHealthInteraction]."
	
	basicReply {
		replyId "A"
		statement "Tak. Vis mig din sundhedsbog."
		action {
			actionType "openApplication"
			parameter "Healthcare Monitor"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
		action {
			actionType "postponeReminder"
			parameter "UseHealthApplication"
			parameter "24"
		}
	}

}