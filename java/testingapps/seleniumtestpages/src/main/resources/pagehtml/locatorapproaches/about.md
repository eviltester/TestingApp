# About Locator Approaches Page

<div class="explanation">
        <p>There are many ways to locate elements on a page. Each li has elements which can be
found in different ways, and built around the locators offered by tooling.
        </p>
</div>

<!-- TOC -->

## Use Dev Tools To Inspect The Page

Use the inbuilt browser developer tools to inspect the DOM.

You'll see that the `li` items have attributes or elements with attributes that can be used for locating them.

Try and find these using a variety of locators.

### Exercises for Automating

Locate each of the items below and automate an assertion against the element.

- `tagName li` - find the `li` item using the element's tag i.e. `li`
- `id li` - find the `li` with the id `find-by-id`
- `className li` - find the `li` with class `find-by-class-name`
- `name button` - find the `button` with the name `find-by-name`
- `partial link text link` - find the `a` with text `partial link text link` by using a partial match on the link text
- `full link text link` - find the `a` with text `full link text link` by using a full match on the link text
- `by css li` - find the `li` item with class `by-css` using a CSS selector
- `by xpath li` - find the `li` with a child span which has an id `child-of-li` using an XPath selector
- `image` button - find the image with the alt tag `an image of a button`
- `testid li` - find the `li` with a test id attribute `data-testid`
- `labeled input value` - find the input field with the label `labeled`
- `find by placeholder` - find the input field with the placeholder `inputplaceholder`
- `titled li` - find the `li` with title `li with title`
- `presentation role li` - find the `li` with the Aria role `presentation`