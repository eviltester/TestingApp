package uk.co.compendiumdev.restlisticator.http;


import org.junit.Assert;
import org.junit.Test;

public class BasicAuthHeaderTest {

    @Test
    public void canEncodeHeaderDetails(){

        String header = BasicAuthHeader.getEncoded("username", "password");

        Assert.assertEquals("Basic dXNlcm5hbWU6cGFzc3dvcmQ=", header);
    }

    @Test
    public void canParseWithDetails(){

        BasicAuthHeader auth = new BasicAuthHeader("Basic dXNlcm5hbWU6cGFzc3dvcmQ=");

        Assert.assertTrue(auth.isBasicAuthHeader());
        Assert.assertEquals("username", auth.getUsername());
        Assert.assertEquals("password", auth.getPassword());
    }

    @Test
    public void canParseWithSpacesDetails(){

        BasicAuthHeader auth = new BasicAuthHeader("   Basic    dXNlcm5hbWU6cGFzc3dvcmQ=   ");

        Assert.assertTrue(auth.isBasicAuthHeader());
        Assert.assertEquals("username", auth.getUsername());
        Assert.assertEquals("password", auth.getPassword());
    }

    @Test
    public void canParseInvalidWithNoPassword(){

        // username:
        BasicAuthHeader auth = new BasicAuthHeader("Basic dXNlcm5hbWU6");

        Assert.assertFalse(auth.isBasicAuthHeader());
        Assert.assertEquals("username", auth.getUsername());
        Assert.assertEquals("", auth.getPassword());
    }

    @Test
    public void canParseInvalidWithNoUsername(){

        // :password
        BasicAuthHeader auth = new BasicAuthHeader("Basic OnBhc3N3b3Jk");

        Assert.assertFalse(auth.isBasicAuthHeader());
        Assert.assertEquals("", auth.getUsername());
        Assert.assertEquals("password", auth.getPassword());
    }

    @Test
    public void canParseInvalidWithNoDetails(){

        // :
        BasicAuthHeader auth = new BasicAuthHeader("Basic Og==");

        Assert.assertFalse(auth.isBasicAuthHeader());
        Assert.assertEquals("", auth.getUsername());
        Assert.assertEquals("", auth.getPassword());
    }

    @Test
    public void canParseInvalidWithNoColon(){

        // abcde
        BasicAuthHeader auth = new BasicAuthHeader("Basic YWJjZGU=");

        Assert.assertFalse(auth.isBasicAuthHeader());
        Assert.assertEquals("", auth.getUsername());
        Assert.assertEquals("", auth.getPassword());
    }



}
