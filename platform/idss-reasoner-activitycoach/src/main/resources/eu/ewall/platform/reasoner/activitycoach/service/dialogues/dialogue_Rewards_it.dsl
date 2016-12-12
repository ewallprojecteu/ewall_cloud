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
	statement "Ciao [firstName]! Vuoi parlare di premi? Come posso aiutarti?"
	
	basicReply {
		replyId "A"
		statement "Quante Monete eWALL ho?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Cosa sono le monete eWALL?"
		nextStepId "ExplanationOne"
	}
	
	basicReply {
		replyId "C"
		statement "Cosa posso comprare con le mie Monete eWALL?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Cosa posso comprare con le mie Monete eWALL?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "Al momento hai [totalCoins] Monete eWALL."
	
	basicReply {
		replyId "A"
		statement "Mostrami l'applicazione Panoramica sul Premio."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "B"
		statement "Cosa posso comprare con le mie Monete eWALL?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Cosa posso comprare con le mie Monete eWALL?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "I seguenti articoli sono disponibili per l'acquisto. Cosa vorresti fare?"
	
	basicReply {
		replyId "A"
		statement "Compra la carta da parati anni sessanta ([rewardCostsWallpaperOneSixties] Monete)."
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
		statement "[Monete Insufficienti] Compra la carta da parati anni sessanta ([rewardCostsWallpaperOneSixties] Monete)."
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
		statement "Compra la carta da parati a strisce ([rewardCostsWallpaperTwoStripes] Monete)."
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
		statement "[Monete Insufficienti] Compra la carta da parati a strisce ([rewardCostsWallpaperTwoStripes] Monete)."
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
		statement "Compra la carta da parati blu ([rewardCostsWallpaperThreeBlue] Monete)."
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
		statement "[Monete Insufficienti] Compra la carta da parati blu ([rewardCostsWallpaperThreeBlue] Monete)."
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
		statement "Compra la carta da parati gialla ([rewardCostsWallpaperFourYellow] Monete)."
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
		statement "[Monete Insufficienti] Compra la carta da parati gialla ([rewardCostsWallpaperFourYellow] Monete)."
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
		statement "Arrivederci."
	}
}

// --------------------
// --- "Congratulations [firstName], it looks like you have unlocked all eWALL rewards!"
// ---   o Goodbye.
// --------------------
step {
	stepId "PurchaseOverviewEmpty"
	statement "Complimenti [firstName], sembra che tu abbia sbloccato tutti i premi di  eWALL!"
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "Sei sicuro di voler comprare la carta da parati anni sessanta per [rewardCostsWallpaperOneSixties] Monete?"
	
	basicReply {
		replyId "A"
		statement "Si, compra la carta da parati."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_ONE_SIXTIES"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "No, mostrami altre opzioni."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "No, mostrami altre opzioni."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}

	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "Sei sicuro di voler comprare la carta da parati a strisce per [rewardCostsWallpaperTwoStripes] Monete?"
	
	basicReply {
		replyId "A"
		statement "Si, compra la carta da parati."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_TWO_STRIPES"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "No, mostrami altre opzioni."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "No, mostrami altre opzioni."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "Sei sicuro di voler comprare la carta da parati blu per [rewardCostsWallpaperThreeBlue] Monete?"
	
	basicReply {
		replyId "A"
		statement "Si, compra la carta da parati."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_THREE_BLUE"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "No, mostrami altre opzioni."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "No, mostrami altre opzioni."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "Sei sicuro di voler comprare la carta da parati gialla per [rewardCostsWallpaperFourYellow] Monete?"
	
	basicReply {
		replyId "A"
		statement "Si, compra la carta da parati."
		action {
			actionType "unlockReward"
			parameter "WALLPAPER_FOUR_YELLOW"
			nextStepIdSuccess "PurchaseWallpaperComplete"
			nextStepIdFailure "PurchaseWallpaperFailed"
		}
	}
	
	basicReply {
		replyId "B"
		statement "No, mostrami altre opzioni."
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "C"
		statement "No, mostrami altre opzioni."
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "Congratulazioni! La tua nuova carta da parati sarà sbloccata in un minuto!"
	
	basicReply {
		replyId "A"
		statement "Quante Monete eWALL mi rimangono?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Mostrami l'applicazione Panoramica sul Premio."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Cos'altro posso comprare con le mie monete eWALL?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Cos'altro posso comprare con le mie monete eWALL?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "Scusaci, qualcosa è andato male mentre sbloccavamo il tuo premio."
	
	basicReply {
		replyId "A"
		statement "Quante Monete eWALL mi rimangono?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "What are eWALL Coins? [NEEDS IT TRANSLATION]"
		nextStepId "ExplanationOne"
	}
	
	basicReply {
		replyId "C"
		statement "Cos'altro posso comprare con le mie monete eWALL?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Cos'altro posso comprare con le mie monete eWALL?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "Mi dispiace [firstName], non hai abbastanza Monete eWALL per quel premio."
	
	basicReply {
		replyId "A"
		statement "Quante Monete eWALL mi rimangono?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Mostrami l'applicazione Panoramica sul Premio."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Cos'altro posso comprare con le mie monete eWALL?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Cos'altro posso comprare con le mie monete eWALL?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
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
	statement "Puoi guadagnare automaticamente monete eWALL riamanendo attivo (passi), facendo gli esercizi video, giocando e usando le applicazioni di eWALL."
	
	basicReply {
		replyId "A"
		statement "Quante Monete eWALL ho?"
		nextStepId "CoinBalance"
	}
	
	basicReply {
		replyId "B"
		statement "Mostrami l'applicazione Panoramica sul Premio."
		action {
			actionType "openApplication"
			parameter "rewardSystem"
		}
	}
	
	basicReply {
		replyId "C"
		statement "Cosa posso comprare con le mie Monete eWALL?"
		nextStepId "PurchaseOverview"
		condition {
			conditionType "rewardAvailable"
			parameter "ANY"
		}
	}
	
	basicReply {
		replyId "D"
		statement "Cosa posso comprare con le mie Monete eWALL?"
		nextStepId "PurchaseOverviewEmpty"
		condition {
			conditionType "rewardAvailable"
			parameter "NONE"
		}
	}
	
	basicReply {
		replyId "BYE"
		statement "Arrivederci."
	}
}
