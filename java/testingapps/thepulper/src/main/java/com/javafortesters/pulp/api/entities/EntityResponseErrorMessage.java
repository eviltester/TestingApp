package com.javafortesters.pulp.api.entities;

import com.google.gson.JsonElement;

public class EntityResponseErrorMessage{

    public final String errorMessage;

    public EntityResponseErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
