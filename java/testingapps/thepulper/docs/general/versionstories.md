
## Version 001 Stories

Version 1 was a prototype, this wasn't done as an Agile process so we just implemented a bunch of stuff which you can see in the release notes.

\newpage

## Version 002 Stories

Release Aim: Create, Amend and Delete Entities so we can actually release this to the users.

### CRUD Functionality

As a user I want to be able to maintain the entities which means Create, Read, Update and Delete.

Entities:

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

### Additional FAQs Functionality

As a user I want to be able to see FAQs for Publishers, Series, Years, and on book details.

Entities:

- year
- series
- publishers
- authors
- books

Acceptance Criteria:

- There should be a an FAQ menu with clickable FAQ links for each entity
- The Books FAQs should show FAQs for each of the fields in the book i.e. title, each author, year, publisher, and series  


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

### FAQ Page Title

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

