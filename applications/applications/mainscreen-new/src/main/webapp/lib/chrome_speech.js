/*
 * Coded by Jason Mayes 2013 for Chrome (but may work on other browsers now).
 * Please keep this disclaimer if you use this code.
 * www.jasonmayes.com
 */

function sayIt(query, language) {
    var q = new SpeechSynthesisUtterance(query);
    q.lang = language;
    q.rate = 1.2;
    speechSynthesis.speak(q);
}

function sayIt(query) {
    var q = new SpeechSynthesisUtterance(query);
    q.lang = 'en-US';
    q.rate = 1.2;
    speechSynthesis.speak(q);
}