ewallApp.service('sudokuTranslation', function($http){
    var gameLabels = [
        {
            lang : 'en',
            labels : {
                welcome: 'Welcome to eWall Sudoku game',
                choose: 'Choose a level to start',
                easy: 'easy',
                medium: 'medium',
                hard: 'hard',
                back: 'back',
                solve: 'solve',
                congrat: 'Congratulations!',
                finish: 'You completed the game',
                moves: 'moves',
                time: 'time',
                playAgain: 'play again'
            }
        }, {
            lang : 'nl',
            labels : {
                welcome: 'Welkom bij het eWALL Sudoku spel',
                choose: 'Kies een niveau om te beginnen',
                easy: 'makkelijk',
                medium: 'gemiddeld',
                hard: 'moeilijk',
                back: 'terug',
                solve: 'oplossen',
                congrat: 'Gefeliciteerd!',
                finish: 'U heeft de puzzel opgelost',
                moves: 'zetten',
                time: 'tijd',
                playAgain: 'speel opnieuw'
            }
        }, {
            lang : 'da',
            labels : {
                welcome: 'Velkommen til eWall Suduko',
                choose: 'Vælg niveau for at starte',
                easy: 'let',
                medium: 'medium',
                hard: 'svær',
                back: 'tilbage',
                solve: 'løs',
                congrat: 'Tillykke!',
                finish: 'Du færdiggjorde spillet',
                moves: 'træk',
                time: 'tid',
                playAgain: 'spil igen'
            }
        }, {
            lang : 'de',
            labels : {
                welcome: 'Willkommen zum eWALL Sudoku Spiel',
                choose: 'Wähle ein Level aus um zu starten',
                easy: 'leicht',
                medium: 'mittel',
                hard: 'schwer',
                back: 'zurück',
                solve: 'lösen',
                congrat: 'Gratulation!',
                finish: 'Du hast das Spiel abgeschlossen',
                moves: 'Spielzüge',
                time: 'Zeit',
                playAgain: 'Nochmal spielen'
            }
        }, {
            lang : 'it',
            labels : {
                welcome: 'Benvenuti al Sudoku di eWall',
                choose: 'Scegli un livello per iniziare',
                easy: 'facile',
                medium: 'medio',
                hard: 'difficile',
                back: 'indietro',
                solve: 'risolvi',
                congrat: 'Complimenti!',
                finish: 'Hai finito il gioco',
                moves: 'mosse',
                time: 'tempo',
                playAgain: 'gioca ancora'
            }
        } ];

    var sudokuLabels = function(language) {
        var lab =  _.find(gameLabels, function(l){
            return l.lang == language;
        });
        return lab.labels;
    };

    return {
        getLabels: function(language) {
            return sudokuLabels(language);
        }
    };

});