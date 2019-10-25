function SecondsFormatter(){
    var seconds;

    this.forValue = function (aValue){
        seconds = aValue;
        return this;
    }

    function add_leading_zero( aNumber ) {
        var strung = aNumber.toString();
        return (strung.length < 2) ? "0" + strung : strung;
    }

    this.asHHMMss = function () {

        var calculatedSeconds = seconds % 60;
        var calculatedMinutes = Math.floor(seconds / 60) % 60;
        var calculatedHours = Math.floor(seconds / 3600);

        var secondsDisplay = add_leading_zero( calculatedSeconds );
        var minutesDisplay = add_leading_zero( calculatedMinutes );
        var hoursDisplay = add_leading_zero( calculatedHours );

        return hoursDisplay + ':' + minutesDisplay + ':' + secondsDisplay;
    }
}

function CountDown() {

    var time_left = 0;
    var timeupMessage = 'Time Up!';
    var everyMillis = 1000;

    this.setTimeoutMessage = function(message){
        timeupMessage = message;
    }

    function show_time_left() {

        var timeDisplay = timeupMessage;

        if(time_left > 0){
            timeDisplay = new SecondsFormatter().forValue(time_left).asHHMMss();
        }

        document.getElementById('javascript_countdown_time').innerHTML = timeDisplay;
    }

    function advanceTime(){
        time_left = time_left - (everyMillis/1000);
        show_time_left();
    }

    var timer=null;

    this.startTimer = function (){
        if(timer == null && time_left > 0) {
            timer = setInterval(advanceTime, everyMillis);
        }
    }

    this.stopTimer = function(){
        if(timer != null) {
            clearInterval(timer);
            timer=null;
        }
    }

    this.clearTimer = function(){
        this.stopTimer();
        time_left = 0;
        show_time_left();
    }

    this.resetTimer = function(seconds){
        if(isNaN(seconds)){
            return;
        }
        time_left = seconds;
        show_time_left();
    }

    this.start = function(seconds){
        this.stopTimer();
        this.resetTimer(seconds);
        this.startTimer();
    };


};