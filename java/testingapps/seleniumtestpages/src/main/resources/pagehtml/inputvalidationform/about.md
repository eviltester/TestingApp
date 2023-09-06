# Input Validation Examples

<div class="explanation">
        <p>A Form with JavaScript, HTML5, and Server-side validation.
        </p>
</div>

## About Page

This page has input validation using JavaScript, HTML5 and there is associated server side validation.

Validation Requirements:

- firstname must be filled
- firstname must be less than 90 chars
- surname should be between 11 and 79 chars  
- age between 18 and 80
- max "Notes" length is 2000
- country must be from list

## Automated Execution

For automating, it is a relatively simple form with a submission.

Automate this through the UI and check the results on the page displayed by the server.

## HTTP Execution

You could automate server side validation directly by issuing HTTP requests.

It may be faster to execute a lot of coverage by issuing HTTP requests directly.

Try to automate via HTTP requests and increase the coverage of the data used.

## Exploratory Testing

For exploratory testing, this page may have been coded with some bugs.

Some test approach ideas:

- Explore the validation on client side
- Does the server side validation match the client side?
- Do all the client side validations match up? Do the JavaScript validation match the HTML5 validations?
- Explore state changes and observe the GUI.  
- Could you automate tactically with tool support to increase the data coverage? e.g. proxy tools, browser plugins, or API automating tools. 