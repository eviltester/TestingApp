# About Triangle App

<div class="explanation">
        <p>Enter the lengths of the three sides of a triangle. The program will inform you if the triangle is equilateral, isosceles or scalene.
        </p>
</div>

<!-- TOC -->

## About This Triangle Application

There are three text input fields, each representing a side of a triangle.

Press the button `Identify Triangle Type` to trigger the JavaScript functionality that reads the input fields and runs an algorithm to determine the type of triangle.

Any field or algorithm errors will be displayed on the page.

When the application believes you have entered a triangle it will also draw a representation of the triangle in the on page canvas element.

### Triangle Algorithm

- A triangle has three sides.
- The sum of the length of any two sides must be greater than the length of the third side.
- Equilateral Triangle all three sides are equal.
- Isosceles Triangle two sides are equal.
- Scalene no sides are equal.

### Dev Tools

Use the Dev Tools and check that the description is correct, does the page actually send the form to the server? You should be able to see the form submission in the Network Tab.

Getting into the habit of using the Dev Tools when testing will help you understand the applications that you work with very quickly.

### Automated Execution

For automating, it is a simple form with a button.

Since the application uses JavaScript you may need to write synchronization code.

Detecting the validation errors and on screen error messages should be easy to find and assert on.

The canvas will be harder to automate.

### Exploratory Testing

The Triangle App is a classic Testing Case Study so you'll find plenty of ideas and documented approaches online.

### JavaScript Hacking

The JavaScript is visible in the page and hooked on to the DOM so it is possible to automate the application and test the functionality from the console or using JavaScript Executor.

### Multiple Versions

There are actually two slightly different implementations of the triangle app.

They might have different issues.

- Automating might make it easier to repeat the same input and output scenarios across each application.
- Reviewing the code might help identify what risks to tackle.

---

## What is The Triangle Problem?

The Triangle Problem is a 'classic' from Software Testing, described in many books and training courses. This post describes the problem, links to some applications you can use to practice on and describes some nuances around the testing.

The application context surrounding the Triangle Problem is usually:

> Given an application which takes three inputs, each representing the length of one side of a triangle, the application will respond with a message identifying if the inputs are: invalid, or represent a triangle, if they represent a triangle then the application will respond with the type of triangle (equilateral, isosceles, scalene).

The 'problem' is usually presented as a test design problem:

- How would you test this?
- What inputs would you use to test this?
- etc.

## Triangle Problem Exercises

### Traditional:

- In advance of testing, create a set of inputs with expected results, try them in the application, review coverage
- Work with others, repeat exercise above, compare inputs and coverage
- Create a 'minimum' input set for your testing.

### Using either of the implementations:

- The input design ignores the GUI implementation, what else do you need to do to test the GUI?
- Test the drawing functionality, does that require different test input?
- Can you spot any hidden functionality? If so, test it.

### Discuss:

- When would creating a 'minimum' input set be helpful?

### Tool Support working with Test Pages Implementation:

- Are there any lightweight adhoc tools or approaches for automating that could help you as you perform exploratory testing?

### Automating Test Pages implementation:

- Review the implementation from the perspective of "Is this automatable?"
- How many different ways could you automate this?
- Would any changes to the implementation make automating easier?
- Try automating the implementation.

### Code Review:

- Review the code for the Test Pages Implementation, does that help or hinder your testing?

### Using the Test Pages implementations:

- Unit test the underlying JavaScript implementation, compare with your GUI approach and original test design.
- Automate the Unit Testing from the console.

Make up your own exercises to push your approach to testing this simple example as far as you can.

## Books and Research Resources

The Triangle Problem is described and referenced in the resources and books that follow.

### Books:

- The Art of Software Testing - Glenford Myers
- Software Testing a Craftsman's Approach - Paul C. Jorgensen
- Testing Object-oriented Systems - Robert Binder
- Test Driven Development By Example - Kent Beck

### Additional Resources:

- Exercise: Analyzing the Triangle Problem by Ross Collard from the Third Annual Workshop on the Teaching of Software Testing 2004
    - testingeducation.org used to have easy to find links to this material and details of the conference but the site seems to have invalidated the content.
    - there are various sites which have archived this pdf
    - https://www.yumpu.com/en/document/view/13190450/exercise-analyzing-the-triangle-problem-testing-education
    - https://pdf4pro.com/view/exercise-analyzing-the-triangle-problem-testing-9fc7.html

### Other Implementations of the Triangle Application

During my research I found an implementation by [MathWarehouse.com](https://www.mathwarehouse.com) the Triangle Calculator below:

- [Triangle Calculator](https://www.mathwarehouse.com/triangle-calculator/online.php)

