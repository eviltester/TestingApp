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
        FAQ_PAGE_TITLE_ALWAYS_AUTHORS;
    }

    public boolean bugIsPresent(final Bug bugKey) {

        if(!knownBugs.containsKey(bugKey)){
            return false;
        }

        return knownBugs.get(bugKey);
    }

    public void setDefault() {
        // by default
        knownBugs.put(Bug.DELETE_BOOK_WHEN_DELETING_HOUSE_AUTHOR, false);
        knownBugs.put(Bug.TEMPLATE_ERROR_IN_SERIES_FAQ, false);
        knownBugs.put(Bug.FAQ_PAGE_TITLE_ALWAYS_AUTHORS, false);
    }

    public void setBugPresenceTo(final Bug bugKey, final boolean presence) {
        if(!knownBugs.containsKey(bugKey)){
            System.out.println("CONFIG ERRROR: bugkey not configured with default" + bugKey);
        }

        knownBugs.put(bugKey, presence);
    }
}
