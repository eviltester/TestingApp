package uk.co.compendiumdev.restlisticator.domain.users;


import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.http.ApiEndPoint;
import uk.co.compendiumdev.restlisticator.http.HttpVerb;

public class UserTest {

    @Test
    public void canCreateAUser(){

        User user = new User("bob","password");
        Assert.assertEquals("bob", user.getUsername());
        Assert.assertTrue(user.passwordMatches("password"));
    }

    @Test
    public void checkDefaultAPIPermissions(){
        User user = new User("eris","password");

        Assert.assertTrue(user.permissions().can(HttpVerb.GET, ApiEndPoint.LISTS));
        Assert.assertTrue(user.permissions().can(HttpVerb.GET, ApiEndPoint.HEARTBEAT));
        Assert.assertTrue(user.permissions().can(HttpVerb.GET, ApiEndPoint.FEATURE_TOGGLES));
    }


}
