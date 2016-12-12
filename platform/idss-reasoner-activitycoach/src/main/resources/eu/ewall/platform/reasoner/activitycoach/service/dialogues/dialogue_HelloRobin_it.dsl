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
	statement "Ciao [firstName], come posso aiutarti?"
	statement "Ciao [firstName], cosa posso fare per te?"
	statement "Ciao [firstName], come posso aiutarti oggi?"
	statement "Ciao [firstName], cosa posso fare per te oggi?"
	
	basicReply {
		replyId "A"
		statement "Mi piacerebbe aggiornare il mio stadio del cambiamento."
		action {
			actionType "startDialogue"
			parameter "PhysicalActivityStageOfChangeFromUser"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Mi piacerebbe aprire un'applicazione"
		nextStepId "OpenApplication"
	}
	
	basicReply {
		replyId "C"
		statement "Ho una domanda sui premi."
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
		statement "Arrivederci."
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
	statement "Quale applicazione vorresti usare?"
	
	basicReply {
		replyId "A"
		statement "Voglio guardare un libro."
		nextStepId "OpenBookApplication"
	}
	
	basicReply {
		replyId "B"
		statement "Voglio giocare."
		action {
			actionType "openApplication"
			parameter "chessboard-button"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Voglio aprire un'altra applicazione."
		nextStepId "OpenOtherApplication"
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "Quale libro vorresti vedere?"
	
	basicReply {
		replyId "A"
		statement "Mostrami il libro 'La Attività'."
		action {
			actionType "openApplication"
			parameter "Daily Physical Activity Monitoring"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Mostrami il libro 'Il Sonno'."
		action {
			actionType "openApplication"
			parameter "Sleep Monitoring"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Mostrami il libro 'La Salute'."
		action {
			actionType "openApplication"
			parameter "Healthcare Monitor"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Mostrami il libro 'Aiuto'."
		action {
			actionType "openApplication"
			parameter "Help"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "Quale applicazione dovrei aprire?"
	
	basicReply {
		replyId "A"
		statement "Mostrami l'applicazione Domotica."
		action {
			actionType "openApplication"
			parameter "Domotics"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Mostrami il calendario."
		action {
			actionType "openApplication"
			parameter "Calendar"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Mostrami l'allenatore video."
		action {
			actionType "openApplication"
			parameter "Physical Trainer"
		}
	}
	
	basicReply {
		replyId "D"
		statement "C'è qualche applicazione nuova?"
		nextStepId "OpenNewApplication"
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "Queste sono le ultime applicazioni eWALL, quale vorresti aprire?"
	
	basicReply {
		replyId "A"
		statement "Mostrami l'applicazione 'Panoramica sui Premi'."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Mostrami l'applicazione 'Prevenzione delle Cadute'."
		action {
			actionType "openApplication"
			parameter "fallPrevention"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
	}
}