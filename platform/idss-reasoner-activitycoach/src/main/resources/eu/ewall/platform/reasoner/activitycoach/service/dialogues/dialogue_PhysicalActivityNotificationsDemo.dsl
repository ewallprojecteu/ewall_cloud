// 1 KM = 1,320 steps
// Brussels to Antwerp = 44,4 KM = 58,608 steps
// Border length of Belgium = 1,385 KM = 1,828,200 steps
// Mont Blanc = 4,809m = 6348 steps

dialogueTypeId "PhysicalActivityNotificationsDemo"
initialStepId "Initial"

// --------------------
// --- "Welcome to the sneak preview of improved physical activity notifications. Which demo would you like to see?"
// ---   o "Physical activity milestones."
// ---   o "Daily activity summaries."
// ---   o "Goodbye."
// --------------------
step {
	stepId "Initial"
	statement "Welcome to the sneak preview of improved physical activity notifications. Which demo would you like to see?"
	statement "I can walk you through some examples of improved physical activity notifications. Which demo would you like to see?"

	basicReply {
		replyId "A"
		statement "Physical activity milestones."
		nextStepId "MilestonesOne"
	}

	basicReply {
		replyId "B"
		statement "Daily activity summaries."
		nextStepId "SummariesOne"
	}

	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Hi [firstName]! Since you started using eWALL you've walked over 58.000 steps, that's the same as walking from the center of Brussels to the center of Antwerp."
// ---   o "Next."
// ---   o "Show me the other demos."
// ---   o "Goodbye."
// --------------------
step {
	stepId "MilestonesOne"
	statement "Hi [firstName]! Since you started using eWALL you've walked over 58.000 steps, that's the same as walking from the center of Brussels to the center of Antwerp."

	basicReply {
		replyId "A"
		statement "Next."
		nextStepId "MilestonesTwo"
	}

	basicReply {
		replyId "B"
		statement "Show me the other demos."
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
// --- "This year you have already done 1,8 million steps! That's like walking along the entire border of Belgium!"
// ---   o "Next."
// ---   o "Show me the other demos."
// ---   o "Goodbye."
// --------------------
step {
	stepId "MilestonesTwo"
	statement "This year you have already done 1,8 million steps! That's like walking along the entire border of Belgium!"

	basicReply {
		replyId "A"
		statement "Next."
		nextStepId "MilestonesThree"
	}

	basicReply {
		replyId "B"
		statement "Show me the other demos."
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
// --- "During your event yesterday 'Visit Joe and Sarah' you did a total of 2,348 steps, nice!"
// ---   o "Next."
// ---   o "Show me the other demos."
// ---   o "Goodbye."
// --------------------
step {
	stepId "MilestonesThree"
	statement "During your event yesterday 'Visit Joe and Sarah' you did a total of 2,348 steps, nice!"

	basicReply {
		replyId "A"
		statement "Next."
		nextStepId "MilestonesFour"
	}

	basicReply {
		replyId "B"
		statement "Show me the other demos."
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
// --- "You did more than 6500 steps yesterday, that's as much as the height of the Mont Blanc, well done!"
// ---   o "Next."
// ---   o "Show me the other demos."
// ---   o "Goodbye."
// --------------------
step {
	stepId "MilestonesFour"
	statement "You did more than 6500 steps yesterday, that's as much as the height of the Mont Blanc, well done!"

	basicReply {
		replyId "A"
		statement "Show me the other demos."
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
// --- "Good evening! That was a good day, [firstName]! You did 6,437 steps beating your Thursday goal of 5,000 steps by a good margin!"
// ---   o "Next."
// ---   o "Show me the other demos."
// ---   o "Goodbye."
// --------------------
step {
	stepId "SummariesOne"
	statement "Good evening! That was a good day, [firstName]! You did 6,437 steps beating your Thursday goal of 5,000 steps by a good margin!"

	basicReply {
		replyId "A"
		statement "Next."
		nextStepId "SummariesTwo"
	}

	basicReply {
		replyId "B"
		statement "Show me the other demos."
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
// --- "Good evening [firstName]! Only 1,480 steps today... but it looks like you did not wear your sensor the whole day today. Let's forget this ever happened and try again tomorrow!"
// ---   o "Next."
// ---   o "Show me the other demos."
// ---   o "Goodbye."
// --------------------
step {
	stepId "SummariesTwo"
	statement "Good evening [firstName]! Only 1,480 steps today... but it looks like you did not wear your sensor the whole day today. Let's forget this ever happened and try again tomorrow!"

	basicReply {
		replyId "A"
		statement "Next."
		nextStepId "SummariesThree"
	}

	basicReply {
		replyId "B"
		statement "Show me the other demos."
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
// --- "Hello [firstName]! I hope you've enjoyed your weekend. Last week you've reached your goal 5 out of 7 days, not bad! Your goals for next week are now set, do you wish to review them?"
// ---   o "Yes, show me my goals for this week."
// ---   o "Goodbye."
// --------------------
step {
	stepId "SummariesThree"
	statement "Hello [firstName]! I hope you've enjoyed your weekend. Last week you've reached your goal 5 out of 7 days, not bad! Your goals for next week are now set, do you wish to review them?"

	basicReply {
		replyId "A"
		statement "Yes, show me my goals for this week."
		nextStepId "SummariesThreeGoals"
	}

	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Your daily goal for this week is 6,500 steps. For your active days (Thursday, Saturday) the goal is 8,200. For your inactive days (Monday) your goal is 5,000 steps."
// ---   o "Okay, sounds good! [Back to Demos]"
// ---   o "Can you lower my step goals a little?"
// ---   o "Can you increase my step goals a little?"
// ---   o "Goodbye."
// --------------------
step {
	stepId "SummariesThreeGoals"
	statement "Your daily goal for this week is 6,500 steps. For your active days (Thursday, Saturday) the goal is 8,200. For your inactive days (Monday) your goal is 5,000 steps."

	basicReply {
		replyId "A"
		statement "Okay, sounds good! [Back to Demos]"
		action {
			actionType "startDialogue"
			parameter "UpcomingFeatures"
		}
	}

	basicReply {
		replyId "B"
		statement "Can you lower my step goals a little?"
		nextStepId "SummariesThreeGoalsLower"
	}

	basicReply {
		replyId "C"
		statement "Can you increase my step goals a little?"
		nextStepId "SummariesThreeGoalsIncrease"
	}

	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Allright! I've lowered your daily step goals for this week by 10%. Your goal for today was changed from 5,000 to 4,500. Good luck!"
// ---   o "Thank you Robin! [Back to Demos]"
// ---   o "Goodbye."
// --------------------
step {
	stepId "SummariesThreeGoalsLower"
	statement "Allright! I've lowered your daily step goals for this week by 10%. Your goal for today was changed from 5,000 to 4,500. Good luck!"

	basicReply {
		replyId "A"
		statement "Thank you Robin! [Back to Demos]"
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
// --- "Allright! I've increased your daily step goals for this week by 10%. Your goal for today was changed from 5,000 to 4,500. Good luck!"
// ---   o "Thank you Robin! [Back to Demos]"
// ---   o "Goodbye."
// --------------------
step {
	stepId "SummariesThreeGoalsIncrease"
	statement "Allright! I've increased your daily step goals for this week by 10%. Your goal for today was changed from 5,000 to 5,500. Good luck!"

	basicReply {
		replyId "A"
		statement "Thank you Robin! [Back to Demos]"
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
