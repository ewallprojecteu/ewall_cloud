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
	statement "Hello [firstName]! It looks like you haven't looked at the My Sleep book for some time."
	
	basicReply {
		replyId "A"
		statement "Show me My Sleep application."
		action {
			actionType "openApplication"
			parameter "Sleep Monitoring"
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
	statement "The last time you looked at the My Sleep book was [daysAgoLastSleepInteraction], at [timeLastSleepInteraction]."
	
	basicReply {
		replyId "A"
		statement "Thanks. Show me My Sleep application."
		action {
			actionType "openApplication"
			parameter "Sleep Monitoring"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
		action {
			actionType "postponeReminder"
			parameter "UseSleepApplication"
			parameter "24"
		}
	}

}