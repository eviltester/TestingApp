package com.javafortesters.pulp.api.entities;

import java.util.Arrays;
import java.util.List;

public class IncludeFieldNames {
    private final List<String> names;

    public IncludeFieldNames(final String ... fieldnames) {
        names = Arrays.asList(fieldnames);
    }

    public List<String> getNames() {
        return names;
    }
}
