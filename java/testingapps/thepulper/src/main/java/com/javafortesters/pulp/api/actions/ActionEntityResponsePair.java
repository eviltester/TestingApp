package com.javafortesters.pulp.api.actions;

import com.javafortesters.pulp.api.EntityResponse;

public class ActionEntityResponsePair {
    EntityResponse response;
    ActionToDo action;

    public ActionEntityResponsePair(final ActionToDo action, final EntityResponse response) {
        this.action = action;
        this.response = response;
    }
}
