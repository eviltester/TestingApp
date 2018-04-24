package uk.co.compendiumdev.restlisticator.domain.utils;


import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;

import java.util.HashMap;
import java.util.Map;

public class PatcherTest {

    @Test
    public void canPatchAListicator(){

        ListicatorList list = new ListicatorList("first title", "first desc");

        Map<String,String> patches = new HashMap<String, String>();
        patches.put("title", "this is the new title");
        patches.put("createdDate", "1996-04-01-14-54-23");
        patches.put("description", "new description");


        ReflectionPatcher patcher = new ReflectionPatcher(list, ListicatorList.class);

        patcher.patch(patches);

        Assert.assertEquals("this is the new title", list.getTitle());
        Assert.assertEquals("1996-04-01-14-54-23", list.getCreatedDate());
        Assert.assertEquals("new description", list.getDescription());

        Assert.assertEquals(0, patcher.getFieldsInError().size());
        Assert.assertEquals(3, patcher.getFieldsPatched().size());

    }
}
