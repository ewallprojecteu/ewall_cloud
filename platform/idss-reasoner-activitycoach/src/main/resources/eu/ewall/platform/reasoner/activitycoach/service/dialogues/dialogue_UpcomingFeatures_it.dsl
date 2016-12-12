dialogueTypeId "UpcomingFeatures"
initialStepId "Initial"


// --------------------
// --- "There are some exciting new features coming to the CloudCare2U Platform, the next version of eWALL. Which one would you like to see?"
// ---  o "Upcoming Physical Activity Features."
// ---  o "Upcoming Environmental Sensing Features."
// ---  o "Upcoming Sleep Monitoring Features."
// ---  o "Goodbye."
// --------------------
step {
	stepId "Initial"
	statement "There are some exciting new features coming to the CloudCare2U Platform, the next version of eWALL. Which one would you like to see?"

	basicReply {
		replyId "A"
		statement "Upcoming Physical Activity Features."
		action {
			actionType "startDialogue"
			parameter "PhysicalActivityNotificationsDemo"
		}
	}

	basicReply {
		replyId "B"
		statement "Upcoming Environmental Sensing Features."
		action {
			actionType "startDialogue"
			parameter "EnvironmentalNotificationsDemo"
		}
	}

	basicReply {
		replyId "C"
		statement "Upcoming Sleep Monitoring Features."
		action {
			actionType "startDialogue"
			parameter "SleepNotificationsDemo"
		}
	}

	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}