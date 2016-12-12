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
	statement "Hallo [firstName]! Het lijkt er op dat u al een tijdje niet meer naar uw Activiteit boek heeft gekeken."
	
	basicReply {
		replyId "A"
		statement "Laat me Mijn Activiteit boek zien."
		action {
			actionType "openApplication"
			parameter "Daily Physical Activity Monitoring"
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
	statement "De laatste keer dat u naar uw Activiteit boek hebt gekeken was [daysAgoLastActivityInteraction], om [timeLastActivityInteraction]."
	
	basicReply {
		replyId "A"
		statement "Bedankt. Laat me Mijn Activiteit boek zien."
		action {
			actionType "openApplication"
			parameter "Daily Physical Activity Monitoring"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
		action {
			actionType "postponeReminder"
			parameter "UseActivityApplication"
			parameter "24"
		}
	}

}