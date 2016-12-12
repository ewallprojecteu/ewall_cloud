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
	statement "Hello [firstName]! It looks like you haven't looked at the My Health book for some time."
	
	basicReply {
		replyId "A"
		statement "Show me My Health book."
		action {
			actionType "openApplication"
			parameter "Healthcare Monitor"
		}
	}
	
	basicReply {
		replyId "B"
		statement "When was the last time I looked at it?"
		nextStepId "LastCheckedTime"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
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
	statement "The last time you looked at the My Health book was [daysAgoLastHealthInteraction], at [timeLastHealthInteraction]."
	
	basicReply {
		replyId "A"
		statement "Thanks. Show me My Health book."
		action {
			actionType "openApplication"
			parameter "Healthcare Monitor"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
		action {
			actionType "postponeReminder"
			parameter "UseHealthApplication"
			parameter "24"
		}
	}

}