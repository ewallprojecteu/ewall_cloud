var sleepJSONFromSB = {
    username: "bilbo",
    from: "2015-04-06T18:00:00.000+01:00",
    to: "2015-04-07T12:00:00.000+01:00",
    inbedEvents: [
        {
            timestamp: "2015-04-06T19:05:00.000+01:00",
            inBed: true
  },
        {
            timestamp: "2015-04-06T23:45:00.000+01:00",
            inBed: false
  },
        {
            timestamp: "2015-04-06T23:55:00.000+01:00",
            inBed: true
  },
        {
            timestamp: "2015-04-07T07:30:00.000+01:00",
            inBed: false
  },
        {
            timestamp: "2015-04-07T07:36:00.000+01:00",
            inBed: true
  },
        {
            timestamp: "2015-04-07T08:55:00.000+01:00",
            inBed: false
  }
 ]
}

var sleepJSONFromCloud = {
    username: "bilbo",
    from: "2015-04-06T18:00:00.000+01:00",
    to: "2015-04-07T12:00:00.000+01:00",
    inbedEvents: [
        {
            timestamp: "2015-04-06T20:05:00.000+01:00",
            inBed: true
  },
        {
            timestamp: "2015-04-07T07:30:00.000+01:00",
            inBed: false
  },
        {
            timestamp: "2015-04-07T07:36:00.000+01:00",
            inBed: true
  },
        {
            timestamp: "2015-04-07T06:55:00.000+01:00",
            inBed: false
  }
 ]
}

var sleepJSONFromHome = {
    username: "bilbo",
    from: "2015-04-06T18:00:00.000+01:00",
    to: "2015-04-07T12:00:00.000+01:00",
    inbedEvents: [
        {
            timestamp: "2015-04-06T19:05:00.000+01:00",
            inBed: true
  },
        {
            timestamp: "2015-04-06T23:45:00.000+01:00",
            inBed: false
  },
        {
            timestamp: "2015-04-06T23:55:00.000+01:00",
            inBed: true
  },
        {
            timestamp: "2015-04-07T07:30:00.000+01:00",
            inBed: false
  },
        {
            timestamp: "2015-04-07T07:36:00.000+01:00",
            inBed: true
  },
        {
            timestamp: "2015-04-07T08:55:00.000+01:00",
            inBed: false
  }
 ]
}


var locationJSONFromSB = {
}
var sleepJSONFromLR = 
    {
        user: "bilbo",
        days: [{
            weekDay: 1,
            parameters: [{
                parameter: "interrupt_count",
                weightedAverage: 1,
                category: "NORMAL"
            }, {
                parameter: "interrupt_duration",
                weightedAverage: 13,
                category: "NORMAL"
            }, {
                parameter: "sleep_duration",
                weightedAverage: 556,
                category: "NORMAL"
            }, {
                parameter: "sleep_time",
                weightedAverage: 1349,
                category: "NORMAL"
            }, {
                parameter: "wake_time",
                weightedAverage: 478,
                category: "NORMAL"
            }]
        }, {
            weekDay: 2,
            parameters: [{
                parameter: "interrupt_count",
                weightedAverage: 1,
                category: "NORMAL"
            }, {
                parameter: "interrupt_duration",
                weightedAverage: 11,
                category: "NORMAL"
            }, {
                parameter: "sleep_duration",
                weightedAverage: 553,
                category: "NORMAL"
            }, {
                parameter: "sleep_time",
                weightedAverage: 1349,
                category: "NORMAL"
            }, {
                parameter: "wake_time",
                weightedAverage: 473,
                category: "NORMAL"
            }]
        }, {
            weekDay: 3,
            parameters: [{
                parameter: "interrupt_count",
                weightedAverage: 1,
                category: "NORMAL"
            }, {
                parameter: "interrupt_duration",
                weightedAverage: 11,
                category: "NORMAL"
            }, {
                parameter: "sleep_duration",
                weightedAverage: 473,
                category: "BELOW_NORMAL"
            }, {
                parameter: "sleep_time",
                weightedAverage: 1315,
                category: "NORMAL"
            }, {
                parameter: "wake_time",
                weightedAverage: 359,
                category: "BELOW_NORMAL"
            }]
        }, {
            weekDay: 4,
            parameters: [{
                parameter: "interrupt_count",
                weightedAverage: 1,
                category: "NORMAL"
            }, {
                parameter: "interrupt_duration",
                weightedAverage: 14,
                category: "NORMAL"
            }, {
                parameter: "sleep_duration",
                weightedAverage: 562,
                category: "NORMAL"
            }, {
                parameter: "sleep_time",
                weightedAverage: 1345,
                category: "NORMAL"
            }, {
                parameter: "wake_time",
                weightedAverage: 480,
                category: "NORMAL"
            }]
        }, {
            weekDay: 5,
            parameters: [{
                parameter: "interrupt_count",
                weightedAverage: 1,
                category: "NORMAL"
            }, {
                parameter: "interrupt_duration",
                weightedAverage: 18,
                category: "NORMAL"
            }, {
                parameter: "sleep_duration",
                weightedAverage: 551,
                category: "NORMAL"
            }, {
                parameter: "sleep_time",
                weightedAverage: 1351,
                category: "NORMAL"
            }, {
                parameter: "wake_time",
                weightedAverage: 480,
                category: "NORMAL"
            }]
        }, {
            weekDay: 6,
            parameters: [{
                parameter: "interrupt_count",
                weightedAverage: 1,
                category: "NORMAL"
            }, {
                parameter: "interrupt_duration",
                weightedAverage: 12,
                category: "NORMAL"
            }, {
                parameter: "sleep_duration",
                weightedAverage: 554,
                category: "NORMAL"
            }, {
                parameter: "sleep_time",
                weightedAverage: 1351,
                category: "NORMAL"
            }, {
                parameter: "wake_time",
                weightedAverage: 477,
                category: "NORMAL"
            }]
        }, {
            weekDay: 7,
            parameters: [{
                parameter: "interrupt_count",
                weightedAverage: 1,
                category: "NORMAL"
            }, {
                parameter: "interrupt_duration",
                weightedAverage: 11,
                category: "NORMAL"
            }, {
                parameter: "sleep_duration",
                weightedAverage: 721,
                category: "ABOVE_NORMAL"
            }, {
                parameter: "sleep_time",
                weightedAverage: 1340,
                category: "NORMAL"
            }, {
                parameter: "wake_time",
                weightedAverage: 632,
                category: "ABOVE_NORMAL"
            }]
        }],
        updated: "2015-04-01"
    }

var sleepJSON = {
    start_time: "2014-07-17 22:20:39",
    end_time: "2014-07-18 07:35:26",
    intervals: [
        {
            start_date: "JUL 17, 2014",
            start_time: "22:20:39",            
            timeline: {
                distance: "0px",
                bulletStyle: "bg-primary",
                bulletIconStyle: "fa fa-moon-o"

            },
            text: [
                {
                    lang: "en",
                    content: {
                        title: "Went to bed",
                        icon: "resources/cat-icons-64/cat_sing.png",
                        text: "The location sensor detected you in the bedroom. Your bed pressure sensor registered you laying on bed.",
                        duration: "Good night!"
                    }
                },
                {
                    lang: "en-US",
                    content: {
                        title: "Went to bed",
                        icon: "resources/cat-icons-64/cat_sing.png",
                        text: "The location sensor detected you in the bedroom. Your bed pressure sensor registered you laying on bed.",
                        duration: " Good night! "
                    }
                },
                {
                    lang: "nl",
                    content: {
                        title: "Naar bed gegaan",
                        icon: "resources/cat-icons-64/cat_sing.png",
                        text: "De locatie sensor gaf aan dat u in uw slaapkamer was. De bedsensor heeft geregistreerd dat u in bed lag.",
                        duration: " Goedenacht! "
                    }
                },
                {
                    lang: "nl-NL",
                    content: {
                        title: "Naar bed gegaan",
                        icon: "resources/cat-icons-64/cat_sing.png",
                        text: "De locatie sensor gaf aan dat u in uw slaapkamer was. De bedsensor heeft geregistreerd dat u in bed lag.",
                        duration: " Goedenacht! "
                    }
                },
                {
                    lang: "da",
                    content: {
                        title: "Gik i seng",
                        icon: "resources/cat-icons-64/cat_sing.png",
                        text: "Placeringssensoren registrerede dig i soveværelset. Din sengetrykssensor registrerede, at du lå i sengen.",
                        duration: " Godnat! "
                    }
                },
                {
                    lang: "da-DK",
                    content: {
                        title: "Gik i seng",
                        icon: "resources/cat-icons-64/cat_sing.png",
                        text: "Placeringssensoren registrerede dig i soveværelset. Din sengetrykssensor registrerede, at du lå i sengen.",
                        duration: " Godnat! "
                    }
                },
                {
                    lang: "de",
                    content: {
                        title: "Zu Bett gegangen",
                        icon: "resources/cat-icons-64/cat_sing.png",
                        text: "Der Ortungssensor erfasste Sie im Badezimmer. Ihr Bettdrucksensor bemerkte, dass Sie im Bett liegen. ",
                        duration: " Gute Nacht! "
                    }
                },
                {
                    lang: "de-AT",
                    content: {
                        title: "Zu Bett gegangen",
                        icon: "resources/cat-icons-64/cat_sing.png",
                        text: "Der Ortungssensor erfasste Sie im Badezimmer. Ihr Bettdrucksensor bemerkte, dass Sie im Bett liegen. ",
                        duration: " Gute Nacht! "
                    }
                },
                {
                    lang: "it",
                    content: {
                        title: "Andato a letto",
                        icon: "resources/cat-icons-64/cat_sing.png",
                        text: "Il sensore di locazione ti ha rilevato nella camera da letto. Il tuo sensore di pressione del letto ha registrato che giacevi a letto.",
                        duration: " Buona notte! "
                    }
                }
            ]
        },
        {
            start_date: "JUL 17, 2014",
            start_time: "22:35:00",
            locationID: 3,
            off_bed: false,
            transitional: false,
            timeline: {
                distance: "40px",
                bulletStyle: "bg-info",
                bulletIconStyle: "fa fa-info"

            },
            text: [
                {
                    lang: "en",
                    content: {
                        title: "Sleep cycle 1",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Your bed sensor detecting you in bed.",
                        duration: " Duration: 1 hour and 39 minutes "
                    }
                },
                {
                    lang: "en-US",
                    content: {
                        title: "Sleep cycle 1",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Your bed sensor detecting you in bed.",
                        duration: " Duration: 1 hour and 39 minutes "
                    }
                },
                {
                    lang: "nl",
                    content: {
                        title: "Slaap cyclus 1",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Uw bedsensor heeft gedetecteerd dat u in bed lag.",
                        duration: " Duur: 1 uur en 39 minuten. "
                    }
                },
                {
                    lang: "nl-NL",
                    content: {
                        title: "Slaap cyclus 1",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Uw bedsensor heeft gedetecteerd dat u in bed lag.",
                        duration: " Duur: 1 uur en 39 minuten. "
                    }
                },
                {
                    lang: "da",
                    content: {
                        title: "Søvncyklus 1",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Din sengesensor registrerer dig i sengen.",
                        duration: " Varighed: En time og 39 minutter "
                    }
                },
                {
                    lang: "da-DK",
                    content: {
                        title: "Søvncyklus 1",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Din sengesensor registrerer dig i sengen.",
                        duration: " Varighed: En time og 39 minutter "
                    }
                },
                {
                    lang: "de",
                    content: {
                        title: "Schlafzyklus 1",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Ihr Bettsensor erfasste Sie im Bett.",
                        duration: " Dauer: eine Stunde und 39 Minuten "
                    }
                },
                {
                    lang: "de-AT",
                    content: {
                        title: "Schlafzyklus 1",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Ihr Bettsensor erfasste Sie im Bett.",
                        duration: " Dauer: eine Stunde und 39 Minuten "
                    }
                },
                {
                    lang: "it",
                    content: {
                        title: "Ciclo di sonno 1",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Il tuo sensore del letto per la rilevazione a letto.",
                        duration: " Durata: 1 ora e 39 minuti "
                    }
                }
            ]

        },
        {
            start_date: "JUL 17, 2014",
            start_time: "23:59:50",
            end_date: "JUL 18, 2014",
            end_time: "00:00:52",
            locationID: 3,
            off_bed: true,
            transitional: true,
            timeline: {
                distance: "",
                bulletStyle: "",
                bulletIconStyle: ""

            },
            text: [
                {
                    lang: "en",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "en-US",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "nl",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "nl-NL",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "da",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "da-DK",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "de",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "de-AT",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "it",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                }
            ]
        },
        {
            start_date: "JUL 18, 2014",
            start_time: "00:01:03",
            locationID: 5,
            off_bed: true,
            transitional: true,
            timeline: {
                distance: "",
                bulletStyle: "",
                bulletIconStyle: ""

            },
            text: [
                {
                    lang: "en",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "en-US",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "nl",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "nl-NL",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "da",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "da-DK",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "de",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "de-AT",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "it",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                }
            ]
        },
        {
            start_date: "JUL 18, 2014",
            start_time: "00:01:26",
            locationID: 4,
            off_bed: true,
            transitional: false,
            timeline: {
                distance: "0px",
                bulletStyle: "bg-warning",
                bulletIconStyle: "fa fa-puzzle-piece"

            },
            text: [
                {
                    lang: "en",
                    content: {
                        title: "Sleep interruption 1",
                        icon: "resources/cat-icons-64/cat_paper.png",
                        text: "Your bed sensor stopped detecting you in bed, yet your position sensor places you in the bathroom.",
                        duration: " Duration: aprox. 8 minutes "
                    }
                },
                {
                    lang: "en-US",
                    content: {
                        title: "Sleep interruption 1",
                        icon: "resources/cat-icons-64/cat_paper.png",
                        text: "Your bed sensor stopped detecting you in bed, yet your position sensor places you in the bathroom.",
                        duration: " Duration: aprox. 8 minutes "
                    }
                },
                {
                    lang: "nl",
                    content: {
                        title: "Slaap onderbreking 1",
                        icon: "resources/cat-icons-64/cat_paper.png",
                        text: "Uw bedsensor heeft gedecteerd dat u niet meer in bed lag, de positie sensor vond u in de badkamer.",
                        duration: " Duur: ongeveer 8 minuten "
                    }
                },
                {
                    lang: "nl-NL",
                    content: {
                        title: "Slaap onderbreking 1",
                        icon: "resources/cat-icons-64/cat_paper.png",
                        text: "Uw bedsensor heeft gedecteerd dat u niet meer in bed lag, de positie sensor vond u in de badkamer.",
                        duration: " Duur: ongeveer 8 minuten "
                    }
                },
                {
                    lang: "da",
                    content: {
                        title: "Søvnafbrydelse 1",
                        icon: "resources/cat-icons-64/cat_paper.png",
                        text: "Din sengesensor er registrerer dig ikke længere i sengen. Din placeringssensor registrerer dig i badeværelset.",
                        duration: " Varighed: Ca. otte minutter "
                    }
                },
                {
                    lang: "da-DK",
                    content: {
                        title: "Søvnafbrydelse 1",
                        icon: "resources/cat-icons-64/cat_paper.png",
                        text: "Din sengesensor er registrerer dig ikke længere i sengen. Din placeringssensor registrerer dig i badeværelset.",
                        duration: " Varighed: Ca. otte minutter "
                    }
                },
                {
                    lang: "de",
                    content: {
                        title: "Schlafunterbrechung 1",
                        icon: "resources/cat-icons-64/cat_paper.png",
                        text: "Ihr Bettsensor hat aufgehört, Sie im Bett zu erfassen; Ihr Positionssensor platziert Sie im Badezimmer.",
                        duration: " Dauer: ca. acht Minuten "
                    }
                },
                {
                    lang: "de-AT",
                    content: {
                        title: "Schlafunterbrechung 1",
                        icon: "resources/cat-icons-64/cat_paper.png",
                        text: "Ihr Bettsensor hat aufgehört, Sie im Bett zu erfassen; Ihr Positionssensor platziert Sie im Badezimmer.",
                        duration: " Dauer: ca. acht Minuten "
                    }
                },
                {
                    lang: "it",
                    content: {
                        title: "Interruzione del sonno 1",
                        icon: "resources/cat-icons-64/cat_paper.png",
                        text: "Il tuo sensore del letto ha smesso di rilevarti a letto; il tuo sensore di posizione ti indica in bagno.",
                        duration: " Durata: circa 8 minuti "
                    }
                }
            ]
        },
        {
            start_date: "JUL 18, 2014",
            start_time: "00:08:05",
            locationID: 5,
            off_bed: true,
            transitional: true,
            timeline: {
                distance: "",
                bulletStyle: "",
                bulletIconStyle: ""

            },
            text: [
                {
                    lang: "en",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "en-US",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "nl",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "nl-NL",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "da",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "da-DK",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "de",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "de-AT",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "it",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                }
            ]
        },
        {
            start_date: "JUL 18, 2014",
            start_time: "00:08:36",
            locationID: 3,
            off_bed: true,
            transitional: true,
            timeline: {
                distance: "",
                bulletStyle: "",
                bulletIconStyle: ""

            },
            text: [
                {
                    lang: "en",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "en-US",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "nl",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "nl-NL",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "da",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "da-DK",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "de",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "de-AT",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                },
                {
                    lang: "it",
                    content: {
                        title: "Transitional episode",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "",
                        duration: ""
                    }
                }
            ]
        },
        {
            start_date: "JUL 18, 2014",
            start_time: "00:08:50",
            locationID: 3,
            off_bed: false,
            transitional: false,

            timeline: {
                distance: "100px",
                bulletStyle: "bg-info",
                bulletIconStyle: "fa fa-info"

            },
            text: [
                {
                    lang: "en",
                    content: {
                        title: "Sleep cycle 2",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Your bed sensor detecting you in bed.",
                        duration: " Duration: 7 hours and 27 minutes "
                    }
                },
                {
                    lang: "en-US",
                    content: {
                        title: "Sleep cycle 2",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Your bed sensor detecting you in bed.",
                        duration: " Duration: 7 hours and 27 minutes "
                    }
                },
                {
                    lang: "nl",
                    content: {
                        title: "Slaap cyclus 2",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Uw bedsensor heeft gedetecteerd dat u in bed lag.",
                        duration: " Duur: 7 uur en 27 minuten "
                    }
                },
                {
                    lang: "nl-NL",
                    content: {
                        title: "Slaap cyclus 2",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Uw bedsensor heeft gedetecteerd dat u in bed lag.",
                        duration: " Duur: 7 uur en 27 minuten "
                    }
                },
                {
                    lang: "da",
                    content: {
                        title: "Søvncyklus 2",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Din sengesensor registrerer dig i sengen.",
                        duration: " Varighed: 7 time og 27 minutter "
                    }
                },
                {
                    lang: "da-DK",
                    content: {
                        title: "Søvncyklus 2",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Din sengesensor registrerer dig i sengen.",
                        duration: " Varighed: 7 time og 27 minutter "
                    }
                },
                {
                    lang: "de",
                    content: {
                        title: "Schlafzyklus 2",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Ihr Bettsensor erfasste Sie im Bett.",
                        duration: " Dauer: sieben Stunde und 27 Minuten "
                    }
                },
                {
                    lang: "de-AT",
                    content: {
                        title: "Schlafzyklus 2",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Ihr Bettsensor erfasste Sie im Bett.",
                        duration: " Dauer: sieben Stunde und 27 Minuten "
                    }
                },
                {
                    lang: "it",
                    content: {
                        title: "Ciclo di sonno 2",
                        icon: "resources/cat-icons-64/cat_sleep.png",
                        text: "Il tuo sensore del letto per la rilevazione a letto.",
                        duration: " Durata: 7 ore e 27 minuti "
                    }
                }
            ]
        },
        {
            start_date: "JUL 18, 2014",
            start_time: "07:35:26",
            locationID: 3,
            off_bed: false,
            transitional: false,
            woke_up: true,

            timeline: {
                distance: "0px",
                bulletStyle: "bg-primary",
                bulletIconStyle: "fa fa-sun-o"

            },
            text: [
                {
                    lang: "en",
                    content: {
                        title: "Woke up",
                        icon: "resources/cat-icons-64/cat_slippers.png",
                        text: "Your bed sensor stopped detecting you in bed, yet your position sensor places you in the bedroom. ",
                        duration: " Good morning! "
                    }
                },
                {
                    lang: "en-US",
                    content: {
                        title: "Woke up",
                        icon: "resources/cat-icons-64/cat_slippers.png",
                        text: "Your bed sensor stopped detecting you in bed, yet your position sensor places you in the bedroom. ",
                        duration: " Good morning! "
                    }
                },
                {
                    lang: "nl",
                    content: {
                        title: "Werd wakker",
                        icon: "resources/cat-icons-64/cat_slippers.png",
                        text: "Uw bedsensor heeft gedecteerd dat u niet meer in bed lag, de positie sensor vond u in de slaapkamer.",
                        duration: " Goedemorgen! "
                    }
                },
                {
                    lang: "nl-NL",
                    content: {
                        title: "Werd wakker",
                        icon: "resources/cat-icons-64/cat_slippers.png",
                        text: "Uw bedsensor heeft gedecteerd dat u niet meer in bed lag, de positie sensor vond u in de slaapkamer.",
                        duration: " Goedemorgen! "
                    }
                },
                {
                    lang: "da",
                    content: {
                        title: "Vågnede",
                        icon: "resources/cat-icons-64/cat_slippers.png",
                        text: "Din sengesensor er registrerer dig ikke længere i sengen. Din placeringssensor registrerer dig i soveværelset.",
                        duration: " Godmorgen ! "
                    }
                },
                {
                    lang: "da-DK",
                    content: {
                        title: "Vågnede",
                        icon: "resources/cat-icons-64/cat_slippers.png",
                        text: "Din sengesensor er registrerer dig ikke længere i sengen. Din placeringssensor registrerer dig i soveværelset.",
                        duration: " Godmorgen ! "
                    }
                },
                {
                    lang: "de",
                    content: {
                        title: "Aufgewacht",
                        icon: "resources/cat-icons-64/cat_slippers.png",
                        text: "Ihr Bettsensor hat aufgehört, Sie im Bett zu erfassen; Ihr Positionssensor platziert Sie im Schlafzimmer.",
                        duration: " Guten Morgen ! "
                    }
                },
                {
                    lang: "de-AT",
                    content: {
                        title: "Aufgewacht",
                        icon: "resources/cat-icons-64/cat_slippers.png",
                        text: "Ihr Bettsensor hat aufgehört, Sie im Bett zu erfassen; Ihr Positionssensor platziert Sie im Schlafzimmer.",
                        duration: " Guten Morgen ! "
                    }
                },
                {
                    lang: "it",
                    content: {
                        title: "Svegliato",
                        icon: "resources/cat-icons-64/cat_slippers.png",
                        text: "Il tuo sensore del letto ha smesso di rilevarti a letto; il tuo sensore di posizione ti indica nella camera da letto.",
                        duration: " Buongiorno ! "
                    }
                }
            ]
        }
    ],
    off_bed_intervals: [
        {
            start_date: "2014-07-17",
            start_time: "23:59:50",
            end_date: "2014-07-18",
            end_time: "00:00:52",
            locationID: 3,

        },
        {
            start_date: "2014-07-18",
            start_time: "00:01:03",
            end_date: "2014-07-18",
            end_time: "00:01:24",
            locationID: 5,

        },
        {
            start_date: "2014-07-18",
            start_time: "00:01:26",
            end_date: "2014-07-18",
            end_time: "00:08:01",
            locationID: 4,

        },
        {
            start_date: "2014-07-18",
            start_time: "00:08:05",
            end_date: "2014-07-18",
            end_time: "00:08:30",
            locationID: 5,

        },
        {
            start_date: "2014-07-18",
            start_time: "00:08:36",
            end_date: "2014-07-18",
            end_time: "00:08:50",
            locationID: 3,

        }
    ],
    on_bed_intervals: [
        {
            start_date: "2014-07-17",
            start_time: "22:20:39",
            end_date: "2014-07-17",
            end_time: "23:59:50"
        },
        {
            start_date: "2014-07-18",
            start_time: "00:08:50",
            end_date: "2014-07-18",
            end_time: "07:35:26",

        }
    ]
};
var backBtn = [
    {
        lang: "en",
        content: {
            label: "BACK TO SUMMARY"
        }
    },
    {
        lang: "en-US",
        content: {
            label: "BACK TO SUMMARY"
        }
    },
    {
        lang: "nl",
        content: {
            label: "TERUG NAAR SAMENVATTING"
        }
    },
    {
        lang: "nl-NL",
        content: {
            label: "TERUG NAAR SAMENVATTING"
        }
    },
    {
        lang: "da",
        content: {
            label: "TILBAGE TIL OVERSIGT"
        }
    },
    {
        lang: "da-DK",
        content: {
            label: "TILBAGE TIL OVERSIGT"
        }
    },
    {
        lang: "de",
        content: {
            label: "ZURÜCK ZU ZUSAMMENFASSUNG"
        }
    },
    {
        lang: "de-AT",
        content: {
            label: "ZURÜCK ZU ZUSAMMENFASSUNG"
        }
    },
    {
        lang: "it",
        content: {
            label: "TORNA AL RIEPLOGO"
        }
    }
];
var uiLanguage = '';
var localDateOptions = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
var localTimeOptions = { hour12: false, hour: 'numeric', minute: 'numeric' };

// [IMPORTANT] Uncomment this before uploading to git
 var userName = ewallApp.currentSession.user.username;
 var userPreferedLanguage = ewallApp.preferedLanguage;
 var timezoneId = ewallApp.currentSession.user.userProfile.vCardSubProfile.timezoneid;
// [IMPORTANT] Comment this before uploading to git
//var userName = 'bilbo';
//var userPreferedLanguage = 'en';
// get current date
var today = new Date();
// set comparrison to today at midday
var todayMidday = new Date();
todayMidday.setHours(12, 0, 0);
// set date for upper interval query for Sleep SB: today 12:00, or current time (if current time < 12:00)
var queryUntil = today;
if (today <= todayMidday) {
    queryUntil = today;
} else {
    queryUntil = todayMidday;
}
// get yesterday date
var yesterdayEvening = new Date();
yesterdayEvening.setDate(yesterdayEvening.getDate() - 1);
yesterdayEvening.setHours(18, 0, 0);
var queryFrom = yesterdayEvening;

//ask data from Sleep SB from: yesterday 18:00 - util today 12:00, or current time (if current time < 12:00)

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);

    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
};



function getLRSleepTime(nightPattern){
var sleepTimeObj;
    for (i in nightPattern.parameters) {         
        if(nightPattern.parameters[i].parameter == "sleep_time") {
            sleepTimeObj = nightPattern.parameters[i];
           
        }
    }
    return sleepTimeObj;
}; 

function getSleepLRParameter(parameterName, nightPattern){
var sleepParamObj;
    for (i in nightPattern.parameters) {         
        if(nightPattern.parameters[i].parameter == parameterName) {
            sleepParamObj = nightPattern.parameters[i];
           
        }
    }
    return sleepParamObj;
}; 


function renderSleepUI(){
    /*
     * insert promise request (userAuthToken, queryFrom, queryUntil)
     */
    if(sleepJSONFromSB.inbedEvents.length == 0) 
    {
        throw "ERROR: service brick returned empty set of bed events" ;  
        
    }
    // cleanu-up sleep SB data (delete off beed intervals before the user goes to sleep)
    while (sleepJSONFromSB.inbedEvents[0].inBed == false) {
        sleepJSONFromSB.inbedEvents.shift();
    }

    // ask data from Sleep LR
    /*
     * insert promise request (userAuthToken)
     */

    var lastNightDayNumber = yesterdayEvening.getDay();
    var lastNightPattern = sleepJSONFromLR.days[lastNightDayNumber];


    // cleanu-up sleepJSON template data
    sleepJSON.intervals = [];

    // define "Went to bed" card
    var wentToBedUIItem = {
        type: "wentToBed",
        start_date: "",
        start_time: "",
        start_date_LR: "",
        start_time_LR: "",
        timeline: {
            distance: "0px",
            bulletStyle: "bg-primary",
            bulletIconStyle: "fa fa-moon-o"

        },
        text: {
            lang: "en",
            content: {
                title: "Went to bed",
                icon: "../Sleep/resources/cat-icons-64/cat_sing.png",
                text: "The location sensor detected you in the bedroom. Your bed pressure sensor registered you laying on bed.",
                quality: {
                    set: false,
                    code: "",
                    color: "#ffffff"
                }
            }
        }
    };

    var timeWentToBedTonight = new Date(moment(sleepJSONFromSB.inbedEvents[0].timestamp).tz(timezoneId).format());    
    // get tonight time of going to bed
    wentToBedUIItem.start_date = timeWentToBedTonight.toLocaleDateString(userPreferedLanguage, localDateOptions);
    wentToBedUIItem.start_time = timeWentToBedTonight.toLocaleTimeString(userPreferedLanguage, localTimeOptions);

    // get usual time of going to bed    
    var timeWentToBedUsually = getSleepLRParameter('sleep_time', lastNightPattern);
    var timeWentToBedUsuallyStr = "" + Math.floor(timeWentToBedUsually.weightedAverage / 60) + " : " + timeWentToBedUsually.weightedAverage % 60;
    wentToBedUIItem.text = getWentToBedText(userPreferedLanguage, wentToBedUIItem.start_time, timeWentToBedUsuallyStr);

    // Calculate quality parameters for going to sleep.
    /* The reference date in the date to go to sleep at 23:00:00
     * Going to sleep later then the reference hour is considered to be LATE
     * (not implemented yet) Going to sleep within 30 minutes from the timeWentToBedUsually is considered to be NORMAL, if timeWentToBedUsually < 23:00:00; else it is LATE
     * Going to sleep before 21:00:00 is considered to be EARLY
     * When in conflict: GOOD > NORMAL and LATE > NORMAL
     */

    var reference23Hour = new Date(timeWentToBedTonight);
    reference23Hour.setHours(23, 0, 0);
    var reference21Hour = new Date(timeWentToBedTonight);
    reference21Hour.setHours(21, 0, 0);
    if (timeWentToBedTonight.getTime() >= reference23Hour.getTime()) {
       // console.log(timeWentToBedTonight + "< - >" + reference23Hour);
        wentToBedUIItem.text.content.quality = getSleepQuality(userPreferedLanguage, "LATE");
    } else {
        if (timeWentToBedTonight.getTime() <= reference21Hour.getTime()) {
         //   console.log(timeWentToBedTonight + "< - >" + reference21Hour);
            wentToBedUIItem.text.content.quality = getSleepQuality(userPreferedLanguage, "EARLY");
        }

    }

    //console.log(wentToBedUIItem.text.content.quality);

    // add wentToBedUIItem to the interface
    sleepJSON.intervals.push(wentToBedUIItem);


    // get sleep period and sleep interruptions
    var listOfBedEvents = [];
    var countSleepPeriod = 0;
    var countSleepInterruption = 0;
    //get sleep interruption average time (minutes)
    var sleepInterruptionAVG = getSleepLRParameter('interrupt_duration', lastNightPattern);

    for (i = 1; i < sleepJSONFromSB.inbedEvents.length; i++) {
        // handle sleep periods
        if (sleepJSONFromSB.inbedEvents[i - 1].inBed == true && sleepJSONFromSB.inbedEvents[i].inBed == false) {
            countSleepPeriod += 1;
            var utilityPrevDate = new Date(moment(sleepJSONFromSB.inbedEvents[i - 1].timestamp).tz(timezoneId).format());
            var utilityDate = new Date(moment(sleepJSONFromSB.inbedEvents[i].timestamp).tz(timezoneId).format());

            var sleepPeriodDurationHours = Math.floor(Math.abs(utilityDate.getTime() - utilityPrevDate.getTime()) / (1000 * 3600));
            var sleepPeriodDurationMinutes = (Math.abs(utilityDate.getTime() - utilityPrevDate.getTime()) % (1000 * 3600)) / (1000 * 60);
            var sleepPeriodDuration = "";
            if (sleepPeriodDurationHours != 0) {
                sleepPeriodDuration = sleepPeriodDurationHours + "h. " + sleepPeriodDurationMinutes + "m.";
                if (sleepPeriodDurationHours == 1) {
                    sleepPeriodDuration = "1h. and " + sleepPeriodDurationMinutes + "m.";
                }
            } else {
                sleepPeriodDuration = sleepPeriodDurationMinutes + "m. ";
            }
            //building the sleep period data
            var bedEvent = {
                type: "Sleep Period",
                start_date: utilityPrevDate.toLocaleDateString(userPreferedLanguage, localDateOptions),
                start_time: utilityPrevDate.toLocaleTimeString(userPreferedLanguage, localTimeOptions),
                timeline: {
                    distance: "0px",
                    bulletStyle: "bg-info",
                    bulletIconStyle: "fa fa-bed"
                },
                text: {}
            };
            bedEvent.text = getSleepPeriodText(userPreferedLanguage, countSleepPeriod, sleepPeriodDuration);
            // add this card to the UI
            sleepJSON.intervals.push(bedEvent);
        }

        // handle sleep interruptions        
        if (sleepJSONFromSB.inbedEvents[i - 1].inBed == false && sleepJSONFromSB.inbedEvents[i].inBed == true) {
            countSleepInterruption += 1;
            var utilityPrevDate = new Date(moment(sleepJSONFromSB.inbedEvents[i - 1].timestamp).tz(timezoneId).format());
            var utilityDate = new Date(moment(sleepJSONFromSB.inbedEvents[i].timestamp).tz(timezoneId).format());

            var interruptionDurationHours = Math.floor(Math.abs(utilityDate.getTime() - utilityPrevDate.getTime()) / (1000 * 3600));
            var interruptionDurationMinutes = (Math.abs(utilityDate.getTime() - utilityPrevDate.getTime()) % (1000 * 3600)) / (1000 * 60);
            var interruptionDuration = "";
            if (interruptionDurationHours != 0) {
                interruptionDuration = interruptionDurationHours + "h. " + interruptionDurationMinutes + "m.";
                if (interruptionDurationHours == 1) {
                    interruptionDuration = "1h. " + interruptionDurationMinutes + "m. ";
                }
            } else {
                interruptionDuration = interruptionDurationMinutes + "m. ";
            }

            // building the sleep interuption data
            var bedEvent = {
                type: "Sleep Interruption",
                start_date: utilityPrevDate.toLocaleDateString(userPreferedLanguage, localDateOptions),
                start_time: utilityPrevDate.toLocaleTimeString(userPreferedLanguage, localTimeOptions),
                timeline: {
                    distance: "0px",
                    bulletStyle: "bg-info",
                    bulletIconStyle: "fa fa-street-view"
                },
                text: { // to do: add translated message
                   /* lang: "en",
                    content: {
                        title: "Sleep Interruption " + countSleepInterruption,
                        icon: "../Sleep/resources/cat-icons-64/cat_walk.png",
                        text: ["You are out of bed during " + interruptionDuration + ".", "Usually you take " + sleepInterruptionAVG.weightedAverage + " minutes."], //[TODO] make poliglote sleepInterruptionText function
                        quality: {
                            set: false,
                            code: "",
                            color: "#ffffff"
                        }
                    } */
                }
            };
            bedEvent.text = getSleepInterruptionText(userPreferedLanguage, countSleepInterruption, interruptionDuration);

            // add this card to the UI
            sleepJSON.intervals.push(bedEvent);
        }
    }

    /* Building  Woke up & a conclusion. The last card in the UI should contain:
     * - the duration of last night's sleep
     * - the duration of the typical sleep night
     * - last night sleep interruption numbers
     * - usual sleep interruption numbers
     * - a overall sleep quality
     */

    // define Woke up card
    var wokeUpUIItem = {
        type: "Woke up",
        start_date: "",
        start_time: "",
        start_date_LR: "",
        start_time_LR: "",
        timeline: {
            distance: "0px",
            bulletStyle: "bg-primary",
            bulletIconStyle: "fa fa-certificate"

        },
        text: {
            lang: "en",
            content: {
                title: "Woke up",
                icon: "../Sleep/resources/cat-icons-64/cat_purr.png",
                text: ["The location sensor detected you in the bedroom. Your bed pressure sensor registered you laying on bed."],
                quality: {
                    set: true,
                    code: "LATE",
                    color: "#ffd600"
                }
            }
        }
    };
    var timeWokeUpToday = new Date(moment(sleepJSONFromSB.inbedEvents[sleepJSONFromSB.inbedEvents.length - 1].timestamp).tz(timezoneId).format());
    // get tonight time of going to bed
    wokeUpUIItem.start_date = timeWokeUpToday.toLocaleDateString(userPreferedLanguage, localDateOptions);
    wokeUpUIItem.start_time = timeWokeUpToday.toLocaleTimeString(userPreferedLanguage, localTimeOptions);

    // get usual time of going to bed    
    var timeWokeUpUsually = getSleepLRParameter('wake_time', lastNightPattern);
    var timewokeUpUsuallyHour = Math.floor(timeWokeUpUsually.weightedAverage / 60);
    var timeWokeUpUsuallyStr = "";
    if (timewokeUpUsuallyHour < 10) {
        timeWokeUpUsuallyStr = "0" + Math.floor(timeWokeUpUsually.weightedAverage / 60) + " : " + timeWokeUpUsually.weightedAverage % 60;
    } else {
        timeWokeUpUsuallyStr = "" + Math.floor(timeWokeUpUsually.weightedAverage / 60) + " : " + timeWokeUpUsually.weightedAverage % 60;
    }
    wokeUpUIItem.text = getWokeUpText(userPreferedLanguage, wokeUpUIItem.start_time, timeWokeUpUsuallyStr);

    // Calculate quality parameters for waking up.
    /* The reference date is the same date as the user woke up
     * Waking up later then 09:00 is considered to be LATE
     * (not implemented yet) Waking up within 30 minutes from the timeWokeUpUsually is considered to be NORMAL, if timeWokeUpUsually < 09:00:00; else it is LATE
     * Waking up before 07:00:00 is considered to be EARLY
     * When in conflict: GOOD > NORMAL and LATE > NORMAL
     */

    var reference9Hour = new Date(timeWokeUpToday);
    reference9Hour.setHours(9, 0, 0);
    var reference7Hour = new Date(timeWokeUpToday);
    reference7Hour.setHours(7, 0, 0);
    if (timeWokeUpToday.getTime() >= reference9Hour.getTime()) {
        //console.log(timeWokeUpToday + "< - >" + reference9Hour);
        wokeUpUIItem.text.content.quality = getSleepQuality(userPreferedLanguage, "LATE");
    } else {
        if (timeWokeUpToday.getTime() <= reference7Hour.getTime()) {
            // console.log(timeWokeUpToday + "< - >" + reference7Hour);
            wokeUpUIItem.text.content.quality = getSleepQuality(userPreferedLanguage, "EARLY");
        }

    }

    // add wentToBedUIItem to the interface
    sleepJSON.intervals.push(wokeUpUIItem);


    // define Conclusion card
    var conclusionUIItem = {
        type: "Conclusion",
        start_date: "",
        start_time: "",
        timeline: {
            distance: "0px",
            bulletStyle: "bg-primary",
            bulletIconStyle: "fa fa-pie-chart"

        },
        text: {
            lang: "en",
            content: {
                title: "Conclusion: Your sleep is ",
                icon: "../Sleep/resources/cat-icons-64/cat_banjo.png",
                text: [],
                quality: {
                    set: false,
                    code: "",
                    color: "#ffffff"
                }
            }
        }
    };
    // get tonight time of going to bed
    conclusionUIItem.start_date = timeWokeUpToday.toLocaleDateString(userPreferedLanguage, localDateOptions);
    conclusionUIItem.start_time = timeWokeUpToday.toLocaleTimeString(userPreferedLanguage, localTimeOptions);
    // calculate duration of last night sleep
    var totalSleepDurationHours = Math.floor(Math.abs(timeWentToBedTonight.getTime() - timeWokeUpToday.getTime()) / (1000 * 3600));
    var totalSleepDurationMinutes = (Math.abs(timeWentToBedTonight.getTime() - timeWokeUpToday.getTime()) % (1000 * 3600)) / (1000 * 60);
    totalSleepDurationMinutes = totalSleepDurationMinutes.toFixed(0);
    var totalSleepDurationStr = ""; // [TODO] Make poliglote hour, minute distribution function
    if (totalSleepDurationHours != 0) {
        totalSleepDurationStr = totalSleepDurationHours + "h. " + totalSleepDurationMinutes + "m. ";
        if (totalSleepDurationHours == 1) {
            totalSleepDurationStr = "1h. " + totalSleepDurationMinutes + "m. ";
        }
    } else {
        totalSleepDurationStr = totalSleepDurationMinutes + "m. ";
    }
    //conclusionUIItem.text.content.text.push(" Your total sleep time: " + totalSleepDurationStr); // [TODO] make ploiglote total sleep duration;

    conclusionUIItem.text = getConclusionText(userPreferedLanguage, totalSleepDurationStr); 
    if (totalSleepDurationHours >= 8) {
        conclusionUIItem.text.content.quality = getSleepQuality(userPreferedLanguage, "GOOD");
    } else {
        if (totalSleepDurationHours >= 6) {
            conclusionUIItem.text.content.quality = getSleepQuality(userPreferedLanguage, "NORMAL");
            //conclusionUIItem.text.content.text.push("For a GOOD night sleep you ned to sleep at least 8 hours."); // [TODO] make poliglote advice function
        } else {
            conclusionUIItem.text.content.quality = getSleepQuality(userPreferedLanguage, "BAD");
            //conclusionUIItem.text.content.text.push("For a GOOD night sleep you need to sleep at least 8 hours.");
        }
    };

    // add conclusionUIItem to the interface
    sleepJSON.intervals.push(conclusionUIItem);


};

function getDataFromSleepLR(username, $scope) {
     var url_SleepLR = '../../platform-dev/lr-sleep-monitor/sleepweekpattern';
    if (typeof ewallApp == 'object') {
        var ReqSleepFromSleepLR = ewallApp.ajax({
            url: url_SleepLR,
            type: "GET",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Content-type", "text/plain");
            },
            data: {
                userid: username
            },
            dataType: "json",
            async: true,
            cache: true,
            success: function(data) {
                        sleepJSONFromLR = data;
                        console.log("data from LR");
                        console.log(data);
                        $scope.noData = noData = false;
                        $scope.sleepCloudDataSourceMarking = '#ffffff';
                        $scope.sleepHomeDataSourceMarking = '#797979';
                        // set data to $scope and build the interface
                        renderSleepUI();
                        $scope.data = sleepJSON;
            }
        });
        return;
    } else {
        
        return;
    }
}

function getDataFromDfmSB(username, fromDate, toDate, $scope) {
    var url_DFM = '../service-brick-dailyfunctioning/v1/' + username + '/inbed';
    var callFromDate = moment(fromDate.toISOString()).tz(timezoneId).format();    
    var callToDate = moment(toDate.toISOString()).tz(timezoneId).format();
    if (typeof ewallApp == 'object') {
        var ReqSleepFromDfmSB = ewallApp.ajax({
            url: url_DFM,
            type: "GET",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Content-type", "text/plain");
            },
            data: {
                from: callFromDate,
                to: callToDate
            },
            dataType: "json",
            async: true,
            cache: true,
            success: function(data) {
                if(data) {
                    sleepJSONFromSB = data;
                    console.log('---- data from DFM SB ----');
                    console.log(data);
                     //getDataFromSleepLR(username, $scope);
                    $scope.noData = noData = false;                   
                    // set data to $scope and build the interface
                    renderSleepUI();
                    $scope.data = sleepJSON;
                    $scope.$apply();
                        
                }
            }
        });
        return;
    } else {
        return;
    }
};



// Store our _invokeQueue length before loading our controllers/directives/services
// This is just so we don't re-register anything
//var queueLen = angular.module('ewallSleep')._invokeQueue.length;

// Load javascript file with controllers/directives/services
//angular.module('ewallSleep')
ewallApp.controller('ewallSleepAppController', function($scope, $rootScope) {
    var noData = true;
    $scope.userPreferedLanguage = userPreferedLanguage;
    $scope.backBtnResources = backBtn;
    
    $scope.changedDataSource = function (selectedDataSource) {
    $scope.noData = noData = true;
    if (selectedDataSource == 1) {
        //adapt UI
         $scope.sleepCloudDataSourceMarking = '#ffffff';
         $scope.sleepHomeDataSourceMarking = '#797979';
        // get data from cloud
        getDataFromDfmSB(userName, queryFrom, queryUntil, $scope);
        //console.log("i get data from cloud");
    } else {
        if (selectedDataSource == 2){
        // get data from cloud
        $scope.noData = noData = false;
        sleepJSONFromSB = sleepJSONFromHome;
        $scope.sleepCloudDataSourceMarking = '#797979';
        $scope.sleepHomeDataSourceMarking = '#ffffff';
        renderSleepUI();
        $scope.data = sleepJSON;
        //$scope.$apply();
            }
        }
    };
    
    
    $scope.noData = noData;
    $scope.sleepCloudDataSourceMarking = '#ffffff';
    $scope.sleepHomeDataSourceMarking = '#797979';
    
    // -----------------------------------------------------------------------
    //[Important] Comment this when the promise is implemented
    /* 
       sleepJSONFromSB = sleepJSONFromCloud;
        renderSleepUI();        
        $scope.data = sleepJSON;
        $scope.noData = noData = false;
    */   
    //-------------------------------------------------------------------------
        
    // load data from cloud, by default
    try{
    getDataFromDfmSB(userName, queryFrom, queryUntil, $scope);   
    } catch(error) {
     $scope.changedDataSource(2);
     //$scope.$apply();
    }
    
// controller ends
})
ewallApp.factory('ewallSleepAppService', function() {
    return { };
})
ewallApp.directive('ewallSleepAppDirective', function() {
    return ;
    }
);


//// Register the controls/directives/services we just loaded
//var queue = angular.module('ewallSleep')._invokeQueue;
//for(var i=queueLen;i<queue.length;i++) {
//    var call = queue[i];
//    // call is in the form [providerName, providerFunc, providerArguments]
//    var provider = providers[call[0]];
//    if(provider) {
//        // e.g. $controllerProvider.register("Ctrl", function() { ... })
//        provider[call[1]].apply(provider, call[2]);
//    }
//}
//
//// compile the new element
//$('body').injector().invoke(function($compile, $rootScope) {
//    $compile($('#sleepApplication'))($rootScope);
//    // $rootScope.$apply();
//});

/*controllerProvider.controller('ewallSleepAppController', ['$scope' , function ($scope) {
    $scope.uiLanguage = uiLanguage;
    $scope.backBtnResources = backBtn;
    renderSleepUI();
    $scope.data = sleepJSON;
    }]);
*/
/*
 angular.element(document).ready(function() {
      angular.bootstrap(document, ['ewallSleepApp']);
    });
*/
/*
$('body').injector().invoke(function($compile) {
    $compile($('#sleepApplication'));    
});
*/
