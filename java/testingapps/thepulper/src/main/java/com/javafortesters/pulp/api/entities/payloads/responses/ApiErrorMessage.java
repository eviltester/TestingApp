package com.javafortesters.pulp.api.entities.payloads.responses;

import com.javafortesters.pulp.api.entities.single.AuthorEntity;
import com.javafortesters.pulp.api.entities.single.BookEntity;
import com.javafortesters.pulp.api.entities.single.PublisherEntity;
import com.javafortesters.pulp.api.entities.single.SeriesEntity;

import java.util.ArrayList;
import java.util.List;

public class ApiErrorMessage {

    public List<String> messages;
    public AuthorEntity author;
    public BookEntity book;
    public SeriesEntity series;
    public PublisherEntity publisher;

    public void addMessage(final String errorMessage) {

        if(messages==null){
            messages = new ArrayList<String>();
        }
        messages.add(errorMessage);

    }
}
