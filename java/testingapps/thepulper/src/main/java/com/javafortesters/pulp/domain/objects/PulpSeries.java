package com.javafortesters.pulp.domain.objects;

import java.util.Comparator;

public class PulpSeries {
    public static final PulpSeries UNKNOWN_SERIES = new PulpSeries("unknown", "unknown series");
    private String name;
    private final String id;

    public PulpSeries(String name) {
        this.name = name;
        this.id = "unknown";
    }

    public PulpSeries(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }



    public static Comparator<PulpSeries> SortNameComparatorAscending() {
        return new Comparator<PulpSeries>() {

            public int compare(PulpSeries item1, PulpSeries item2) {

                String field1 = item1.getName().toUpperCase();
                String field2 = item2.getName().toUpperCase();

                return field1.compareTo(field2);
            }
        };
    }

    public static Comparator<PulpSeries> SortNameComparatorDescending() {
        return new Comparator<PulpSeries>() {

            public int compare(PulpSeries item1, PulpSeries item2) {

                String field1 = item1.getName().toUpperCase();
                String field2 = item2.getName().toUpperCase();

                return field2.compareTo(field1);
            }
        };
    }

    public PulpSeries amendName(final String newName) {

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
