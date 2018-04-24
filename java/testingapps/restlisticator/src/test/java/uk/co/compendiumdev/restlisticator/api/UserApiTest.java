package uk.co.compendiumdev.restlisticator.api;

import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.domain.users.User;

public class UserApiTest {

    @Test
    public void canGetAdminUserKey() {

        Api api = new Api(new TheListicator());

        ApiRequest request = new TestApiRequest();
        ApiResponse response = new TestApiResponse();

        // simulate authentication
        request.setUserDetails("admin", "password");
        User user = api.getUser("admin");

        String []parts = {"admin"};

        request.setPathParts(parts);
        api.getUserDetails(request, response);

        System.out.println(response.getBody());

        Assert.assertEquals(200, response.getStatus());
        Assert.assertTrue(response.getBody().contains(user.getApikey()));

    }

    @Test
    public void adminCanCreateUserWithUsernamePassword() {

        Api api = new Api(new TheListicator());

        ApiRequest request = new TestApiRequest();
        ApiResponse response = new TestApiResponse();

        // simulate authentication
        request.setUserDetails("admin", "password");
        User user = api.getUser("bobdobbs");

        Assert.assertNull(user);

        String []parts = {};

        request.setPathParts(parts);
        request.setBody("{username:'bobdobbs',password:'tvrepairman'}");
        api.createUserDetails(request, response);

        System.out.println(response.getBody());

        Assert.assertEquals(201, response.getStatus());
        Assert.assertNotNull(api.getUser("bobdobbs"));

    }

    @Test
    public void cannotGetUserKeyIfNotCorrectPermissions() {

        Api api = new Api(new TheListicator());

        ApiRequest request = new TestApiRequest();
        ApiResponse response = new TestApiResponse();

        // simulate authentication
        request.setUserDetails("user", "password");
        User user = api.getUser("admin");

        String []parts = {"admin"};

        request.setPathParts(parts);
        api.getUserDetails(request, response);

        System.out.println(response.getBody());

        Assert.assertEquals(403, response.getStatus());
        Assert.assertEquals("",response.getBody());
    }

    @Test
    public void canGetListOfUsers() {

        Api api = new Api(new TheListicator());

        ApiRequest request = new TestApiRequest();
        ApiResponse response = new TestApiResponse();

        // simulate authentication
        //request.setUserDetails("user", "password");

        String []parts = {};

        request.setPathParts(parts);
        api.getUsers(request, response);

        System.out.println(response.getBody());

        Assert.assertEquals(200, response.getStatus());
        Assert.assertTrue(response.getBody().contains("{\"username\":\"superadmin\"}"));
        Assert.assertTrue(response.getBody().contains("{\"username\":\"admin\"}"));
        Assert.assertTrue(response.getBody().contains("{\"username\":\"user\"}"));
    }

    @Test
    public void canFilterListOfUsersByExactMatch() {

        Api api = new Api(new TheListicator());

        ApiRequest request = new TestApiRequest();
        ApiResponse response = new TestApiResponse();

        // simulate authentication
        //request.setUserDetails("user", "password");

        String []parts = {};

        request.setPathParts(parts);
        request.setQuery("username=user");
        api.getUsers(request, response);

        System.out.println(response.getBody());

        Assert.assertEquals(200, response.getStatus());
        Assert.assertFalse(response.getBody().contains("{\"username\":\"superadmin\"}"));
        Assert.assertFalse(response.getBody().contains("{\"username\":\"admin\"}"));
        Assert.assertTrue(response.getBody().contains("{\"username\":\"user\"}"));
    }

    @Test
    public void canFilterListOfUsersByPartialMatch() {

        Api api = new Api(new TheListicator());

        ApiRequest request = new TestApiRequest();
        ApiResponse response = new TestApiResponse();

        // simulate authentication
        //request.setUserDetails("user", "password");

        String []parts = {};

        request.setPathParts(parts);
        request.setQuery("username=\"admin\"");
        api.getUsers(request, response);

        System.out.println(response.getBody());

        Assert.assertEquals(200, response.getStatus());
        Assert.assertTrue(response.getBody().contains("{\"username\":\"superadmin\"}"));
        Assert.assertTrue(response.getBody().contains("{\"username\":\"admin\"}"));
        Assert.assertFalse(response.getBody().contains("{\"username\":\"user\"}"));
    }



    @Test
    public void userCanChangeTheirPasswordUsingJson() {

        Api api = new Api(new TheListicator());

        ApiRequest request = new TestApiRequest();
        ApiResponse response = new TestApiResponse();

        // simulate authentication
        request.setUserDetails("user", "password");
        User user = api.getUser("user");

        String []parts = {"user", "password"};

        request.setPathParts(parts);
        request.setBody("{password:'newPassword'}");
        api.setUserPassword(request, response);

        System.out.println(response.getBody());

        Assert.assertEquals(204, response.getStatus());
        Assert.assertEquals("",response.getBody());

        Assert.assertTrue(user.passwordMatches("newPassword"));
    }

    @Test
    public void userCanChangeTheirPasswordUsingXml() {

        Api api = new Api(new TheListicator());

        ApiRequest request = new TestApiRequest();
        ApiResponse response = new TestApiResponse();

        request.setRequestFormat(ApiRequestResponseFormat.XML);

        // simulate authentication
        request.setUserDetails("user", "password");

        User user = api.getUser("user");

        String []parts = {"user", "password"};

        request.setPathParts(parts);
        request.setBody("<user><password>newPassword</password></user>");
        api.setUserPassword(request, response);

        System.out.println(response.getBody());

        Assert.assertEquals(204, response.getStatus());
        Assert.assertEquals("",response.getBody());

        Assert.assertTrue(user.passwordMatches("newPassword"));

    }

    @Test
    public void userCanNotChangeSomeoneElsesPassword() {

        Api api = new Api(new TheListicator());

        ApiRequest request = new TestApiRequest();
        ApiResponse response = new TestApiResponse();

        // simulate authentication
        request.setUserDetails("user", "password");
        User user = api.getUser("admin");

        String []parts = {"admin", "password"};

        request.setPathParts(parts);
        request.setBody("{password:'newPassword'}");
        api.setUserPassword(request, response);

        System.out.println(response.getBody());

        Assert.assertEquals(403, response.getStatus());
        Assert.assertEquals("",response.getBody());

        Assert.assertTrue(user.passwordMatches("password"));
    }

    @Test
    public void adminUserCanChangeSomeoneElsesPassword() {

        Api api = new Api(new TheListicator());

        ApiRequest request = new TestApiRequest();
        ApiResponse response = new TestApiResponse();

        // simulate authentication
        request.setUserDetails("admin", "password");
        User user = api.getUser("user");
        User admin = api.getUser("admin");

        String []parts = {"user", "password"};

        request.setPathParts(parts);
        request.setBody("{password:'newPassword'}");
        api.setUserPassword(request, response);

        System.out.println(response.getBody());

        Assert.assertEquals(204, response.getStatus());
        Assert.assertEquals("",response.getBody());

        Assert.assertTrue(user.passwordMatches("newPassword"));
        Assert.assertTrue(admin.passwordMatches("password"));
    }

    @Test
    public void userCanChangeTheirApiKeyUsingJson() {

        Api api = new Api(new TheListicator());

        ApiRequest request = new TestApiRequest();
        ApiResponse response = new TestApiResponse();

        // simulate authentication
        request.setUserDetails("user", "password");
        User user = api.getUser("user");

        String []parts = {"user", "password"};

        request.setPathParts(parts);
        request.setBody("{apikey:'newkeynewkey'}");
        api.setUserApiKey(request, response);

        System.out.println(response.getBody());

        Assert.assertEquals(204, response.getStatus());
        Assert.assertEquals("",response.getBody());

        Assert.assertEquals("newkeynewkey", user.getApikey());
    }
}
