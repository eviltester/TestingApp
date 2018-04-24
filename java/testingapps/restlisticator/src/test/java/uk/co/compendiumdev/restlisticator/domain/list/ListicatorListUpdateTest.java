package uk.co.compendiumdev.restlisticator.domain.list;


import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;

public class ListicatorListUpdateTest {

    @Test
    public void canPUTAListGivenGUIDToCreateANewList(){

        ListicatorList list = new ListicatorList("My New List","This is a list of my cool stuff");
        TheListicator app = new TheListicator();

        app.addList(list);

        Assert.assertEquals(1, app.listCount());

        ListicatorList putlist = new ListicatorList("My PUT List","This is a PUT list of my cool stuff");

        // putting a new list with guid creates
        ListicatorList readList = app.putList(putlist.getGUID(), putlist);

        Assert.assertEquals(putlist.getGUID(), readList.getGUID());
        Assert.assertEquals("My PUT List", readList.getTitle());
        Assert.assertEquals("This is a PUT list of my cool stuff", readList.getDescription());
        Assert.assertEquals(2, app.listCount());

    }


    @Test
    public void canPUTAListGivenExistingGUIDToAmendAList(){

        ListicatorList list = new ListicatorList("My New List","This is a list of my cool stuff");
        TheListicator app = new TheListicator();

        app.addList(list);

        Assert.assertEquals(1, app.listCount());

        ListicatorList putlist = new ListicatorList("My PUT List","This is a PUT list of my cool stuff");

        // putting a new list with guid creates
        ListicatorList readList = app.putList(list.getGUID(), putlist);

        Assert.assertEquals(list.getGUID(), readList.getGUID());
        Assert.assertEquals("My PUT List", readList.getTitle());
        Assert.assertEquals("This is a PUT list of my cool stuff", readList.getDescription());
        Assert.assertEquals(1, app.listCount());

    }
}
