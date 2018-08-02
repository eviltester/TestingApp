package com.javafortesters.pulp.domain.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class PulpAuthor {
    public static final PulpAuthor UNKNOWN_AUTHOR = new PulpAuthor("unknown", "unknown author");
    private String name;
    private final String id;

    public PulpAuthor(String name) {
        this.name = name;
        this.id = "unknown";
    }

    public PulpAuthor(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public static Collection<String> parseNameAsMultipleAuthors(String authorsField) {

        Collection<String> authorsToAdd = new ArrayList<>();

        String[] authorNames = authorsField.split(" / ");
        for(String authorName : authorNames){

            authorsToAdd.add(authorName.trim());
        }

        return authorsToAdd;
    }

    public static Comparator<PulpAuthor> SortNameComparatorAscending() {
                return new Comparator<PulpAuthor>() {

                    public int compare(PulpAuthor item1, PulpAuthor item2) {

                        String field1 = item1.getName().toUpperCase();
                        String field2 = item2.getName().toUpperCase();

                        return field1.compareTo(field2);
                    }
        };
    }

    public static Comparator<PulpAuthor> SortNameComparatorDescending() {
        return new Comparator<PulpAuthor>() {

            public int compare(PulpAuthor item1, PulpAuthor item2) {

                String field1 = item1.getName().toUpperCase();
                String field2 = item2.getName().toUpperCase();

                return field2.compareTo(field1);
            }
        };
    }

    public String getName() {
        return name;
    }

    public PulpAuthor amendName(String newName){

        if(newName == null){
            return this;
        }

        if(newName.trim().isEmpty()){
            return this;
        }

        this.name = newName;
        return this;
    }

    public String getId() {
        return id;
    }
}
