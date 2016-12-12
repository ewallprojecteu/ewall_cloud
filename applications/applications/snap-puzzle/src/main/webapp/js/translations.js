ewallApp.service('snapTranslation', function($http){
    var gameLabels = [
        {
            lang : 'en',
            labels : {
                welcome: 'Welcome to eWall Puzzle game',
                level: 'Choose a level to start',
                choose: 'I want to play with this picture...',
                easy: 'easy',
                medium: 'medium',
                hard: 'hard',
                back: 'back',
                restart: 'restart',
                solve: 'solve',
                congrat: 'Congratulations!',
                finish: 'You completed the game',
                moves: 'moves',
                time: 'time',
                playAgain: 'play again',
                well: 'Well done!'
            }
        }, {
            lang : 'nl',
            labels : {
                welcome: 'Welkom bij het eWALL Puzzle spel',
                level: '',
                choose: 'Ik wil met dit plaatje spelen...',
                easy: 'makkelijk',
                medium: 'gemiddeld',
                hard: 'moeilijk',
                back: 'terug',
                restart: 'opnieuw',
                solve: 'oplossen',
                congrat: 'Gefeliciteerd!',
                finish: 'U heeft de puzzel opgelost',
                moves: 'zetten',
                time: 'tijd',
                playAgain: 'speel opnieuw',
                well: 'Goed gedaan!'
            }
        }, {
            lang : 'da',
            labels : {
                welcome: 'Velkommen til eWall Puslespillet',
                level: '',
                choose: 'Jeg vil spille med dette billede...',
                easy: 'let',
                medium: 'medium',
                hard: 'svær',
                back: 'tilbage',
                restart: 'genstart',
                solve: 'løs',
                congrat: 'Tillykke!',
                finish: 'Du færdiggjorde spillet',
                moves: 'træk',
                time: 'tid',
                playAgain: 'spil igen',
                well: 'Godt gået!'
            }
        }, {
            lang : 'de',
            labels : {
                welcome: 'Willkommen zum eWALL Puzzle Spiel',
                level: '',
                choose: 'Ich möchte mit diesem Bild spielen...',
                easy: 'leicht',
                medium: 'mittel',
                hard: 'schwer',
                back: 'zurück',
                restart: 'neu starten',
                solve: 'lösen',
                congrat: 'Gratulation!',
                finish: 'Du hast das Spiel abgeschlossen',
                moves: 'Spielzüge',
                time: 'Zeit',
                playAgain: 'Nochmal spielen',
                well: 'Gut gemacht!'               
            } 
        }, {
            lang : 'it',
            labels : {
                welcome: 'Benvenuti al Puzzle di eWall',
                level: 'Scegli un livello',
                choose: 'Voglio giocare con quest\'immagine...',
                easy: 'facile',
                medium: 'medio',
                hard: 'difficile',
                back: 'indietro',
                restart: 'ricomincia',
                solve: 'risolvi',
                congrat: 'Complimenti!',
                finish: 'Hai finito il gioco',
                moves: 'mosse',
                time: 'tempo',
                playAgain: 'gioca ancora',
                well: 'Ben fatto!'
            }
        } ];

    var snapLabels = function(language) {
        var lab =  _.find(gameLabels, function(l){
            return l.lang == language;
        });
        return lab.labels;
    };

    return {
        getLabels: function(language) {
            return snapLabels(language);
        }
    };

});