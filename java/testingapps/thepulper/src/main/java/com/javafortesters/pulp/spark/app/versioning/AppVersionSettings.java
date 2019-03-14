package com.javafortesters.pulp.spark.app.versioning;

public enum AppVersionSettings {

    /** when a title or name is displayed in a table, is it to the list of books?*/
    AMEND_LINKS_SHOWN_IN_LIST(false, "Entity Instance [Amend] Links are shown in table/list views"),
    DELETE_LINKS_SHOWN_IN_LIST(false,"Entity Instance [Delete] Links are shown in table/list views"),
    TITLE_LINKS_TO_BOOKS_LIST(false, "Entity Instance Links shown in table/list views direct to book list"),
    HTML_TAGS_EASY_TO_AUTOMATE(false, "Pages have additional HTML tag elements to make automating the GUI easier"),
    LISTS_SHOWING_CORRECT_NUMBER_OF_THINGS(false, "List show the correct thing name and count");

    private final Boolean byDefault;
    private final String description;

    AppVersionSettings(final boolean aDefault, final String capabilitySummary) {
        this.byDefault = aDefault;
        this.description = capabilitySummary;
    }

    public String getKey() {
        return name();
    }

    public Boolean getDefault() {
        return byDefault;
    }

    public String getDescription(){
        return description;
    }
}
