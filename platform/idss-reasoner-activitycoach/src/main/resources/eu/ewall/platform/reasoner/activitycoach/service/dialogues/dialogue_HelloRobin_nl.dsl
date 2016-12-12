// --- Open Application options:
// --- - "Calendar"
// --- - "Sleep Monitoring"
// --- - "Physical Trainer"
// --- - "chessboard-button"
// --- - "Help"
// --- - "Healthcare Monitor"
// --- - "Domotics"
// --- - "Daily Functioning Monitor"
// --- - "rewardSystem"
// --- - "fallPrevention"
// --- - "Daily Physical Activity Monitoring"

dialogueTypeId "HelloRobin"
initialStepId "Initial"

// --------------------
// --- "Hello, how can I help you?"
// ---  o "I would like to update my stage of change."
// ---  o "I would like to open an application."
// ---  o "I want to talk about rewards."
// ---  o "Show me some of your upcoming features."
// ---  o Goodbye.
// --------------------
step {
	stepId "Initial"
	statement "Hallo [firstName], waar kan ik je mee helpen?"
	statement "Hallo [firstName], wat kan ik voor je doen?"
	statement "Hallo [firstName], waar kan ik je vandaag mee helpen?"
	statement "Hallo [firstName], wat kan ik vandaag voor je doen?"
	
	basicReply {
		replyId "A"
		statement "Ik wil graag mijn Stadia van Gedragsverandering updaten."
		action {
			actionType "startDialogue"
			parameter "PhysicalActivityStageOfChangeFromUser"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Ik zou graag een applicatie willen openen."
		nextStepId "OpenApplication"
	}
	
	basicReply {
		replyId "C"
		statement "Ik heb een vraag over beloningen."
		action {
			actionType "startDialogue"
			parameter "Rewards"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Show me some of your upcoming features."
		action {
			actionType "startDialogue"
			parameter "UpcomingFeatures"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
	}
}

// --------------------
// --- "Which application would you like to use?"
// ---  o "I want to look at a book."
// ---  o "I want to play a game."
// ---  o "I want to open another application."
// ---  o Goodbye.
// --------------------
step {
	stepId "OpenApplication"
	statement "Welke applicatie wilt u graag gebruiken?"
	
	basicReply {
		replyId "A"
		statement "Ik wil een boek bekijken."
		nextStepId "OpenBookApplication"
	}
	
	basicReply {
		replyId "B"
		statement "Ik wil een spelletje spelen."
		action {
			actionType "openApplication"
			parameter "chessboard-button"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Ik wil een andere app openen."
		nextStepId "OpenOtherApplication"
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
	}
}

// --------------------
// --- "Which book would you like to see?"
// ---  o "Show me 'My Activity' book."
// ---  o "Show me 'My Sleep' book."
// ---  o "Show me 'My Health' book."
// ---  o "Show me the 'Help' book."
// ---  o Goodbye.
// --------------------
step {
	stepId "OpenBookApplication"
	statement "Welk boek wilt u graag zien?"
	
	basicReply {
		replyId "A"
		statement "Laat me 'Mijn activiteiten' boek zien."
		action {
			actionType "openApplication"
			parameter "Daily Physical Activity Monitoring"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Laat me 'Mijn slaap' boek zien."
		action {
			actionType "openApplication"
			parameter "Sleep Monitoring"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Laat me 'Mijn gezondheid' boek zien."
		action {
			actionType "openApplication"
			parameter "Healthcare Monitor"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Laat me het 'Help' boek zien."
		action {
			actionType "openApplication"
			parameter "Help"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
	}
}

// --------------------
// --- "Which application should I open?"
// ---  o "Show me the domotics application."
// ---  o "Show me the calendar."
// ---  o "Show me the video trainer."
// ---  o "Are there any new applications?"
// ---  o Goodbye.
// --------------------
step {
	stepId "OpenOtherApplication"
	statement "Welke applicatie moet ik openen?"
	
	basicReply {
		replyId "A"
		statement "Laat me de Domotica applicatie zien."
		action {
			actionType "openApplication"
			parameter "Domotics"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Laat me de agenda zien."
		action {
			actionType "openApplication"
			parameter "Calendar"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Laat me de gymnastiek applicatie zien."
		action {
			actionType "openApplication"
			parameter "Physical Trainer"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Zijn er andere nieuwe apps?"
		nextStepId "OpenNewApplication"
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
	}
}

// --------------------
// --- "These are the latest eWALL applications, which one would you like to use?"
// ---  o "Show me the Reward Overview application."
// ---  o "Show me the Fall Prevention application."
// ---  o Goodbye.
// --------------------
step {
	stepId "OpenNewApplication"
	statement "Dit zijn de laatste eWALL apps, welke wilt u graag gebruiken?"
	
	basicReply {
		replyId "A"
		statement "Laat me het Beloning Overzicht zien."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Laat me de Val Preventie app zien."
		action {
			actionType "openApplication"
			parameter "fallPrevention"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
	}
}