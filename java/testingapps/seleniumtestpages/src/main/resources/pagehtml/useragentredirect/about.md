# About User Agent Redirect Page

<div class="explanation">
        <p>Different pages will be shown, depending on the `user-agent` header.
        </p>
</div>

## About

If you hit the page with a browser, that the server thinks is a mobile browser then you will be redirected to the mobile version of this page. The server decides when to redirect based on the user-agent header in the request.

The server does not have particularly good user agent detection so it will not work for all mobile user agents. It is deliberately buggy.

_Note: This is a pretty old fashioned way of handling mobile devices. Most websites now are responsive, and will work on both desktop and mobile, and so no separate mobile pages, or subdomain would be used._

In the real world, you will normally find a subdomain being used e.g. instead of `https://eviltester.com` it would be `https://m.eviltester.com`.

## Functionality

- The page which detects user-agent for redirection is  [/styled/redirect/user-agent-redirect-test](/styled/redirect/user-agent-redirect-test)
- The redirected user agent page is at [/styled/redirect/mobile/user-agent-mobile-test](/styled/redirect/mobile/user-agent-mobile-test)

If you are automating, then you can either detect you were redirected by looking at the title, or the url.

- If the title contains "Mobile" then you were redirected.
- If the url contains "/mobile" or "/mobile/" then you were redirected

## Dev Tools

You can change the browser user-agent header by using the dev tools.

e.g. the "Toggle Device Toolbar" in Chrome.

This simulates a mobile device by changing the view port size and sending the appropriate header to the server when the page is refreshed.

## Extensions

There are many browser extensions which will also do this for you, these often come with more user-agents and make it possible to simulate visiting the site as GoogleBot or BingBot.