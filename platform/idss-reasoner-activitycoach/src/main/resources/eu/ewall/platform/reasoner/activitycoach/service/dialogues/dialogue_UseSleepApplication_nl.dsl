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
	statement "Hallo [firstName]! Het lijkt er op dat u al een tijdje niet meer naar uw Mijn Slaap boek hebt gekeken."
	
	basicReply {
		replyId "A"
		statement "Laat me Mijn Slaap boek zien."
		action {
			actionType "openApplication"
			parameter "Sleep Monitoring"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Wanneer heb ik voor het laatst gekeken?"
		nextStepId "LastCheckedTime"
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "De laatste keer dat u naar uw Mijn Slaap boek hebt gekeken was [daysAgoLastSleepInteraction], om [timeLastSleepInteraction]."
	
	basicReply {
		replyId "A"
		statement "Bedankt. Laat me Mijn Slaap boek zien."
		action {
			actionType "openApplication"
			parameter "Sleep Monitoring"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
		action {
			actionType "postponeReminder"
			parameter "UseSleepApplication"
			parameter "24"
		}
	}

}