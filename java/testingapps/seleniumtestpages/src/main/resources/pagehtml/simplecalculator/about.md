# About Simple Calculator

<div class="explanation">
        <p>A server side calculator. Submit the form and receive the result.
        </p>
</div>

<!-- TOC -->

## About Page

This is a simple application with a GUI, generated by the server.

When you submit the calculation the request is sent to the server and returned as a newly formatted web page.

## Dev Tools

Use the Dev Tools and check that the description is correct, does the page actually send the form to the server? You should be able to see the form submission in the Network Tab.

Getting into the habit of using the Dev Tools when testing will help you understand the applications that you work with very quickly.

## Automated Execution

For automating, it is a relatively simple form with a submission.

Since the form submits a page and the page refreshes, there is no real need to synchronize.

Simply:

- input your data
- submit the form
- assert on the expected results


## HTTP Execution

You could automate server side validation directly by issuing HTTP requests.

It may be faster to execute a lot of coverage by issuing HTTP requests directly.

Try to automate via HTTP requests and increase the coverage of the data used.

## Exploratory Testing

For exploratory testing you might want to use some tactical tools to help increase the data scope for simple data.

The form doesn't seem to have much validation so you can explore edge cases.

A Proxy Tool might help here, by intercepting the request to the server, you could resend the request with changed data without having to fill in the web form all the time.

Proxy tools also have fuzzers, and you might be able to use those to cover a lot of data combinations quickly.