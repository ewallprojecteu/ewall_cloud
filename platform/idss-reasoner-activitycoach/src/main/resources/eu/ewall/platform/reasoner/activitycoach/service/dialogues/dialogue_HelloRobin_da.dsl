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
	statement "Hej [firstName]. Hvordan kan jeg hjælpe dig?"
	statement "Hej [firstName]. Hvad kan jeg gøre for dig?"
	statement "Hej [firstName]. Hvordan kan jeg hjælpe dig i dag?"
	statement "Hej [firstName]. Hvad kan jeg gøre for dig i dag?"
	
	basicReply {
		replyId "A"
		statement "Jeg vil gerne opdatere mit forandringstrin."
		action {
			actionType "startDialogue"
			parameter "PhysicalActivityStageOfChangeFromUser"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Jeg vil gerne åbne en applikation."
		nextStepId "OpenApplication"
	}
	
	basicReply {
		replyId "C"
		statement "Jeg har et spørgsmål omkring præmier."
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
		statement "Farvel."
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
	statement "Hvilken applikation vil du gerne bruge?"
	
	basicReply {
		replyId "A"
		statement "Jeg vil gerne se i en bog."
		nextStepId "OpenBookApplication"
	}
	
	basicReply {
		replyId "B"
		statement "Jeg vil spille et spil."
		action {
			actionType "openApplication"
			parameter "chessboard-button"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Jeg vil åbne en anden applikation."
		nextStepId "OpenOtherApplication"
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
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
	statement "Hvilken bog vil du gerne se?"
	
	basicReply {
		replyId "A"
		statement "Vis mig bogen 'Mine Aktiviteter'."
		action {
			actionType "openApplication"
			parameter "Daily Physical Activity Monitoring"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Vis mig bogen 'Min Søvn'."
		action {
			actionType "openApplication"
			parameter "Sleep Monitoring"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Vis mig bogen 'Min Sundhed'."
		action {
			actionType "openApplication"
			parameter "Healthcare Monitor"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Vis mig bogen 'Hjælp'."
		action {
			actionType "openApplication"
			parameter "Help"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
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
	statement "Hvilken applikation skal jeg åbne?"
	
	basicReply {
		replyId "A"
		statement "Vis mig informationer om hjemmet."
		action {
			actionType "openApplication"
			parameter "Domotics"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Vis mig kalenderen."
		action {
			actionType "openApplication"
			parameter "Calendar"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Vis mig videotræningsøvelserne."
		action {
			actionType "openApplication"
			parameter "Physical Trainer"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Er der nogle nye applikationer?"
		nextStepId "OpenNewApplication"
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
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
	statement "Disse er de seneste eWALL applikationer. Hvilke vil du gerne se?"
	
	basicReply {
		replyId "A"
		statement "Vis mig Præmieoversigten."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Vis mig Faldforebyggeren."
		action {
			actionType "openApplication"
			parameter "fallPrevention"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
	}
}