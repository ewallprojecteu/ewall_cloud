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
	statement "Hallo [firstName]! Het lijkt er op dat u al een tijdje niet meer in uw agenda heeft gekeken."
	
	basicReply {
		replyId "A"
		statement "Laat me de agenda applicatie zien."
		action {
			actionType "openApplication"
			parameter "Calendar"
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
	statement "De laatste keer dat u in uw agenda heeft gekeken was [daysAgoLastCalendarInteraction], om [timeLastCalendarInteraction]."
	
	basicReply {
		replyId "A"
		statement "Bedankt. Laat me de agenda applicatie zien."
		action {
			actionType "openApplication"
			parameter "Calendar"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
		action {
			actionType "postponeReminder"
			parameter "UseCalendarApplication"
			parameter "24"
		}
	}

}