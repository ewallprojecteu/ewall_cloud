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
	statement "Hej [firstName]! Det ser ud til, at du ikke har kigget på dine informationer om hjemmet i et stykke tid."
	
	basicReply {
		replyId "A"
		statement "Vis mig dine informationer om hjemmet."
		action {
			actionType "openApplication"
			parameter "Domotics"
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
	statement "Sidste gang, du så på dine informationer om hjemmet, var [daysAgoLastDomoticsInteraction], klokken [timeLastDomoticsInteraction]."
	
	basicReply {
		replyId "A"
		statement "Tak. Vis mig dine informationer om hjemmet."
		action {
			actionType "openApplication"
			parameter "Domotics"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
		action {
			actionType "postponeReminder"
			parameter "UseDomoticsApplication"
			parameter "24"
		}
	}

}