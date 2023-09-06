# About JavaScript Canvas Page

<div class="explanation">
        <p>Draw on the canvas using JavaScript commands.
        </p>
</div>

## About Page

The UI shows a canvas with some input fields.

Selecting the various values in the input fields will trigger a JavaScript command that will draw shapes on the canvas.

Each command will be shown in the list on the right of the canvas.

## JavaScript Instructions

Commands:

- `draw(shape, x, y, size, "#colour")`
- `clearCanvas()`

Note:

- shape is 1 for circle, 0 for square
- colour is an rgb string e.g. "#FF1C0A"
- size is in pixels
- canvas is 300 x 300

## Automating UI

The form itself is possible to automate using simple locators.

## JavaScript Hacking

Because the underlying commands are hooked into the dom it is possible to run the commands from the JavaScript console to create shapes on the canvas.

## Exploratory Testing

Exploratory Testing might highlight usability improvements or missing functionality that would aid the user.

## Suggested Exercises

- There is no 'save' function in the app. Write some JavaScript in the console that will scrape the commands and output them in a string. Clear the screen and use the generated script to recreate the image. 