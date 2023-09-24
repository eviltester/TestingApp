# About CSS Media Queries Sizing Page

<div class="explanation">
        <p>CSS Media Queries change the displayed content based on the width of the browser.
        </p>
</div>

<!-- TOC -->

## CSS Media Queries

CSS Media queries can be used to style the page differently based on the browser size.

```css
@media only screen and (min-width: 1800px) {
    h2.s1800{
        visibility: visible;
    }
}
```

## Functionality

On this page, media queries are used to 'hide' headings.

The wider the page, the more headings you will see.

## Automating

You might be limited in automating this by your tooling or screen size.

If the tool does not support resizing the browser then this will be difficult to automate.

If your machine has limited resolution then this might also be difficult to automate.

## Dev Tools

Using the Dev Tools in the browser and the "Toggle Device Toolbar" should allow you to test this, regardless of the size of your browser.

In the Dev Tools the Media Queries toolbar can make it easy to adjust the size of the responsive view based on the queries available in the CSS.