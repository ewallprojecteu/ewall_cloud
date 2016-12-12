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
	statement "Hast du kurz Zeit um über deinen körperliche Aktivitäten zu reden?"
	
	basicReply {
		replyId "A"
		statement "Sicher, nur zu."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Worum geht es?"
		nextStepId "ExplanationOne"
	}
	
	basicReply {
		replyId "C"
		statement "Leider, jetzt nicht."
		nextStepId "PostponeShort"
		action {
			actionType "postponeReminder"
			parameter "PhysicalActivityStageOfChange"
			parameter "24"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
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
	statement "Nun, deine Veränderungsprozesse bezüglich körperlicher Aktivität definieren wie du körperliche Aktivität wahrnimmst."
	
	basicReply {
		replyId "A"
		statement "Okay, ich verstehe. Ich bin bereit für die Frage."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Kannst du mir etwas mehr erzählen?"
		nextStepId "ExplanationTwo"
	}
	
	basicReply {
		replyId "C"
		statement "Warum willst du das wissen?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
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
	statement "Es ist eine Maßnahme aus dem Bereich der Psychologie, um die Haltung einer Person bezüglich Verhaltensänderungen zu charakterisieren."
	
	basicReply {
		replyId "A"
		statement "Okay, ich verstehe. Ich bin bereit für die Frage."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Erzähle mir mehr."
		nextStepId "ExplanationThree"
	}
	
	basicReply {
		replyId "C"
		statement "Warum willst du das wissen?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen.."
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
	statement "Es wurde in den 1980ern von James Prochaska und Carlo DiClemente entwickelt."
	
	basicReply {
		replyId "A"
		statement "Okay, ich verstehe. Ich bin bereit für die Frage."
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
		statement "Auf Wiedersehen."
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
	statement "James O. Prochaska arbeitete damals an der Universität Rhode Island in den USA."
	
	basicReply {
		replyId "A"
		statement "Okay, ich verstehe. Ich bin bereit für die Frage."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Und was hat dieser Carlo DiClemento gemacht?"
		nextStepId "ExplanationDiClemente"
	}
	
	basicReply {
		replyId "C"
		statement "Okay, aber warum willst du diese Veränderungsprozesse wissen?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
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
	statement "Carlo C. DiClemente arbeitete am Texas Institute of Mental Sciences in den USA."
	
	basicReply {
		replyId "A"
		statement "Okay, ich verstehe. Ich bin bereit für die Frage."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Und was hat James Prochaska gemacht?"
		nextStepId "ExplanationProchaska"
	}
	
	basicReply {
		replyId "C"
		statement "Okay, aber warum willst du diese Veränderungsprozesse wissen?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
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
	statement "Ich kann die Information nutzen, um persönlichere Ratschläge zu körperlicher Aktivität zu geben!"
	
	basicReply {
		replyId "A"
		statement "Okay, ich verstehe. Ich bin bereit für die Frage."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
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
	statement "Bist du zumindest 5 Mal die Woche für je mehr als 30 Minuten körperlich akltiv (gehen, Rad fahren, oder anderen Sport)?"
	
	basicReply {
		replyId "A"
		statement "Ja, für mehr als 6 Monate."
		nextStepId "ThanksMaintenance"
		action {
			actionType "stageOfChangeUpdate"
			parameter "MAINTENANCE"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Ja, für weniger als 6 Monate."
		nextStepId "ThanksAction"
		action {
			actionType "stageOfChangeUpdate"
			parameter "ACTION"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Nein, aber ich möchte es in den nächsten 30 Tagen versuchen."
		nextStepId "ThanksPreparation"
		action {
			actionType "stageOfChangeUpdate"
			parameter "PREPARATION"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Nein, aber ich möchte es in den nächsten 6 Monaten versuchen."
		nextStepId "ThanksContemplation"
		action {
			actionType "stageOfChangeUpdate"
			parameter "CONTEMPLATION"
		}
	}
	
	basicReply {
		replyId "E"
		statement "Nein, und ich möchte dies auch in den nächsten 6 Monaten nicht ändern."
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
	statement "Exzellent! Mach weiter so!"
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
	}
}

// --------------------
// --- "Very good! Try to stay active every day, you're on the right path!"
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksAction"
	statement "Sehr gut! Versuche jeden Tag aktiv zu sein, du bist am besten Weg!"
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
	}
}

// --------------------
// --- "A good decision, but every day is a good day to start getting active."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksPreparation"
	statement "Eine gute Entscheidung, aber jeder Tag ist ein guter Tag um aktiv zu werden."
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
	}
}

// --------------------
// --- "Physical activity is important, best to start sooner rather than later."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksContemplation"
	statement "Körperliche Aktivität ist wichtig, es ist gut so bald wie möglich damit zu beginnen."
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
	}
}

// --------------------
// --- "Hmmm, I'm sorry to hear that. You know physical activity is important for everyone."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksPrecontemplation"
	statement "Hmmm, schade das zu hören. Du weißt, dass körperliche Aktivität für jeden wichtig ist."
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
	}
}

// --------------------
// --- "Don't worry, I will try again tomorrow."
// ---  o "Goodbye."
// --------------------
step {
	stepId "PostponeShort"
	statement "Macht nichts, ich werde es morgen nochmals probieren."
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
	}
}