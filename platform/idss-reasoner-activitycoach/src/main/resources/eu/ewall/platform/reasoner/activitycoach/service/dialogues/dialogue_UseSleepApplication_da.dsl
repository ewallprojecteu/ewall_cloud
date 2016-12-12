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
	statement "Hej [firstName]! Det ser ud til at være længe siden, du sidst har kigget i din søvnbog."
	
	basicReply {
		replyId "A"
		statement "Vis mig hvor du ser din søvn"
		action {
			actionType "openApplication"
			parameter "Sleep Monitoring"
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
	statement "Sidste gang du så på søvnbogen var [daysAgoLastSleepInteraction], klokken [timeLastSleepInteraction]."
	
	basicReply {
		replyId "A"
		statement "Tak. Vis mig hvor du ser din søvn."
		action {
			actionType "openApplication"
			parameter "Sleep Monitoring"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
		action {
			actionType "postponeReminder"
			parameter "UseSleepApplication"
			parameter "24"
		}
	}

}