package uk.co.compendiumdev.restlisticator.domain.list;


import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;

import java.util.HashMap;
import java.util.Map;

public class ListicatorPatchTest {

    @Test
    public void canPatchAListGivenGUIDAndPayloadOfFields(){

        ListicatorList list = new ListicatorList("My New List","This is a list of my cool stuff");
        TheListicator app = new TheListicator();

        app.addList(list);


        Map<String,String> patches = new HashMap<String, String>();
        patches.put("title", "this is the new title");
        patches.put("createdDate", "1996-04-01-14-54-23");

        ListicatorList readList = app.patchList(list.getGUID(), patches);



        Assert.assertEquals("this is the new title", list.getTitle(), readList.getTitle());
        Assert.assertEquals("1996-04-01-14-54-23", readList.getCreatedDate());
    }
}
