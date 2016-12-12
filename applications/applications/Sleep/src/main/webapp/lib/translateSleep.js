function getSleepQuality(language, quality)
{  
    var output= {
                set: false,
                color: "#ffffff", // white
                code: ""
                };
    
    switch(quality)
    {
        case "GOOD":
                output.set = true;
                output.color = "#12c700"; // green
                switch(language){
                        case 'en':
                        case 'en-GB':
                        case 'en-US': output.code = "GOOD"; break;
                        case 'it': output.code = "BUONO"; break;
                        case 'de':
                        case 'de-AT': output.code = "GUT"; break;
                        case 'nl': output.code = "IS GOED"; break;
                        default: output.code = "GOOD"; break;
                        
                };
            return output;
            
        case "LATE":
            
            output.set = true;
            output.color = "#ffd600"; // green
            switch(language){
                        case 'en':
                        case 'en-US': output.code = "LATE"; break;
                        case 'it': output.code = "TARDO"; break;
                        case 'de':                        
                        case 'de-AT': output.code = "FRÜH"; break;
                        case 'nl': output.code = "LAAT"; break;
                        default: output.code = "LATE"; break;
            };
            return output;
            
        case "EARLY":
            output.set = true;
            output.color = "#64dd17"; // green
            switch(language){
                        case 'en':
                        case 'en-US': output.code = "EARLY"; break;
                        case 'it': output.code = "PRESTO"; break;
                        case 'de':                        
                        case 'de-AT': output.code = "SPÄT"; break;
                        case 'nl': output.code = "VROEG"; break;
                        default: output.code = "EARLY"; break;
            };
            return output;
            
        case "NORMAL":
            output.set = true;
            output.color = "#64dd17"; // green
            switch(language){
                        case 'en':
                        case 'en-US': output.code = "NORMAL"; break;
                        case 'it': output.code = "NORMALE"; break;
                        case 'de':
                        case 'de-AT': output.code = "NORMAL"; break;
                        case 'nl': output.code = "NORMAAL"; break;
                        default: output.code = "NORMAL"; break;
                        
                };
            return output;
            
        case "BAD":
            output.set = true;
            output.color = "#ffab00"; // green
            switch(language){
                        case 'en':
                        case 'en-US': output.code = "BAD"; break;
                        case 'it': output.code = "MALE"; break;
                        case 'de':
                        case 'de-AT': output.code = "SCHLECHT"; break;
                        case 'nl': output.code = "SLECHT"; break;
                        default: output.code = "BAD"; break;
                        
                }
            return output;
            
        default: output.set = true;
                 output.color = "#ffffff"; // green
                 output.code = "";
                 return output;
    }
};

function getWentToBedText(language, sleepStartTime, sleepLRStartTime)
{
    switch(language){
            
    case 'da':
    case 'da-DK':
            var output = {
                    lang: "da",
                    content: {
                        title: "Gik i seng ",
                        icon: "../Sleep/resources/cat-icons-64/cat_sing.png",
                        text: [" Du gik i seng på " + sleepStartTime + ".",
                        " Du plejer at gå i seng på " + sleepLRStartTime + "."],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'nl':
    case 'nl-NL':
            var output = {
                    lang: "nl",
                    content: {
                        title: "Naar bed gegaan ",
                        icon: "../Sleep/resources/cat-icons-64/cat_sing.png",
                        text: [" Je ging naar bed " + sleepStartTime + ".",
                        " Ga je meestal naar bed " + sleepLRStartTime + "."],                       
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'it':
            var output = {
                    lang: "it",
                    content: {
                        title: "Andato a letto ",
                        icon: "../Sleep/resources/cat-icons-64/cat_sing.png",
                        text: [" Sei andato a letto alle " + sleepStartTime + ".",
                        " Di solito va a letto alle " + sleepLRStartTime + "."],                       
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'en':
    case 'en-US':
            var output = {
                    lang: "en",
                    content: {
                        title: "Went to bed ",
                        icon: "../Sleep/resources/cat-icons-64/cat_sing.png",
                        text: [" You went to bed at " + sleepStartTime + ".",
                        " You usually go to bed at " + sleepLRStartTime + "."],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'de':
    case 'de-AT': 
            var output = {
                    lang: "de",
                    content: {
                        title: "Zu Bett gegangen ",
                        icon: "../Sleep/resources/cat-icons-64/cat_sing.png",
                        text: [" Sie ging zu Bett an " + sleepStartTime + ".", " Sie gehen in der Regel zu Bett an " + sleepLRStartTime + "."],                
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    default: var output = {
                    lang: "en",
                    content: {
                        title: "Went to bed",
                        icon: "../Sleep/resources/cat-icons-64/cat_sing.png",
                        text: ["You went to bed at " + sleepStartTime + ".", " You usually go to bed at " + sleepLRStartTime + "."],
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    }
}

function getSleepPeriodText(language, countSleepPeriod, sleepPeriodDuration){
    switch(language){
    
    case 'it':
        var output = {
                    lang: "it",
                    content: {
                        title: "Periodo di sonno "  + countSleepPeriod,
                        icon: "../Sleep/resources/cat-icons-64/cat_sleep.png",
                         text: [" Eri a letto per " + sleepPeriodDuration + "."],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;            
            
    case 'de':
    case 'de-AT':
            var output = {
                    lang: "de",
                    content: {
                        title: "Schlafperiode " + countSleepPeriod,
                        icon: "../Sleep/resources/cat-icons-64/cat_sleep.png",
                         text: [" Sie waren im Bett für "  + sleepPeriodDuration + "."],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'nl':
    case 'nl-NL':
            var output = {
                    lang: "nl",
                    content: {
                        title: "Slaapperiode " + countSleepPeriod,
                        icon: "../Sleep/resources/cat-icons-64/cat_sleep.png",
                         text: [" Je was in bed voor " + sleepPeriodDuration + "."],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'da':
    case 'da-DK':
            var output = {
                    lang: "da",
                    content: {
                        title: "Sleep periode " + countSleepPeriod,
                        icon: "../Sleep/resources/cat-icons-64/cat_sleep.png",
                         text: [" Du var i seng til " + sleepPeriodDuration + "."],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'en':
    case 'en-US':
            var output = {
                    lang: "en",
                    content: {
                        title: "Sleep period " + countSleepPeriod,
                        icon: "../Sleep/resources/cat-icons-64/cat_sleep.png",
                        text: [" You were in bed for " + sleepPeriodDuration + "."],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    default:
            var output = {
                    lang: "en",
                    content: {
                        title: "Sleep period " + countSleepPeriod,
                        icon: "../Sleep/resources/cat-icons-64/cat_sleep.png",
                        text: [" You were in bed for " + sleepPeriodDuration + "."],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
    }
}

function getSleepInterruptionText(language, countSleepInterruption, sleepInterruptionDuration){
    switch(language){
    
    case 'it':
        var output = {
                    lang: "it",
                    content: {
                        title: "Interruzione del sonno "  + countSleepInterruption,
                        icon: "../Sleep/resources/cat-icons-64/cat_sleep.png",
                         text: [" Eri fuori dal letto per " + sleepInterruptionDuration + "."],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;            
            
    case 'de':
    case 'de-AT':
            var output = {
                    lang: "de",
                    content: {
                        title: "Schlaf-Unterbrechung " + countSleepInterruption,
                        icon: "../Sleep/resources/cat-icons-64/cat_sleep.png",
                         text: [" Sie waren aus dem Bett für "  + sleepInterruptionDuration + "."],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'nl':
    case 'nl-NL':
            var output = {
                    lang: "nl",
                    content: {
                        title: "Sleep onderbreking " + countSleepInterruption,
                        icon: "../Sleep/resources/cat-icons-64/cat_sleep.png",
                         text: [" Je was uit bed gedurende " + sleepInterruptionDuration + "."],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'da':
    case 'da-DK':
            var output = {
                    lang: "da",
                    content: {
                        title: "Søvn afbrydelse " + countSleepInterruption,
                        icon: "../Sleep/resources/cat-icons-64/cat_sleep.png",
                         text: [" Du var ude af sengen i " + sleepInterruptionDuration + "."],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'en':
    case 'en-US':
            var output = {
                    lang: "en",
                    content: {
                        title: "Sleep interruption " + countSleepInterruption,
                        icon: "../Sleep/resources/cat-icons-64/cat_sleep.png",
                        text: [" You were out of bed for " + sleepInterruptionDuration + "."],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    default:
            var output = {
                    lang: "en",
                    content: {
                        title: "Sleep interruption " + countSleepInterruption,
                        icon: "../Sleep/resources/cat-icons-64/cat_sleep.png",
                        text: [" You were out of bed for " + sleepInterruptionDuration + "."],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
    }
}


function getWokeUpText(language, sleepStartTime, sleepLRStartTime)
{
     switch(language){
            
    case 'da':
    case 'da-DK':
            var output = {
                    lang: "da",
                    content: {
                        title: "Vågnede ",
                        icon: "../Sleep/resources/cat-icons-64/cat_sing.png",
                        text: [" Du vågnede ved " + sleepStartTime + ".",
                        " Du normalt vågner op på " + sleepLRStartTime + "."],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'nl':
    case 'nl-NL':
            var output = {
                    lang: "nl",
                    content: {
                        title: "Wakker geworden ",
                        icon: "../Sleep/resources/cat-icons-64/cat_sing.png",
                        text: [" Je ging naar bed " + sleepStartTime + ".",
                        " Ga je meestal naar bed " + sleepLRStartTime + "."],                       
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'it':
            var output = {
                    lang: "it",
                    content: {
                        title: "Svegliato ",
                        icon: "../Sleep/resources/cat-icons-64/cat_sing.png",
                        text: [" Ti sei svegliato alle " + sleepStartTime + ".",
                        " Di solito sveglia alle " + sleepLRStartTime + "."],                       
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'en':
    case 'en-US':
            var output = {
                    lang: "en",
                    content: {
                        title: "Woke up ",
                        icon: "../Sleep/resources/cat-icons-64/cat_purr.png",
                        text: [" You woke up at " + sleepStartTime + ".",
                        " You usually wake-up at " + sleepLRStartTime + "."],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'de':
    case 'de-AT': 
            var output = {
                    lang: "de",
                    content: {
                        title: "Wachte auf ",
                        icon: "../Sleep/resources/cat-icons-64/cat_sing.png",
                        text: [" Sie wachte auf um " + sleepStartTime + ".", " Sie aufwachen in der Regel an " + sleepLRStartTime + "."],                
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    default: var output = {
                    lang: "en",
                    content: {
                        title: "Woke up ",
                        icon: "../Sleep/resources/cat-icons-64/cat_purr.png",
                       text: [" You woke up at " + sleepStartTime + ".",
                        " You usually wake-up at " + sleepLRStartTime + "."],
                        duration: "Good night!",
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    }
}

function getConclusionText(language, totalSleepDurationStr)
{
     switch(language){
            
    case 'da':
    case 'da-DK':
            var output = {
                    lang: "da",
                    content: {
                        title: "Konklusion: Din søvn er ",
                        icon: "../Sleep/resources/cat-icons-64/cat_sing.png",
                        text: [" Din samlede søvn tid: " + totalSleepDurationStr + ".",
                        " For en god nat søvn, du har brug for at sove mindst 8 timer om natten. "],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'nl':
    case 'nl-NL':
            var output = {
                    lang: "nl",
                    content: {
                        title: "Conclusie: Uw slaap is ",
                        icon: "../Sleep/resources/cat-icons-64/cat_sing.png",
                        text: [" Uw totale slaaptijd: " + totalSleepDurationStr + ".",
                        " Voor een goede nachtrust moet u minimaal 8 uur per nacht slapen. "],                       
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'it':
            var output = {
                    lang: "it",
                    content: {
                        title: "Conclusione: Il sonno è ",
                        icon: "../Sleep/resources/cat-icons-64/cat_sing.png",
                        text: [" Il tuo durata totale del sonno: " + totalSleepDurationStr + ".",
                        " Per una notte di sonno BUONA avete bisogno di dormire almeno 8 ore per notte. "],                       
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'en':
    case 'en-US':
            var output = {
                    lang: "en",
                    content: {
                        title: "Conclusion: Your sleep is ",
                        icon: "../Sleep/resources/cat-icons-64/cat_purr.png",
                        text: [" Your total sleep time: " + totalSleepDurationStr + ".",
                        " For a GOOD night sleep you need to sleep at least 8 hours per night. "],                        
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    case 'de':
    case 'de-AT': 
            var output = {
                    lang: "de",
                    content: {
                        title: "Fazit: Ihr Schlaf ist ",
                        icon: "../Sleep/resources/cat-icons-64/cat_sing.png",
                        text: [" Ihre Gesamtschlafdauer: " + totalSleepDurationStr + ".",
                        " Für einen guten Schlaf Sie brauchen, um mindestens 8 Stunden pro Nacht schlafen. "],                
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    default: var output = {
                    lang: "en",
                    content: {
                        title: "Conclusion: Your sleep is ",
                        icon: "../Sleep/resources/cat-icons-64/cat_purr.png",
                       text: [" Your total sleep time: " + totalSleepDurationStr + ".",
                        " For a GOOD night sleep you need to sleep at least 8 hours per night. "],
                        duration: "Good night!",
                        quality: { 
                                   set: false,
                                   code: "",
                                   color: "#ffffff"
                                 }
                    }
                }
            return output;
            
    }
}

var interfaceLanguage = [
    {
        lang : "en",
         components : {
            summaryCoverBtn : "SHOW SUMMARY",
            summaryTitle : "Last Night",
            introductionSummaryParagraph : "Last night you went to bed at 22:20. That's relatively late according your usual bed time, 21:30. You fell asleep after relatively half an hour.",
             mainSummaryParagraph : "You had two sleep cycles. The first one started at around 22:40 o'clock and lasted approximately 1 hour and 49 minutes. The second one started at around 00:08 o'clock and lasted 7 hours and 29 minuted. In between you went to the bathroom once.",
            endSummaryParagraph : "You slept more then 9 hours last night, during which the sensors detected minimum movement. This means that your sleep was tranquil.",
            moreDetailsBtn: "MORE DETAILS",
            tagQuestion : "Add 3 tags to describe your sleep",
            addTagBtn : "ADD TAG",
            tags : {
                fallingAsleepLabel: "Falling asleep",
                
                sleepy : "sleepy",
                tired : "tired",
                tranquil : "tranquil",
                headache : "headache",
                happy : "happy",
                sad : "sad",
                
                overallSleepLabel : "Overall sleep",
                
                dreams : "dreams",
                nightmare : "nightmare",
                peacefull : "peacefull",
                restless : "restless",
                warm : "warm",
                cold : "cold",
                
                wakingUpLabel : "Waking up",
                
                happy : "happy",
                sad : "sad",
                rested : "rested",
                tired : "tired",
                excited : "excited",
                annoyed : "annoyed"
                },
            starRateQuestion : "Rate your sleep with stars"
        }
        
    },
    {
        lang : "en-US",
         components : {
            summaryCoverBtn : "SHOW SUMMARY",
            summaryTitle : "Last Night",
            introductionSummaryParagraph : "Last night you went to bed at 22:20. That's relatively late according your usual bed time, 21:30. You fell asleep after relatively half an hour.",
             mainSummaryParagraph : "You had two sleep cycles. The first one started at around 22:40 o'clock and lasted approximately 1 hour and 49 minutes. The second one started at around 00:08 o'clock and lasted 7 hours and 29 minuted. In between you went to the bathroom once.",
            endSummaryParagraph : "You slept more then 9 hours last night, during which the sensors detected minimum movement. This means that your sleep was tranquil.",
            moreDetailsBtn: "MORE DETAILS",
            tagQuestion : "Add 3 tags to describe your sleep",
            addTagBtn : "ADD TAG",
            tags : {
                fallingAsleepLabel: "Falling asleep",
                
                sleepy : "sleepy",
                tired : "tired",
                tranquil : "tranquil",
                headache : "headache",
                happy : "happy",
                sad : "sad",
                
                overallSleepLabel : "Overall sleep",
                
                dreams : "dreams",
                nightmare : "nightmare",
                peacefull : "peacefull",
                restless : "restless",
                warm : "warm",
                cold : "cold",
                
                wakingUpLabel : "Waking up",
                
                happy : "happy",
                sad : "sad",
                rested : "rested",
                tired : "tired",
                excited : "excited",
                annoyed : "annoyed"
                },
            starRateQuestion : "Rate your sleep with stars"
        }
        
    },
    {
        lang : "da",
         components : {
            summaryCoverBtn : "VIS OVERSIGT",
            summaryTitle : "I går nat",
            introductionSummaryParagraph : "I går gik du i seng kl. 22.00. Det er forholdsvis sent i forhold til din normale sengetid 21.30. Du faldt i søvn efter ca. en time og 30 minutter.",
             mainSummaryParagraph : "Du havde to søvncyklusser. Den første startede omkring kl. 22.40 og varede ca. en time og 49 minutter. Den anden startede omkring kl. 00.08 og varede syv timer og 29 minutter. Indimellem gik du på toilettet én gang.",
            endSummaryParagraph : "Du sov mere end ni timer i nat, hvor sensorene registrerede meget lidt bevægelse. Det betyder, at din søvn var rolig.",
            moreDetailsBtn: "FLERE DETALJER",
            tagQuestion : "Tilføj tre betegnelser som beskriver din søvn",
            addTagBtn : "Tilføj betegnelse",
            tags : {
                fallingAsleepLabel: "Falde i søvn",
                
                sleepy : "søvnig",
                tired : "træt",
                tranquil : "rolig",
                headache : "hovedpine",
                happy : "glad",
                sad : "bedrøvet",
                
                overallSleepLabel : "Søvn overordnet",
                
                dreams : "drømme",
                nightmare : "mareridt",
                peacefull : "fredfyldt",
                restless : "rastløs",
                warm : "varm",
                cold : "kold",
                
                wakingUpLabel : "Vågne op",
                
                happy : "glad",
                sad : "bedrøvet",
                rested : "udhvilet",
                tired : "træt",
                excited : "begejstret",
                annoyed : "irriteret"
                },
            starRateQuestion : "Bedøm din søvn med stjerner"
        }
        
    },
    {
        lang : "da-DK",
         components : {
            summaryCoverBtn : "VIS OVERSIGT",
            summaryTitle : "I går nat",
            introductionSummaryParagraph : "I går gik du i seng kl. 22.00. Det er forholdsvis sent i forhold til din normale sengetid 21.30. Du faldt i søvn efter ca. en time og 30 minutter.",
             mainSummaryParagraph : "Du havde to søvncyklusser. Den første startede omkring kl. 22.40 og varede ca. en time og 49 minutter. Den anden startede omkring kl. 00.08 og varede syv timer og 29 minutter. Indimellem gik du på toilettet én gang.",
            endSummaryParagraph : "Du sov mere end ni timer i nat, hvor sensorene registrerede meget lidt bevægelse. Det betyder, at din søvn var rolig.",
            moreDetailsBtn: "FLERE DETALJER",
            tagQuestion : "Tilføj tre betegnelser som beskriver din søvn",
            addTagBtn : "Tilføj betegnelse",
            tags : {
                fallingAsleepLabel: "Falde i søvn",
                
                sleepy : "søvnig",
                tired : "træt",
                tranquil : "rolig",
                headache : "hovedpine",
                happy : "glad",
                sad : "bedrøvet",
                
                overallSleepLabel : "Søvn overordnet",
                
                dreams : "drømme",
                nightmare : "mareridt",
                peacefull : "fredfyldt",
                restless : "rastløs",
                warm : "varm",
                cold : "kold",
                
                wakingUpLabel : "Vågne op",
                
                happy : "glad",
                sad : "bedrøvet",
                rested : "udhvilet",
                tired : "træt",
                excited : "begejstret",
                annoyed : "irriteret"
                },
            starRateQuestion : "Bedøm din søvn med stjerner"
        }
        
    },
    {
        lang : "de",
         components : {
            summaryCoverBtn : "ZUSAMMENFASSUNG ZEIGEN",
            summaryTitle : "Letzte Nacht",
            introductionSummaryParagraph : "Letzte Nacht sind Sie um 22:20 Uhr ins Bett gegangen. Das ist relativ spät im Vegleich zu Ihrer gewöhnlichen Bettzeit: 21:30 Uhr. Sie sind nach einer Stunde und 30 Minuten eingeschlafen.",
             mainSummaryParagraph : "Sie hatten zwei Schlafzyklen. Der erste fing ungefähr um 22:40 Uhr und dauerte ungefähr eine Stunde und 49 Minuten. Der zweite Zyklus fing ungefähr um 08:08 an und dauerte sieben Stunden und 29 Minuten. In der Zwischenzeit gingen Sie einmal ins Badezimmer.",
            endSummaryParagraph : "Sie schliefen letzte Nacht mehr als neun Stunden, in denen die Sensoren minimale Bewegung erfassten. Das heißt, dass Ihr Schlaf ruhig war.",
            moreDetailsBtn: "MEHR EINZELHEITEN",
            tagQuestion : "Fügen Sie drei Kategorien hinzu, um Ihren Schlaf zu beschreiben",
            addTagBtn : "KATEGORIE HINZUFÜGEN",
            tags : {
                fallingAsleepLabel: "Einschlafen",
                
                sleepy : "schläfrig",
                tired : "müde",
                tranquil : "ruhig",
                headache : "Kopfschmerzen",
                happy : "glücklich",
                sad : "traurig",
                
                overallSleepLabel : "Schlaf insgesamt",
                
                dreams : "Träume",
                nightmare : "Albtraum",
                peacefull : "friedlich",
                restless : "unruhig",
                warm : "warm",
                cold : "kalt",
                
                wakingUpLabel : "Aufwachen",
                
                happy : "glücklich",
                sad : "traurig",
                rested : "ausgeruht",
                tired : "müde",
                excited : "aufgeregt",
                annoyed : "verärgert"
                },
            starRateQuestion : "Bewerten Sie Ihren Schlaf mit Sternchen"
        }
        
    },
    {
        lang : "de-AT",
         components : {
            summaryCoverBtn : "ZUSAMMENFASSUNG ZEIGEN",
            summaryTitle : "Letzte Nacht",
            introductionSummaryParagraph : "Letzte Nacht sind Sie um 22:20 Uhr ins Bett gegangen. Das ist relativ spät im Vegleich zu Ihrer gewöhnlichen Bettzeit: 21:30 Uhr. Sie sind nach einer Stunde und 30 Minuten eingeschlafen.",
             mainSummaryParagraph : "Sie hatten zwei Schlafzyklen. Der erste fing ungefähr um 22:40 Uhr und dauerte ungefähr eine Stunde und 49 Minuten. Der zweite Zyklus fing ungefähr um 08:08 an und dauerte sieben Stunden und 29 Minuten. In der Zwischenzeit gingen Sie einmal ins Badezimmer.",
            endSummaryParagraph : "Sie schliefen letzte Nacht mehr als neun Stunden, in denen die Sensoren minimale Bewegung erfassten. Das heißt, dass Ihr Schlaf ruhig war.",
            moreDetailsBtn: "MEHR EINZELHEITEN",
            tagQuestion : "Fügen Sie drei Kategorien hinzu, um Ihren Schlaf zu beschreiben",
            addTagBtn : "KATEGORIE HINZUFÜGEN",
            tags : {
                fallingAsleepLabel: "Einschlafen",
                
                sleepy : "schläfrig",
                tired : "müde",
                tranquil : "ruhig",
                headache : "Kopfschmerzen",
                happy : "glücklich",
                sad : "traurig",
                
                overallSleepLabel : "Schlaf insgesamt",
                
                dreams : "Träume",
                nightmare : "Albtraum",
                peacefull : "friedlich",
                restless : "unruhig",
                warm : "warm",
                cold : "kalt",
                
                wakingUpLabel : "Aufwachen",
                
                happy : "glücklich",
                sad : "traurig",
                rested : "ausgeruht",
                tired : "müde",
                excited : "aufgeregt",
                annoyed : "verärgert"
                },
            starRateQuestion : "Bewerten Sie Ihren Schlaf mit Sternchen"
        }
        
    },
    {
        lang : "nl",
         components : {
            summaryCoverBtn : "SAMENVATTING TONEN",
            summaryTitle : "Afgelopen nacht",
            introductionSummaryParagraph : "Afgelopen nacht bent u om 22:20 naar bed gegaan. Dat is relatief laat vergeleken met de tijd waarop u gewoonlijk naar bed gaat, 21:30. U bent na 1 uur en 30 minuten in slaap gevallen.",
             mainSummaryParagraph : "U heeft bent 2 slaapcycli doorgaan. De eerste begon rond 22:40 en duurde ongeveer 1 uur en 49 minuten. De tweede begon om ongeveer 00:08 en duurde 7 uur en 29 minuten. Tussendoor bent u één keer naar de wc geweest.",
            endSummaryParagraph : "U heeft afgelopen nacht meer dan 9 uur geslapen waarin de sensoren een minimale hoeveelheid beweging hebben gedetecteerd. Dit betekent dat uw slaap rustig is verlopen.",
            moreDetailsBtn: "MEER DETAILS",
            tagQuestion : "Voeg 3 labels toe om uw slaap te beschrijven",
            addTagBtn : "VOEG LABEL TOE",
            tags : {
                fallingAsleepLabel: "In slaap vallen",
                
                sleepy : "Slaperig",
                tired : "Moe",
                tranquil : "Rustig",
                headache : "Hoofdpijn",
                happy : "Blij",
                sad : "Verdrietig",
                
                overallSleepLabel : "Slaap algemeen",
                
                dreams : "Dromen",
                nightmare : "Nachtmerrie",
                peacefull : "Vredig",
                restless : "Rusteloos",
                warm : "Warm",
                cold : "Koud",
                
                wakingUpLabel : "Wakker worden",
                
                happy : "Blij",
                sad : "Verdrietig",
                rested : "Uitgerust",
                tired : "Moe",
                excited : "Opgewonden",
                annoyed : "Geirriteerd"
                },
            starRateQuestion : "Beoordeel uw slaap met sterren"
        }
    },
    {
        lang : "nl-NL",
         components : {
            summaryCoverBtn : "SAMENVATTING TONEN",
            summaryTitle : "Afgelopen nacht",
            introductionSummaryParagraph : "Afgelopen nacht bent u om 22:20 naar bed gegaan. Dat is relatief laat vergeleken met de tijd waarop u gewoonlijk naar bed gaat, 21:30. U bent na 1 uur en 30 minuten in slaap gevallen.",
             mainSummaryParagraph : "U heeft bent 2 slaapcycli doorgaan. De eerste begon rond 22:40 en duurde ongeveer 1 uur en 49 minuten. De tweede begon om ongeveer 00:08 en duurde 7 uur en 29 minuten. Tussendoor bent u één keer naar de wc geweest.",
            endSummaryParagraph : "U heeft afgelopen nacht meer dan 9 uur geslapen waarin de sensoren een minimale hoeveelheid beweging hebben gedetecteerd. Dit betekent dat uw slaap rustig is verlopen.",
            moreDetailsBtn: "MEER DETAILS",
            tagQuestion : "Voeg 3 labels toe om uw slaap te beschrijven",
            addTagBtn : "VOEG LABEL TOE",
            tags : {
                fallingAsleepLabel: "In slaap vallen",
                
                sleepy : "Slaperig",
                tired : "Moe",
                tranquil : "Rustig",
                headache : "Hoofdpijn",
                happy : "Blij",
                sad : "Verdrietig",
                
                overallSleepLabel : "Slaap algemeen",
                
                dreams : "Dromen",
                nightmare : "Nachtmerrie",
                peacefull : "Vredig",
                restless : "Rusteloos",
                warm : "Warm",
                cold : "Koud",
                
                wakingUpLabel : "Wakker worden",
                
                happy : "Blij",
                sad : "Verdrietig",
                rested : "Uitgerust",
                tired : "Moe",
                excited : "Opgewonden",
                annoyed : "Geirriteerd"
                },
            starRateQuestion : "Beoordeel uw slaap met sterren"
        }
        
    },
    {
        lang : "it",
         components : {
            summaryCoverBtn : "MOSTRA IL RIEPILOGO",
            summaryTitle : "La notte scorsa",
            introductionSummaryParagraph : "La notte scorsa sei andato/a a letto alle 22:20. È relativamente tardi rispetto alla tua solita ora, 21:30. Ti sei addormentato/a dopo 1 ora e 30 minuti.",
             mainSummaryParagraph : "Hai avuto 2 cicli di sonno. Il primo è iniziato intorno alle 22:40 ed è durato 1 ora e 49 minuti. Il secondo è iniziato intorno alle 00:08 ed è durato 7 ore e 29 minuti. Tra questi sei andato in bagno una volta.",
            endSummaryParagraph : "La notte scorsa hai dormito più di 9 ore, durante le quali i sensori hanno rilevato movimenti minimi. Questo significa che il tuo sonno è stato tranquillo.",
            moreDetailsBtn: "ULTERIORI DETTAGLI",
            tagQuestion : "Aggiungi tre etichette per descrivere il tuo sonno.",
            addTagBtn : "AGGIUNGI UN'ETICHETTA",
            tags : {
                fallingAsleepLabel: "Addormentarsi",
                
                sleepy : "Sonnolento",
                tired : "Stanco",
                tranquil : "Tranquillo",
                headache : "Mal di testa",
                happy : "Felice",
                sad : "Triste",
                
                overallSleepLabel : "Sonno generale",
                
                dreams : "Sogni",
                nightmare : "Incubo",
                peacefull : "Pacifico",
                restless : "Inquieto",
                warm : "Caldo",
                cold : "Freddo",
                
                wakingUpLabel : "Svegliarsi",
                
                happy : "Felice",
                sad : "Triste",
                rested : "Riposato",
                tired : "Stanco",
                excited : "Eccitato",
                annoyed : "Annoiato"
                },
            starRateQuestion : "Dai un voto al tuo sonno con le stelle"
        }
        
    }
];

function loadInterefaceLanguage(lang) {
    for(var i = 0; i < interfaceLanguage.length; i++) {
        if(lang == interfaceLanguage[i].lang) 
        {      
          $(".summaryCoverBtn").html(interfaceLanguage[i].components.summaryCoverBtn);
          $(".summary-title").html(interfaceLanguage[i].components.summaryTitle);
          $(".introduction-summary-paragraph").html(interfaceLanguage[i].components.introductionSummaryParagraph);
            $(".main-summary-paragraph").html(interfaceLanguage[i].components.mainSummaryParagraph);
            $(".end-summary-paragraph").html(interfaceLanguage[i].components.endSummaryParagraph);
            $(".more-details-btn").html(interfaceLanguage[i].components.moreDetailsBtn);           
            
            
            $(".tag-question").html(interfaceLanguage[i].components.tagQuestion);
            $(".add-tag-btn").html(interfaceLanguage[i].components.addTagBtn);
                        
            $(".tag-list-fellAsleep label").text(interfaceLanguage[i].components.tags.fallingAsleepLabel);
            $(".tag-text.sleepy").html(interfaceLanguage[i].components.tags.sleepy);
            $(".tag-text.tired").html(interfaceLanguage[i].components.tags.tired);
            $(".tag-text.tranquil").html(interfaceLanguage[i].components.tags.tranquil);
            $(".tag-text.headache").html(interfaceLanguage[i].components.tags.headache);
            $(".tag-text.happy").html(interfaceLanguage[i].components.tags.happy);
            $(".tag-text.sad").html(interfaceLanguage[i].components.tags.sad);
            
            $(".tag-list-sleepQuality label").html(interfaceLanguage[i].components.tags.overallSleepLabel);
            $(".tag-text.dreams").html(interfaceLanguage[i].components.tags.dreams);
            $(".tag-text.nightmare").html(interfaceLanguage[i].components.tags.nightmare);
            $(".tag-text.peacefull").html(interfaceLanguage[i].components.tags.peacefull);
            $(".tag-text.restless").html(interfaceLanguage[i].components.tags.restless);
            $(".tag-text.warm").html(interfaceLanguage[i].components.tags.warm);
            $(".tag-text.cold").html(interfaceLanguage[i].components.tags.cold);
            
            $(".tag-list-wokeUp label").html(interfaceLanguage[i].components.tags.wakingUpLabel);
            $(".tag-text.happy").html(interfaceLanguage[i].components.tags.happy);
            $(".tag-text.sad").html(interfaceLanguage[i].components.tags.sad);
            $(".tag-text.rested").html(interfaceLanguage[i].components.tags.rested);
            $(".tag-text.tired").html(interfaceLanguage[i].components.tags.tired);
            $(".tag-text.excited").html(interfaceLanguage[i].components.tags.excited);
            $(".tag-text.annoyed").html(interfaceLanguage[i].components.tags.annoyed);
            
            
            $(".star-rate-question").html(interfaceLanguage[i].components.starRateQuestion);       
            
        }   
    }
};
