package com.javafortesters.pulp.api.entities.single;

import com.javafortesters.pulp.api.entities.IncludeFieldNames;

import java.util.List;

public class PublisherEntity {
    public String id;
    public String name;

    public PublisherEntity(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public void includeOnlyFields(final IncludeFieldNames includeFieldNames) {

        List<String> fieldNames = includeFieldNames.getNames();

        if(!fieldNames.contains("id")){
            this.id=null;
        }

        if(!fieldNames.contains("name")){
            this.name=null;
        }
    }
}
