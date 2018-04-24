package uk.co.compendiumdev.restlisticator.domain.list;

import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;


public class ListicatorDeleteTest {

    @Test
    public void canDeleteAListUsingGUID(){

        ListicatorList list = new ListicatorList("My New List","This is a list of my cool stuff");

        TheListicator app = new TheListicator();

        Assert.assertEquals(0, app.listCount());

        app.addList(list);

        Assert.assertEquals(1, app.listCount());

        boolean didDelete = app.deleteList(list.getGUID());

        Assert.assertTrue(didDelete);
        Assert.assertEquals(0, app.listCount());
    }

    @Test
    public void cannotDeleteWithWrongGUID(){

        ListicatorList list = new ListicatorList("My New List","This is a list of my cool stuff");

        TheListicator app = new TheListicator();

        Assert.assertEquals(0, app.listCount());

        app.addList(list);

        Assert.assertEquals(1, app.listCount());

        boolean didDelete = app.deleteList(list.getGUID()+"zzzzzzzzzz");

        Assert.assertTrue(!didDelete);
        Assert.assertEquals(1, app.listCount());
    }

}
