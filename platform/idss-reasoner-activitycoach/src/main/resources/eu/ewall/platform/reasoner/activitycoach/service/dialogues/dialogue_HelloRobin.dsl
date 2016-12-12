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
// ---  o "I have a question about rewards."
// ---  o "Show me some of your upcoming features."
// ---  o "Goodbye."
// --------------------
step {
	stepId "Initial"
	statement "Hello [firstName], how can I help you?"
	statement "Hello [firstName], what can I do for you?"
	statement "Hello [firstName], how may I help you today?"
	statement "Hello [firstName], what can I do for you today?"

	basicReply {
		replyId "A"
		statement "I would like to update my stage of change."
		action {
			actionType "startDialogue"
			parameter "PhysicalActivityStageOfChangeFromUser"
		}
	}

	basicReply {
		replyId "B"
		statement "I would like to open an application."
		nextStepId "OpenApplication"
	}

	basicReply {
		replyId "C"
		statement "I have a question about rewards."
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
		statement "Goodbye."
	}
}

// --------------------
// --- "Which application would you like to use?"
// ---  o "I want to look at a book."
// ---  o "I want to play a game."
// ---  o "I want to open another application."
// ---  o "Goodbye."
// --------------------
step {
	stepId "OpenApplication"
	statement "Which application would you like to use?"

	basicReply {
		replyId "A"
		statement "I want to look at a book."
		nextStepId "OpenBookApplication"
	}

	basicReply {
		replyId "B"
		statement "I want to play a game."
		action {
			actionType "openApplication"
			parameter "chessboard-button"
		}
	}

	basicReply {
		replyId "C"
		statement "I want to open another application."
		nextStepId "OpenOtherApplication"
	}

	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Which book would you like to see?"
// ---  o "Show me 'My Activity' book."
// ---  o "Show me 'My Sleep' book."
// ---  o "Show me 'My Health' book."
// ---  o "Show me the 'Help' book."
// ---  o "Goodbye."
// --------------------
step {
	stepId "OpenBookApplication"
	statement "Which book would you like to see?"

	basicReply {
		replyId "A"
		statement "Show me 'My Activity' book."
		action {
			actionType "openApplication"
			parameter "Daily Physical Activity Monitoring"
		}
	}

	basicReply {
		replyId "B"
		statement "Show me 'My Sleep' book."
		action {
			actionType "openApplication"
			parameter "Sleep Monitoring"
		}
	}

	basicReply {
		replyId "C"
		statement "Show me 'My Health' book."
		action {
			actionType "openApplication"
			parameter "Healthcare Monitor"
		}
	}

	basicReply {
		replyId "D"
		statement "Show me the 'Help' book."
		action {
			actionType "openApplication"
			parameter "Help"
		}
	}

	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Which application should I open?"
// ---  o "Show me the domotics application."
// ---  o "Show me the calendar."
// ---  o "Show me the video trainer."
// ---  o "Are there any new applications?"
// ---  o "Goodbye."
// --------------------
step {
	stepId "OpenOtherApplication"
	statement "Which application should I open?"

	basicReply {
		replyId "A"
		statement "Show me the domotics application."
		action {
			actionType "openApplication"
			parameter "Domotics"
		}
	}

	basicReply {
		replyId "B"
		statement "Show me the calendar."
		action {
			actionType "openApplication"
			parameter "Calendar"
		}
	}

	basicReply {
		replyId "C"
		statement "Show me the video trainer."
		action {
			actionType "openApplication"
			parameter "Physical Trainer"
		}
	}

	basicReply {
		replyId "D"
		statement "Are there any new applications?"
		nextStepId "OpenNewApplication"
	}

	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "These are the latest eWALL applications, which one would you like to use?"
// ---  o "Show me the Reward Overview application."
// ---  o "Show me the Fall Prevention application."
// ---  o "Goodbye."
// --------------------
step {
	stepId "OpenNewApplication"
	statement "These are the latest eWALL applications, which one would you like to use?"

	basicReply {
		replyId "A"
		statement "Show me the Reward Overview application."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}

	basicReply {
		replyId "B"
		statement "Show me the Fall Prevention application."
		action {
			actionType "openApplication"
			parameter "fallPrevention"
		}
	}

	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}
