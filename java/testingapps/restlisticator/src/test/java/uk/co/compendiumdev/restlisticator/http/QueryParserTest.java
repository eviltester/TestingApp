package uk.co.compendiumdev.restlisticator.http;

import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void canParseQuery(){

        String theQuery = "    ?without=title,description";

        QueryParser qp = new QueryParser(theQuery);

        Assert.assertEquals(1, qp.getAttributeCount());
        Assert.assertTrue(qp.hasAttribute("without"));
        Assert.assertEquals("title,description", qp.getValueFor("without"));
        Assert.assertFalse(qp.hasAttribute("with"));
        Assert.assertEquals("", qp.getValueFor("with"));

    }

    @Test
    public void canParseQueryWitMultipleAttributes(){

        String theQuery = "?without=title,description&with=createdDate";

        QueryParser qp = new QueryParser(theQuery);

        Assert.assertEquals(2, qp.getAttributeCount());
        Assert.assertTrue(qp.hasAttribute("without"));
        Assert.assertEquals("title,description", qp.getValueFor("without"));
        Assert.assertTrue(qp.hasAttribute("with"));
        Assert.assertEquals("createdDate", qp.getValueFor("with"));

    }

    @Test
    public void attributeWithNoValueIsEmptyString(){

        String theQuery = "?without=";

        QueryParser qp = new QueryParser(theQuery);

        Assert.assertEquals(1, qp.getAttributeCount());
        Assert.assertTrue(qp.hasAttribute("without"));
        Assert.assertEquals("", qp.getValueFor("without"));
    }

}
