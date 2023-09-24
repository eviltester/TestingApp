# Fake Alert Test Page

<div class="explanation">
        <p>Not all alerts are JavaScript Alerts, as this page demonstrates.
        </p>
</div>

<!-- TOC -->

## Alert, Confirm, Prompt Box

JavaScript provides three types of alerts: Alert, Confirm, Prompt.

Because these are implemented by browsers, most automated execution tools have specific methods for dealing with them.

e.g. when using WebDriver we switch to the alert `driver.switchTo().alert()` and then `dismiss()` or `accept()` etc.

## Using a Div

Most modern web applications do not use alerts very frequently, if at all, because they are very invasive to the user experience and cannot be styled by CSS.

This leads to situations where people automating blame the tool for not working with alert boxes, when the reality is the user interaction does not use JavaScript alerts.

`div` can be made to look like an alert, and work as a modal control which prevents access to the rest of the page until it is dismissed.

These 'fake alerts' can be interacted with using normal DOM interaction approaches.

- inspect the DOM to understand the control
- find elements and 'click' them to dismiss the control

## DOM Interaction

On this page, an attribute on the trigger inputs updates to count the number of times the dialog has been displayed.
