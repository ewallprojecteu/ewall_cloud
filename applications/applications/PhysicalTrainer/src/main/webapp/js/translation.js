var ui = {
    'en': {
        header: {
            h1: "VIDEO GYMNASTICS",
            h2: "Welcome! Let's get moving!",
            we_recommend: "We Recommend",
            your_favorite: "Your Favorite",
            better_breathing: "Better Breathing",
            better_strength: "Better Strength",
            sensor_intruction: "Make sure you wear the FitBit bracelet.",
            chair_instruction: "You might need a chair for some exercises. Have one near by.",
            ready_text:"When you are ready press",
            start_routine: "START"
            
        },
        routine: {
            rest_time: "Rest Time",
            enjoy_rating: "How much did you enjoy this exercise?",
            difficult_rating: "How difficult was this exercise?",
            end_of_routine: "End of Routine",
            done: "Done",
            quit: "Quit"
        }
    },
    'nl': {
        header: {
            h1: "VIDEO GYMNASTIEK",
            h2: "Welkom! Laten we verhuizen!",
            we_recommend: "Wij raden",
            your_favorite: "Je favoriet",
            better_breathing: "Betere ademhaling",
            better_strength: "betere sterkte",
            sensor_intruction: "Zorg dat u de Fitbit armband om heeft.",
            chair_instruction: "U heeft misschien een stoel nodig bij sommige oefeningen. Zorg ervoor dat er een dichtbij staat.",
            ready_text:"Als u er klaar voor bent, druk op",
            start_routine: "START"
        },
        routine: {
            rest_time: "Rusttijd",
            enjoy_rating: "Hoeveel heeft u genoten van deze oefening?",
            difficult_rating: "Hoe moeilijk was deze oefening?",
            end_of_routine: "Einde van Routine",
            done: "Klaar",
            quit: "Stoppen"
        }
    },
    'it': {
        header: {
            h1: "GINNASTICA VIDEO",
            h2: "Benvenuto! Facciamo un pó di esercizio.",
            we_recommend: "Noi raccomandiamo",
            your_favorite: "Il tuo preferito",
            better_breathing: "Per respirare meglio",
            better_strength: "Per essere piú forte",
            sensor_intruction: "Assicurati di indossare il braccialetto FitBit.",
            chair_instruction: "Per alcuni esercizi potrebbe essere necessario avere una sedia accanto a te.",
            ready_text:"Quando sei pronto premi",
            start_routine: "COMINCIAMO"
        },
        routine: {
            rest_time: "Tempo di riposo",
            enjoy_rating: "Quanto ti è piaciuto questo esercizio?",
            difficult_rating: "Quanto è stato difficile questo esercizio?",
            end_of_routine: "Fine della sequenza",
            done: "Fatto",
            quit: "Stop"
        }
    },
    'de': {
        header: {
            h1: "VIDEO GYMNASTIK",
            h2: "Herzlich Willkommen! Los geht’s!",
            we_recommend: "Wir empfehlen",
            your_favorite: "Deine Favoriten",
            better_breathing: "Besser atmen",
            better_strength: "Mehr Kraft",
            sensor_intruction: "Achte darauf, dass dein FitBit Armband angelegt ist.",
            chair_instruction: "Möglicherweise benötigst du einen Sessel für die Übungen. Halte einen bereit.",
            ready_text:"Wenn du bereit bist, drücke",
            start_routine: "Los geht's"
        },
        routine: {
            rest_time: "Pause",
            enjoy_rating: "Wie sehr hat dir diese Übung gefallen?",
            difficult_rating: "Wie schwierig war diese Übung?",
            end_of_routine: "Ende der Routine",
            done: "Fertig",
            quit: "Stop"
        }
    },
     'da': {
        header: {
            h1: "GYMNASTIK",
            h2: " ",
            we_recommend: "Vi anbefaler",
            your_favorite: "Din favorit",
            better_breathing: "bedre vejrtrækning",
            better_strength: "bedre styrke",
            sensor_intruction: "Sørg for at bære dit Fitbit-armbånd,",
            chair_instruction: "Du har måske brug for en stol til nogle øvelser. Hav en klar ved siden af dig,",
            ready_text:"Når du er klar, tryk",
            start_routine: "START"
        },
        routine: {
            rest_time: "Hviletid",
            enjoy_rating: "Hvor godt kunne du lide denne øvelse?",
            difficult_rating: "Hvor svær var denne øvelse?",
            end_of_routine: " ",
            done: "Færdig",
            quit: "Afslut"
        }
    },

};

var language =  ewallApp.preferedLanguage;
$(document).ready(function () {
    
    // header title and subtitle
    $('.gwd-div-rdsn').html(ui[language].header.h1);
    $('.gwd-div-d8ml').html(ui[language].header.h2);
    // start page
    $('#sensor-instruction').html(ui[language].header.sensor_intruction);
    $('#chair-instruction').html(ui[language].header.chair_instruction);
    $('#ready-text').html(ui[language].header.ready_text);
    $('#start-routine').html(ui[language].header.start_routine);
    // hrader choice buttons
    $('#we-recommend').html(ui[language].header.we_recommend);
    $('#your-favorite').html(ui[language].header.your_favorite);
    $('#better-breathing').html(ui[language].header.better_breathing);
    $('#better_strength').html(ui[language].header.better_strength);
    
    // routine
    $('.rest-time-title').html(ui[language].routine.rest_time);
    $('.enjoy-rating-title').html(ui[language].routine.enjoy_rating);
    $('.difficulty-rating-title').html(ui[language].routine.difficult_rating);
    
    $('#end-routine-title').html(ui[language].routine.end_of_routine);
    $('#end-routine-quality').html(ui[language].routine.done);
    $('.enjoy-rutine-rating-title').html(ui[language].routine.enjoy_rating);
    $('.difficulty-rutine-rating-title').html(ui[language].routine.difficult_rating);
    $('#close').html(ui[language].routine.quit);
 
});