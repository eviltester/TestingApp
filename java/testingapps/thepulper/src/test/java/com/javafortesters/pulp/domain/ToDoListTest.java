package com.javafortesters.pulp.domain;

public class ToDoListTest {

    // NEXT:
    //      help page
    //      ids in the dom to make automating less challenging - the aim is to support not challenge
    //      move as much out into templates as possible so that they can be versioned and have different looking apps with a version toggle

    // TODO: Basic GUI functionality
    // Can have a lot without login

    // # TODO: CREATE
    // - refactor create pages into single page with template

    // # AMEND


    // # DELETE
    // - incorporate into lists

    // # TODO: READ

    // Format footer to be a little nicer - possibly add a page header
    // put the filter tester behind an /admin url

    // Create an entity Read Page which will link to Amend
    // Entity Read page could show a filtered list of books published by, written by, in series, by date etc.
    // Book Read page chould have a list of the authors, publishers, series, dates etc. with links

    // TODO: fix paging bug to support authors, series, publishers etc. - currently just books
    // switch between 'easy to automate' (with ids) and 'hard to automate' (without ids)
    // TODO: Create a Frames view which has side frame for list and mainframe for report and top frame for search?

    // AJAX
//* Ajax - an Ajax end point (not an API that searches for items) and displays
//    * []As list []as table etc.
//* Form for ‘advanced’ search with drop downs etc.
//* Ajax based Form for advanced search
//* Could use Frames to render based on a set of searches
//    * e.g. Author, Publisher, data range etc.


    // TODO: allow user configuration of the search engines: google, bing, duckduckgo etc.
    // TODO: for test filter page add [author], [series], [year], [publisher] as drop downs
    // TODO: CSS for styling - swap in/out for different versions

    // TODO: Table Column sort by DOM using Javascript
    // TODO: filter sort by direct page load call
    // TODO: filter sort by AJAX

    // Report for Author: name, list of "published by" pbulishers, "wrote series" list of series, "tiltes", list of books authrored, FAQS - all on one page
    // CRUD create your own reportes - templates columns, styles

    // TODO: add sorting button on the series, authors and publishers list pages


    // # TODO: App Versioning

    // TODO: Add more page templates to allow easier 'app updating via version number'

    // TODO: create rendering objects
    // TODO: add names, ids, classes etc. to rendered items

    // # TODO: API
    // - try and add thingifier as the backend then get a lot of this for free
    // TODO: Basic API
    // TODO: add report classes to use for JSON and XML serialisation
    // TODO: create a list of basic end points and add methods to support end point reporting
    // TODO: add a basic REST API for get requests and reporting

    // # TODO: GUI version which is API Powered
    // TODO: add a simple GUI that uses the REST API for reporting the books
    // TODO: add an AJAX based GUI that uses the REST API for reporting the books


    // TODO: can READ and get without authentication
    // TODO: add a single admin user hard coded
    // TODO: can see an admin interface when logged in
    // TODO: expand back end for delete, replace, partial amend - require admin user permissions and authentication
    // TODO: books.authors().delete(1) - delete all books from that author
    // TODO: add other users
    // TODO: add permissions on entities, created-by, owner
    // TODO: amendment based on permissions of user
    // TODO: the static books reader and collection classes were created with a high level exploratory test - not low level TDD, really neeed class specific tests on the collections
    //       20180415 bugs that slipped through because of this - hard coded paths, not reading books as unique books - i.e. every book was first in csv file

    // TODO create unit tests for each of the pages and apps routings
    // TODO: Create Web Tests for the pages


    // 20180731 Done
    // - create Book - which has drop down of authors, publishers and series
    // - added a link to the list of books for the created thing when created
    // - added amendment for books, authors, series, and publisher
    //  add an edit screen for each of the things e.g. edit author, edit book (allow house author amendment), edit publisher, edit series

    // 20180802 DONE
    // - allow amending authors through a multi select drop down
    // initially add the DELETE on the Edit screen
    // - add an are you sure? dialog on delete button press
    // Search should have links

    //20180803 DONE
    //     make it look like a simple app - this will probably require new templates and 'refactoring' of existing old code
    //      common header and footer
    //      css styling
    //      index page
    //      Menu: Books | Authors > List, FAQ | Publishers | Series | Years | Search | Create > Author, Series, Publisher, Book | Reports > Books, Authors, Publishers, Series, Years | Admin > Filter
    //      Footer: copyright, links to sites, contact
}
