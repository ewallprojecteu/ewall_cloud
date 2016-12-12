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
	statement "Per aggiornare il tuo stadio del cambiamento tutto quello che devi fare è rispondere ad una facile domanda. Sei pronto?"
	
	basicReply {
		replyId "A"
		statement "Si, fammi la domanda."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Puoi dirmi qualcosa in più rispetto allo stadio del cambiamento?"
		nextStepId "ExplanationOne"
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "Bene, il tuo Stadio del Cambiamento sulle attività fisica definisce quanto sei fisicamente attivo."
	
	basicReply {
		replyId "A"
		statement "Ok, Ho capito, sono pronto per la domanda."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Puoi dirmi qualcosa in più?"
		nextStepId "ExplanationTwo"
	}
	
	basicReply {
		replyId "C"
		statement "Perchè lo vuoi sapere?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "È una misura psicologica che descrive l'attitudine di una persona a cambiare un certo comportamento."
	
	basicReply {
		replyId "A"
		statement "Ok, Ho capito, sono pronto per la domanda."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Dimmi di più"
		nextStepId "ExplanationThree"
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "Èstato inventato negli anni '80 da James Prochaska e Carlo DiClemente."
	
	basicReply {
		replyId "A"
		statement "Ok, Ho capito, sono pronto per la domanda."
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
		statement "Arrivederci."
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
	statement "In quegli anni James Prochaska lavorava all'Università di Rhode Island negli Stati Uniti."
	
	basicReply {
		replyId "A"
		statement "Ok, Ho capito, sono pronto per la domanda."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "E cosa mi dici di Carlo DiClemente?"
		nextStepId "ExplanationDiClemente"
	}
	
	basicReply {
		replyId "C"
		statement "Ok, ma perchè vuoi sapere dello Stadio del Cambiamento?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "Carlo C. DiClemente a quel tempo lavorava al Texas Institute of Mental Sciences negli Stati Uniti."
	
	basicReply {
		replyId "A"
		statement "Ok, Ho capito, sono pronto per la domanda."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "E cosa mi dici di James Prochaska?"
		nextStepId "ExplanationProchaska"
	}
	
	basicReply {
		replyId "C"
		statement "Ok, ma perchè vuoi sapere dello Stadio del Cambiamento?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
	}
}

// --------------------
// --- "I can use the information to give more personal advice regarding your activity!"
// ---  o "Okay, I understand. I'm ready for the question."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ExplanationWhy"
	statement "Posso usare le informazioni per darti consigli personalizzati sulla tua attività!"
	
	basicReply {
		replyId "A"
		statement "Ok, Ho capito, sono pronto per la domanda."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "Ok, ecco qui. Sei fisicamente attivo (a piedi, in bicicletta o con qualche sport) almeno 5 volte a settimana e per più di mezz'ora ogni volta?"
	
	basicReply {
		replyId "A"
		statement "Si, lo sono stato per PIÙ di 6 mesi."
		nextStepId "ThanksMaintenance"
		action {
			actionType "stageOfChangeUpdate"
			parameter "MAINTENANCE"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Si, lo sono stato per MENO di 6 mesi."
		nextStepId "ThanksAction"
		action {
			actionType "stageOfChangeUpdate"
			parameter "ACTION"
		}
	}
	
	basicReply {
		replyId "C"
		statement "No, ma vorrei esserlo nei prossimi 30 giorni."
		nextStepId "ThanksPreparation"
		action {
			actionType "stageOfChangeUpdate"
			parameter "PREPARATION"
		}
	}
	
	basicReply {
		replyId "D"
		statement "No, ma vorrei esserlo nei prossimi 6 mesi."
		nextStepId "ThanksContemplation"
		action {
			actionType "stageOfChangeUpdate"
			parameter "CONTEMPLATION"
		}
	}
	
	basicReply {
		replyId "E"
		statement "No, e NON vorrei esserlo nei prossimi 6 mesi."
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
	statement "Eccellente! Cerca di continuare così!"
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
	}
}

// --------------------
// --- "Very good! Try to stay active every day, you're on the right path!"
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksAction"
	statement "Molto bene! Cerca di essere attivo ogni giorno, sei sulla strada giusta!"
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
	}
}

// --------------------
// --- "A good decision, but every day is a good day to start getting active."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksPreparation"
	statement "Un'ottima scelta, ma ogni giorno è buono per essere attivi."
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
	}
}

// --------------------
// --- "Physical activity is important, best to start sooner rather than later."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksContemplation"
	statement "L'attività fisica è importante, meglio partire prima che dopo."
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
	}
}

// --------------------
// --- "Hmmm, I'm sorry to hear that. You know physical activity is important for everyone."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksPrecontemplation"
	statement "Ummmm, mi dispiace sentire questo. Sai che l'attività fisica è importante per tutti."
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
	}
}