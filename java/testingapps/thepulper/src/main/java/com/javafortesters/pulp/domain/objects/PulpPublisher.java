package com.javafortesters.pulp.domain.objects;

import java.util.Comparator;

public class PulpPublisher {
    public static final PulpPublisher UNKNOWN_PUBLISHER = new PulpPublisher("unknonwn", "unknown publisher");
    private String name;
    private final String id;

    public PulpPublisher(String name) {
        this.name = name;
        this.id = "unknown";
    }

    public PulpPublisher(String indexId, String name) {
        this.name = name;
        this.id = indexId;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public static Comparator<PulpPublisher> SortNameComparatorAscending() {
        return new Comparator<PulpPublisher>() {

            public int compare(PulpPublisher item1, PulpPublisher item2) {

                String field1 = item1.getName().toUpperCase();
                String field2 = item2.getName().toUpperCase();

                return field1.compareTo(field2);
            }
        };
    }

    public static Comparator<PulpPublisher> SortNameComparatorDescending() {
        return new Comparator<PulpPublisher>() {

            public int compare(PulpPublisher item1, PulpPublisher item2) {

                String field1 = item1.getName().toUpperCase();
                String field2 = item2.getName().toUpperCase();

                return field2.compareTo(field1);
            }
        };
    }

    public PulpPublisher amendName(final String newName) {

            if(newName == null){
                return this;
            }

            if(newName.trim().isEmpty()){
                return this;
            }

            this.name = newName;
            return this;
    }
}
