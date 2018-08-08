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
import com.javafortesters.pulp.html.gui.entitycrud.viewPages.ViewAuthorPage;
import com.javafortesters.pulp.html.gui.entitycrud.viewPages.ViewBookPage;
import com.javafortesters.pulp.html.gui.entitycrud.viewPages.ViewPublisherPage;
import com.javafortesters.pulp.html.gui.entitycrud.viewPages.ViewSeriesPage;
import com.javafortesters.pulp.html.templates.FilledHTMLTemplate;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;

public class AppPages {
    private final PulpData books;
    private final AppVersion appversion;

    public AppPages(final PulpData theBooks, AppVersion appversion) {
        this.books = theBooks;
        this.appversion = appversion;
    }


    /**
     * Create delegates off to Create pages
     *
     * @return
     */
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
     * View Pages
     *
     * @param seriesId of the thing to view
     * @return
     */

    public ViewSeriesPage viewSeriesPage(final String seriesId) {
        final PulpSeries series = this.books.series().get(seriesId);
        final ViewSeriesPage page = new ViewSeriesPage(series, appversion);
        if(series == PulpSeries.UNKNOWN_SERIES){
            page.setOutput(new FilledHTMLTemplate(appversion).error("ERROR: Series not found"));
        }

        return new ViewSeriesPage(series, appversion);
    }

    public ViewPublisherPage viewPublisherPage(final String id) {
        final PulpPublisher publisher = this.books.publishers().get(id);
        final ViewPublisherPage page = new ViewPublisherPage(publisher, appversion);
        if(publisher == PulpPublisher.UNKNOWN_PUBLISHER){
            page.setOutput(new FilledHTMLTemplate(appversion).error("ERROR: Publisher not found"));
        }

        return page;
    }

    public ViewAuthorPage viewAuthorPage(final String authorId) {
        final PulpAuthor author = this.books.authors().get(authorId);
        final ViewAuthorPage page = new ViewAuthorPage(author, appversion);
        if(author == PulpAuthor.UNKNOWN_AUTHOR){
            page.setOutput(new FilledHTMLTemplate(appversion).error("ERROR: Author not found"));
        }

        return page;
    }

    public ViewBookPage viewBookPage(final String id) {
        final PulpBook book = this.books.books().get(id);
        final ViewBookPage page = new ViewBookPage(books, book, appversion);
        if(book == PulpBook.UNKNOWN_BOOK){
            page.setOutput(new FilledHTMLTemplate(appversion).error("ERROR: Book not found"));
        }

        return page;
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
            page.setOutput(new FilledHTMLTemplate(appversion).error("ERROR: Author not found"));
        }

        return page;
    }

    public AmendSeriesPage amendSeriesPage(final String id) {
        final PulpSeries series = this.books.series().get(id);
        final AmendSeriesPage page = new AmendSeriesPage(series, appversion);
        if(series == PulpSeries.UNKNOWN_SERIES){
            page.setOutput(new FilledHTMLTemplate(appversion).error("ERROR: Series not found"));
        }

        return page;
    }

    public AmendPublisherPage amendPublisherPage(final String id) {
        final PulpPublisher publisher = this.books.publishers().get(id);
        final AmendPublisherPage page = new AmendPublisherPage(publisher, appversion);
        if(publisher == PulpPublisher.UNKNOWN_PUBLISHER){
            page.setOutput(new FilledHTMLTemplate(appversion).error("ERROR: Publisher not found"));
        }

        return page;
    }

    public AmendBookPage amendBookPage(final String id) {
        final PulpBook book = this.books.books().get(id);
        final AmendBookPage page = new AmendBookPage(books, book, appversion);
        if(book == PulpBook.UNKNOWN_BOOK){
            page.setOutput(new FilledHTMLTemplate(appversion).error("ERROR: Book not found"));
        }

        return page;
    }

    /*
            Other pages

     */

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
