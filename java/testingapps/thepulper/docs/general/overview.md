# Overview

A simple overview of the app to support usage and workshops.

The application has multiple versions. Select a version using the admin drop down or admin menu screen. The 'help' menu shows high level changes for each version. This document describes them in more detail. Note this document may have 'spoilers' in terms of deliberate bugs to find etc.

"The Pulper" is a CRUD app (Create, Read, Update, Delete) for Pulp Magazine and book collections.

The Pulper Application Links:

- https://thepulper.herokuapp.com/apps/pulp/
    - live version
- https://www.eviltester.com/page/tools/thepulper/
    - downloads and API documentation links
- API Documentation
    - https://documenter.getpostman.com/view/27996/SzKWucnk    
    
It comes pre-populated with data.

It should support multiple users, but this depends on what people do with it. Each user is given a unique session and the data is stored on a per user basis, so users should not be able to interfere with each other. This has the risk of consuming more memory, if many people use the application. Inactive sessions are removed after about 10 to 15 minutes of inactivity, and the user will be provided with a new session. 

- https://eviltester.com/thepulper
- https://thepulper.herokuapp.com/
- https://github.com/eviltester/TestingApp/releases

I recommend using a local version which you can download from the releases page above, in case the heroku version becomes over loaded.

\newpage

# Version Release Notes

## Version 001

Version 001 is a read only version of the application.

Released in 2018.

It contains information about pulp Series:

- Doc Savage
- The Avenger
- The Spider

The application supports:

- Home
    - Main Menu with a cut down set of options
    - Help Menu
- Books
    - Table listing of all books with link to the Book entity view page
    - List (ul, li) listing of all books with link to the Book entity view page
- Authors
    - List of all authors, with link to their entity view page.
    - FAQ list, which is a list of all authors with link to their entity view page, and a link to frequently asked questions page
- Publishers
    - List of all Publishers with link to their entity view page   
- Series
    - List of all Series with link to their entity view page
- Years
    - List of all Years with link to a list of books published in that year
    
- Search
   - search by title using form
   - search by title using dialog
   - prompt for "are you sure you want to search?" can be toggled in the search form
   - exit button on page to take to main page
   - help button shows an alert explaining search functionality
        
- Reports
    - printable list of books as table, books as list, author names, publisher names, series names, and publication years
        
- View Pages
    - Entity Views
        - Book
            - shows each book details
            - has links to the publisher, year published, etc. view pages
            - link to book as list
        - Author
            - shows each author details
            - has a link to all books by the author
        - Author
            - shows each author details
            - has a link to all books by the author
            
- Entity FAQ Views (Author)
    - only accessible from the list
    - a list of questions for each entity, which link to a search engine search to answer the question e.g. What books did 'author' write?
                

---

## Version 002

Version 002 adds the ability to create, amend and delete entities.

Released in 2018.

There is an 'admin' function to experiment with the prototype filtering and paging functionality.


---

## Version 003

Version 003 has automation hooks and responsive GUI features.

Released in 2018.

- exposes the Publishers, Series, Years and Books FAQs as drop down menu list items.
- adds more ids etc. to make the GUI easier to automate:
- hamburger menu used for mobile site access

---

## Version 004

Version 004 has additional inline list functionality.

Released in 2018.

Inline amend links in the list:

- Publishers
- Series
- Authors
- Books (table and list)

Inline delete links in the list (confirm required):

- Publishers
- Series
- Authors

---

## Version 005

Version 005 is a bug fix release for the "Show All X books." and now this should show the correct number and entity name for the various lists and reports in the application.

Released in 2018.

---

## Version 006

Version 006 is a prototype MVP release.

Released in 2019.

The Create Author screen now uses Ajax XHTTP calls to the backend form processor to create an author.

This should make the application more up to date and dynamic. 

---

## Version 007

Version 007 is another prototype MVP release.

Released in 2019.

The Create Author screen now uses Ajax XHTTP calls to the api to create an author.

This should make the application even more up to date and dynamic. 

---

## Version 008

We added a search API end point into the API.

Released in 2020.

The search form now uses Ajax calls to this end point.

This also uses cookies and local storage to track previous searches.

---

## Version 009

We extended use of the API from the GUI to all the Create, Update and Delete functionality for all the entities.

Released in 2020.


---
    
## Version 010

Released in 2020.

We added bug fixes for the extended use of the API from the GUI.

Primarily on Book, Series and deletion from list.

- All deletions should be confirmed
- All updates are via POST calls
- All deletes are via DELETE calls

\newpage


# Agile Stories

This section has Agile Story versions of functionality developed for the application.

Treat these as you would any normal Agile Story.

If you are working in a team or training then ask the instructor questions as though they were the product owner, to help resolve any ambiguity and target your testing.

Suggested General Exercises:

- Review story for Ambiguity
- Write down any questions you have about the story.
- Write down any additional information you would like to have for the story.
- Write down any risks you perceive for the story, implementation of the story, or your testing or the story.
- Estimate the testing for the Story
- Estimate the automation for the Story
- Write down your proposed Test Ideas, or Test Conditions for the story
- Write down your proposed Test Approach for the Story with reasons "Why?" you are adopting the approach and the parts of your approach.
- If the story doesn't have any then, create examples for the story
- Create some Gherkin Feature descriptions for the story
- Perform exploratory testing for the story 

This is to offer you the chance to 'test' or 'automate' from Stories for a test application.

It also gives you the chance to approach an application as a set of version enhancements e.g. work from Release 1 to Release 2, look for any regression errors etc.

\newpage

## Version 001 Stories

Version 1 was a prototype, this wasn't done as an Agile process so we just implemented a bunch of stuff which you can see in the release notes.

\newpage

## Version 002 Stories

Release Aim: Create, Amend and Delete Entities so we can actually release this to the users.

### CRUD Functionality

As a user I want to be able to maintain the entities which means Create, Read, Update and Delete.

Entites:

- Author
- Series
- Publisher
- Book

Acceptance Criteria:

- A new Create menu is available allowing me to create entities
- Create screens should have validation
- No duplicates should be allowed
- Delete should be possible from the 'amend' screens
- Link to amend/update screen from the 'read' screens

### Experimental Admin Functionality

There is an 'admin' function to experiment with the prototype filtering and paging functionality.

Acceptance Criteria:

- need to check if this (paging, filtering, etc.) is ready to be implemented yet

\newpage

## Version 03 Stories

Release Aim: Allow the test team to be able to automate the GUI more easily so add some GUI Automation hooks, ids etc. to the GUI items. And make the GUI responsive.

### Tech Debt Automation Hooks

Test team to work with dev team to agree the hooks and implement them.

### Responsive Page

As a user I want to be able to use the GUI on a mobile so add a hamburger menu and resize properly.

Acceptance Criteria:

- on smaller screens a hamburger menu is shown, rather than a drop down menu
- this should work on all browsers
- this should work on mobile devices



\newpage

## Version 04 Stories

Release Aim: Rather than force the user to use the edit screens, allow deleting from the list, and a link to the amend screen from list (instead of the view screen)

### Inline Amend Link

As a user I no longer want to be forced to 'view' an entity prior to being able to amend it, I want to be able to go to the amend screen when I view a list or table.

Entities:

- author
- publisher
- series
- book

Acceptance Criteria:

- An amend link for the entity shown in the list or table view for the entity
- The amend link takes the user to the amend screen
- The amend screen still allows the user to amend the entity

### Inline Delete Functionality

As a user I no longer want to be forced to 'edit' an entity prior to deleting it, I want to be able to delete it when I view a list or table.

Entities:

- author
- publisher
- series
- book

Acceptance Criteria:

- the list view for the entity allows me to delete an entity
- can still delete entity from the edit screen



\newpage

## Version 05 Stories

Release Aim: Bug fix release.

### Show all X
 
As a user I want to see the current number of things in a list rather than "Show All X books." when I am looking at a list of publishers.

Acceptance Criteria:
  
- Each of the list screens should now show the correct number of things displayed.
- Each of the list screens should now show the correct entity displayed.
- This should work on all browsers.

\newpage

## Version 06 Stories

Release Aim: To modernise the GUI we need to start using javascript to make requests to the server rather than simple POST forms, start with create author using existing backend form functionality.

### Create an author via JavaScript

As a user I want an up to date GUI using JavaScript to create entities - specifically an author.


Acceptance Criteria:

- create an author uses JavaScript to post a form, rather than a page form submission
- no change to user experience
- this needs to work on all browsers

    
## Version 07 Stories

Release Aim: To derisk API usage, we will get the Create Author functionality working via an API using Ajax XHTTP calls.

### Add a Create Author API

As a user I Want to be able to use an API to create authors.

Bob says: really? our users want to use an API. When did that happen?    

Technical notes

- POST /authors

Acceptance Criteria:

- API exists
- API requires X-API-AUTH header
- 401 unauthorised
- 201 created
- 400 if failed validation, response has error message

### Create Author form uses API

As a user I want the API to be called when I create an author using the GUI.

Acceptance Criteria:

- works on all browsers
- should be no change to user experience on GUI
    
\newpage

## Version 08 Stories

Release Aim: To derisk API usage, we will get the Search functionality working. This means actually creating a search API end point and calling it from the GUI. Also de-risk future use of Cookies and Local storage by using these for the search form. /gui/reports/books/search

### Add a Search API end point into the API

As a User I want to be able to use the API to search based on titles of books.

Tech notes:

- GET /api/books?searchterm=SEARCH_TERM_ENTERED

Acceptance Criteria:

- end point for GET for search exists and works
- JSON with all book details for matching titles returned
- API documented

### Search form calls the Search API

As a User I want the search form to use the API.

Acceptance Criteria:

- same user experience for search
- works on all browsers

### LocalStorage used to store historic searches

As a user I want a list of historic searches on screen so I need my historic searches stored in the browser.

Implementation details: after discussion we decided to use a CSV list in local storage rather than cookies so cookies will be untouched.

Acceptance Criteria:

- CSV of historic searches stored in the local storage
- historic searches should not contain duplicate searches
- store searches in order of usage

### Historic searches shown on the search screen

As a user I want to be able to "replay" my historic searches so historic searches should be shown on the search screen when I search.

Acceptance Criteria:

- Historic searches should be shown on the search screen
- Searches should be clickable to allow me to repeat the search

    
\newpage

## Version 09 Stories

Release Aim: Extended use of the API from the GUI to all the Create, Update and Delete functionality for all the entities. To reduce the risk that the GUI interface differently from the API and we can 'test' less because we know the API is being used, not a form post.

### Create actions should use API

As a user I want the API to be used so that the application is more like a modern web application than an old web application because I don't like page re-loads.

Entities:

- Book
- Author
- Publisher
- Series

Acceptance Criteria:

- page does not reload when Created
- API call is used to create entity
- no change to user experience via GUI
- Works on all browsers

### Delete actions should use API

As a user I want the API to be used for deleting so that the application is more like a modern web application than an old web application because I don't like page re-loads.

Entities:

- Book
- Author
- Publisher
- Series

Acceptance Criteria:

- page does not reload when Deleted on 'amend' page
- page does not reload when Deleted on 'list' page
- no change to user experience via GUI
- API call is used to create entity
- Works on all browsers

### Update actions should use API

As a user I want the API to be used for updating so that the application is more like a modern web application than an old web application because I don't like page re-loads.

Entities:

- Book
- Author
- Publisher
- Series

Acceptance Criteria:

- page does not reload when Deleted on 'amend' page
- page does not reload when Deleted on 'list' page
- no change to user experience via GUI
- API call is used to create entity
- Works on all browsers

\newpage

## Version 10 Stories

Release Aims: Fix the API usage and bugs introduced.
 
NOTE: the last retrospective showed that we are not asking enough questions during the story planning so too many ambiguities and edge cases slipped through. The last release overran by a week so we need this one to be tight.

### Delete on Amend Form should prompt for are you sure

As a user, when I delete a book, or author, etc. from an Amend Form, I should be prompted to check if I am sure.

Entities:

- Book
- Author
- Publisher
- Series

Acceptance Criteria:

- prompt appears when click delete and only delete if answer Yes
- This works on all browsers

### Delete on Amend Form should not encourage further interaction

As a user, when I delete a book, or author, etc. from an Amend Form, I should not appear to be able to edit or 'delete again', the entity.

Entities:

- Book
- Author
- Publisher
- Series

Acceptance Criteria:

- All entities listed have minimal GUI after deletion.
- This works on all browsers
- An appropriate message for each entity is displayed after deletion. eg. "Deleted Book"

Question from bob: I get why we want this, but this isn't a user story, this is a tech story. What is the user benefit from this?

### Delete from publisher list

As a user, when I click delete on a publisher, it should actually delete the publisher, and prompt me if I want to delete the publisher.

### Amend Series amends series

As a user, when I amend the name of a series, I Want it to amend the series, not a publisher.

Acceptance Criteria:

- series is amended when I amend a series
- publisher is not amended when I amend a series


### Delete updates Count in List

As a user, when I delete an item from the list view, the item is deleted, and the "Showing All X" at the bottom of the list is updated to show the current count.

Acceptance Criteria:

- count is updated to the current count of entities


### Amend Book Via API

As a user when I amend a book I do not want the page to update, I want the app to use the API.

Question from bob: are we sure this is a "user" story, it seems like a tech story

Implementation notes:

POST to https://thepulper.herokuapp.com/apps/pulp/api/books

Acceptance Criteria:

- No change to User experience, GUI should update with a message
- API used to make the update
- Works on all browsers



\newpage

# Version Simple Exercises

For all versions the following exercises apply:

- Explore the application and see what you find
- Use the release notes to target your testing, do not assume the release notes are complete
- Identify any ambiguities and presuppositions from the release notes or your model of the application.
- Build a model of the application
- Use supporting observation tools to help you understand the functionality
- Try to automate the application - tactically
- Try to automate the application - strategically
- Identify tools appropriate to the technology used in the application

The sections which follow describe version specific suggested exercises.

You can use the versions in any order and switch between them as the application is running without losing any data.

## Version 001 Exercises

- use the app, model it, and perform exploratory testing, take notes as you go
- documentation coverage - does the documentation match the application? anything missing or incorrect?
- help coverage - does the help file in the app match the app?
- take note of the URLs as you use the application
    - change the parameters in the urls, does the application handle 'invalid' parameters well?
    - try changing the urls and see if you can find 'hidden' or unreleased functionality            

## Version 002 Exercises

- explore the CRUD functionality for each entity (Book, Series, Year, Author, Publisher)
    - can we create each entity?
    - can we delete each entity?
    - can we amend each entity?
    - can we read each entity correctly after amendment?
    - is there appropriate validation?
    
- explore the experimental 'filtering' functionality in the admin menu, which parts could we use in the main application? Which parts need work?

## Version 003 Exercises

- do all the FAQs work well enough?
- do the drop down menus match the main menus?
- responsive and adaptive web design
    - does the hamburger menu work effectively?
    - is the site responsive enough?
- automating
    - are there enough automation hooks?
    - are any parts of the application hard to automate?    
    
## Version 004 Exercises

- do all delete links have confirmation?
- do all amend links work effectively?

## Version 005 Exercises

- exploratory test reporting and list views and check that the 'show all x' works as expected

## Version 006 Exercises

- exploratory test the Create Author screen. This is experimental code. Any issues? Any Risks?
- can you conduct a code review on the JavaScript? Any risks or issues with this?
- is this easier or harder to automate? Any risks with this approach going forward in terms of automating?
- do you need any extra tools to help with this?
    - Browser Dev Tools? Proxy Tools? How might they change your testing?

## Version 007 Exercises

- exploratory test the Create Author screen. This is experimental code. Any issues? Any Risks?
- can you conduct a code review on the JavaScript? Any risks or issues with this?
- is this easier or harder to automate? Any risks with this approach going forward in terms of automating?
- do you need any extra tools to help with this?
    - Browser Dev Tools? Proxy Tools? How might they change your testing?
- what risks does using the API vs Form submission change?

## Version 008 Exercises

- exploratory test the Search screen. This is experimental code. Any issues? Any Risks?
- can you conduct a code review on the JavaScript? Any risks or issues with this?
- is this easier or harder to automate? Any risks with this approach going forward in terms of automating?
- do you need any extra tools to help with this?
    - Browser Dev Tools? Proxy Tools?
    - API GUI Tools?
    - How might they change your testing?
    
## Version 009 Exercises

- exploratory test the Search screen. This is experimental code. Any issues? Any Risks?
- can you conduct a code review on the JavaScript? Any risks or issues with this?
- is this easier or harder to automate? Any risks with this approach going forward in terms of automating?
- Can you test at the API level?
- do you need any extra tools to help with this?
    - Browser Dev Tools? Proxy Tools?
    - API GUI Tools?
    - How might they change your testing?
   
## Version 010 Exercises

- exploratory test the Book, Series and list screens. Any issues? Any Risks?
- can you conduct a code review on the JavaScript? Any risks or issues with this?
- is this easier or harder to automate? Any risks with this approach going forward in terms of automating?
- Can you test at the API level?
- do you need any extra tools to help with this?
    - Browser Dev Tools? Proxy Tools?
    - API GUI Tools?
    - How might they change your testing? 