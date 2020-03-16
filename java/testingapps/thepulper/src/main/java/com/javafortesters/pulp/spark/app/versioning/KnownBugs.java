package com.javafortesters.pulp.spark.app.versioning;

import java.util.HashMap;

public class KnownBugs {

    HashMap<Bug, Boolean> knownBugs;

    public KnownBugs(){
        knownBugs = new HashMap<Bug, Boolean>();
        setDefault();
    }

    public enum Bug{
        DELETE_BOOK_WHEN_DELETING_HOUSE_AUTHOR,
        TEMPLATE_ERROR_IN_SERIES_FAQ,
        FAQ_PAGE_TITLE_ALWAYS_AUTHORS,
        DELETE_HOUSE_AUTHOR_FIRST_ALLOWS_CREATING_BOOK_WITH_NO_AUTHORS,
        DELETE_BOOK_WHEN_ZERO_AUTHORS_BUT_STILL_HAS_HOUSE_AUTHOR,
        DUPLICATE_HOUSE_AUTHOR_NAME_IN_VIEW;
    }

    public boolean bugIsPresent(final Bug bugKey) {

        if(!knownBugs.containsKey(bugKey)){
            return false;
        }

        return knownBugs.get(bugKey);
    }

    public void setDefault() {
        // by default no bugs are active
        knownBugs.put(Bug.DELETE_BOOK_WHEN_DELETING_HOUSE_AUTHOR, false);
        knownBugs.put(Bug.TEMPLATE_ERROR_IN_SERIES_FAQ, false);
        knownBugs.put(Bug.FAQ_PAGE_TITLE_ALWAYS_AUTHORS, false);
        knownBugs.put(Bug.DELETE_HOUSE_AUTHOR_FIRST_ALLOWS_CREATING_BOOK_WITH_NO_AUTHORS, false);
        knownBugs.put(Bug.DELETE_BOOK_WHEN_ZERO_AUTHORS_BUT_STILL_HAS_HOUSE_AUTHOR, false);
        knownBugs.put(Bug.DUPLICATE_HOUSE_AUTHOR_NAME_IN_VIEW, false);
    }

    public void setBugPresenceTo(final Bug bugKey, final boolean presence) {
        if(!knownBugs.containsKey(bugKey)){
            System.out.println("CONFIG ERRROR: bugkey not configured with default" + bugKey);
        }

        knownBugs.put(bugKey, presence);
    }
}
