# About Windows Links Examples

<div class="explanation">
        <p>Links can open new pages, these can have 'names'
associated with them.
        </p>
</div>

## Page Specific Details

- This window is named 'windowsindex'.
- The Basic Ajax window is named 'basicajax' (configured in the page itself).
- The Attributes window, when opened, has no name.
- The Alerts window will be named 'alerts' but only if the link on this page is used to open it.

## Window Names

Windows or browser tabs can have 'names'. This allows links to jump to the open tab without opening a new tab or window.

This page is configured to have the name `windowsindex`.

The name is set using JavaScript. Use the developer tools to find the code which sets the name.

## Window Links

Links on a page can open a new window.

This might be done using the `target` attribute.

Links can also be implemented using JavaScript to open a window with a specific name associated with it.

## Synchronization

Window names can be used to switch back and forward between the windows.

## Dev Tools

Use the dev tools to inspect and explore the page DOM to understand the controls.

You can also use the JavaScript console to show the current window name:

```javascript
window.name
```