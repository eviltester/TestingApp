# About Nested Frames Example

<div class="explanation">
        <p>Framesets are deprecated in HTML but periodically used.
        </p>
</div>

## Example

The example has a set of nested frames. The `frame` elements are contained in `frameset` elements.

## Frames

Frames are rarely used since pages have to conform to an invalid HTML approach.

The page has to have no `body` tag.

Make sure to inspect the page to double-check this.

The frame contents can be well form html. But the parent page with the `frameset` has no `body`.

## Automated Tools

Most automated tools have commands dedicated to working with frames.

In WebDriver you would `switchTo().frame(...)` the frame to work with it.

