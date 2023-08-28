# About Simple Note Taker

<div class="explanation">
        <p>This is a simple note taking application where the
            notes are stored locally in the browser application storage.
        </p>
</div>

## Use Session or Local Storage

You can switch between storing data in the Local Storage or the Session Storage by clicking on the associate link i.e. "Local Storage" to use Local Storage, or "Session Storage" to use Session Storage.

Use the dev tools to view the Application Storage to see the data stored in the browser.

- Session Storage is cleared when the browser is closed. 
- Local Storage is persisted with the browser profile.

## Adding Notes

Validation is performed when adding the note details.

- Title must not be blank
- Note must not be blank

Click the `Add` button to store the note and show it in the browser.

## Managing Storage

By default the stored data is not loaded into the browser.

Press:

- `Load` to load in the data from the currently chosen storage.
- `Show` to show the data in the browser
- `Clear All` to clear all of the stored data
