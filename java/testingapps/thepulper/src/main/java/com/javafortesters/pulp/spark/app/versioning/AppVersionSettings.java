package com.javafortesters.pulp.spark.app.versioning;

public enum AppVersionSettings {

    /** when a title or name is displayed in a table, is it to the list of books?*/
    AMEND_LINKS_SHOWN_IN_LIST(false),
    DELETE_LINKS_SHOWN_IN_LIST(false),
    TITLE_LINKS_TO_BOOKS_LIST(false),
    HTML_TAGS_EASY_TO_AUTOMATE(false);

    private final Boolean byDefault;

    AppVersionSettings(final boolean aDefault) {
        this.byDefault = aDefault;
    }

    public String getKey() {
        return name();
    }

    public Boolean getDefault() {
        return byDefault;
    }
}
