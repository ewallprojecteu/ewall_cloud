dialogueTypeId "Rewards"
initialStepId "Initial"

// --------------------
// --- "Hello [firstName]! So, you want to talk about rewards? How can I help you?"
// ---   o How many eWALL Coins do I have?
// ---   o What are eWALL Coins?
// ---   o What can I buy with my eWALL Coins?
// ---   o Goodbye.
// --------------------
step {
	stepId "Initial"
	statement "Hello [firstName]! So, you want to talk about rewards? How can I help you?"
	
	basicReply {
		replyId "A"
		statement "How many eWALL Coins do I have?"
		nextStepId "CoinBalance"
	}	
	
	basicReply {
		replyId "B"
		statement "What are eWALL Coins?"
		nextStepId "ExplanationOne"
	}
	
	basicReply {
		replyId "C"
		statement "What can I buy with my eWALL Coins?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "What can I buy with my eWALL Coins?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "You currently have [totalCoins] eWALL Coins."
// ---   o Show me the Reward Overview application.
// ---   o What can I buy with my eWALL Coins?
// ---   o Goodbye.
// --------------------
step {
	stepId "CoinBalance"
	statement "You currently have [totalCoins] eWALL Coins."
	
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
		statement "What can I buy with my eWALL Coins?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "What can I buy with my eWALL Coins?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "The following items are available for purchase. What would you like to do?"
// ---   o Buy the sixties wallpaper ([rewardCostsWallpaperOneSixties] Coins).
// ---   o [Insufficient Coins] Buy the sixties wallpaper ([rewardCostsWallpaperOneSixties] Coins).
// ---   o Buy the striped wallpaper ([rewardCostsWallpaperTwoStripes] Coins).
// ---   o [Insufficient Coins] Buy the striped wallpaper ([rewardCostsWallpaperTwoStripes] Coins).
// ---   o Buy the blue wallpaper ([rewardCostsWallpaperThreeBlue] Coins).
// ---   o [Insufficient Coins] Buy the blue wallpaper ([rewardCostsWallpaperThreeBlue] Coins).
// ---   o Buy the yellow wallpaper ([rewardCostsWallpaperFourYellow] Coins).
// ---   o [Insufficient Coins] Buy the yellow wallpaper ([rewardCostsWallpaperFourYellow] Coins).
// ---   o Goodbye.
// --------------------
step {
	stepId "PurchaseOverview"
	statement "The following items are available for purchase. What would you like to do?"
	
	basicReply {
		replyId "A"
		statement "Buy the sixties wallpaper ([rewardCostsWallpaperOneSixties] Coins)."
		nextStepId "PurchaseConfirmWallpaperOneSixties"
		condition {
			conditionType "totalCoinsMinimum"
			parameter "[rewardCostsWallpaperOneSixties]"
		}
		condition {
			conditionType "rewardAvailable"
			parameter "WALLPAPER_ONE_SIXTIES"
		}
	}
	
	basicReply {
		replyId "B"
		statement "[Insufficient Coins] Buy the sixties wallpaper ([rewardCostsWallpaperOneSixties] Coins)."
		nextStepId "InsufficientCoins"
		condition {
			conditionType "totalCoinsMaximum"
			parameter "[rewardCostsWallpaperOneSixties]"
		}
		condition {
			conditionType "rewardAvailable"
			parameter "WALLPAPER_ONE_SIXTIES"
		}
	}
		
	basicReply {
		replyId "C"
		statement "Buy the striped wallpaper ([rewardCostsWallpaperTwoStripes] Coins)."
		nextStepId "PurchaseConfirmWallpaperTwoStripes"
		condition {
			conditionType "totalCoinsMinimum"
			parameter "[rewardCostsWallpaperTwoStripes]"
		}
		condition {
			conditionType "rewardAvailable"
			parameter "WALLPAPER_TWO_STRIPES"
		}
	}
	
	basicReply {
		replyId "D"
		statement "[Insufficient Coins] Buy the striped wallpaper ([rewardCostsWallpaperTwoStripes] Coins)."
		nextStepId "InsufficientCoins"
		condition {
			conditionType "totalCoinsMaximum"
			parameter "[rewardCostsWallpaperTwoStripes]"
		}
		condition {
			conditionType "rewardAvailable"
			parameter "WALLPAPER_TWO_STRIPES"
		}
	}
	
	basicReply {
		replyId "E"
		statement "Buy the blue wallpaper ([rewardCostsWallpaperThreeBlue] Coins)."
		nextStepId "PurchaseConfirmWallpaperThreeBlue"
		condition {
			conditionType "totalCoinsMinimum"
			parameter "[rewardCostsWallpaperThreeBlue]"
		}
		condition {
			conditionType "rewardAvailable"
			parameter "WALLPAPER_THREE_BLUE"
		}		
	}
	
	basicReply {
		replyId "F"
		statement "[Insufficient Coins] Buy the blue wallpaper ([rewardCostsWallpaperThreeBlue] Coins)."
		nextStepId "InsufficientCoins"
		condition {
			conditionType "totalCoinsMaximum"
			parameter "[rewardCostsWallpaperThreeBlue]"
		}
		condition {
			conditionType "rewardAvailable"
			parameter "WALLPAPER_THREE_BLUE"
		}
	}
	
	basicReply {
		replyId "G"
		statement "Buy the yellow wallpaper ([rewardCostsWallpaperFourYellow] Coins)."
		nextStepId "PurchaseConfirmWallpaperFourYellow"
		condition {
			conditionType "totalCoinsMinimum"
			parameter "[rewardCostsWallpaperFourYellow]"
		}
		condition {
			conditionType "rewardAvailable"
			parameter "WALLPAPER_FOUR_YELLOW"
		}
	}
	
	basicReply {
		replyId "H"
		statement "[Insufficient Coins] Buy the yellow wallpaper ([rewardCostsWallpaperFourYellow] Coins)."
		nextStepId "InsufficientCoins"
		condition {
			conditionType "totalCoinsMaximum"
			parameter "[rewardCostsWallpaperFourYellow]"
		}
		condition {
			conditionType "rewardAvailable"
			parameter "WALLPAPER_FOUR_YELLOW"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Congratulations [firstName], it looks like you have unlocked all eWALL rewards!"
// ---   o Goodbye.
// --------------------
step {
	stepId "PurchaseOverviewEmpty"
	statement "Congratulations [firstName], it looks like you have unlocked all eWALL rewards!"
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Are you sure you want to buy the Sixties Wallpaper for [rewardCostsWallpaperOneSixties] Coins?"
// ---   o Yes, buy wallpaper.
// ---   o No, show me the other options.
// ---   o Goodbye.
// --------------------
step {
	stepId "PurchaseConfirmWallpaperOneSixties"
	statement "Are you sure you want to buy the Sixties Wallpaper for [rewardCostsWallpaperOneSixties] Coins?"
	
	basicReply {
		replyId "A"
		statement "Yes, buy wallpaper."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_ONE_SIXTIES"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}		
	}
	
	basicReply {
		replyId "B"
		statement "No, show me the other options."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "No, show me the other options."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}

	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Are you sure you want to buy the Striped Wallpaper for [rewardCostsWallpaperTwoStripes] Coins?"
// ---   o Yes, buy wallpaper.
// ---   o No, show me the other options.
// ---   o Goodbye.
// --------------------
step {
	stepId "PurchaseConfirmWallpaperTwoStripes"
	statement "Are you sure you want to buy the Striped Wallpaper for [rewardCostsWallpaperTwoStripes] Coins?"
	
	basicReply {
		replyId "A"
		statement "Yes, buy wallpaper."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_TWO_STRIPES"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "No, show me the other options."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "No, show me the other options."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Are you sure you want to buy the Blue Wallpaper for [rewardCostsWallpaperThreeBlue] Coins?"
// ---   o Yes, buy wallpaper.
// ---   o No, show me the other options.
// ---   o Goodbye.
// --------------------
step {
	stepId "PurchaseConfirmWallpaperThreeBlue"
	statement "Are you sure you want to buy the Blue Wallpaper for [rewardCostsWallpaperThreeBlue] Coins?"
	
	basicReply {
		replyId "A"
		statement "Yes, buy wallpaper."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_THREE_BLUE"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "No, show me the other options."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "No, show me the other options."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Are you sure you want to buy the Yellow Wallpaper for [rewardCostsWallpaperFourYellow] Coins?"
// ---   o Yes, buy wallpaper.
// ---   o No, show me the other options.
// ---   o Goodbye.
// --------------------
step {
	stepId "PurchaseConfirmWallpaperFourYellow"
	statement "Are you sure you want to buy the Yellow Wallpaper for [rewardCostsWallpaperFourYellow] Coins?"
	
	basicReply {
		replyId "A"
		statement "Yes, buy wallpaper."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_FOUR_YELLOW"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "No, show me the other options."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "No, show me the other options."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Congratulations! Your new wallpaper will be unlocked in a minute!"
// ---   o How many eWALL Coins do I have left?
// ---   o Show me the Reward Overview application.
// ---   o What else can I buy with my eWALL Coins?
// ---   o Goodbye.
// --------------------
step {
	stepId "PurchaseWallpaperComplete"
	statement "Congratulations! Your new wallpaper will be unlocked in a minute!"
	
	basicReply {
		replyId "A"
		statement "How many eWALL Coins do I have left?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Show me the Reward Overview application."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "What else can I buy with my eWALL Coins?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "What else can I buy with my eWALL Coins?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "Sorry, something went wrong with unlocking your reward."
// ---   o How many eWALL Coins do I have left?
// ---   o Show me the Reward Overview application.
// ---   o What else can I buy with my eWALL Coins?
// ---   o Goodbye.
// --------------------
step {
	stepId "PurchaseWallpaperFailed"
	statement "Sorry, something went wrong with unlocking your reward."
	
	basicReply {
		replyId "A"
		statement "How many eWALL Coins do I have left?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Show me the Reward Overview application."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "What else can I buy with my eWALL Coins?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "What else can I buy with my eWALL Coins?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
	
}

// --------------------
// --- "I'm sorry [firstName], you don't have enough eWALL Coins for that reward."
// ---   o How many eWALL Coins do I have left?
// ---   o Show me the Reward Overview application.
// ---   o What else can I buy with my eWALL Coins?
// ---   o Goodbye.
// --------------------
step {
	stepId "InsufficientCoins"
	statement "I'm sorry [firstName], you don't have enough eWALL Coins for that reward."
	
	basicReply {
		replyId "A"
		statement "How many eWALL Coins do I have left?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Show me the Reward Overview application."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "What else can I buy with my eWALL Coins?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "What else can I buy with my eWALL Coins?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}

// --------------------
// --- "You automatically earn eWALL Coins by being active (steps), doing video exercise, playing games and using eWALL applications."
// ---   o How many eWALL Coins do I have?
// ---   o Show me the Reward Overview application.
// ---   o What can I buy with my eWALL Coins?
// ---   o Goodbye.
// --------------------
step {
	stepId "ExplanationOne"
	statement "You automatically earn eWALL Coins by being active (steps), doing video exercise, playing games and using eWALL applications."
	
	basicReply {
		replyId "A"
		statement "How many eWALL Coins do I have?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Show me the Reward Overview application."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "What can I buy with my eWALL Coins?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "What can I buy with my eWALL Coins?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Goodbye."
	}
}
