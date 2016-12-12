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
	statement "Hallo [firstName]! Het lijkt erop dat u de Domotica app al een tijdje niet heeft gebruikt."
	
	basicReply {
		replyId "A"
		statement "Laat me de Domotica app zien."
		action {
			actionType "openApplication"
			parameter "Domotics"
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
	statement "De laatste keer dat u naar de Domotica app hebt gekeken was [daysAgoLastDomoticsInteraction], om [timeLastDomoticsInteraction]."
	
	basicReply {
		replyId "A"
		statement "Bedankt. Laat me de Domotica app zien."
		action {
			actionType "openApplication"
			parameter "Domotics"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
		action {
			actionType "postponeReminder"
			parameter "UseDomoticsApplication"
			parameter "24"
		}
	}

}