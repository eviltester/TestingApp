package com.javafortesters.pulp.domain.groupings;

import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpPublisher;

import java.util.*;

public class PulpAuthors {
    private int key;
    private ArrayList<PulpAuthor> authors;

    public PulpAuthors(){
        authors = new ArrayList<>();
        key = 1;
    }
    public int count() {
        return authors.size();
    }

    public PulpAuthor add(String authorName) {
        // check if author exists
        if(findByName(authorName)!=PulpAuthor.UNKNOWN_AUTHOR){
            return null;
        }
        // add new author
        PulpAuthor author = getNextAuthor(authorName);
        authors.add(author);
        return author;
    }


    private PulpAuthor getNextAuthor(String authorName) {
        return new PulpAuthor(getNextAuthorId(), authorName);
    }

    public PulpAuthor get(String key) {

        if(key==null){
            return PulpAuthor.UNKNOWN_AUTHOR;
        }

        for(PulpAuthor author : authors){
            if(author.getId().contentEquals(key)){
                return author;
            }
        }

        return PulpAuthor.UNKNOWN_AUTHOR;
    }

    public PulpAuthor findByName(String name) {

        if(name==null){
            return PulpAuthor.UNKNOWN_AUTHOR;
        }

        for(PulpAuthor author : authors){
            if(author.getName().equalsIgnoreCase(name)){
                return author;
            }
        }

        return PulpAuthor.UNKNOWN_AUTHOR;
    }

    public List<String> keys() {
        List<String> keys = new ArrayList<>();
        for(PulpAuthor item : authors){
            keys.add(item.getId());
        }
        return keys;
    }

    public Collection<PulpAuthor> getAll() {
        return authors;
    }

    public Collection<PulpAuthor> getAllOrderedByName() {
        List<PulpAuthor> sorted = new ArrayList<>(authors);

        Collections.sort(sorted, PulpAuthor.SortNameComparatorAscending());
        return sorted;
    }

    public Collection<PulpAuthor> getAll(Collection<String> authorIndexes) {
        Collection<PulpAuthor> someAuthors = new ArrayList<>();
        for(String index : authorIndexes){
            someAuthors.add(get(index));
        }
        return someAuthors;
    }

    public boolean delete(final String id) {
        if(id==null){
            return false;
        }

        if(id.isEmpty()){
            return false;
        }

        return authors.remove(get(id));
    }

    public String getNextAuthorId() {
        return String.valueOf(key++);
    }
}
