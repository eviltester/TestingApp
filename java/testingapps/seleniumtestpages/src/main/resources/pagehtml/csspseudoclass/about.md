# About CSS Pseudo Class Examples

<div class="explanation">
        <p>CSS Pseudo classes can be used to implement what looks like JavaScript functionality.
        </p>
</div>

## Hover

In this example the elements on the left have 'hover' implemented. If you hover over the element then you should see an effect.

e.g

```
p:hover{ /* restyle here */ }
```

## Automating

Hover can be harder to trigger during automating.

But there is no delay on the display here so synchronization should be a little easier than other examples.

Try to hover over the elements, synchronize on, and then assert that the new content displayed is visible.

## Hacking the page

Sometimes people add automation hacks e.g. if you change the style of the 'hover' element to 'hovered' you should be able to trigger the same effect as hovering.

You can also do this using the dev tools as you experiment with the DOM.