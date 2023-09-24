# About WebDriver Example Page

<div class="explanation">
        <p>This is a simple web page use for basic automated execution examples with some interesting hidden functionality and synchronization requirements.
        </p>
</div>

<!-- TOC -->

## Example Page Fixed Text

There are two main fixed text elements.

- A series of two paragraphs
- A list containing three items

All of the above have id's, class names and custom attributes to explore different approaches to finding elements on the page. They also have surrounding parent div elements to practice relative locator techniques.

## Numbers to Text

There is a 'number to text' feature. Type in some numbers and they can be converted into text values.

This can be done:

- dynamically added to the page using `Show as Para`
- in an alert using `Show as Alert`
- on the server and rendered as part of the page using `Process On Server`
- on the server using `Show From Link`

There are various timings involved in the JavaScript rendering to make synchronisation a little harder when making sure that the text is displayed and visible on the screen.

## Hidden Functionality

Additional attributes are amended when the numbers are converted to text so it is worth having the Developer Tools open when testing to see the full range of amendments made to the page.