package com.javafortesters.pulp.html.gui;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;
import com.javafortesters.pulp.html.gui.entitycrud.createPages.CreateAuthorPage;
import com.javafortesters.pulp.html.gui.entitycrud.createPages.CreateBookPage;
import com.javafortesters.pulp.html.gui.entitycrud.createPages.CreateSeriesPage;
import com.javafortesters.pulp.html.gui.entitycrud.createPages.CreatePublisherPage;
import com.javafortesters.pulp.html.gui.entitycrud.updatePages.AmendAuthorPage;
import com.javafortesters.pulp.html.gui.entitycrud.updatePages.AmendBookPage;
import com.javafortesters.pulp.html.gui.entitycrud.updatePages.AmendPublisherPage;
import com.javafortesters.pulp.html.gui.entitycrud.updatePages.AmendSeriesPage;

public class AppPages {
    private final PulpData books;
    private final String appversion;

    public AppPages(final PulpData theBooks, String appversion) {
        this.books = theBooks;
        this.appversion = appversion;
    }

    public CreateBookPage createBookPage() {
        return new CreateBookPage(books, appversion);
    }

    public CreateAuthorPage createAuthorPage() {
        return new CreateAuthorPage(appversion);
    }

    public CreatePublisherPage createPublisherPage() {
        return new CreatePublisherPage(appversion);
    }

    public CreateSeriesPage createSeriesPage() {
        return new CreateSeriesPage(appversion);
    }

    /**
     * Given an author index we try to return a page with that author
     * If author is not found then UNKNOWN author will be used.
     *
     * @param authorId
     * @return
     */
    public AmendAuthorPage amendAuthorPage(String authorId) {

        final PulpAuthor author = this.books.authors().get(authorId);
        final AmendAuthorPage page = new AmendAuthorPage(author, appversion);
        if(author == PulpAuthor.UNKNOWN_AUTHOR){
            page.setOutput("<h2>ERROR: Author not found</h2>");
        }

        return page;
    }

    public AmendSeriesPage amendSeriesPage(final String id) {
        final PulpSeries series = this.books.series().get(id);
        final AmendSeriesPage page = new AmendSeriesPage(series, appversion);
        if(series == PulpSeries.UNKNOWN_SERIES){
            page.setOutput("<h2>ERROR: Series not found</h2>");
        }

        return page;
    }

    public AmendPublisherPage amendPublisherPage(final String id) {
        final PulpPublisher publisher = this.books.publishers().get(id);
        final AmendPublisherPage page = new AmendPublisherPage(publisher, appversion);
        if(publisher == PulpPublisher.UNKNOWN_PUBLISHER){
            page.setOutput("<h2>ERROR: Publisher not found</h2>");
        }

        return page;
    }

    public AmendBookPage amendBookPage(final String id) {
        final PulpBook book = this.books.books().get(id);
        final AmendBookPage page = new AmendBookPage(books, book, appversion);
        if(book == PulpBook.UNKNOWN_BOOK){
            page.setOutput("<h2>ERROR: Book not found</h2>");
        }

        return page;
    }

    public FaqRenderPage getFaqRenderPage(final String typeOfFaq, final String forTerm, final boolean showiframe) {
        return new FaqRenderPage(typeOfFaq, forTerm, showiframe, appversion);
    }

    public AlertSearchPage getAlertSearchPage() {
        return new AlertSearchPage(appversion).setDataFrom(books);
    }

    public FilterTestPage getFilterTestPage(final boolean isList, final boolean navigation, final boolean canSearch, final boolean isPaginated) {
        return new FilterTestPage(isList, navigation, canSearch, isPaginated, appversion).setData(books);
    }
}
