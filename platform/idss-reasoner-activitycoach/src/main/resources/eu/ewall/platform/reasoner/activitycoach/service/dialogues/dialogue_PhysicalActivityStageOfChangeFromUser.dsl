dialogueTypeId "PhysicalActivityStageOfChangeFromUser"
initialStepId "Initial"

// --------------------
// --- "To update your stage of change, all you have to is answer one simple question. Are you ready?"
// ---  o "Yes, ask me the question."
// ---  o "Can you tell me a little more about stage of change?"
// ---  o "Goodbye."
// --------------------
step {
	stepId "Initial"
	statement "To update your stage of change, all you have to is answer one simple question. Are you ready?"
	
	basicReply {
		replyId "A"
		statement "Yes, ask me the question."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Can you tell me a little more about stage of change?"
		nextStepId "ExplanationOne"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Well, your Stage of Change regarding physical activity defines how you look at being physically active."
// ---  o "Okay, I understand. I'm ready for the question."
// ---  o "Can you tell me a bit more?"
// ---  o "Why do you want to know?"
// ---  o "Goodbye."
// --------------------
step {
	stepId "ExplanationOne"
	statement "Well, your Stage of Change regarding physical activity defines how you look at being physically active."
	
	basicReply {
		replyId "A"
		statement "Okay, I understand. I'm ready for the question."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Can you tell me a bit more?"
		nextStepId "ExplanationTwo"
	}
	
	basicReply {
		replyId "C"
		statement "Why do you want to know?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "It is a measure from the field of psychology invented to characterize what a person's attitude is towards changing a certain behavior."
// ---  o "Okay, I understand. I'm ready for the question."
// ---  o "Tell me more."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ExplanationTwo"
	statement "It is a measure from the field of psychology invented to characterize what a person's attitude is towards changing a certain behavior."
	
	basicReply {
		replyId "A"
		statement "Okay, I understand. I'm ready for the question."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Tell me more."
		nextStepId "ExplanationThree"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "It was invented in the 1980s by James Prochaska and Carlo DiClemente."
// ---  o "Okay, I understand. I'm ready for the question."
// ---  o "James Prochaska?"
// ---  o "Carlo DiClemente?"
// ---  o "Goodbye."
// --------------------
step {
	stepId "ExplanationThree"
	statement "It was invented in the 1980s by James Prochaska and Carlo DiClemente."
	
	basicReply {
		replyId "A"
		statement "Okay, I understand. I'm ready for the question."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "James Prochaska?"
		nextStepId "ExplanationProchaska"
	}
	
	basicReply {
		replyId "C"
		statement "Carlo DiClemente?"
		nextStepId "ExplanationDiClemente"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "James O. Prochaska was at that time working at the University of Rhode Island in the United States."
// ---  o "Okay, I understand. I'm ready for the question."
// ---  o "And what about this Carlo DiClemente?"
// ---  o "Okay, but why do you need to know this Stage of Change?"
// ---  o "Goodbye."
// --------------------
step {
	stepId "ExplanationProchaska"
	statement "James O. Prochaska was at that time working at the University of Rhode Island in the United States."
	
	basicReply {
		replyId "A"
		statement "Okay, I understand. I'm ready for the question."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "And what about this Carlo DiClemente?"
		nextStepId "ExplanationDiClemente"
	}
	
	basicReply {
		replyId "C"
		statement "Okay, but why do you need to know this Stage of Change?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Carlo C. DiClemente was at that time working at the Texas Institute of Mental Sciences in the United States."
// ---  o "Okay, I understand. I'm ready for the question."
// ---  o "And what about this James Prochaska?"
// ---  o "Okay, but why do you need to know this Stage of Change?"
// ---  o "Goodbye."
// --------------------
step {
	stepId "ExplanationDiClemente"
	statement "Carlo C. DiClemente was at that time working at the Texas Institute of Mental Sciences in the United States."
	
	basicReply {
		replyId "A"
		statement "Okay, I understand. I'm ready for the question."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "And what about this James Prochaska?"
		nextStepId "ExplanationProchaska"
	}
	
	basicReply {
		replyId "C"
		statement "Okay, but why do you need to know this Stage of Change?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "I can use the information to give more personal advice regarding your activity!"
// ---  o "Okay, I understand. I'm ready for the question."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ExplanationWhy"
	statement "I can use the information to give more personal advice regarding your activity!"
	
	basicReply {
		replyId "A"
		statement "Okay, I understand. I'm ready for the question."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}	
}

// --------------------
// --- "Okay, here goes. Are you at least 5 times per week physically active (walking, cycling, or doing sports) for more than 30 minutes each time?"
// ---  o "Yes, I have been for MORE than 6 months."
// ---  o "Yes, I have been for LESS than 6 months."
// ---  o "No, but I intend to in the next 30 days."
// ---  o "No, but I intend to in the next 6 months."
// ---  o "No, and I do NOT intend to in the next 6 months."
// --------------------
step {
	stepId "Question"
	statement "Okay, here goes. Are you at least 5 times per week physically active (walking, cycling, or doing sports) for more than 30 minutes each time?"
	
	basicReply {
		replyId "A"
		statement "Yes, I have been for MORE than 6 months."
		nextStepId "ThanksMaintenance"
		action {
			actionType "stageOfChangeUpdate"
			parameter "MAINTENANCE" 
		}
	}
	
	basicReply {
		replyId "B"
		statement "Yes, I have been for LESS than 6 months."
		nextStepId "ThanksAction"
		action {
			actionType "stageOfChangeUpdate"
			parameter "ACTION"
		}
	}
	
	basicReply {
		replyId "C"
		statement "No, but I intend to in the next 30 days."
		nextStepId "ThanksPreparation"
		action { 
			actionType "stageOfChangeUpdate"
			parameter "PREPARATION"
		}
	}
	
	basicReply {
		replyId "D"
		statement "No, but I intend to in the next 6 months."
		nextStepId "ThanksContemplation"
		action { 
			actionType "stageOfChangeUpdate"
			parameter "CONTEMPLATION"
		}
	}
	
	basicReply {
		replyId "E"
		statement "No, and I do NOT intend to in the next 6 months."
		nextStepId "ThanksPrecontemplation"
		action {
			actionType "stageOfChangeUpdate"
			parameter "PRECONTEMPLATION"
		}
	}
}

// --------------------
// --- "Excellent! Try to keep it up!"
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksMaintenance"
	statement "Excellent! Try to keep it up!"
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Very good! Try to stay active every day, you're on the right path!"
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksAction"
	statement "Very good! Try to stay active every day, you're on the right path!"
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "A good decision, but every day is a good day to start getting active."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksPreparation"
	statement "A good decision, but every day is a good day to start getting active."
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Physical activity is important, best to start sooner rather than later."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksContemplation"
	statement "Physical activity is important, best to start sooner rather than later."
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Hmmm, I'm sorry to hear that. You know physical activity is important for everyone."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksPrecontemplation"
	statement "Hmmm, I'm sorry to hear that. You know physical activity is important for everyone."
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}