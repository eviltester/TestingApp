package uk.co.compendiumdev.restlisticator.domain.list;


import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ListicatorListTest {

    @Test
    public void canCreateAListicatorList() throws ParseException {

        ListicatorList list = new ListicatorList("My New List","This is a list of my cool stuff");
        
        Assert.assertEquals("My New List", list.getTitle());
        Assert.assertEquals("This is a list of my cool stuff", list.getDescription());
        Assert.assertNotNull(list.getGUID());
        Assert.assertEquals("", list.getOwner());

        Assert.assertNotNull(list.getCreatedDate());
        Assert.assertNotEquals("", list.getCreatedDate());
        Assert.assertEquals(list.getCreatedDate(), list.getAmendedDate());

    }


    @Test
    public void whenAmendAListicatorListTitleAmendedDateChanges() throws ParseException {

        ListicatorList list = new ListicatorList("My New List","This is a list of my cool stuff");

        list.forceSetCreatedDate("2016-04-01-01-01-01");
        list.forceSetAmendedDate("2016-04-01-01-01-01");

        list.setTitle("bob");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        Date createdDate = dateFormat.parse(list.getCreatedDate());
        Date amendedDate = dateFormat.parse(list.getAmendedDate());
        Assert.assertTrue(amendedDate.after(createdDate));
    }

    @Test
    public void whenAmendAListicatorListDescriptionAmendedDateChanges() throws ParseException {

        ListicatorList list = new ListicatorList("My New List","This is a list of my cool stuff");

        list.forceSetCreatedDate("2016-04-01-01-01-01");
        list.forceSetAmendedDate("2016-04-01-01-01-01");

        list.setDescription("Don't be pink");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        Date createdDate = dateFormat.parse(list.getCreatedDate());
        Date amendedDate = dateFormat.parse(list.getAmendedDate());
        Assert.assertTrue(amendedDate.after(createdDate));
    }

    @Test
    public void ownerCanChange() throws ParseException {

        ListicatorList list = new ListicatorList("My New List","This is a list of my cool stuff");

        list.forceSetCreatedDate("2016-04-01-01-01-01");
        list.forceSetAmendedDate("2016-04-01-01-01-01");

        Assert.assertEquals("", list.getOwner());

        list.setOwner("bob");

        Assert.assertEquals("bob", list.getOwner());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        Date createdDate = dateFormat.parse(list.getCreatedDate());
        Date amendedDate = dateFormat.parse(list.getAmendedDate());
        Assert.assertTrue(amendedDate.after(createdDate));
    }



}
