# About 7 Char Validation

<div class="explanation">
        <p>A very simple validation input controlled by JavaScript.
        </p>
</div>

<!-- TOC -->

Patreon supporters can download a [free 205 page ebook of exercises and notes](https://www.patreon.com/posts/75068248) based on this single 7 Char Validation application.

## About Page

This was the first application I specifically created to teach testing.

I created it as a team exercise for a team I was managing at the time.

The form is simple:

- an input field
- a button to validate the contents of the field
- a field that displays the results of the validation

The validation rules are simple, the input should be 7 characters in length, and the only valid characters are:

- `A-Z`,
- `a-z`,
- `0-9`,
- and `*`

When the input is valid the result is "Valid Value", when invalid the result shows "Invalid Value"

There are deliberate bugs in the application, so this can be used for testing.

## UI Automated Execution

The UI is basic, and has locators for each field.

You may have to synchronize on the result field.

## JavaScript Hacking

You could automate this from the console and create a lot of input variation very quickly.

The controlling JavaScript is obfuscated, but I'm sure that won't stop you.

## Exploratory Testing

This is a fun application to test. There are lots of different ways of approaching the application.

e.g.

- Exercise Set 1 - First Steps
  - Instructions
  - Try the app
  - Find a Bug 
  - Build a Risk Model of the Application
- Exercise Set 2 - Input Output Domain Coverage
  - Model Output Domain and go for Coverage 
  - Model Input Domain and Expand Coverage
- Exercise Set 3 - GUI State and Interaction
  - Investigate Application State
  - Investigate GUI Interaction
  - Investigate the GUI Technically
  - Investigate Error Handling for Malformed Page
- Exercise Set 4 - Environment Interaction
- Exercise: Set 5 - Tooling Use
- Exercise: Set 6 - Automate Tactically
- Exercise: Set 7 - End Game - Source investigation
- Exercise: Set 8 - Debriefs

## Ebook

I documented the various ways I approached the testing of this application for my Patreon supporters and ended up creating a 205 page e-book filled with exercises, testing notes, theory and automated execution tips and code samples.

[Patreon supports can download the ebook for free](https://www.patreon.com/posts/75068248)

The workbook contains eight sets of exercises that cover approaches for practicing.

In total there are twenty five exercises. Each exercise should take between thirty minutes to one hour to complete.

The workbook also contains the basic theory that I used when I was completing the exercises, so if you are new to testing then you may find those sections useful to give you some ideas, and to help understand the language and thought processes behind testing.

Additionally, I have included my notes for the practice sessions I performed for each exercise.

## Excerpt - Learning to Test with 7CharVal

7CharVal was one of the first applications I wrote to have something small and focussed to use as a testing exercise for teams.

When I first used this as a testing exercise the instructions were simple:

- spend 30 minutes
- test this
- see what you find

This is a very open set of instructions.

It was interesting to see how people approach it:

- some people immediately go on a bug hunt
- some people test basic inputs and then start exploring with [BugMagnet](https://bugmagnet.org/)
- some people test and create a coverage model as a mind map
- some people test and take notes as a text file
- some people freeze, not knowing what to do

## Excerpt - On Freezing When Testing

The 'freeze' was a concern for me because testing in its most simple form is:

- create a model of the system
- investigate to see if the system matches the model
- use the differences to:
    - ask questions
    - amend the model and test more,
    - raise issues when the model is believed to be correct

Freezing meant that people didn't know how to kickstart their process.

## Excerpt - Modelling from NULL

Even when we know nothing about a system, e.g. we have a NULL model, we can look at the system and see that it is not NULL, then immediately expand our model to include what we see e.g.

- we see a button
- click it
- something happens

We have now expanded our model to include:

- a button
- an action
- am observable result

We can then reflect on the 'happening' and decide "did the resulting 'happening' make sense given the state of the application?" and to do that we have to:

- build a model of the 'state' of the application
- identify some behaviour rules for the application

We can then identify:

- more possible states
- manipulate the system so that it does, or does not move into those states
- if our behaviour rules model matches the observable results

We can also go further and move from surface level observations to interrogate the system more deeply and expand our understanding e.g. if the application uses a database then look in the database to see if it matches the information on the GUI.

## Excerpt - A Simple Model Of Testing

Above I've presented a very simple model of how I approach the act of testing:

- Modeling - creating multiple models of the application e.g. to aid my understanding, to assess coverage, etc.
- Observing - seeing what happens, what changes, what options are available, what I'm thinking, etc.
- Reflecting - comparing my observations with my model, identifying gaps, deciding what to do next, etc.
- Interrogating - going more deeply into the application, examining results in more detail, etc.
- Manipulating - interacting with the application, controlling its state, etc.

These are all very active processes and I draw on my experiences, testing knowledge, technical domain knowledge and more as I test.

## Excerpt - Practice Makes us Unique

Testing is a very unique process for each individual that tests, we all do it differently, this is why practicing is very helpful:

- expand our approach to include techniques and information we learn from others
- boost our technical skills to improve our competence
- experiment with different ways of making notes as we test
- trying new tools to see how they help or hinder us
- etc.

Even an application as simple as 7CharVal opens up a lot of possibilities for testing and practicing testing.

## Excerpt - Old Apps, New Practices

I wrote 7CharVal back in 2009 and it remains unchanged since then. I revisit it periodically... generally when I've forgotten what the bugs are, how it works and what I did with it in the past.

As I approach it anew every few years I can see that my testing approach has changed and I find new ways to approach the testing.

What was once a 30 minute exercise can now easily occupy a productive day or more of practice.

The application hasn't changed.

I've changed, my approach to testing has changed through a combination of practice and experience.




