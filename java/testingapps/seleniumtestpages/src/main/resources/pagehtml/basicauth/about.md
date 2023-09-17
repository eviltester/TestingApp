# About Basic Auth Example

<div class="explanation">
        <p>The most basic form of HTTP authentication is Basic Authentication.
Where a username and password are base64 encoded in an Authentication header.
        </p>
</div>

## Basic Authentication

When a page protected by basic authentication is accessed in a browser, a dialog will usually be shown so the user can enter a `username` and `password`.

This will then be sent to the server in an `Authorization` header.

To access the protected page, use the `username` and `password`

- username: `authorized`
- password: `password001`

After entering the details, the results page will show you if you have been authenticated or not, and the reason will also be displayed.

## Explore Using the Dev Tools

Using the browser dev tools you can see the network traffic.

View the request send to the server and examine the `Authorization` header.

Explore using different combinations of input and see which states the server checks for.

## Automating

Most tools for Automating Browsers have specific methods for accessing Basic Authenticated pages.

## API Testing

You could also treat the protected page as an API and create the `Authorization` header yourself.

You should probably set the `Accept` header to return JSON.