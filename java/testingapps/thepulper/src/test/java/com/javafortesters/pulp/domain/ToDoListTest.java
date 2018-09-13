package com.javafortesters.pulp.domain;

public class ToDoListTest {

    // NEXT:

    //
    // create a v 5 and add animation to the drop down menu reveal to make it harder to automate
    //
    //  Allow creating custom versions
    //      via JSON API
    //      via loading from file in app
    //      via admin form
    //      via CLI interface
    //
    //  v3
    //      add faqs to the view page
    //      have the [amend][x][i] only appear in a list when filtered to 1 item
    //      re-enable and test the iframe on faqs
    //      have an admin option to configure which search engine is used for the faqs
    //      admin option to configure pagination settings
    //      add a config option to allow lists to link to list or view or amend, etc.
    //      add an [i] info screen where we can 'read' the information for the thing
    //      have an [i] link in the tables - configurable along with [amend]
    //      add pagination to lists

    // TODO: Basic GUI functionality
    // Can have a lot without login

    // # TODO: CREATE
    // - refactor create pages into single page with template

    // # AMEND

    // # DELETE

    // # TODO: READ
    // Entity Read page could show a filtered list of books published by, written by, in series, by date etc.

    // TODO: fix paging bug to support authors, series, publishers etc. - currently just books
    // TODO: Create a Frames view which has side frame for list and mainframe for report and top frame for search?

    // AJAX
//* Ajax - an Ajax end point (not an API that searches for items) and displays
//    * []As list []as table etc.
//* Form for ‘advanced’ search with drop downs etc.
//* Ajax based Form for advanced search
//* Could use Frames to render based on a set of searches
//    * e.g. Author, Publisher, data range etc.


    // TODO: allow user configuration of the search engines: google, bing, duckduckgo etc. - in admin
    // TODO: for test filter page add [author], [series], [year], [publisher] as drop downs


    // TODO: Table Column sort by DOM using Javascript
    // TODO: filter sort by direct page load call
    // TODO: filter sort by AJAX

    // Report for Author: name, list of "published by" pbulishers, "wrote series" list of series, "tiltes", list of books authrored, FAQS - all on one page
    // CRUD create your own reportes - templates columns, styles

    // TODO: add sorting button on the series, authors and publishers list pages

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
    // Format footer to be a little nicer - possibly add a page header
    // put the filter tester behind an /admin url
    // help page and shutdown menu item with alert
    // moved all templates under v001/page-template to allow changing versions later

    //20180804 DONE
    //      create mechanism to choose v1.1 or v1.2 from the admin drop down
    //      have version toggle internally

    //20180805 DONE
    //      ids in the dom to make automating less challenging - the aim is to support not challenge
    //      target internal HTML generation next - move as much out into templates as possible so that they can be versioned and have different looking apps with a version toggle
    //      have template field !!Version!! in the help file
    //      re-instated faq for all entities and made list rendering a little more configurable, added some year FAQs and some more FAQs

    //20180806 DONE
    // incorporate DELETE into lists for version 002

    //20180808 DONE
    // create a READ page for Series, Author, Publisher, Book and link these to/from the edit screen, and add link to list of books from both view and edit screen
    //      with links to the related things eg. books published by, faq etc.
    //      have the names in tables and lists link to the view screen instead of the list screen
    //  AppVersion should be an object
    //  convert code to a AppVersion instead of contentEquals("v001")
    //  We can ask AppVersion .canDo(ACTION) and if it is avail in that version it will tell us
    //      convert code to use a .canDo(ACTION) AppVersion instead of currentVersionIs(1)
    //  created a VersionedResourceReader that will drop down through the versions looking for a resource -
    // currently implemented on help
    //      Roll out versioned reader and delete any templates where they are identical between versions
    //               - find new ResourceReader() and replace with VersionedResourceReader
    //      templates should have the ability to drop down to previous version if nothing for current version
    //          e.g. /v003/page-template/entity-crud/update/edit-book-series-content.html NotExist - check for v002 if exists, use, check for v001 if exist, use, pass back "" as fall through
    //
    //  Allow app to support versions more easily for planning features based on testing exercises
    //
    //  Create different versions
    //      e.g.
    //      v 1 - just a READ app so only lists and the lists link to View screens
    //      v 2 - CRUD app - basic CRUD APP
    //      v 3 - CRUD app that is easier to automate with HTML IDs - current v002
    //      v 4 - CRUD app - easier to automate with insitu amend and delete on Series, Author and Publisher lists

    //20180809 DONE
    //
    //      Create a text line for each capability and have the HELP page show the capabilities that are 'true'
    //      create a history list to add to the bottom of the help file
    //
    //  Tidy todo file to trim work done
    // Create an entity Read Page which will link to Amend
    // Book Read page should have a list of the authors, publishers, series, dates etc. with links
    // CSS for styling - swap in/out for different versions
    // create rendering objects
    // add names, ids, classes etc. to rendered items



}
