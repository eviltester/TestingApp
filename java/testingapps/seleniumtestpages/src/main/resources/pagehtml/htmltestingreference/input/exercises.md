# Input Elements - Exercises

<div class="explanation">
        <p>Software Testing exercises related to `input` elements.
        </p>
</div>

<!-- TOC -->

## Hidden Fields

### Amend a Hidden Field:

- Using "The Pulper" - [online here](https://thepulper.herokuapp.com)
- Switch to a version of the app which supports book amendments
    - Choose a version number from the Admin menu drop down
- Visit a book amendment page [e.g.](https://thepulper.herokuapp.com/apps/pulp/gui/amend/book?book=1)
- This form has hidden fields, what happens if you amend the value?

### View a Hidden Field:

- Using "The Pulper" - [online here](https://thepulper.herokuapp.com)
- Visit a book amendment page [e.g.](https://thepulper.herokuapp.com/apps/pulp/gui/amend/book?book=1)
- Amend the type of field from 'hidden' to 'text'

## Text Fields

### Invalid Values

Change Form fields to text and enter invalid values.

- Using [The Pulper](https://www.eviltester.com/page/tools/thepulper/) find it [online here](https://thepulper.herokuapp.com)
- Select a Book and use the Amend form [e.g.](https://thepulper.herokuapp.com/apps/pulp/gui/amend/book?book=1)
- Change the various form fields to 'text' and enter invalid values.

### Explore Field

Explore the 7CharVal validation message field and its use of Javascript events.

- The [7CharVal](/styled/apps/7charval/simple7charvalidation.html) application.
- Has two input fields, an entry input field and a validation message field.
- The validation message field is not editable.
- Explore the HTML and Element Attributes for the input fields.
- Make the second validation field editable.


## Number Fields

- What can you enter in the [Test Pages HTML5 Form](/styled/html5-form-test.html) as a number which might cause validation errors on the server side?

## Image

Try changing the `src` on the image` so that you can see what is rendered when the wrong or missing image is used.

## Form Submission

Try to identify different ways to submit the form. Then experiment with them all.

Hint: I know of at least 6

## Form Validation

When a form is submitted, some of the input types provide some validation protection e.g. email, url, etc.

Explore the form to find out which types offer validation, and when the validation is applied.

Can you think of any risks associated with this type of validation?

 