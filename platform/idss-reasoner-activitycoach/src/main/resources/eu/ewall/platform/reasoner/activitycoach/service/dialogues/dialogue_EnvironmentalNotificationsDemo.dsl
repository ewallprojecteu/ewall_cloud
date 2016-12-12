dialogueTypeId "EnvironmentalNotificationsDemo"
initialStepId "Initial"

// --------------------
// --- "Hello [firstName]. I will demonstrate some of my upcoming features regarding environmental notifications."
// ---   o "Allright, show me the demos."
// ---   o "Goodbye."
// --------------------
step {
	stepId "Initial"
	statement "Hello [firstName]. I will demonstrate some of my upcoming features regarding environmental notifications."
	
	basicReply {
		replyId "A"
		statement "Allright, show me the demos."
		nextStepId "DemoCOPDOneStart"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "[The weather outside is cold and windy] Hello [firstName]! You have planned an appointment with your physiotherapist today. Are you going by car or by bike?"
// ---   o "By car."
// ---   o "By bike."
// ---   o "Goodbye."
// --------------------
step {
	stepId "DemoCOPDOneStart"
	statement "[The weather outside is cold and windy] Hello [firstName]! You have planned an appointment with your physiotherapist today. Are you going by car or by bike?"
	
	basicReply {
		replyId "A"
		statement "By car."
		nextStepId "DemoCOPDOneCar"
	}
	
	basicReply {
		replyId "B"
		statement "By bike."
		nextStepId "DemoCOPDOneBike"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Really? Being active is very important for everyone. Even if you are going by car, don't forget tot take a scarf as it will be very windy and therefore more difficult to breath."
// ---   o "Thank you Robin! [Next Demo]."
// ---   o "Goodbye."
// --------------------
step {
	stepId "DemoCOPDOneCar"
	statement "Really? Being active is very important for everyone. Even if you are going by car, don't forget tot take a scarf as it will be very windy and therefore more difficult to breath."
	
	basicReply {
		replyId "A"
		statement "Thank you Robin! [Next Demo]"
		nextStepId "DemoCOPDTwoStart"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "That’s great! I see that you are eager to increase your fitness level. However, it will be very windy outside what makes breathing more difficult. Don’t forget to cover your mouth and nose with a scarf!"
// ---   o "Thank you Robin! [Next Demo]."
// ---   o "Goodbye."
// --------------------
step {
	stepId "DemoCOPDOneBike"
	statement "That’s great! I see that you are eager to increase your fitness level. However, it will be very windy outside what makes breathing more difficult. Don’t forget to cover your mouth and nose with a scarf!"
	
	basicReply {
		replyId "A"
		statement "Thank you Robin! [Next Demo]"
		nextStepId "DemoCOPDTwoStart"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "[It's getting colder] Hello [firstName]! Brrrrrrr it is getting cold inside. Did you know that COPD patients are more likely to feel tired in a cold environment? Would you like me to increase the temperature of the house?"
// ---   o "Yes, change the temperature."
// ---   o "No, thank you Robin."
// ---   o "Goodbye."
// --------------------
step {
	stepId "DemoCOPDTwoStart"
	statement "[It's getting colder] Hello [firstName]! Brrrrrrr it is getting cold inside. Did you know that COPD patients are more likely to feel tired in a cold environment? Would you like me to increase the temperature of the house?"
	
	basicReply {
		replyId "A"
		statement "Yes, change the temperature."
		nextStepId "DemoCOPDTwoChangeTemperature"
	}
	
	basicReply {
		replyId "B"
		statement "No, thank you Robin."
		nextStepId "DemoCOPDTwoNoThanks"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "What would you prefer as a comfortable temperature?"
// ---   o "Set 22 degrees."
// ---   o "Set 21 degrees."
// ---   o "Set 20 degrees."
// ---   o "Set 19 degrees."
// ---   o "Set 18 degrees."
// --------------------
step {
	stepId "DemoCOPDTwoChangeTemperature"
	statement "What would you prefer as a comfortable temperature?"
	
	basicReply {
		replyId "A"
		statement "Set 22 degrees."
		nextStepId "DemoCOPDTwoTemperatureChanged"
	}
	
	basicReply {
		replyId "B"
		statement "Set 21 degrees."
		nextStepId "DemoCOPDTwoTemperatureChanged"
	}
	
	basicReply {
		replyId "C"
		statement "Set 20 degrees."
		nextStepId "DemoCOPDTwoTemperatureChanged"
	}
	
	basicReply {
		replyId "D"
		statement "Set 19 degrees."
		nextStepId "DemoCOPDTwoTemperatureChanged"
	}
	
	basicReply {
		replyId "E"
		statement "Set 18 degrees."
		nextStepId "DemoCOPDTwoTemperatureChanged"
	}	
}

// --------------------
// --- "Okay, I just talked to the Nest Thermostat and it'll get warmer in no time!"
// ---   o "Thank you Robin! [Next Demo]"
// ---   o "Goodbye."
// --------------------
step {
	stepId "DemoCOPDTwoTemperatureChanged"
	statement "Okay, I just talked to the Nest Thermostat and it'll get warmer in no time!"
	
	basicReply {
		replyId "A"
		statement "Thank you Robin! [Next Demo]"
		nextStepId "DemoCOPDThreeStart"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Okay, it's up to you! But please make sure not to freeze!"
// ---   o "I will, thank you Robin! [Next Demo]"
// ---   o "Goodbye."
// --------------------
step {
	stepId "DemoCOPDTwoNoThanks"
	statement "Okay, it's up to you! But please make sure not to freeze!"
	
	basicReply {
		replyId "A"
		statement "I will, thank you Robin! [Next Demo]"
		nextStepId "DemoCOPDThreeStart"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Pfff yesterday was very hot and humid, wouldn’t you say that? I am curious about how your COPD reacted to that sticky weather."
// ---   o "I felt very good, actually!"
// ---   o "I didn't notice anything remarkable."
// ---   o "I had problems with my breathing."
// ---   o "Goodbye."
// --------------------
step {
	stepId "DemoCOPDThreeStart"
	statement "Pfff yesterday was very hot and humid, wouldn't you say that? I am curious about how your COPD reacted to that sticky weather."
	
	basicReply {
		replyId "A"
		statement "I felt very good, actually!"
		nextStepId "DemoCOPDThreeGood"
	}
	
	basicReply {
		replyId "B"
		statement "I didn't notice anything remarkable."
		nextStepId "DemoCOPDThreeNeutral"
	}
	
	basicReply {
		replyId "B"
		statement "I had problems with my breathing."
		nextStepId "DemoCOPDThreeBad"
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}	
}

// --------------------
// --- "That is good to know! Apparently the warm and humid weather does not affect you too much. Thank you for the information."
// ---   o "Goodbye. [End of Demos]"
// --------------------
step {
	stepId "DemoCOPDThreeGood"
	statement "That is good to know! Apparently the warm and humid weather does not affect you too much. Thank you for the information."
	
	basicReply {
		replyId "BYE"
		statement "Goodbye. [End of Demos]"
	}
}

// --------------------
// --- "That is good to know, thank you for the information."
// ---   o "Goodbye. [End of Demos]"
// --------------------
step {
	stepId "DemoCOPDThreeNeutral"
	statement "That is good to know, thank you for the information."
	
	basicReply {
		replyId "BYE"
		statement "Goodbye. [End of Demos]"
	}
}

// --------------------
// --- "Thank you for the information! I will take it into consideration in the future."
// ---   o "Goodbye. [End of Demos]"
// --------------------
step {
	stepId "DemoCOPDThreeBad"
	statement "Thank you for the information! I will take it into consideration in the future."
	
	basicReply {
		replyId "BYE"
		statement "Goodbye. [End of Demos]"
	}
}