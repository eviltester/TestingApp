\newpage

# Appendix - Known bugs

This section has spoilers. You may want to find these bugs yourself by testing the application first.

\newpage

## v001

- what is the point of the 'link to book as list' from the book entity screen? Would it be better to link to the 'books' list?
- The list footer text "Showing All X books." does not change based on the list or menu shown e.g. on 'series' it should probably say "Showing All 3 Series", but instead says 'books' and the number of books in memory.
    - Odd that the number changes, e.g. it is "Showing All 0 books." until the list or table of books is shown, then it becomes "Showing All 364 books." This might imply some 'state' which is bleeding over into the list.
- 404 page has no information, and no menu e.g. https://thepulper.herokuapp.com/apps/pulp/bob
- publisher, books, series and years faqs link in list can be triggerd by adding apps/pulp/gui/reports/publishers/list/navigation?faqs (is this intended or should this be inaccessible)
- the list of books for a year can be converted into a table by changing the url - is that intended?
- search for "" with confirm searches selected asks "Do you want to search" before responding with 'you must enter a search term' - better to do that check prior to asking if want to search
- there should be validation on the search term length as it is possible to trigger a 414 - URI too long - from the GUI
- search results does not show a "Showing X books." message at the bottom of the list, which would be helpful to see how many match
- the reports do not have a printable CSS to change the formatting so it is not really a 'good' report
- the reports have the "Showing All 364 books" bug as mentioned earlier
- the drop down menus overlap and don't show properly on a small screen, the app needs to be more responsive in its layout.

## v002

fixed issue with drop down menu overlap

- after adding a book, it could be useful to have a link to amend the book to allow adding a house author
- after deleting an author and going 'back' I see "Cannot amend an unknown author"
- amending an author, but not changing details says "Author name amended", but no changes took place
- after creating a book, and viewing the book details is says "No House Author Assigned" but this is a clickable link to author -1, suspect this should not be a clickable link
- when I amend a book with no house author, the house authors list shows 'all' the authors, it should only show house authors
- house author is shown twice in the view book screen

## v003

fixed house author shown twice

- help file has bug where version change "Books FAQ page accessible from drop down menu" is listed twice
- the series FAQ list has a template error so one of the FAQs does not work _Is "!!name!"! pulp available to download as pdf?_
- the title of every FAQs page is "Authors with FAQs"
- menu titles are inconsistent e.g. mix of FAQs and FAQ "Years > FAQs" and "Series > FAQ"

## v004

fixed faq series template error

- deleting a house author, deletes all books for author, even if other authors on the book, I don't think we should be doing this, we are allowed to have no house author on a book, but we are not allowed to create a book with no authors.
- cannot delete book from list or table, only on author, publisher and series (book might have been a copy and past error in the story)

## v005

fixed bug title of FAQs page

- help content seems to have misleading information i.e. "GUI Uses Form Submissions" the [x] delete form submission was added in version 4
- there is a bug with the house author deletion, if you delete the house author before deleting the last author then you can create a book with no authors

## v006

* user experience has changed, previously the "Added Author" was styled as an h2, now it is text `p` with an id of `outputmessage` - story says "no change to user experience"

## v007

* error message when adding a duplicate author from GUI has changed, i.e. it used to be "Could not add author" now it is "Error: Cannot create author. Author 'bob' already exists with id 25." - story says "should be no change to user experience on GUI"

## v008

* the history links on search result in a form based GET search, not the API search so the results look very different and the history is not shown
* same with the full search link on the Ajax search results, this does a GET search, there is no way to do a GET search from the GUI other than this route.
* the search results rendering from the Ajax search is different from the GET Search i.e. only title is shown, but GET search had links for author, series etc.
* local storage is a CSV list but the csv list starts with a ','
* the search history is only shown after doing a search, surely it should be shown when we visit the page to allow us to repeat a search at the point we want to do a search
* if I search for "[null],death" then it is possible to break the rendering of the search history because this is shown as two separate searches "[[null]" and "death]"
* why are so many cookies created? when I do a search there are JSESSIONID created for "/", "/apps/pulp/api", and "/apps/pulp/api/books" I think we should only have one for "/"
* I thought the release aim mentioned using Cookies - did we add any functionality around cookies or just a bug?
* API is not really a search API, it is more like a filter on the book title - are we sure that we implemented this correctly? If it was going to be a filter then shouldn't the argument be ?title= or possibly title~= for approximately = (I'm not sure how search is normally implemented in an API)


## v009

* delete on the amend forms should probably remove the update buttons or redirect to the list
* we lost the confirm that you want to delete a book, author, series, etc. in the amend form for delete
* forgot to add the output message on series form api usage
   *  bonus points, and if we had it would say "Deleted Publisher" instead of "Deleted Series|
* publisher list insitu removal on delete has been commented out in the code
* did not add the amend book via javascript so the form uses a post
* series amend form via api amends a publisher, not a series
* list totals do not update when removing a list item
* on the Amend Author screen we lost the 'View Author' and 'List of Books by Author' on the Amend Author screen
* on the Amend Book screen we lost the "View Book" and "Book As List" links
* on the Amend Publisher screen we lost the "View Publisher" and "List of Books by Publisher" links
* on the Amend Series screen we lost the "View Series" and "List of Books in Series" links
* if session expires then GUI will report an invalid X-API-AUTH session when using API calls from GUI, user has to refresh to get a new session, but that isn't obvious "Error: X-API-AUTH header is invalid - check in the GUI"
* delete house author - deletes all books associated with author - regression from v5 fix
* house author shown twice in view screen again

## v010

fixed

* delete on the amend forms removes the form details after deletion
* confirm deletion from form - that you want to delete a book, author, series, etc.
* publisher list insitu delete works
* series amend form via api amends series
* update number in list when delete an item
* amend book via javascript
* delete house author


* there is still a bug in v10 where it does not update authors properly because it does not remove any authors - but the #form version does. Might need to use a PUT for this.
* a book can have multiple authors but the API call does not support this, only a single author is provided. This means we can put the book in a state with multiple authors we we cannot remove authors from the book.
* current count shown at bottom of list when item is deleted, is updated for the numbers shown on screen, not the actual numbers on the server i.e. multi-window delete shows wrong list of items in list and wrong count - list refresh is not performed from server, just a delete request issued.
* there is a bug with the house author deletion, if you delete the house author before deleting the last author then you can create a book with no authors (present since v 5), in v10 this is shown as a failed ajax call as well as book display
* there is a bug with the book author deletion, if we delete the last author, but there is still a house author then book will be deleted. It should probably be valid to have a book with just a house author.

## v011

fixed

* delete house author prior to deleting last author
* deleting last author, but still with house author does not delete the book
* can remove authors when editing as now use a PUT request, and API call sends multiple authors
* house author should not be shown twice in the view screen