# About Disabled Dynamic Buttons

<div class="explanation">
        <p>Click all 4 dynamic buttons.
        </p>
</div>

## Functionality

This page has dynamic buttons. They are slightly harder to automate.

Buttons `One`, `Two` and `Three` are shown on the page disabled.

The `start` button is added by JavaScript dynamically.

Click each `enabled` button in sequence to end the challenge.

Each button is enabled on a delay so you will have to wait for it to become enabled before clicking.

When you click the final button the message "All Buttons Clicked" will be displayed and the buttons will reset allowing you to start the challenge again.

## Automating

You will need to synchronize on the `start` button becoming available.

Additionally you will need to synchronize on the `one`, `two`, `three` buttons becoming enabled.

## Testing

This set of buttons is also suitable as an Exploratory Testing Challenge.
