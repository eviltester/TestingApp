package com.javafortesters.pulp.api.actions;

import com.google.gson.Gson;
import com.javafortesters.pulp.api.DomainToEntityConvertor;
import com.javafortesters.pulp.api.EntityResponse;
import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.api.entities.single.BookEntity;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;

import java.util.ArrayList;
import java.util.List;

public class ActionProcessor {

    private final PulpData bookdata;
    private final DomainToEntityConvertor convertor;
    private final String rooturl;

    public ActionProcessor(final PulpData bookdata, final DomainToEntityConvertor convertor, final String rooturl) {
        this.bookdata = bookdata;
        this.convertor = convertor;
        this.rooturl = rooturl;
    }

    public EntityResponse process(final ActionToDo action) {

        String exceptionMessage = "";

        try{
            if(action.errorMessage!=null){
                EntityResponse response = new EntityResponse().setErrorStatus(action.errorStatus, action.errorMessage);
                for(String headerKey : action.headers.keySet()){
                    response.addHeader(headerKey, action.headers.get(headerKey));
                }
                return response;
            }

            if(action.actionName.contentEquals("CREATE")){
                if(action.authorEntityToActOn!=null){
                    final PulpAuthor actualAuthor = bookdata.authors().add(action.authorEntityToActOn.name);
                    action.actualAuthor = actualAuthor;
                    EntityResponse response = new EntityResponse().setSuccessStatus(201, convertor.toJson(actualAuthor));
                    response.addHeader("location", getLocationHeaderFor(actualAuthor));
                    return response;
                }
                if(action.seriesEntityToActOn!=null){
                    final PulpSeries created = bookdata.series().add(action.seriesEntityToActOn.name);
                    action.actualSeries = created;
                    EntityResponse response = new EntityResponse().setSuccessStatus(201, convertor.toJson(created));
                    response.addHeader("location", getLocationHeaderFor(created));
                    return response;
                }
                if(action.publisherEntityToActOn!=null){
                    final PulpPublisher created = bookdata.publishers().add(action.publisherEntityToActOn.name);
                    action.actualPublisher = created;
                    EntityResponse response = new EntityResponse().setSuccessStatus(201, convertor.toJson(created));
                    response.addHeader("location", getLocationHeaderFor(created));
                    return response;
                }
                if(action.bookEntityToActOn!=null){
                    return processCreateAmendBookAction(action);
                }

            }
            if(action.actionName.contentEquals("AMEND")){
                if(action.authorEntityToActOn!=null){
                    final PulpAuthor actual = bookdata.authors().get(action.authorEntityToActOn.id);
                    actual.amendName(action.authorEntityToActOn.name);
                    action.actualAuthor=actual;
                    EntityResponse response = new EntityResponse().setSuccessStatus(200, convertor.toJson(actual));
                    return response;
                }
                if(action.seriesEntityToActOn!=null){
                    final PulpSeries actual = bookdata.series().get(action.seriesEntityToActOn.id);
                    actual.amendName(action.seriesEntityToActOn.name);
                    action.actualSeries=actual;
                    EntityResponse response = new EntityResponse().setSuccessStatus(200, convertor.toJson(actual));
                    return response;
                }
                if(action.publisherEntityToActOn!=null){
                    final PulpPublisher actual = bookdata.publishers().get(action.publisherEntityToActOn.id);
                    actual.amendName(action.publisherEntityToActOn.name);
                    action.actualPublisher=actual;
                    EntityResponse response = new EntityResponse().setSuccessStatus(200, convertor.toJson(actual));
                    return response;
                }
                if(action.bookEntityToActOn!=null){
                    return processCreateAmendBookAction(action);
                }
            }
            if(action.actionName.contentEquals("REPLACE")){
                if(action.bookEntityToActOn!=null){
                    return processCreateAmendBookAction(action);
                }
            }
            if(action.actionName.contentEquals("PATCH")){
                if(action.bookEntityToActOn!=null){
                    return processCreateAmendBookAction(action);
                }
            }
        }catch(Exception e) {
            exceptionMessage = e.getMessage();
        }

        return new EntityResponse().setErrorStatus(500, "Could not process action : " + new Gson().toJson(action) + " " + exceptionMessage);


    }

    private EntityResponse processCreateAmendBookAction(final ActionToDo action) {

        final BookEntity book = action.bookEntityToActOn;

        PulpPublisher publisher = null;
        if(book.publisher != null) {
            if (book.publisher.id == null) {
                publisher = bookdata.publishers().findByName(book.publisher.name);
            } else {
                publisher = bookdata.publishers().get(book.publisher.id);
            }

            if(action.actionName.contentEquals("PATCH")){
                if (publisher == null || publisher == PulpPublisher.UNKNOWN_PUBLISHER) {
                    return new EntityResponse().setErrorStatus(404, String.format("Cannot find publisher %s named: %s", book.publisher.id, book.publisher.name));
                }
            }
        }

        PulpSeries series=null;
        if(book.series != null) {
            if (book.series != null && book.series.id == null) {
                series = bookdata.series().findByName(book.series.name);
            } else {
                series = bookdata.series().get(book.series.id);
            }

            if(action.actionName.contentEquals("PATCH")){
                if (series == null || series == PulpSeries.UNKNOWN_SERIES) {
                    return new EntityResponse().setErrorStatus(404, String.format("Cannot find series %s named: %s", book.series.id, book.series.name));
                }
            }

        }


        List<PulpAuthor> authors = new ArrayList<PulpAuthor>();
        List<String> authorIds = new ArrayList<>();

        if(book.authors!=null) {
            for (AuthorEntity anAuthor : book.authors) {

                PulpAuthor author;
                if (anAuthor.id == null) {
                    author = bookdata.authors().findByName(anAuthor.name);
                } else {
                    author = bookdata.authors().get(anAuthor.id);
                }

                if (author == null || author == PulpAuthor.UNKNOWN_AUTHOR) {
                    return new EntityResponse().setErrorStatus(404, String.format("Cannot find author %s named: %s", anAuthor.id, anAuthor.name));
                }

                authors.add(author);
                authorIds.add(author.getId());
            }
        }

        if(action.actionName.contentEquals("CREATE")) {

            if(publisher==null || publisher==PulpPublisher.UNKNOWN_PUBLISHER){
                return new EntityResponse().setErrorStatus(400, String.format("Cannot find publisher %s named: %s", book.publisher.id, book.publisher.name));
            }

            if(series==null || series==PulpSeries.UNKNOWN_SERIES){
                return new EntityResponse().setErrorStatus(400, String.format("Cannot find series %s named: %s", book.series.id, book.series.name));
            }

            if(authors.size()==0){
                return new EntityResponse().setErrorStatus(400, String.format("Cannot create a book without authors"));
            }

            final PulpBook actualBook = bookdata.books().add(series.getId(), authors.get(0).getId(), "", book.title, book.seriesId, book.publicationYear, publisher.getId());

            for (PulpAuthor addAuthor : authors) {
                actualBook.addCoAuthor(addAuthor.getId());
            }

            action.actualBook = actualBook;
            return new EntityResponse().
                            setSuccessStatus(201, convertor.toJson(actualBook)).
                            addHeader("location", getLocationHeaderFor(actualBook));

        }

        if(action.actionName.contentEquals("AMEND") || action.actionName.contentEquals("REPLACE") || action.actionName.contentEquals("PATCH")) {

            if(publisher!=null && publisher==PulpPublisher.UNKNOWN_PUBLISHER){
                return new EntityResponse().setErrorStatus(400, String.format("Cannot find publisher %s named: %s", book.publisher.id, book.publisher.name));
            }

            if(series!=null && series==PulpSeries.UNKNOWN_SERIES){
                return new EntityResponse().setErrorStatus(400, String.format("Cannot find series %s named: %s", book.series.id, book.series.name));
            }

            final BookEntity bookDetails = action.bookEntityToActOn;

            if(bookDetails.authors!=null && authorIds.size()==0){
                return new EntityResponse().setErrorStatus(400, String.format("Cannot remove all authors"));
            }

            final PulpBook actualBook = bookdata.books().get(action.bookEntityToActOn.id);

            actualBook.amendTitle(bookDetails.title);

            if(bookDetails.publicationYear!=null && bookDetails.publicationYear>0) {
                actualBook.amendPublicationYear(String.valueOf(bookDetails.publicationYear));
            }

            if(publisher!=null) {
                actualBook.amendPublisher(publisher.getId());
            }

            if(series!=null) {
                actualBook.amendSeries(series.getId());
            }

            if(action.actionName.contentEquals("AMEND")) {
                actualBook.amendPatchAuthors(authorIds);
            }else{ //  if action.actionName.contentEquals("REPLACE")
                actualBook.amendAuthors(authorIds);
            }

            actualBook.amendSeriesIdentifier(bookDetails.seriesId);

            action.actualBook = actualBook;
            return new EntityResponse().setSuccessStatus(200, convertor.toJson(actualBook));
        }

        return new EntityResponse().setErrorStatus(500, String.format("Error processing action %s", new Gson().toJson(action)));
    }

    private String getLocationHeaderFor(final PulpBook existing) {
        return rooturl + "/books/" + existing.getId();
    }

    private String getLocationHeaderFor(final PulpAuthor existing) {
        return rooturl + "/authors/" + existing.getId();
    }

    private String getLocationHeaderFor(final PulpSeries existing) {
        return rooturl + "/series/" + existing.getId();
    }

    private String getLocationHeaderFor(final PulpPublisher existing) {
        return rooturl + "/publishers/" + existing.getId();
    }
}
