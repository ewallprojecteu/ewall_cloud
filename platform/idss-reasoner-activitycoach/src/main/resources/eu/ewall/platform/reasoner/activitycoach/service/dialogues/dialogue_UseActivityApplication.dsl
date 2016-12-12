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
	statement "Hello [firstName]! It looks like you haven't looked at your Activity book for some time."
	
	basicReply {
		replyId "A"
		statement "Show me My Activity book."
		action {
			actionType "openApplication"
			parameter "Daily Physical Activity Monitoring"
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
	statement "The last time you looked at your Activity book was [daysAgoLastActivityInteraction], at [timeLastActivityInteraction]."
	
	basicReply {
		replyId "A"
		statement "Thanks. Show me My Activity book."
		action {
			actionType "openApplication"
			parameter "Daily Physical Activity Monitoring"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
		action {
			actionType "postponeReminder"
			parameter "UseActivityApplication"
			parameter "24"
		}
	}

}