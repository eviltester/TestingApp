# About Triangle App

<div class="explanation">
        <p>Enter the lengths of the three sides of a triangle. The program will inform you if the triangle is equilateral, isosceles or scalene.
        </p>
</div>

## About Page

There are three text input fields, each representing a side of a triangle.

Press the button `Identify Triangle Type` to trigger the JavaScript functionality that reads the input fields and runs an algorithm to determine the type of triangle.

Any field or algorithm errors will be displayed on the page.

When the application believes you have entered a triangle it will also draw a representation of the triangle in the on page canvas element.

## Triangle Algorithm

- A triangle has three sides.
- The sum of the length of any two sides must be greater than the length of the third side.
- Equilateral Triangle all three sides are equal.
- Isosceles Triangle two sides are equal.
- Scalene no sides are equal.

## Dev Tools

Use the Dev Tools and check that the description is correct, does the page actually send the form to the server? You should be able to see the form submission in the Network Tab.

Getting into the habit of using the Dev Tools when testing will help you understand the applications that you work with very quickly.

## Automated Execution

For automating, it is a simple form with a button.

Since the application uses JavaScript you may need to write synchronization code.

Detecting the validation errors and on screen error messages should be easy to find and assert on.

The canvas will be harder to automate.

## Exploratory Testing

The Triangle App is a classic Testing Case Study so you'll find plenty of ideas and documented approaches online.

## JavaScript Hacking

The JavaScript is visible in the page and hooked on to the DOM so it is possible to automate the application and test the functionality from the console or using JavaScript Executor.

## Multiple Versions

There are actually two slightly different implementations of the triangle app.

They might have different issues.

- Automating might make it easier to repeat the same input and output scenarios across each application.
- Reviewing the code might help identify what risks to tackle.
