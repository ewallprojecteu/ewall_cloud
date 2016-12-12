    
function translateLabels(language) {
        var uiLabels = [
                {
                lang: 'en',
                labels: {
                    activity: 'ACTIVITY',
                    exercise: 'EXERCISE',
                    bike: 'BIKE',
                    games: 'GAMES',
                    back: 'BACK'
                    }    
                },
                {
                lang: 'nl',
                labels: {
                    activity: 'BEWEGING',
                    exercise: 'GYMNASTIEK',
                     bike: 'FIETS',
                    games: 'SPELLEN',
                    back: 'BACK'
                    }    
                },
                {
                lang: 'da',
                labels: {
                    activity: 'Antal skridt',
                    exercise: 'GYMNASTIK',
                    bike: 'CYKEL',
                    games: 'SPIL',
                    back: 'Tilbage'
                    }    
                },
                {
                lang: 'de',
                labels: {
                    activity: 'BEWEGUNG',
                    exercise: 'GYMNASTIK',
                    bike: 'FAHRRAD',
                    games: 'SPIELE',
                    back: 'ZURÜCK'
                    }    
                },
                {
                lang: 'it',
                labels: {
                    activity: 'ATTIVITÀ',
                    exercise: 'GINNASTICA',
                    bike: 'BICICLETTA',
                    games: 'GIOCHI',
                    back: 'INDIETRO'
                    
                    }    
                },
            
        ];
        
        var foundMatch = false;
        // loop through the labels translation set and match the corresponding one    
        for(i=0; i<uiLabels.length; i++) {
            if(language == uiLabels[i].lang) {
                foundMatch = true;
                $("#activity-label").html(uiLabels[i].labels.activity);
                $("#video-label").html(uiLabels[i].labels.exercise);
                $("#bike-label").html(uiLabels[i].labels.bike);
                $("#games-label").html(uiLabels[i].labels.games);
                $("#game-frame-back-button").html(uiLabels[i].labels.back);
            }
        };
        // if the language cannot be found, make all labels English
        if(foundMatch == false) {
            $("#activity-label").html(uiLabels[0].labels.activity);
            $("#video-label").html(uiLabels[0].labels.exercise);
            $("#bike-label").html(uiLabels[0].labels.bike);
            $("#games-label").html(uiLabels[0].labels.games);
            $("#game-frame-back-button").html(uiLabels[0].labels.back);
        }
    };
    
function translateWeather(language) {
        var uiWeather = [
                {
                lang: 'en',
                labels: {
                    chance: 'chance',
                    next3Hours: 'next 3 hours',
                    tomorrow: 'tomorrow'                    
                    }    
                },
                {
                lang: 'nl',
                labels: {
                    chance: 'kans',
                    next3Hours: 'komende 3 uur',
                    tomorrow: 'morgen'                    
                    }    
                },
                {
                lang: 'da',
                labels: {
                    chance: 'sandsynlighed',
                    next3Hours: 'næste 3 timer',
                    tomorrow: 'i morgen'                    
                    }     
                },
                {
                lang: 'de',
                labels: {
                    chance: 'Chance',
                    next3Hours: 'nächsten 3 Stunden',
                    tomorrow: 'morgen'                    
                    }      
                },
                {
                lang: 'it',
                labels: {
                    chance: 'probabilitá',
                    next3Hours: 'prossime 3 ore',
                    tomorrow: 'domani'                    
                    }     
                },
            
        ];
        
        var foundMatch = false;
        // loop through the weather ui labels translation set and match the corresponding one    
        for(i=0; i<uiWeather.length; i++) {
            if(language == uiWeather[i].lang) {
                foundMatch = true;
                $("#value-clouds-text").html(uiWeather[i].labels.chance);
                $("#future-forecast-panel-right-label").html(uiWeather[i].labels.next3Hours);
                $("#future-forecast-panel-left-label").html(uiWeather[i].labels.tomorrow);
            }
        };
        // if the language cannot be found, make all labels English
        if(foundMatch == false) {
            $("#value-clouds-text").html(uiWeather[0].labels.chance);
                $("#future-forecast-panel-right-label").html(uiWeather[0].labels.next3Hours);
                $("#future-forecast-panel-left-label").html(uiWeather[0].labels.tomorrow);
        }
    };

 function translateTVAdd(language) {
        var uiTVAdd = [
                {
                lang: 'en',
                labels: {
                    h1: 'In the mood for some reading?',
                    h2: 'Best enjoyed with a cup of tea.'                                     
                    }    
                },
                {
                lang: 'nl',
                labels: {
                    h1: 'In de stemming voor wat te lezen?',
                    h2: 'Best genoten met een kopje thee.'                                     
                    }     
                },
                {
                lang: 'da',
                labels: {
                    h1: 'I humør til nogle læsning?',
                    h2: 'Bedst nydes med en kop te.'                                     
                    }      
                },
                {
                lang: 'de',
                labels: {
                    h1: 'In der Stimmung für etwas zu lesen?',
                    h2: 'Am besten mit einer Tasse Tee genießen.'                                     
                    }       
                },
                {
                lang: 'it',
                labels: {
                    h1: 'In vena di qualche lettura?',
                    h2: 'Da gustare con una tazza di tè.'                                     
                    }      
                },
            
        ];
        
        var foundMatch = false;
        // loop through the tv add ui labels translation set and match the corresponding one
     // this will be removed, once the connection to the MN will be implemented
        for(i=0; i<uiTVAdd.length; i++) {
            if(language == uiTVAdd[i].lang) {
                foundMatch = true;
                $("#tv-add-header-1").html(uiTVAdd[i].labels.h1);
                $("#tv-add-header-2").html(uiTVAdd[i].labels.h2);
                
            }
        };
        // if the language cannot be found, make all labels English
        if(foundMatch == false) {
            $("#tv-add-header-1").html(uiTVAdd[0].labels.h1);
            $("#tv-add-header-2").html(uiTVAdd[0].labels.h2);
        }
    };

function translateTVVideoTrainer(language) {
        var uiTVVideoTrainer = [
                {
                lang: 'en',
                labels: {
                    h1: 'Welcome to your video trainer',
                    h2: 'Time for some sport!'                                     
                    }    
                },
                {
                lang: 'da',
                labels: {
                    h1: 'Velkommen til din video træner',
                    h2: 'Tid til lidt sport!'                                     
                    }     
                },
                {
                lang: 'nl',
                labels: {
                    h1: 'Harte welkom om uw video trainer',
                    h2: 'Tijd voor wat sport!'                                     
                    }      
                },
                {
                lang: 'de',
                labels: {
                    h1: 'Willkommen in Ihrem Video-Trainer',
                    h2: 'Zeit für etwas Sport!'                                     
                    }       
                },
                {
                lang: 'it',
                labels: {
                    h1: 'Benvenuto dal tuo video allenatore',
                    h2: 'É tempo di fare un pó di sport!'                                     
                    }      
                },
            
        ];
        
        var foundMatch = false;
        // loop through the tv video trainer ui labels translation set and match the corresponding one    
        for(i=0; i<uiTVVideoTrainer.length; i++) {
            if(language == uiTVVideoTrainer[i].lang) {
                foundMatch = true;
                $("#tv-tv-header-1").html(uiTVVideoTrainer[i].labels.h1);
                $("#tv-tv-header-2").html(uiTVVideoTrainer[i].labels.h2);
                
            }
        };
        // if the language cannot be found, make all labels English
        if(foundMatch == false) {
            $("#tv-tv-header-1").html(uiTVVideoTrainer[0].labels.h1);
            $("#tv-tv-header-2").html(uiTVVideoTrainer[0].labels.h2);
        }
    };

function translateTVAgenda(language) {
        var uiTVAgenda = [
                {
                lang: 'en',
                labels: {
                    agendaTitle: "Agenda for "                                      
                    }    
                },
                {
                lang: 'da',
                labels: {
                    agendaTitle: "Dagsorden for "                                      
                    }    
                },
                {
                lang: 'nl',
                labels: {
                    agendaTitle: "Agenda voor "                                      
                    }       
                },
                {
                lang: 'de',
                labels: {
                    agendaTitle: "Termine für "                                      
                    }        
                },
                {
                lang: 'it',
                labels: {
                    agendaTitle: "Agenda del "                                      
                    }      
                },
            
        ];
        
        var foundMatch = false;
        // loop through the tv video trainer ui labels translation set and match the corresponding one    
        for(i=0; i<uiTVAgenda.length; i++) {
            if(language == uiTVAgenda[i].lang) {
                foundMatch = true;
                $("#agenda-title-text").html(uiTVAgenda[i].labels.agendaTitle);
                
            }
        };
        // if the language cannot be found, make all labels English
        if(foundMatch == false) {
            $("#agenda-title-text").html(uiTVAgenda[0].labels.agendaTitle);
        }
    };



function translateLogOutBtn(language) {
        var uiLogOutBtn = [
                {
                lang: 'en',
                labels: {
                    logoutLabel: "Logout"                                      
                    }    
                },
                {
                lang: 'da',
                labels: {
                    logoutLabel: "Log ud"                                      
                    }    
                },
                {
                lang: 'nl',
                labels: {
                    logoutLabel: "Uitloggen"                                      
                    }       
                },
                {
                lang: 'de',
                labels: {
                    logoutLabel: "Abmelden"                                      
                    }        
                },
                {
                lang: 'it',
                labels: {
                    logoutLabel: "Esci"                                      
                    }      
                },
            
        ];
        
        var foundMatch = false;
        // loop through the tv video trainer ui labels translation set and match the corresponding one    
        for(i=0; i<uiLogOutBtn.length; i++) {
            if(language == uiLogOutBtn[i].lang) {
                foundMatch = true;
                $("#logOutBtn").html(uiLogOutBtn[i].labels.logoutLabel);
                
            }
        };
        // if the language cannot be found, make all labels English
        if(foundMatch == false) {
            $("#logOutBtn").html(uiLogOutBtn[0].labels.logoutLabel);
        }
    };