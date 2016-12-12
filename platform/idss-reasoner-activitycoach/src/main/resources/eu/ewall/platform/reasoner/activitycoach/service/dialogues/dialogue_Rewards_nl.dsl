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
	statement "Hallo [firstName]! Dus, je wilt het hebben over beloningen? Hoe kan ik je helpen?"
	
	basicReply {
		replyId "A"
		statement "Hoeveel eWALL Munten heb ik?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Wat zijn eWALL Munten?"
		nextStepId "ExplanationOne"
	}
	
	basicReply {
		replyId "C"
		statement "Wat kan ik kopen met mijn eWALL Munten?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Wat kan ik kopen met mijn eWALL Munten?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "Je hebt op dit moment [totalCoins] eWALL Munten."
	
	basicReply {
		replyId "A"
		statement "Laat me het Beloning Overzicht zien."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Wat kan ik kopen met mijn eWALL Munten?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Wat kan ik kopen met mijn eWALL Munten?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
	}
}

// --------------------
// --- "The following rewards are available for purchase. What would you like to do?"
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
	statement "De onderstaande beloningen zijn beschikbaar. Wat wil je doen?"
	
	basicReply {
		replyId "A"
		statement "Koop het jaren '60 behang ([rewardCostsWallpaperOneSixties] Munten)."
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
		statement "[Onvoldoende Munten] Koop het jaren '60 behang ([rewardCostsWallpaperOneSixties] Munten)."
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
		statement "Koop het gestreepte behang ([rewardCostsWallpaperTwoStripes] Munten)."
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
		statement "[Onvoldoende Munten] Koop het gestreepte behang ([rewardCostsWallpaperTwoStripes] Munten)."
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
		statement "Koop het blauwe behang ([rewardCostsWallpaperThreeBlue] Munten)."
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
		statement "[Onvoldoende Munten] Koop het blauwe behang ([rewardCostsWallpaperThreeBlue] Munten)."
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
		statement "Koop het gele behang ([rewardCostsWallpaperFourYellow] Munten)."
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
		statement "[Onvoldoende Munten] Koop het gele behang ([rewardCostsWallpaperFourYellow] Munten)."
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
		statement "Tot ziens."
	}
}

// --------------------
// --- "Congratulations [firstName], it looks like you have unlocked all eWALL rewards!"
// ---   o Goodbye.
// --------------------
step {
	stepId "PurchaseOverviewEmpty"
	statement "Gefeliciteerd [firstName], het lijkt er op dat je alle beloningen hebt ontgrendeld."
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "Weet je zeker dat je het jaren '60 behang wilt kopen voor [rewardCostsWallpaperOneSixties] Munten?"
	
	basicReply {
		replyId "A"
		statement "Ja, koop behang."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_ONE_SIXTIES"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Nee, laat me de andere opties zien."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Nee, laat me de andere opties zien."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}

	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "Weet je zeker dat je het gestreepte behang wilt kopen voor [rewardCostsWallpaperTwoStripes] Munten?"
	
	basicReply {
		replyId "A"
		statement "Ja, koop behang."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_TWO_STRIPES"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Nee, laat me de andere opties zien."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Nee, laat me de andere opties zien."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "Weet je zeker dat je het blauwe behang wilt kopen voor [rewardCostsWallpaperThreeBlue] Munten?"
	
	basicReply {
		replyId "A"
		statement "Ja, koop behang."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_THREE_BLUE"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Nee, laat me de andere opties zien."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Nee, laat me de andere opties zien."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "Weet je zeker dat je het gele behang wilt kopen voor [rewardCostsWallpaperFourYellow] Munten?"
	
	basicReply {
		replyId "A"
		statement "Ja, koop behang."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_FOUR_YELLOW"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Nee, laat me de andere opties zien."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Nee, laat me de andere opties zien."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "Gefeliciteerd! Je nieuwe behang wordt over een minuutje ontgrendeld!"
	
	basicReply {
		replyId "A"
		statement "Hoeveel eWALL Munten heb ik nog over?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Laat me het Beloning Overzicht zien."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Wat kan ik nog meer met mijn eWALL Munten kopen?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Wat kan ik nog meer met mijn eWALL Munten kopen?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "Sorry, er ging iets mis bij het ontgrendelen van de beloning."
	
	basicReply {
		replyId "A"
		statement "Hoeveel eWALL Munten heb ik nog over?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Laat me het Beloning Overzicht zien."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Wat kan ik nog meer met mijn eWALL Munten kopen?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Wat kan ik nog meer met mijn eWALL Munten kopen?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "Het spijt me [firstName], je hebt niet genoeg Munten voor die beloning."
	
	basicReply {
		replyId "A"
		statement "Hoeveel eWALL Munten heb ik nog over?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Laat me het Beloning Overzicht zien."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Wat kan ik nog meer met mijn eWALL Munten kopen?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Wat kan ik nog meer met mijn eWALL Munten kopen?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
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
	statement "Je verdient automatisch eWALL Munten door actief te zijn (stappen), video oefeningen te doen, spelletjes te spelen en eWALL applicaties te gebruiken."
	
	basicReply {
		replyId "A"
		statement "Hoeveel eWALL Munten heb ik?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Laat me het Beloning Overzicht zien."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Wat kan ik kopen met mijn eWALL Munten?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Wat kan ik kopen met mijn eWALL Munten?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Tot ziens."
	}
}
