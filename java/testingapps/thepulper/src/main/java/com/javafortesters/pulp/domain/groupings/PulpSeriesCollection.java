package com.javafortesters.pulp.domain.groupings;

import com.javafortesters.pulp.domain.objects.PulpSeries;

import java.util.*;

public class PulpSeriesCollection {
    private int key;
    private ArrayList<PulpSeries> serieses;

    public PulpSeriesCollection(){
        serieses = new ArrayList<>();
        key = 1;
    }

    public int count() {
        return serieses.size();
    }

    public PulpSeries add(String seriesName) {

        if(findByName(seriesName)!=PulpSeries.UNKNOWN_SERIES){
            return null;
        }

        PulpSeries series = getNextSeries(seriesName);
        serieses.add(series);
        return series;
    }

    private PulpSeries getNextSeries(String seriesName) {
        return new PulpSeries(String.valueOf(key++), seriesName);
    }

    public PulpSeries get(String key) {

        if(key==null){
            return PulpSeries.UNKNOWN_SERIES;
        }

        for(PulpSeries aSeries : serieses){
            if(aSeries.getId().contentEquals(key)){
                return aSeries;
            }
        }

        return PulpSeries.UNKNOWN_SERIES;
    }

    public PulpSeries findByName(String name) {

        if(name==null){
            return PulpSeries.UNKNOWN_SERIES;
        }

        for(PulpSeries aSeries : serieses){
            if(aSeries.getName().equalsIgnoreCase(name)){
                return aSeries;
            }
        }

        return PulpSeries.UNKNOWN_SERIES;
    }

    public List<String> keys() {
        Set<String> itemKeys = new TreeSet<>();
        for(PulpSeries item : serieses){
            itemKeys.add(item.getId());
        }
        return new ArrayList<>(itemKeys);
    }

    public Collection<PulpSeries> getAll() {
        return serieses;
    }

    public Collection<PulpSeries> getAllOrderedByName() {

        List<PulpSeries> sorted = new ArrayList<>(serieses);

        Collections.sort(sorted, PulpSeries.SortNameComparatorAscending());
        return sorted;
    }

    public boolean delete(final String id) {
        if(id==null){
            return false;
        }

        if(id.isEmpty()){
            return false;
        }

        return serieses.remove(get(id));
    }
}
