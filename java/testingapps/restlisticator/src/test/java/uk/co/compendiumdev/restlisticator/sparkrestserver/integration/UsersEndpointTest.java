package uk.co.compendiumdev.restlisticator.sparkrestserver.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;
import uk.co.compendiumdev.http.HttpMessageSender;
import uk.co.compendiumdev.http.HttpResponse;
import uk.co.compendiumdev.restlisticator.sparkrestserver.integration.listicatorstarter.RestListicatorSparkStarter;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.ApiEndPointNames;
import uk.co.compendiumdev.restlisticator.sparkrestserver.utils.SimpleMessageProcessor;

public class UsersEndpointTest{


    protected HttpMessageSender http;

    @BeforeClass
    public static void startServer() {
        RestListicatorSparkStarter.get("localhost").startSparkAppIfNotRunning(4567);
    }

    @Before
    public void httpConnect() {
        http = new HttpMessageSender("http://" + "localhost:" + Spark.port());
    }

    @Test
    public void needToBeAuthenticatedToGetUserAPIKey() {

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        http.deleteHeader(http.HEADER_AUTHORIZATION);
        HttpResponse response = http.get(ApiEndPointNames.USERS + "/admin");

        Assert.assertEquals(401, response.statusCode);
    }

    @Test
    public void canGetUserAPIKeyWhenAuthorised() {

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        http.setBasicAuth("admin", "password");
        HttpResponse response = http.get(ApiEndPointNames.USERS + "/admin");

        Assert.assertEquals(200, response.statusCode);
        Assert.assertTrue(response.body.contains("<apikey>"));

        String apiKey = new SimpleMessageProcessor().getXmlElement("apikey", response.body);

        System.out.println(apiKey);
        Assert.assertTrue(apiKey.length()>0);
    }

    /*
        TODO:
            [x] protect GET /user/username  - user can get their own
            [x] protect GET /user/username  - user cannot get others
            [x] protect GET /user/username  - admin can get any
     */

    @Test
    public void userCanOnlyGetTheirUserAPIKey() {

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        http.setBasicAuth("user", "password");
        HttpResponse response = http.get(ApiEndPointNames.USERS + "/admin");

        // user not authorized to acces that user
        Assert.assertEquals(403, response.statusCode);
        Assert.assertTrue(response.body.length()==0);
    }

    @Test
    public void adminUserCanGetAnyUserAPIKey() {

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        http.setBasicAuth("admin", "password");
        HttpResponse response = http.get(ApiEndPointNames.USERS + "/user");

        // user not authorized to acces that user
        Assert.assertEquals(200, response.statusCode);
        System.out.println(response.body);
    }

    @Test
    public void cannotUseIncorrectVerbOnUsers(){

        String users = ApiEndPointNames.USERS + "/*";

        HttpResponse response = http.post(users, "");
        Assert.assertEquals(405, response.statusCode);

        response = http.put(users, "");
        Assert.assertEquals(405, response.statusCode);

        response = http.delete(users);
        Assert.assertEquals(405, response.statusCode);

        response = http.patch(users, "");
        Assert.assertEquals(405, response.statusCode);

        response = http.head(users);
        Assert.assertEquals(405, response.statusCode);

        response = http.trace(users);
        Assert.assertEquals(405, response.statusCode);

        response = http.connect(users);
        Assert.assertEquals(405, response.statusCode);
    }

    @Test
    public void usersSupportsOptions(){

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        HttpResponse response = http.options(ApiEndPointNames.USERS + "/admin");
        Assert.assertEquals(200, response.statusCode);
        Assert.assertEquals("", response.body);
        Assert.assertEquals("GET", response.getHeaders().get("Allow"));
    }

    @Test
    public void usersListSupportsOptions(){

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        HttpResponse response = http.options(ApiEndPointNames.USERS);
        Assert.assertEquals(200, response.statusCode);
        Assert.assertEquals("", response.body);
        Assert.assertEquals("GET, POST", response.getHeaders().get("Allow"));
    }

    @Test
    public void anonymousUserCanGetListOfUsernames() {

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        //http.setBasicAuth("user", "password");
        HttpResponse response = http.get(ApiEndPointNames.USERS);

        System.out.println(response.body);

        Assert.assertEquals(200, response.statusCode);
        String bodyWithNoWhiteSpace = response.body.replaceAll("\\s", "");
        Assert.assertFalse(bodyWithNoWhiteSpace.contains("password"));
        Assert.assertTrue(bodyWithNoWhiteSpace.contains("<user><username>superadmin</username></user>"));
        Assert.assertTrue(bodyWithNoWhiteSpace.contains("<user><username>admin</username></user>"));
        Assert.assertTrue(bodyWithNoWhiteSpace.contains("<user><username>user</username></user>"));

    }

    @Test
    public void anonymousUserCanGetPartialFilteredListOfUsernames() {

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        //http.setBasicAuth("user", "password");
        HttpResponse response = http.get(ApiEndPointNames.USERS + "?username=\"admin\"");

        System.out.println(response.body);

        Assert.assertEquals(200, response.statusCode);
        String bodyWithNoWhiteSpace = response.body.replaceAll("\\s", "");
        Assert.assertFalse(bodyWithNoWhiteSpace.contains("password"));
        Assert.assertTrue(bodyWithNoWhiteSpace.contains("<user><username>superadmin</username></user>"));
        Assert.assertTrue(bodyWithNoWhiteSpace.contains("<user><username>admin</username></user>"));
        Assert.assertFalse(bodyWithNoWhiteSpace.contains("<user><username>user</username></user>"));

    }

    @Test
    public void anonymousUserCanGetFilteredListOfUsernames() {

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        //http.setBasicAuth("user", "password");
        HttpResponse response = http.get(ApiEndPointNames.USERS + "?username=admin");

        System.out.println(response.body);

        Assert.assertEquals(200, response.statusCode);

        String bodyWithNoWhiteSpace = response.body.replaceAll("\\s", "");
        Assert.assertFalse(bodyWithNoWhiteSpace.contains("password"));
        Assert.assertFalse(bodyWithNoWhiteSpace.contains("<user><username>superadmin</username></user>"));
        Assert.assertTrue(bodyWithNoWhiteSpace.contains("<user><username>admin</username></user>"));
        Assert.assertFalse(bodyWithNoWhiteSpace.contains("<user><username>user</username></user>"));

    }


    @Test
    public void userAmendTheirPassword() {

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        http.setHeader(http.HEADER_CONTENT_TYPE, http.CONTENT_XML);
        http.setBasicAuth("user", "password");
        String body = "<user><password>newPassword</password></user>";
        HttpResponse response = http.put(ApiEndPointNames.USERS + "/user/password",body);

        System.out.println(response.body);

        Assert.assertEquals(204, response.statusCode);

        // check password changed
        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        http.setBasicAuth("user", "newPassword");
        response = http.get(ApiEndPointNames.USERS + "/user");
        Assert.assertEquals(200, response.statusCode);


        // set it back
        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        http.setHeader(http.HEADER_CONTENT_TYPE, http.CONTENT_XML);
        http.setBasicAuth("user", "newPassword");
        body = "<user><password>password</password></user>";
        response = http.put(ApiEndPointNames.USERS + "/user/password",body);
        Assert.assertEquals(204, response.statusCode);
    }

    @Test
    public void userAmendTheirApiKey() {

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        http.setHeader(http.HEADER_CONTENT_TYPE, http.CONTENT_XML);
        http.setBasicAuth("user", "password");
        String body = "<user><apikey>newApiKeyThisIsIt</apikey></user>";
        HttpResponse response = http.put(ApiEndPointNames.USERS + "/user/apikey",body);

        System.out.println(response.body);

        Assert.assertEquals(204, response.statusCode);

        // check apikey changed by using it
        http.deleteHeader(http.HEADER_AUTHORIZATION);
        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        http.setHeader("X-API-AUTH","newApiKeyThisIsIt");
        response = http.get(ApiEndPointNames.USERS + "/user");
        System.out.println(response.body);
        Assert.assertEquals(200, response.statusCode);
        Assert.assertTrue(response.body.contains("newApiKeyThisIsIt"));

    }

    @Test
    public void adminCanCreateUsers() {

        // check user does not exist
        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        http.setHeader(http.HEADER_CONTENT_TYPE, http.CONTENT_XML);
        http.setBasicAuth("user", "password");
        HttpResponse response = http.get(ApiEndPointNames.USERS);

        Assert.assertFalse(response.body.contains("<username>newuser101</username>"));

        // create user
        http.deleteHeader(http.HEADER_AUTHORIZATION);
        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        http.setHeader(http.HEADER_CONTENT_TYPE, http.CONTENT_XML);
        http.setBasicAuth("admin", "password");
        String body = "<user><username>newuser101</username><password>password</password></user>";
        response = http.post(ApiEndPointNames.USERS,body);

        System.out.println(response.body);

        Assert.assertEquals(201, response.statusCode);


        // Fail to create duplicate user
        http.deleteHeader(http.HEADER_AUTHORIZATION);
        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        http.setHeader(http.HEADER_CONTENT_TYPE, http.CONTENT_XML);
        http.setBasicAuth("admin", "password");
        body = "<user><username>newuser101</username><password>password</password></user>";
        response = http.post(ApiEndPointNames.USERS,body);

        System.out.println(response.body);

        Assert.assertEquals(409, response.statusCode);

        // user can check that they exist
        http.deleteHeader(http.HEADER_AUTHORIZATION);
        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        http.setBasicAuth("user", "password");
        response = http.get(ApiEndPointNames.USERS );
        System.out.println(response.body);
        Assert.assertEquals(200, response.statusCode);
        Assert.assertTrue(response.body.contains("<username>newuser101</username>"));

    }

}
