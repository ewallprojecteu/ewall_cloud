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
	statement "Heeft u tijd om over uw lichamelijke activiteit te praten?"
	
	basicReply {
		replyId "A"
		statement "Natuurlijk, ga je gang."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Waar gaat de vraag over?"
		nextStepId "ExplanationOne"
	}
	
	basicReply {
		replyId "C"
		statement "Sorry, op dit moment niet."
		nextStepId "PostponeShort"
		action {
			actionType "postponeReminder"
			parameter "PhysicalActivityStageOfChange"
			parameter "24"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "Nou, het model van de Stadia van Gedragsverandering definieert wat uw houding is ten opzichte van fysieke activiteit."
	
	basicReply {
		replyId "A"
		statement "Oke, ik begrijp het. Ik ben klaar voor de vraag."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Kun je mij wat meer vertellen?"
		nextStepId "ExplanationTwo"
	}
	
	basicReply {
		replyId "C"
		statement "Waarom wil je dat weten?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "Het is een meting die vanuit de Psychologie is ontwikkeld en bekijkt de houding van iemand tegenover het veranderen van bepaald gedrag."
	
	basicReply {
		replyId "A"
		statement "Oke, ik begrijp het. Ik ben klaar voor de vraag."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "Vertel me meer."
		nextStepId "ExplanationThree"
	}
	
	basicReply {
		replyId "C"
		statement "Waarom wil je dat weten?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "Het was uitgevonden in 1980, door James Prochaska en Carlo DiClemente."
	
	basicReply {
		replyId "A"
		statement "Oke, ik begrijp het. Ik ben klaar voor de vraag."
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
		statement "Tot ziens."
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
	statement "James O. Prochaska was op dat moment werkzaam bij de Universiteit van Rhode Island in de Verenigde Staten."
	
	basicReply {
		replyId "A"
		statement "Oke, ik begrijp het. Ik ben klaar voor de vraag."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "En hoe zit het met Carlo DiClemente?"
		nextStepId "ExplanationDiClemente"
	}
	
	basicReply {
		replyId "C"
		statement "Okee, maar waarom wil je iets weten over deze Stadia van Gedragsverandering?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "Carlo C. DiClemente was op dat moment werkzaam bij Texas Institute of Mental Sciences in de Verenigde Staten."
	
	basicReply {
		replyId "A"
		statement "Oke, ik begrijp het. Ik ben klaar voor de vraag."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "B"
		statement "En hoe zit het met James Prochaska?"
		nextStepId "ExplanationProchaska"
	}
	
	basicReply {
		replyId "C"
		statement "Okay, but why do you need to know this Stage of Change?"
		nextStepId "ExplanationWhy"
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "Ik kan de informatie gebruiken om persoonlijker advies te geven met betrekking tot uw activiteit!"
	
	basicReply {
		replyId "A"
		statement "Oke, ik begrijp het. Ik ben klaar voor de vraag."
		nextStepId "Question"
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "Bent u minimaal 5 keer per week lichamelijk actief (wandelen, fietsen of sporten), gedurende minimaal 30 minuten per keer?"
	
	basicReply {
		replyId "A"
		statement "Ja, voor MEER dan 6 maanden."
		nextStepId "ThanksMaintenance"
		action {
			actionType "stageOfChangeUpdate"
			parameter "MAINTENANCE"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Ja, maar voor MINDER dan 6 maanden."
		nextStepId "ThanksAction"
		action {
			actionType "stageOfChangeUpdate"
			parameter "ACTION"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Nee, maar ik ben van plan om dit binnen de komende 30 dagen te gaan doen."
		nextStepId "ThanksPreparation"
		action {
			actionType "stageOfChangeUpdate"
			parameter "PREPARATION"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Nee, maar ik ben van plan om dit binnen de komende 6 maanden te gaan doen."
		nextStepId "ThanksContemplation"
		action {
			actionType "stageOfChangeUpdate"
			parameter "CONTEMPLATION"
		}
	}
	
	basicReply {
		replyId "E"
		statement "Nee, en ik ben NIET van plan dit de komende 6 maanden te gaan doen."
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
	statement "Uitstekend! Probeer dit zo te houden!"
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
	}
}

// --------------------
// --- "Very good! Try to stay active every day, you're on the right path!"
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksAction"
	statement "Heel goed! Probeer om elke dag actief te zijn, u bent op de goede weg."
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
	}
}

// --------------------
// --- "A good decision, but every day is a good day to start getting active."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksPreparation"
	statement "Een goede beslissing, maar elke dag is een goede dag om te starten met lichamelijk activiteiten."
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
	}
}

// --------------------
// --- "Physical activity is important, best to start sooner rather than later."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksContemplation"
	statement "Lichamelijke activiteit is belangrijk, het is beter om eerder te beginnen dan later."
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
	}
}

// --------------------
// --- "Hmmm, I'm sorry to hear that. You know physical activity is important for everyone."
// ---  o "Goodbye."
// --------------------
step {
	stepId "ThanksPrecontemplation"
	statement "Hmmm, het spijt me dat te horen. U weet dat lichamelijke activiteit voor iedereen belangrijk is."
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
	}
}

// --------------------
// --- "Don't worry, I will try again tomorrow."
// ---  o "Goodbye."
// --------------------
step {
	stepId "PostponeShort"
	statement "Maak u geen zorgen, ik zal het morgen opnieuw proberen."
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
	}
}