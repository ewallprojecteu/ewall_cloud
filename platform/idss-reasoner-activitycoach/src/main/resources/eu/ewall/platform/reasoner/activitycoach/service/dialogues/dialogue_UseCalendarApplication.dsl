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
	statement "Hello [firstName]! It looks like you haven't looked at your Calendar for some time."
	
	basicReply {
		replyId "A"
		statement "Show me the Calendar application."
		action {
			actionType "openApplication"
			parameter "Calendar"
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
	statement "The last time you looked at your Calendar was [daysAgoLastCalendarInteraction], at [timeLastCalendarInteraction]."
	
	basicReply {
		replyId "A"
		statement "Thanks. Show me the Calendar application."
		action {
			actionType "openApplication"
			parameter "Calendar"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
		action {
			actionType "postponeReminder"
			parameter "UseCalendarApplication"
			parameter "24"
		}
	}

}