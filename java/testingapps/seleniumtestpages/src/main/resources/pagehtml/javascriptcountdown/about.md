# About JavaScript Countdown

<div class="explanation">
        <p>A countdown timer implemented in JavaScript.
        </p>
</div>

<!-- TOC -->

## About Page

This page contains text displaying a countdown with `hours : minutes : seconds`.

The countdown is controlled by JavaScript running in the background.

The timer starts automatically when the page loads and can be re-configured and controlled by the GUI.

- `seconds` input field - the number of seconds to use as the countdown
- `Reset Timer` - set the timer to the number of `seconds`
- `Start Timer` - start/restart timer using the number of seconds shown
- `Stop Timer` - pause the countdown
- `Clear Timer` - set timer to "Time Up!" with no seconds value.

## Automated Execution

For automating, it is a relatively simple set of elements with attributes to make locating easier.

Results checking can be implemented by asserting on the value of the countdown time.

## Exploratory Testing

The application maintains internal state e.g. paused, current time, pause -> restart, clear -> reset etc.

Track your coverage and be sure to have the JavaScript Console open in the Dev Tools as you test, just in case there are any JavaScript issues.

## JavaScript Hacking

As with the majority of our test pages, the JavaScript is easily callable from the console and many test input combinations could be triggered from JavaScript.