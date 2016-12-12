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
	statement "Ciao [firstName]! Sembra che non dai uno sguardo al libro 'il Mio Sonno' da un po."
	
	basicReply {
		replyId "A"
		statement "Mostrami l'applicazione Il Mio Sonno."
		action {
			actionType "openApplication"
			parameter "Sleep Monitoring"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Quando è stata l'ultima volta che l'hai consultata?"
		nextStepId "LastCheckedTime"
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "L'ultima volta che hai consultato L'applicazione Il Mio Sonno è stata [daysAgoLastSleepInteraction], alle [timeLastSleepInteraction]."
	
	basicReply {
		replyId "A"
		statement "Grazie, Mostrami l'applicazione Il Mio Sonno."
		action {
			actionType "openApplication"
			parameter "Sleep Monitoring"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
		action {
			actionType "postponeReminder"
			parameter "UseSleepApplication"
			parameter "24"
		}
	}

}