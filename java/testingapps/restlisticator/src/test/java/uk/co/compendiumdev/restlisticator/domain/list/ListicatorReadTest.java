package uk.co.compendiumdev.restlisticator.domain.list;

import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;

/**
 * Created by Alan on 06/07/2017.
 */
public class ListicatorReadTest {

    @Test
    public void canReadAListGivenGUID(){

        ListicatorList list = new ListicatorList("My New List","This is a list of my cool stuff");

        TheListicator app = new TheListicator();

        Assert.assertEquals(0, app.listCount());

        app.addList(list);

        Assert.assertEquals(1, app.listCount());

        ListicatorList readList = app.getList(list.getGUID());

        Assert.assertEquals(list.getTitle(), readList.getTitle());
        Assert.assertEquals(list.getDescription(), readList.getDescription());
    }

    @Test
    public void cannotReadAListGivenNonExistantGUID(){

        ListicatorList list = new ListicatorList("My New List","This is a list of my cool stuff");

        TheListicator app = new TheListicator();

        Assert.assertEquals(0, app.listCount());

        app.addList(list);

        Assert.assertEquals(1, app.listCount());

        Assert.assertNull(app.getList(list.getGUID()+"jasfidjasdifj"));

    }
}
