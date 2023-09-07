# About Selenium Simplified Search

<div class="explanation">
        <p>A very simple server based search engine .
        </p>
</div>

## About Page

This page contains an input field and a `search` button.

There is some matching of your search term to the hardcoded data in the backend, if not enough results are matched the the list of results will be filled with random results to make up the numbers.

There will always be the same number of results returned.

Cookies are used for the page and much of this page functionality concerns the cookies.

Three Cookies are used:

- one for the last search term
- one for the number of visits to the page
- one for the last visit date

Information from the cookies is shown on the page.

Use the Browser Dev Tools to explore and model the cookies used, also to understand the server side interaction.

## Automated Execution

Most GUI automated execution tools allow setting and amending the cookies, so this is a useful page to explore that functionality.

The form has no complicated JavaScript and is submitted directly to the server.

Minimal synchronisation will be required because the page is populated by the server.

## Exploratory Testing

There are many comments about this page that you might make to the developer.

Since the page is populated by the server you might choose to engage in HTTP level testing in addition to the GUI level testing.
