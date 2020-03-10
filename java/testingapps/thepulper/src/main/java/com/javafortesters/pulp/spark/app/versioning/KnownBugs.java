package com.javafortesters.pulp.spark.app.versioning;

import java.util.HashMap;

public class KnownBugs {

    HashMap<Bug, Boolean> knownBugs;

    public KnownBugs(){
        knownBugs = new HashMap<Bug, Boolean>();
        setDefault();
    }

    public enum Bug{
        DELETE_BOOK_WHEN_DELETING_HOUSE_AUTHOR
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
    }

    public void setBugPresenceTo(final Bug bugKey, final boolean presence) {
        if(!knownBugs.containsKey(bugKey)){
            System.out.println("CONFIG ERRROR: bugkey not configured with default" + bugKey);
        }

        knownBugs.put(bugKey, presence);
    }
}
