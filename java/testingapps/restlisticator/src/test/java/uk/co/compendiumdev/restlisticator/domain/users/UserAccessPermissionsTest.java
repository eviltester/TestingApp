package uk.co.compendiumdev.restlisticator.domain.users;

import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.http.ApiEndPoint;
import uk.co.compendiumdev.restlisticator.http.HttpVerb;

/**
 * Created by Alan on 16/07/2017.
 */
public class UserAccessPermissionsTest {

    @Test
    public void canCreateAUserAccessPermission(){

        UserAccessPermission permission = new UserAccessPermissionBuilder(ApiEndPoint.LISTS).build();

        Assert.assertEquals("/lists", permission.getEndpoint());
        Assert.assertTrue(permission.can(HttpVerb.find("get")));
        Assert.assertTrue(permission.can(HttpVerb.PUT));
        Assert.assertTrue(permission.can("post"));
        Assert.assertTrue(permission.can("patch"));
        Assert.assertTrue(permission.can("options"));
        Assert.assertTrue(permission.can("delete"));
    }


    @Test
    public void canRestrictAUserAccessPermission(){

        UserAccessPermission permission = new UserAccessPermissionBuilder(ApiEndPoint.LISTS).
                                                cannot(HttpVerb.DELETE).
                                                build();

        Assert.assertEquals("/lists", permission.getEndpoint());

        Assert.assertTrue(permission.can("get"));
        Assert.assertTrue(permission.can("put"));
        Assert.assertTrue(permission.can("post"));
        Assert.assertTrue(permission.can("patch"));
        Assert.assertTrue(permission.can("options"));

        Assert.assertFalse(permission.can("delete"));
    }


    @Test
    public void canRestrictAllUserAccessPermission(){

        UserAccessPermission permission = new UserAccessPermissionBuilder(ApiEndPoint.LISTS).
                withDefault(false).
                build();

        Assert.assertEquals("/lists", permission.getEndpoint());

        Assert.assertFalse(permission.can("get"));
        Assert.assertFalse(permission.can("put"));
        Assert.assertFalse(permission.can("post"));
        Assert.assertFalse(permission.can("patch"));
        Assert.assertFalse(permission.can("options"));

        Assert.assertFalse(permission.can("delete"));
    }

    @Test
    public void canMixUserAccessPermission(){

        UserAccessPermission permission = new UserAccessPermissionBuilder(ApiEndPoint.LISTS).
                withDefault(false).
                can("delete").
                build();

        Assert.assertEquals("/lists", permission.getEndpoint());

        Assert.assertFalse(permission.can("get"));
        Assert.assertFalse(permission.can("put"));
        Assert.assertFalse(permission.can("post"));
        Assert.assertFalse(permission.can("patch"));
        Assert.assertFalse(permission.can("options"));

        Assert.assertTrue(permission.can("delete"));
    }
}
