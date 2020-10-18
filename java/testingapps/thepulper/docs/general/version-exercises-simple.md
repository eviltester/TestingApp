
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