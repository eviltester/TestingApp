package com.javafortesters.pulp.api;

import com.google.gson.Gson;
import com.javafortesters.pulp.api.entities.lists.BooksListEntity;
import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.api.entities.lists.AuthorListEntity;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;

public class PulpEntities {
    private final PulpData bookdata;

    public PulpEntities(final PulpData books) {
        this.bookdata = books;
    }

    public EntityResponse getAuthor(final String authorid, final String acceptformat) {
        final PulpAuthor author = bookdata.authors().get(authorid);
        final EntityResponse response = new EntityResponse();

        if(author==PulpAuthor.UNKNOWN_AUTHOR){
            response.setErrorStatus(404, String.format("Author %s not found", authorid));
            return response;
        }

        AuthorEntity entity = new AuthorEntity(author.getId(), author.getName());

        response.setSuccessStatus(200,new Gson().toJson(entity));
        return response;
    }

    public EntityResponse getAuthors(final String acceptformat) {
        final EntityResponse response = new EntityResponse();

        AuthorListEntity entity = new AuthorListEntity(bookdata.authors());

        response.setSuccessStatus(200,new Gson().toJson(entity));
        return response;
    }

    public EntityResponse getBooks(final String acceptFormat) {
        final EntityResponse response = new EntityResponse();

        BooksListEntity entity = new BooksListEntity(bookdata);

        response.setSuccessStatus(200,new Gson().toJson(entity));
        return response;

    }
}
