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
	statement "Hallo [firstName]! Het lijkt er op dat u al een tijdje niet in uw Mijn Gezondheid boek hebt gekeken."
	
	basicReply {
		replyId "A"
		statement "Laat me Mijn Gezondheid boek zien."
		action {
			actionType "openApplication"
			parameter "Healthcare Monitor"
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
	statement "De laatste keer dat u naar uw Gezondheid boek hebt gekeken was [daysAgoLastHealthInteraction], om [timeLastHealthInteraction]."
	
	basicReply {
		replyId "A"
		statement "Bedankt. Laat me Mijn Gezondheid boek zien."
		action {
			actionType "openApplication"
			parameter "Healthcare Monitor"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
		action {
			actionType "postponeReminder"
			parameter "UseHealthApplication"
			parameter "24"
		}
	}

}