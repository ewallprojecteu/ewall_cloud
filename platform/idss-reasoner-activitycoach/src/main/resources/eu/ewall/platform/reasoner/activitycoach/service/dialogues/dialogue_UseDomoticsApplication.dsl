// [daysAgoLastDomoticsInteraction]
// [timeLastDomoticsInteraction]

dialogueTypeId "UseDomoticsApplication"
initialStepId "Initial"

// --------------------
// --- "Hello [firstName]! It looks like you haven't used the Domotics application for some time."
// ---   o "Show me the Domotics application."
// ---   o "When was the last time I looked at it?"
// ---   o "Goodbye."
// --------------------
step {
	stepId "Initial"
	statement "Hello [firstName]! It looks like you haven't used the Domotics application for some time."
	
	basicReply {
		replyId "A"
		statement "Show me the Domotics application."
		action {
			actionType "openApplication"
			parameter "Domotics"
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
			parameter "UseDomoticsApplication"
			parameter "24"
		}
	}
}

// --------------------
// --- "The last time you looked at the Domotics application was [daysAgoLastDomoticsInteraction], at [timeLastDomoticsInteraction]."
// ---   o "Thanks. Show me the Domotics application."
// ---   o "Goodbye."
// --------------------
step {
	stepId "LastCheckedTime"
	statement "The last time you looked at the Domotics application was [daysAgoLastDomoticsInteraction], at [timeLastDomoticsInteraction]."
	
	basicReply {
		replyId "A"
		statement "Thanks. Show me the Domotics application."
		action {
			actionType "openApplication"
			parameter "Domotics"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
		action {
			actionType "postponeReminder"
			parameter "UseDomoticsApplication"
			parameter "24"
		}
	}

}