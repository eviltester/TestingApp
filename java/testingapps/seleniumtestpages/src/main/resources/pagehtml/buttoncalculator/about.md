# About Button Based Calculator

<div class="explanation">
        <p>A calculator implemented using JavaScript and styled HTML components.
        </p>
</div>

## About Page

This is a simple calculator. The GUI is a set of buttons styled with CSS and the functionality implemented using JavaScript

The calculator has common controls you'd see on a desktop physical calculator.

- buttons `0`-`9` and `.` to enter numbers
- arithmetic operators (`+`,`-`,`/`,`*`)
- `=` to see the results of the calculation
- `AC` all clear
- `CE` clear entry
- `M+` add entry to memory
- `MR` memory recall
- `Min` insert entry to memory

## Dev Tools

Since this calculator is implemented using JavaScript. Use the Dev Tools to keep the JavaScript console open and spot any JavaScript errors, if any should appear.

Also use the Dev Tools to inspect the DOM and examine the HTML structure.

## Automated Execution

For automating, all of the buttons and elements on screen have enough attributes to be findable.

Click can be used to trigger the JavaScript functionality.

The display is a text field. Find this and assert on the value.

## Exploratory Testing

The application has simple arithmetic functionality to test.

The application maintains state with the memory so there may be state interaction issues to explore.

## JavaScript Hacking

All of the JavaScript functionality is accessible in the document so you can use the Dev Tools console to test the application functionality or combine GUI automated execution with JavaScript executor.
