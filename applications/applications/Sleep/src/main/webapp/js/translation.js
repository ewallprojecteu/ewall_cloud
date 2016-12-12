var ui = [
    // en
    {
        lang: 'en',
        elements: {
            night: {
                tab: 'NIGHT',
                went_to_bed: 'You went to bed at <b id="went-to-bed-data"> NO DATA </b> . <br> <i> You usually go to bed at around <b id="went-to-bed-LR-data"> NO DATA </b> </i>',
                woke_up_times: 'During the night, you woke up <b id="wake-up-times-data"> NO DATA </b> times.',
                morning_wake_up: 'You woke up at <b id="morning-wake-up-data"> NO DATA </b> . <br> <i> Usually you wake up at <b id="morning-woke-up-LR-data"> NO DATA </b> </i>',
                conclusion: 'Sleep efficiency: <b id="sleep-efficiency-data"> NO DATA </b> . <br> Total sleep time: <b id="total-sleep-time-data"> NO DATA </b>.'
            },
            week: {
                tab: 'WEEK',
                legend: {
                    present: 'present measurement',
                    usually: 'your usual behavior'
                },
                efficiency: {
                    btn: 'Efficiency',
                    title: 'Sleep Efficiency (%)',
                    definition: 'The proportion of time you spend sleeping from your total time in bed'
                },
                sleep_time: {
                    btn: 'Sleep time',
                    title: 'Sleep time (hrs)',
                    definition: ""
                },
                awakenings: {
                    btn: 'Awakenings',
                    title: 'Number of Awakenings',
                    definition: 'The number of times you woke up during your sleep time'
                }
            }

        }
    },

    // de    
    {
        lang: 'de',
        elements: {
            night: {
                tab: 'Nacht',
                went_to_bed: 'Du bist um <b id="went-to-bed-data"> NO DATA </b> Uhr zu Bett gegangen. <br> <i> Durchschnittlich gehst du um <b id="went-to-bed-LR-data"> NO DATA </b> Uhr zu Bett. </i>',
                woke_up_times: 'Während der Nacht bist du <b id="wake-up-times-data"> NO DATA </b> Mal aufgewacht.',
                morning_wake_up: 'Aufgewacht bist du um <b id="morning-wake-up-data"> NO DATA </b> Uhr. <br> <i> Durchschnittlich wachst du um <b id="morning-woke-up-LR-data"> NO DATA </b> Uhr auf. </i>',
                conclusion: 'Schlafeffizienz: <b id="sleep-efficiency-data"> NO DATA </b> <br> Gesamte Schlafdauer: <b id="total-sleep-time-data"> NO DATA </b>'
            },
            week: {
                tab: 'Wochenansicht',
                legend: {
                    present: 'Aktuelle Messwert',
                    usually: 'Deine durchschnittlichen Werte'
                },
                efficiency: {
                    btn: 'Schlafeffizienz',
                    title: 'Schlafeffizienz (%)',
                    definition: 'Deine tatsächlich geschlafene Zeit gemessen vom Zeitpunkt an dem du dich ins Bett gelegt hast'
                },
                sleep_time: {
                    btn: 'Schlafdauer',
                    title: 'Schlafdauer (h)',
                    definition: ""
                },
                awakenings: {
                    btn: 'Schlafunterbrechungen',
                    title: 'Schlafunterbrechungen',
                    definition: 'Anzahl der Schlafunterbrechungen während der Nacht'
                }
            }

        }
    },

    //nl
    {
        lang: 'nl',
        elements: {
            night: {
                tab: 'NACHT',
                went_to_bed: 'U bent om <b id="went-to-bed-data"> NO DATA </b> naar bed gegaan. <br> <i> U gaat meestal om <b id="went-to-bed-LR-data"> NO DATA </b> naar bed. </i>',
                woke_up_times: 'Gedurende de nacht bent u <b id="wake-up-times-data"> NO DATA </b> keer wakker geworden.',
                morning_wake_up: 'U bent om <b id="morning-wake-up-data"> NO DATA </b> wakker geworden. <br> <i> U wordt meestal om <b id="morning-woke-up-LR-data"> NO DATA </b> wakker. </i>',
                conclusion: 'Slaap efficientie: <b id="sleep-efficiency-data"> NO DATA </b> <br> Totale slaap tijd: <b id="total-sleep-time-data"> NO DATA </b>'
            },
            week: {
                tab: 'WEEK',
                legend: {
                    present: 'huidige meting',
                    usually: 'uw gebruikelijke gedrag'
                },
                efficiency: {
                    btn: 'Slaapefficientie',
                    title: 'Slaapefficientie (%)',
                    definition: 'Het aandeel van de slaap in de episode mogelijk opgevuld door slaap'
                },
                sleep_time: {
                    btn: 'Slaapduur',
                    title: 'Slaapduur (uren)',
                    definition: ""
                },
                awakenings: {
                    btn: 'Slaaponderbrekingen',
                    title: 'Slaaponderbrekingen (aantal)',
                    definition: 'Het aantal keren dat je wakker tijdens de slaap tijd'
                }
            }

        }
    },

    //dk 
    {
        lang: 'da',
        elements: { // to do
            night: {
                tab: 'Nat',
                went_to_bed: 'Du gik i seng kl. <b id="went-to-bed-data"> NO DATA </b>. <br> <i> Du går normalt i seng kl. <b id="went-to-bed-LR-data"> NO DATA</b>. </i>',
                woke_up_times: 'Du vågnede <b id="wake-up-times-data"> NO DATA </b> gange i løbet af i nat.',
                morning_wake_up: 'Du vågnede kl. <b id="morning-wake-up-data"> NO DATA </b> <br> <i> Normalt vågner du kl. <b id="morning-woke-up-LR-data"> NO DATA</b> </i>',
                conclusion: 'Søvnkvalitet: <b id="sleep-efficiency-data"> NO DATA </b>. <br> Søvn i alt: <b id="total-sleep-time-data"> NO DATA </b>.'
            },
            week: {
                tab: 'Uge',
                legend: {
                    present: 'Nuværende måling',
                    usually: 'din normale søvn'
                },
                efficiency: {
                    btn: 'Søvnkvalitet',
                    title: 'Kvaliteten af din søvn (%)',
                    definition: 'Andelen af søvn i episonden potentielt fyldt med søvn = Sådan har du sovet'
                },
                sleep_time: {
                    btn: 'Opvågninger i løbet',
                    title: 'opvågninger i løbet af natten (antal)',
                    definition: ""
                },
                awakenings: {
                    btn: 'Slaaponderbrekingen',
                    title: 'Slaaponderbrekingen (aantal)',
                    definition: 'Det antal gange, du vågnede op i løbet af din søvn tid'
                }
            }

        }
    },
    // it
    {
        lang: 'it',
        elements: {
            night: {
                tab: 'NOTTE',
                went_to_bed: 'Sei andato a letto alle <b id="went-to-bed-data"> NO DATA </b>. <br> <i> Di solito vai a letto intorno alle <b id="went-to-bed-LR-data"> NO DATA </b>. </i>',
                woke_up_times: 'Durante la notte, ti sei svegliato <b id="wake-up-times-data"> NO DATA </b> volte.',
                morning_wake_up: 'Ti sei svegliato alle <b id="morning-wake-up-data"> NO DATA </b>. <br> <i> Di solito ti svegli alle <b id="morning-woke-up-LR-data"> NO DATA </b> </i>.',
                conclusion: 'Efficienza del periodo di sonno: <b id="sleep-efficiency-data"> NO DATA </b> <br> Durata totale del periodo di sonno: <b id="total-sleep-time-data"> NO DATA </b>'
            },
            week: {
                tab: 'SETTIMANA',
                legend: {
                    present: 'i tuoi valori attuali',
                    usually: 'il tuo comportamento abituale'
                },
                efficiency: {
                    btn: 'Efficienza',
                    title: 'Efficienza del sonno (%)',
                    definition: "La percentuale di tempo in cui dormi rispetto al tempo che spendi a letto" 
                },
                sleep_time: {
                    btn: 'Durata del sonno',
                    title: 'Durata del sonno (ore)',
                    definition: ""
                },
                awakenings: {
                    btn: 'Risvegli',
                    title: 'Numero di Risvegli',
                    definition: 'Il numero di volte in cui ti sei svegliato durante il periodo di sonno.'
                }
            }

        }
    },

];


 
var monday = {
    'en':'Monday',
    'nl':'Maandag',
    'de':'Montag',
    'da':'Mandag',
    'it':'Lunedì'
};
var tuesday = {
    'en':'Tuesday',
    'nl':'Dinsdag',
    'de':'Dienstag',
    'da':'Tirsdag',
    'it':'Martedì'
};
var wednesday = {
    'en':'Wednesday',
    'nl':'Woensdag',
    'de':'Mittwoch',
    'da':'Onsdag',
    'it':'Mercoledì'
};
var thursday = {
    'en':'Thursday',
    'nl':'Donderdag',
    'de':'Donnerstag',
    'da':'Torsdag',
    'it':'Giovedì'
};
var friday = {
    'en':'Friday',
    'nl':'Vrijdag',
    'de':'Freitag',
    'da':'Fredag',
    'it':'Venerdì'
};
var saturday = {
    'en':'Saturday',
    'nl':'Zaterdag',
    'de':'Samstag',
    'da':'Lørdag',
    'it':'Sabato'
};
var sunday = {
    'en':'Sunday',
    'nl':'Zondag',
    'de':'Sonntag',
    'da':'Søndag',
    'it':'Domenica'
};

var undefinedData = {
    'en': 'undefined',
    'nl':'onbestemd',
    'de':'undefiniert',
    'da':'udefineret',
    'it':'indefinito'
};

var language = ewallApp.preferedLanguage;;
$(document).ready(function () {
            for (index in ui) {
                if (ui[index].lang == language) {
                    // set the ui
                    
                    // tabs
                    $('#today-tab').html(ui[index].elements.night.tab);
                    $('#week-tab').html(ui[index].elements.week.tab);
                    
                    // night
                    $('#went-to-bed').html(ui[index].elements.night.went_to_bed);
                    $('#wake-up-times').html(ui[index].elements.night.woke_up_times);
                    $('#morning-wake-up').html(ui[index].elements.night.morning_wake_up);
                    $('#conclusion').html(ui[index].elements.night.conclusion);
                    
                    //week
                    $('#sleep-efficiency-week').html(ui[index].elements.week.efficiency.btn);
                    $('#sleep-time-week').html(ui[index].elements.week.sleep_time.btn);
                    $('#sleep-awakenings-week').html(ui[index].elements.week.awakenings.btn);
                    // week-chat-legend
                    $('#present-label').html(ui[index].elements.week.legend.present);
                    $('#usually-label').html(ui[index].elements.week.legend.usually);

                }
            }
        });

