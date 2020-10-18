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

Version 003 has automation hooks, responsive GUI features and more FAQs.

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

Fixed bug - deleting house author deletes books.

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
