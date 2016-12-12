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
	statement "Hej [firstName]! Nå, så du vil snakke om præmier? Hvordan kan jeg hjælpe dig?"
	
	basicReply {
		replyId "A"
		statement "Hvor mange eWALL Mønter har jeg?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Hvad er eWALL Mønter?"
		nextStepId "ExplanationOne"
	}
	
	basicReply {
		replyId "C"
		statement "Hvad kan jeg købe med mine eWALL Mønter?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Hvad kan jeg købe med mine eWALL Mønter?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
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
	statement "Du har nu [totalCoins] eWALL Mønter."
	
	basicReply {
		replyId "A"
		statement "Vis mig præmieoversigten."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Hvad kan jeg købe med mine eWALL Mønter?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Hvad kan jeg købe med mine eWALL Mønter?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
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
	statement "Følgende genstande er til salg. Hvad vil du gerne gøre?"
	
	basicReply {
		replyId "A"
		statement "Køb 60'er tapetet ([rewardCostsWallpaperOneSixties] Mønter)."
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
		statement "[Ikke nok mønter] Køb 60'er tapetet ([rewardCostsWallpaperOneSixties] Mønter)."
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
		statement "Køb det stribede tapet ([rewardCostsWallpaperTwoStripes] Mønter)."
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
		statement "[Ikke nok mønter] Køb det stribede tapet ([rewardCostsWallpaperTwoStripes] Mønter)."
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
		statement "Køb det blå tapet ([rewardCostsWallpaperThreeBlue] Mønter)."
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
		statement "[Ikke nok mønter] Køb det blå tapet ([rewardCostsWallpaperThreeBlue] Mønter)."
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
		statement "Køb det gule tapet ([rewardCostsWallpaperFourYellow] Mønter)."
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
		statement "[Ikke nok mønter] Køb det gule tapet ([rewardCostsWallpaperFourYellow] Mønter)."
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
		statement "Farvel."
	}
}

// --------------------
// --- "Congratulations [firstName], it looks like you have unlocked all eWALL rewards!"
// ---   o Goodbye.
// --------------------
step {
	stepId "PurchaseOverviewEmpty"
	statement "Tillykke [firstName], det ser ud til, at du har låst op for eWALL præmier!"
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
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
	statement "Er du sikker på, at du vil købe 60'er tapetet for [rewardCostsWallpaperOneSixties] Mønter?"
	
	basicReply {
		replyId "A"
		statement "Ja, køb tapet."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_ONE_SIXTIES"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Nej, vis mig andre muligheder."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Nej, vis mig andre muligheder."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}

	basicReply {
		replyId "BYE"
		statement "Farvel."
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
	statement "Er du sikker på, at du vil købe det stribede tapet for [rewardCostsWallpaperTwoStripes] Mønter?"
	
	basicReply {
		replyId "A"
		statement "Ja, køb tapet."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_TWO_STRIPES"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Nej, vis mig andre muligheder."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Nej, vis mig andre muligheder."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
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
	statement "Er du sikker på, at du vil købe det blå tapet for [rewardCostsWallpaperThreeBlue] Mønter?"
	
	basicReply {
		replyId "A"
		statement "Ja, køb tapet."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_THREE_BLUE"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Nej, vis mig andre muligheder."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Nej, vis mig andre muligheder."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
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
	statement "Er du sikker på, at du vil købe det gule tapet for [rewardCostsWallpaperFourYellow] Mønter?"
	
	basicReply {
		replyId "A"
		statement "Ja, køb tapet."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_FOUR_YELLOW"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Nej, vis mig andre muligheder."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Nej, vis mig andre muligheder."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
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
	statement "Tillykke! Der vil være låst op for dit nye tapet om et øjeblok!"
	
	basicReply {
		replyId "A"
		statement "Hvor mange eWALL Mønter har jeg tilbage?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Vis mig præmieoversigten."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Hvad kan jeg ellers købe for mine eWALL mønter?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Hvad kan jeg ellers købe for mine eWALL mønter?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
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
	statement "Beklager. Der gik noget galt med at låse din præmie op."
	
	basicReply {
		replyId "A"
		statement "Hvor mange eWALL Mønter har jeg tilbage?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Vis mig præmieoversigten."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Hvad kan jeg ellers købe for mine eWALL mønter?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Hvad kan jeg ellers købe for mine eWALL mønter?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
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
	statement "Jeg beklager [firstName]. Du kar ikke nok eWALL mønter til den præmie."
	
	basicReply {
		replyId "A"
		statement "Hvor mange eWALL Mønter har jeg tilbage?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Vis mig præmieoversigten."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Hvad kan jeg ellers købe for mine eWALL mønter?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Hvad kan jeg ellers købe for mine eWALL mønter?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
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
	statement "Du optjener automatisk eWALL Mønter ved at være aktiv (skridt), ved at lave træningsøvelserne, spille spil og bruge de forskellige eWALL applikationer."
	
	basicReply {
		replyId "A"
		statement "Hvor mange eWALL Mønter har jeg?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Vis mig præmieoversigten."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Hvad kan jeg købe med mine eWALL Mønter?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Hvad kan jeg købe med mine eWALL Mønter?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Farvel."
	}
}
