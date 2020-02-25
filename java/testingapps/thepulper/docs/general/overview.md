# Overview

A simple overview of the app to support usage and workshops.

The application has multiple versions. Select a version using the admin drop down or admin menu screen. The 'help' menu shows high level changes for each version. This document describes them in more detail. Note this document may have 'spoilers' in terms of deliberate bugs to find etc.

"The Pulper" is a CRUD app (Create, Read, Update, Delete) for Pulp Magazine and book collections.

It comes pre-populated with data.

It should support multiple users, but this depends on what people do with it. Each user is given a unique session and the data is stored on a per user basis, so users should not be able to interfere with each other. This has the risk of consuming more memory, if many people use the application. Inactive sessions are removed after about 10 to 15 minutes of inactivity, and the user will be provided with a new session. 

- https://eviltester.com/thepulper
- https://thepulper.herokuapp.com/
- https://github.com/eviltester/TestingApp/releases

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

- View Pages
    - Entity Views
        - Book
            - shows each book details
            - has links to the publisher, year published, etc. view pages
            - link to book as list
        - Author
            - shows each author details
            - has a link to all books by the author
    - Entity FAQ Views (Author)
        - a list of questions for each entity, which link to a search engine search to answer the question e.g. What books did 'author' write?
        
        
## Version 001 Exercises

- use the app, model it, and perform exploratory testing, take notes as you go
- documentation coverage - does the documentation match the application? anything missing or incorrect?
- help coverage - does the help file in the app match the app?
- take note of the URLs as you use the application
    - change the parameters in the urls, does the application handle 'invalid' parameters well?
    - try changing the urls and see if you can find 'hidden' or unreleased functionality            



