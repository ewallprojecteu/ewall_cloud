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
	statement "Hallo [firstName]! Du willst über Belohnungen reden? Wie kann ich dir helfen?"
	
	basicReply {
		replyId "A"
		statement "Wie viele eWALL Münzen habe ich?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Was sind eWALL Münzen?"
		nextStepId "ExplanationOne"
	}
	
	basicReply {
		replyId "C"
		statement "Was kann ich mit meinen eWALL Münzen kaufen?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Was kann ich mit meinen eWALL Münzen kaufen?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
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
	statement "Du hast momentan [totalCoins] eWALL Münzen."
	
	basicReply {
		replyId "A"
		statement "Zeige mir die Belohunungsübersicht."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Was kann ich mit meinen eWALL Münzen kaufen?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Was kann ich mit meinen eWALL Münzen kaufen?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
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
	statement "Die folgenden Dinge kannst du dir kaufen. Was möchtest du?"
	
	basicReply {
		replyId "A"
		statement "Kaufe die 60's Wandtapete ([rewardCostsWallpaperOneSixties] Münzen)."
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
		statement "[Zu Wenige Münzen] Kaufe die 60's Wandtapete ([rewardCostsWallpaperOneSixties] Münzen)."
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
		statement "Kaufe die gestreifte Tapete ([rewardCostsWallpaperTwoStripes] Münzen)."
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
		statement "[Zu Wenige Münzen] Kaufe die gestreifte Tapete ([rewardCostsWallpaperTwoStripes] Münzen)."
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
		statement "Kaufe die blaue Tapete ([rewardCostsWallpaperThreeBlue] Münzen)."
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
		statement "[Zu Wenige Münzen] Kaufe die blaue Tapete ([rewardCostsWallpaperThreeBlue] Münzen)."
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
		statement "Kaufe die gelbe Tapete ([rewardCostsWallpaperFourYellow] Münzen)."
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
		statement "[Zu Wenige Münzen] Kaufe die gelbe Tapete ([rewardCostsWallpaperFourYellow] Münzen)."
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
		statement "Auf Wiedersehen."
	}
}

// --------------------
// --- "Congratulations [firstName], it looks like you have unlocked all eWALL rewards!"
// ---   o Goodbye.
// --------------------
step {
	stepId "PurchaseOverviewEmpty"
	statement "Gratuliere [firstName], es sieht so aus als hättest du alle eWALL Belohnungen bekommen!"
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
	}
}

// --------------------
// --- "Are you sure you want to buy the Sixties Wallpaper for [rewardCostsWallpaperOneSixties] coins?"
// ---   o Yes, buy wallpaper.
// ---   o No, show me the other options.
// ---   o Goodbye.
// --------------------
step {
	stepId "PurchaseConfirmWallpaperOneSixties"
	statement "Bist du sicher, dass du die 60's Tapete für [rewardCostsWallpaperOneSixties] Münzen kaufen möchtest?"
	
	basicReply {
		replyId "A"
		statement "Ja, Tapete kaufen."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_ONE_SIXTIES"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Nein, zeige mir andere Optionen."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Nein, zeige mir andere Optionen."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}

	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
	}
}

// --------------------
// --- "Are you sure you want to buy the Striped Wallpaper for [rewardCostsWallpaperTwoStripes] coins?"
// ---   o Yes, buy wallpaper.
// ---   o No, show me the other options.
// ---   o Goodbye.
// --------------------
step {
	stepId "PurchaseConfirmWallpaperTwoStripes"
	statement "Bist du sicher, dass du die gestreifte Tapete um [rewardCostsWallpaperTwoStripes] Münzen kaufen möchtest?"
	
	basicReply {
		replyId "A"
		statement "Ja, Tapete kaufen."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_TWO_STRIPES"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Nein, zeige mir andere Optionen."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Nein, zeige mir andere Optionen."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
	}
}

// --------------------
// --- "Are you sure you want to buy the Blue Wallpaper for [rewardCostsWallpaperThreeBlue] coins?"
// ---   o Yes, buy wallpaper.
// ---   o No, show me the other options.
// ---   o Goodbye.
// --------------------
step {
	stepId "PurchaseConfirmWallpaperThreeBlue"
	statement "Bist du sicher, dass du die blaue Tapete für [rewardCostsWallpaperThreeBlue] Münzen kaufen möchtest?"
	
	basicReply {
		replyId "A"
		statement "Ja, Tapete kaufen."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_THREE_BLUE"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Nein, zeige mir andere Optionen."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Nein, zeige mir andere Optionen."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
	}
}

// --------------------
// --- "Are you sure you want to buy the Yellow Wallpaper for [rewardCostsWallpaperFourYellow] coins?"
// ---   o Yes, buy wallpaper.
// ---   o No, show me the other options.
// ---   o Goodbye.
// --------------------
step {
	stepId "PurchaseConfirmWallpaperFourYellow"
	statement "Bist du sicher, dass du die gelbe Tapete für [rewardCostsWallpaperFourYellow] Münzen kaufen möchtest?"
	
	basicReply {
		replyId "A"
		statement "Ja, Tapete kaufen."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_FOUR_YELLOW"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Nein, zeige mir andere Optionen."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Nein, zeige mir andere Optionen."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
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
	statement "Gratulation! Deine neue Taptet wird dir in einer Minute freigegeben!"
	
	basicReply {
		replyId "A"
		statement "Wie viele eWALL Münzen habe ich übrig?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Zeige mir die Belohunungsübersicht."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Was kann ich sonst mit meinen eWALL Münzen kaufen?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Was kann ich sonst mit meinen eWALL Münzen kaufen?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
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
	statement "Entschuldige, entwas ist schief gegangen beim Freigeben deiner Belohnung."
	
	basicReply {
		replyId "A"
		statement "Wie viele eWALL Münzen habe ich übrig?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Zeige mir die Belohunungsübersicht."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Was kann ich sonst mit meinen eWALL Münzen kaufen?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Was kann ich sonst mit meinen eWALL Münzen kaufen?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
	}
	
}

// --------------------
// --- "I'm sorry [firstName], you don't have enough coins for that reward."
// ---   o How many eWALL Coins do I have left?
// ---   o Show me the Reward Overview application.
// ---   o What else can I buy with my eWALL Coins?
// ---   o Goodbye.
// --------------------
step {
	stepId "InsufficientCoins"
	statement "Tut mir leid [firstName], du hast nicht genug eWALL Münzen für diese Belohnung."
	
	basicReply {
		replyId "A"
		statement "Wie viele eWALL Münzen habe ich übrig?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Zeige mir die Belohunungsübersicht."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Was kann ich sonst mit meinen eWALL Münzen kaufen?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Was kann ich sonst mit meinen eWALL Münzen kaufen?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
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
	statement "Du erhältst automatisch eWALL Münzen wenn du  körperlich aktiv bist (Schritte und Videoübungen), kognitive Spiele spielst oder die eWALL Funktionen verwendest."
	
	basicReply {
		replyId "A"
		statement "Wie viele eWALL Münzen habe ich?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Zeige mir die Belohunungsübersicht."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Was kann ich mit meinen eWALL Münzen kaufen?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Was kann ich mit meinen eWALL Münzen kaufen?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Auf Wiedersehen."
	}
}
