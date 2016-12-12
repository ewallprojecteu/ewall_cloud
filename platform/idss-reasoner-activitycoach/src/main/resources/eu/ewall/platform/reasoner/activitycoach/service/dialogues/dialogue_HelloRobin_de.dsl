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
	statement "Hallo [firstName], wie kann ich dir helfen?"
	statement "Hallo [firstName], was kann ich für dich tun?"
	statement "Hallo [firstName], wie kann ich dir heute helfen?"
	statement "Hallo [firstName], was kann ich heute für dich tun?"
	
	basicReply {
		replyId "A"
		statement "Ich möchte meinen Gesundheitsstatus aktualisieren."
		action {
			actionType "startDialogue"
			parameter "PhysicalActivityStageOfChangeFromUser"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Ich möchte eine Funktion öffnen."
		nextStepId "OpenApplication"
	}
	
	basicReply {
		replyId "C"
		statement "Ich habe eine Frage zu den Belohnungen."
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
		statement "Auf Wiedersehen."
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
	statement "Welche Funktion möchtest du nutzen?"
	
	basicReply {
		replyId "A"
		statement "Ich möchte ein Buch ansehen."
		nextStepId "OpenBookApplication"
	}
	
	basicReply {
		replyId "B"
		statement "Ich möchte ein Spiel spielen."
		action {
			actionType "openApplication"
			parameter "chessboard-button"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Ich möchte eine andere Funktion öffnen."
		nextStepId "OpenOtherApplication"
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
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
	statement "Welches Buch würdest du gerne sehen?"
	
	basicReply {
		replyId "A"
		statement "Zeig mir das Buch mit meiner Aktivitätsübersicht."
		action {
			actionType "openApplication"
			parameter "Daily Physical Activity Monitoring"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Zeig mir das Buch mit meiner Schlafübersicht."
		action {
			actionType "openApplication"
			parameter "Sleep Monitoring"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Zeig mir das Buch mit meiner Gesundheitsübersicht."
		action {
			actionType "openApplication"
			parameter "Healthcare Monitor"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Zeig mir Buch mit Hilfestellungen."
		action {
			actionType "openApplication"
			parameter "Help"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
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
	statement "Welche Anwendung soll ich öffnen?"
	
	basicReply {
		replyId "A"
		statement "Zeig mir den Hausstatus."
		action {
			actionType "openApplication"
			parameter "Domotics"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Zeig mir den Kalender."
		action {
			actionType "openApplication"
			parameter "Calendar"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Zeig mir den Video Trainer."
		action {
			actionType "openApplication"
			parameter "Physical Trainer"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Gibt es neue Anwendungen?"
		nextStepId "OpenNewApplication"
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
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
	statement "Das sind die aktuellen eWALL Funktionen. Welche möchtest du verwenden?"
	
	basicReply {
		replyId "A"
		statement "Zeig mir die Belohnungsübersicht."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Zeig mir die Sturzprävention-Funktion."
		action {
			actionType "openApplication"
			parameter "fallPrevention"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
	}
}