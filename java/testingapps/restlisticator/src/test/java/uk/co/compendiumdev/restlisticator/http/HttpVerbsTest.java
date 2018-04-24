package uk.co.compendiumdev.restlisticator.http;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Alan on 17/07/2017.
 */
public class HttpVerbsTest {


    @Test
    public void haveAllHttpVerbsRepresented(){

        Assert.assertEquals("get", HttpVerb.GET.getName());
        Assert.assertEquals("put", HttpVerb.PUT.getName());
        Assert.assertEquals("post", HttpVerb.POST.getName());
        Assert.assertEquals("patch", HttpVerb.PATCH.getName());
        Assert.assertEquals("delete", HttpVerb.DELETE.getName());
        Assert.assertEquals("options", HttpVerb.OPTIONS.getName());
        Assert.assertEquals("head", HttpVerb.HEAD.getName());
    }
}
