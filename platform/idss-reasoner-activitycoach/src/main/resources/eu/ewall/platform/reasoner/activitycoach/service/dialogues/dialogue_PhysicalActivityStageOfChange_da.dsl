dialogueTypeId "PhysicalActivityStageOfChange"
initialStepId "Initial"

// --------------------
// --- "Do you have some time to talk about your physical activity?"
// ---  o "Sure, go ahead."
// ---  o "What's the question about?"
// ---  o "Sorry, not right now."
// ---  o Goodbye.
// --------------------
step {
	stepId "Initial"
	statement "Har du tid til at snakke om din fysiske aktivitet?"
	
	basicReply {
		replyId "A"
		statement "Ja da, fotsæt."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Hvad omhandler spørgsmålet?"
		nextStepId "ExplanationOne"
	}
	
	basicReply {
		replyId "C"
		statement "Beklager. Ikke lige nu."
		nextStepId "PostponeShort"
		action {
			actionType "postponeReminder"
			parameter "PhysicalActivityStageOfChange"
			parameter "24"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
		action {
			actionType "postponeReminder"
			parameter "PhysicalActivityStageOfChange"
			parameter "24"
		}
	}
}

// --------------------
// --- "Well, your Stage of Change regarding physical activity defines how you look at being physically active."
// ---  o "Okay, I understand. I'm ready for the question."
// ---  o "Can you tell me a bit more?"
// ---  o "Why do you want to know?"
// ---  o Goodbye.
// --------------------
step {
	stepId "ExplanationOne"
	statement "Altså, dit forandringstrin vedrørende fysisk aktivitet definerer, hvordan du ser på det at være fysisk aktiv."
	
	basicReply {
		replyId "A"
		statement "Okay, jeg forstår. Jeg er klar til spørgsmålet."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Kan du fortælle mig lidt mere?"
		nextStepId "ExplanationTwo"
	}
	
	basicReply {
		replyId "C"
		statement "Hvad vil du gerne vide?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
		action {
			actionType "postponeReminder"
			parameter "PhysicalActivityStageOfChange"
			parameter "24"
		}
	}
}

// --------------------
// --- "It is a measure from the field of psychology invented to characterize what a person's attitude is towards changing a certain behavior."
// ---  o "Okay, I understand. I'm ready for the question."
// ---  o "Tell me more."
// ---  o "Why do you want to know?"
// ---  o Goodbye.
// --------------------
step {
	stepId "ExplanationTwo"
	statement "Det er et mål fra psykologiens verden, som er opfundet for at definere, hvad der karakteriserer en persons indstilling til at ændre en bestemt adfærd."
	
	basicReply {
		replyId "A"
		statement "Okay, jeg forstår. Jeg er klar til spørgsmålet."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Fortæl mig mere."
		nextStepId "ExplanationThree"
	}
	
	basicReply {
		replyId "C"
		statement "Hvad vil du gerne vide?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
		action {
			actionType "postponeReminder"
			parameter "PhysicalActivityStageOfChange"
			parameter "24"
		}
	}
}

// --------------------
// --- "It was invented in the 1980s by James Prochaska and Carlo DiClemente."
// ---  o "Okay, I understand. I'm ready for the question."
// ---  o "James Prochaska?"
// ---  o "Carlo DiClemente?"
// ---  o Goodbye.
// --------------------
step {
	stepId "ExplanationThree"
	statement "Det blev opfundet i 80'erne af James Prochaska og Carlo DiClemente."
	
	basicReply {
		replyId "A"
		statement "Okay, jeg forstår. Jeg er klar til spørgsmålet."
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
		statement "Farvel."
		action {
			actionType "postponeReminder"
			parameter "PhysicalActivityStageOfChange"
			parameter "24"
		}
	}
}

// --------------------
// --- "James O. Prochaska was at that time working at the University of Rhode Island in the United States."
// ---  o "Okay, I understand. I'm ready for the question."
// ---  o "And what about this Carlo DiClemente?"
// ---  o "Okay, but why do you need to know this Stage of Change?"
// ---  o Goodbye.
// --------------------
step {
	stepId "ExplanationProchaska"
	statement "James O. Prochaska arbejdede dengang på Rhode Island Universitet i USA."
	
	basicReply {
		replyId "A"
		statement "Okay, jeg forstår. Jeg er klar til spørgsmålet."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Og hvad så med Carlo DiClemente?"
		nextStepId "ExplanationDiClemente"
	}
	
	basicReply {
		replyId "C"
		statement "Okay, men hvorfor skal du kende dette forandringstrin?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
		action {
			actionType "postponeReminder"
			parameter "PhysicalActivityStageOfChange"
			parameter "24"
		}
	}
}

// --------------------
// --- "Carlo C. DiClemente was at that time working at the Texas Institute of Mental Sciences in the United States."
// ---  o "Okay, I understand. I'm ready for the question."
// ---  o "And what about this James Prochaska?"
// ---  o "Okay, but why do you need to know this Stage of Change?"
// ---  o Goodbye.
// --------------------
step {
	stepId "ExplanationDiClemente"
	statement "Carlo C. DiClemente arbejdede dengang på Texas Institute of Mental Sciences i USA."
	
	basicReply {
		replyId "A"
		statement "Okay, jeg forstår. Jeg er klar til spørgsmålet."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Og hvad med denne James Prochaska?"
		nextStepId "ExplanationProchaska"
	}
	
	basicReply {
		replyId "C"
		statement "Okay, men hvorfor skal du kende dette forandringstrin?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
		action {
			actionType "postponeReminder"
			parameter "PhysicalActivityStageOfChange"
			parameter "24"
		}
	}
}

// --------------------
// --- "I can use the information to give more personal advice regarding your activity!"
// ---  o "Okay, I understand. I'm ready for the question."
// ---  o Goodbye.
// --------------------
step {
	stepId "ExplanationWhy"
	statement "Jeg kan bruge informationen til at give mere personlige råd vedrørende din aktivitet!"
	
	basicReply {
		replyId "A"
		statement "Okay, jeg forstår. Jeg er klar til spørgsmålet."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
		action {
			actionType "postponeReminder"
			parameter "PhysicalActivityStageOfChange"
			parameter "24"
		}
	}
}

// --------------------
// --- "Are you at least 5 times per week physically active (walking, cycling, or doing sports) for more than 30 minutes each time?"
// ---  o "Yes, I have been for MORE than 6 months."
// ---  o "Yes, I have been for LESS than 6 months."
// ---  o "No, but I intend to in the next 30 days."
// ---  o "No, but I intend to in the next 6 months."
// ---  o "No, and I do NOT intend to in the next 6 months."
// --------------------
step {
	stepId "Question"
	statement "Er du fysisk aktiv mindst fem gange hver uge (gang, cykling eller anden sport) i mere end 30 minutter hver gang?"
	
	basicReply {
		replyId "A"
		statement "Ja, det har jeg været i MERE end seks måneder."
		nextStepId "ThanksMaintenance"
		action {
			actionType "stageOfChangeUpdate"
			parameter "MAINTENANCE"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Ja, det har jeg været i MINDRE end seks måneder."
		nextStepId "ThanksAction"
		action {
			actionType "stageOfChangeUpdate"
			parameter "ACTION"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Nej, men det har jeg tænkt mig at være de næste 30 dage."
		nextStepId "ThanksPreparation"
		action {
			actionType "stageOfChangeUpdate"
			parameter "PREPARATION"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Nej, men det har jeg tænkt mig at være de næste seks måneder."
		nextStepId "ThanksContemplation"
		action {
			actionType "stageOfChangeUpdate"
			parameter "CONTEMPLATION"
		}
	}
	
	basicReply {
		replyId "E"
		statement "Nej, og det har jeg IKKE tænkt mig at være de næste seks måneder."
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
	statement "Fantastisk! Prøv at blive ved!"
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
	}
}

// --------------------
// --- "Very good! Try to stay active every day, you're on the right path!"
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksAction"
	statement "Rigtig godt! Forsøg at holde dig aktiv hver dag. Du er på rette vej!"
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
	}
}

// --------------------
// --- "A good decision, but every day is a good day to start getting active."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksPreparation"
	statement "En god beslutning men hver dag er en god dag at starte med at blive aktiv."
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
	}
}

// --------------------
// --- "Physical activity is important, best to start sooner rather than later."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksContemplation"
	statement "Fysisk aktivitet er vigtig. Det er bedre at starte før end senere."
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
	}
}

// --------------------
// --- "Hmmm, I'm sorry to hear that. You know physical activity is important for everyone."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksPrecontemplation"
	statement "Hmm, det er jeg ked af at høre. Du ved, at fysisk aktivitet er vigtigt for alle."
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
	}
}

// --------------------
// --- "Don't worry, I will try again tomorrow."
// ---  o "Goodbye."
// --------------------
step {
	stepId "PostponeShort"
	statement "Det skal du ikke tænke på. Jeg prøver igen i morgen."
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
	}
}