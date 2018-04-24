package uk.co.compendiumdev.restlisticator.api.payloads;


import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ResultsFilterTest {

    @Test
    public void canCreateFilterFromQuery(){

        ResultsFilter rf = new ResultsFilter();
        rf.setFromQuery("?with=title,description");

        List with = rf.getWithFilterValues();
        Assert.assertEquals(2, with.size());
        Assert.assertTrue(with.contains("title"));
        Assert.assertTrue(with.contains("description"));
    }

    @Test
    public void canCreateWithoutFilterFromQuery(){

        ResultsFilter rf = new ResultsFilter();
        rf.setFromQuery("?without=title,description");

        List with = rf.getWithoutFilterValues();
        Assert.assertEquals(2, with.size());
        Assert.assertTrue(with.contains("title"));
        Assert.assertTrue(with.contains("description"));
    }

    @Test
    public void canCreateWithAndWithoutFilterFromQuery(){

        ResultsFilter rf = new ResultsFilter();
        rf.setFromQuery("?with=title,description&without=createdDate");

        List with = rf.getWithFilterValues();
        Assert.assertEquals(2, with.size());
        Assert.assertTrue(with.contains("title"));
        Assert.assertTrue(with.contains("description"));

        List without = rf.getWithoutFilterValues();
        Assert.assertEquals(1, without.size());
        Assert.assertTrue(without.contains("createdDate"));


    }
}
