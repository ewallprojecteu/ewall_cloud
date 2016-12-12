dialogueTypeId "SleepNotificationsDemo"
initialStepId "Initial"

// --------------------
// --- "Hello [firstName], in order to get better insights in your sleep, I have a few questions for you."
// ---   o "Why should I answer these questions?"
// ---   o "Okay, go ahead Robin!"
// ---   o "Goodbye."
// --------------------
step {
	stepId "Initial"
	statement "Hello [firstName], in order to get better insights in your sleep, I have a few questions for you."
	
	basicReply {
		replyId "A"
		statement "Why should I answer these questions?"
		nextStepId "ExplanationOne"
	}
	
	basicReply {
		replyId "B"
		statement "Okay, go ahead Robin!"
		nextStepId "QuestionsOne"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "I want to ask a few things about drinking caffeine, eating before sleep and they way you feel when waking up..."
// ---  o "Tell me more..."
// ---  o "Okay, go ahead Robin!"
// ---  o "Goodbye."
// --------------------
step {
	stepId "ExplanationOne"
	statement "I want to ask a few things about drinking caffeine, eating before sleep and they way you feel when waking up..."
	
	basicReply {
		replyId "A"
		statement "Tell me more..."
		nextStepId "ExplanationTwo"
	}
	
	basicReply {
		replyId "B"
		statement "Okay, go ahead Robin!"
		nextStepId "QuestionsOne"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Using these subjective questions is a standard of care and considered to be accurate for screening sleep problems..."
// ---  o "Tell me more..."
// ---  o "Okay, go ahead Robin!"
// ---  o "Goodbye."
// --------------------
step {
	stepId "ExplanationTwo"
	statement "Using these subjective questions is a standard of care and considered to be accurate for screening sleep problems..."
	
	basicReply {
		replyId "A"
		statement "Tell me more..."
		nextStepId "ExplanationThree"
	}
	
	basicReply {
		replyId "B"
		statement "Okay, go ahead Robin!"
		nextStepId "QuestionsOne"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "I can measure your sleep with the Fitbit, but some additional information is required for a complete picture..."
// ---  o "Tell me more..."
// ---  o "Okay, go ahead Robin!"
// ---  o "Goodbye."
// --------------------
step {
	stepId "ExplanationThree"
	statement "I can measure your sleep with the Fitbit, but some additional information is required for a complete picture..."
	
	basicReply {
		replyId "A"
		statement "Tell me more..."
		nextStepId "ExplanationFour"
	}
	
	basicReply {
		replyId "B"
		statement "Okay, go ahead Robin!"
		nextStepId "QuestionsOne"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "And the more information I have the better I can tailor your sleep advice!"
// ---  o "Okay, go ahead Robin!"
// ---  o "Goodbye."
// --------------------
step {
	stepId "ExplanationFour"
	statement "And the more information I have the better I can tailor your sleep advice!"
	
	basicReply {
		replyId "A"
		statement "Okay, go ahead Robin!"
		nextStepId "QuestionsOne"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Okay, I have a few questions for you related to your sleep. First: what was the last time you drank caffeine yesterday?"
// ---  o "I didn't drink caffeine today."
// ---  o "Only in the morning."
// ---  o "The last time was in the afternoon."
// ---  o "In the evening."
// --------------------
step {
	stepId "QuestionsOne"
	statement "Okay, I have a few questions for you related to your sleep. First: what was the last time you drank caffeine yesterday?"
	
	basicReply {
		replyId "A"
		statement "I didn't drink caffeine yesterday."
		nextStepId "QuestionsTwo"
	}
	
	basicReply {
		replyId "B"
		statement "Only in the morning."
		nextStepId "QuestionsTwo"
	}
	
	basicReply {
		replyId "C"
		statement "The last time was in the afternoon."
		nextStepId "QuestionsTwo"
	}
	
	basicReply {
		replyId "D"
		statement "In the evening."
		nextStepId "QuestionsTwo"
	}
}

// --------------------
// --- "Okay, I have a few questions for you related to your sleep. First: when was the last time you drank a coffeine drink?"
// ---  o "I had something alcoholic to drink before going to bed."
// ---  o "I ate meate before going to bed."
// ---  o "None of the above."
// --------------------
step {
	stepId "QuestionsTwo"
	statement "Did you ate or drank anything of the following yesterday before going to sleep (within 2-3 hours)?"
	
	basicReply {
		replyId "A"
		statement "I had something alcoholic to drink before going to bed."
		nextStepId "QuestionsThree"
	}
	
	basicReply {
		replyId "B"
		statement "I ate meate before going to bed."
		nextStepId "QuestionsThree"
	}
	
	basicReply {
		replyId "C"
		statement "None of the above."
		nextStepId "QuestionsThree"
	}
}

// --------------------
// --- "And finally, how did you feel this morning when you woke up?"
// ---  o "I felt refreshed."
// ---  o "I felt somewhat refreshed."
// ---  o "I felt fatigued."
// --------------------
step {
	stepId "QuestionsThree"
	statement "And finally, how did you feel this morning when you woke up?"
	
	basicReply {
		replyId "A"
		statement "I felt refreshed."
		nextStepId "AdviceIntro"
	}
	
	basicReply {
		replyId "B"
		statement "I felt somewhat refreshed."
		nextStepId "AdviceIntro"
	}
	
	basicReply {
		replyId "C"
		statement "I felt fatigued."
		nextStepId "AdviceIntro"
	}
}

// --------------------
// --- "When measuring your sleep, and answering the questions regularly, personal advice can be given. A number of examples follow."
// ---  o "Show advice examples."
// ---  o "Goodbye."
// --------------------
step {
	stepId "AdviceIntro"
	statement "When measuring your sleep, and answering the questions regularly, personal advice can be given. A number of examples follow."
	
	basicReply {
		replyId "A"
		statement "Show advice examples."
		nextStepId "AdviceOne"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Do not take coffee or caffeine-containing substances in the evening, avoid smoking before bedtime; the evening alcohol should be taken in moderation because it can cause sleep fragmentation."
// ---  o "Show next advice."
// ---  o "Goodbye."
// --------------------
step {
	stepId "AdviceOne"
	statement "Do not take coffee or caffeine-containing substances in the evening, avoid smoking before bedtime; the evening alcohol should be taken in moderation because it can cause sleep fragmentation."
	
	basicReply {
		replyId "A"
		statement "Show next advice."
		nextStepId "AdviceTwo"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Establish dinnertime so that it is at least 3 hours before going to bed."
// ---  o "Show next advice."
// ---  o "Goodbye."
// --------------------
step {
	stepId "AdviceTwo"
	statement "Establish dinnertime so that it is at least 3 hours before going to bed."
	
	basicReply {
		replyId "A"
		statement "Show next advice."
		nextStepId "AdviceThree"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "..."
// ---  o "Show next advice."
// ---  o "Goodbye."
// --------------------
step {
	stepId "AdviceThree"
	statement "Go to sleep and wake up preferably at the same time, even during holiday periods and weekends."
	
	basicReply {
		replyId "A"
		statement "Show next advice."
		nextStepId "AdviceFour"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "..."
// ---  o "Show next advice."
// ---  o "Goodbye."
// --------------------
step {
	stepId "AdviceFour"
	statement "Develop and maintain an evening ritual before going to sleep (hot bath, read some pages of a book, drink an herbal tea or milk), thus creating positive influences."
	
	basicReply {
		replyId "A"
		statement "Show next advice."
		nextStepId "AdviceFive"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Avoid having too hot or too cold in the room or at least into bed; to make sure that the bedroom is quiet and silent throughout the night."
// ---  o "Show next advice."
// ---  o "Goodbye."
// --------------------
step {
	stepId "AdviceFive"
	statement "Avoid having too hot or too cold in the room or at least into bed; to make sure that the bedroom is quiet and silent throughout the night."
	
	basicReply {
		replyId "A"
		statement "Show next advice."
		nextStepId "AdviceSix"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Think of the bed, the pillow and the mattress as the most comfortable place in the world."
// ---  o "Show next advice."
// ---  o "Goodbye."
// --------------------
step {
	stepId "AdviceSix"
	statement "Think of the bed, the pillow and the mattress as the most comfortable place in the world."
	
	basicReply {
		replyId "A"
		statement "Show next advice."
		nextStepId "AdviceSeven"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Use the bedroom and the bed only for sleeping (not to watch TV, eat, read, relax, think, write); never sleep anyway in front of the TV."
// ---  o "Show next advice."
// ---  o "Goodbye."
// --------------------
step {
	stepId "AdviceSeven"
	statement "Use the bedroom and the bed only for sleeping (not to watch TV, eat, read, relax, think, write); never sleep anyway in front of the TV."
	
	basicReply {
		replyId "A"
		statement "Show next advice."
		nextStepId "AdviceEight"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Do not think about what you will do the next day or rethink too long at what has happened over the course of the day; let your mind from wandering between places or free scenarios, random, imaginary, out of the newspaper."
// ---  o "Show next advice."
// ---  o "Goodbye."
// --------------------
step {
	stepId "AdviceEight"
	statement "Do not think about what you will do the next day or rethink too long at what has happened over the course of the day; let your mind from wandering between places or free scenarios, random, imaginary, out of the newspaper."
	
	basicReply {
		replyId "A"
		statement "Show next advice."
		nextStepId "AdviceNine"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "If there is numbness, avoid tossing and turning for a long time in bed, but get up and go to another room."
// ---  o "Show next advice."
// ---  o "Goodbye."
// --------------------
step {
	stepId "AdviceNine"
	statement "If there is numbness, avoid tossing and turning for a long time in bed, but get up and go to another room."
	
	basicReply {
		replyId "A"
		statement "Show next advice."
		nextStepId "AdviceTen"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Avoid using drugs that can cause insomnia and reside in low light during the day."
// ---  o "Show next advice."
// ---  o "Goodbye."
// --------------------
step {
	stepId "AdviceTen"
	statement "Avoid using drugs that can cause insomnia and reside in low light during the day."
	
	basicReply {
		replyId "BYE"
		statement "Goodbye [End of Demo]."
	}
}