# About Configurable Automatic Dynamic Buttons

<div class="explanation">
        <p>Buttons will be replaced by new buttons automatically. Clicks are tracked.
            You can amend the number of milliseconds between each button refresh.
        </p>
</div>

<!-- TOC -->

## Functionality

This page has dynamic buttons. They are re-created automatically every X milliseconds,
where the number of milliseconds is configurable using the intput field.

If you find the button and click it then it should work if you click fast enough.

### Hidden Functionality

- There is a javascript variable `showIdInButton` which is set to `false` by default.
   - Amend this to `true` to see the numeric id of the button in the button text.
- The millisecond refresh is controlled by a global variable `millisChange`


## Exploratory Testing Exercise Suggestions

- does the input field work effectively?
- what happens when the refresh time gets low?
- use the console to interact with the global variables `showIdInButton` and `millisChange`

## Automating Exercise Suggestions

- try to click the button every time that it is changed
- automate amending the refresh time
- can you click faster than the human?
- what is the lowest refresh time you can automate against?
- amend the `showIdInButton` using your tooling
- use the global variable `millisChange` to amend the time 
- use this page to see what your tool does if you find an element, then the element is replaced, and you click on it


