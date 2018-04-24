package uk.co.compendiumdev.restlisticator.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.domain.app.ListOfListicatorLists;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;
import uk.co.compendiumdev.restlisticator.http.HttpStatusCode;
import uk.co.compendiumdev.restlisticator.testappconfig.FeatureToggles;

import java.io.IOException;

import static uk.co.compendiumdev.strings.Quoter.dbq;

public class ListCreationApiTest {

    ApiRequest apiRequest;
    ApiResponse apiResponse;
    TheListicator listicator;
    Api api;

    @Before
    public void resetRequestAndAPI(){

        apiRequest = new TestApiRequest();
        apiResponse = new TestApiResponse();

        listicator = new TheListicator();
        api = new Api(listicator);

    }

    @Test
    public void needToBeAuthenticatedToCreateListViaApiWithAListJSON(){


        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        String sentRequest = dbq("{'title':'title','description':'this description'}");

        apiRequest.setVerb("post");
        apiRequest.setBody(sentRequest);

        System.out.println(sentRequest);

        ApiResponse response = api.setLists(apiRequest, apiResponse);


        Assert.assertEquals(HttpStatusCode.UNAUTHORIZED_401,apiResponse.getStatus());
        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(0, listicator.getLists().listCount());

    }


    @Test
    public void aNormalUserCanNotCreateMultipleListsViaApiWithAList(){


        apiRequest.setUserDetails("user", "password");

        String sentRequest = dbq("{'lists':[{'title':'title'},{'title':'title2'}]}");

        apiRequest.setVerb("post");
        apiRequest.setBody(sentRequest);

        System.out.println(sentRequest);

        ApiResponse response = api.setLists(apiRequest, apiResponse);


        Assert.assertEquals(HttpStatusCode.FORBIDDEN_403,apiResponse.getStatus());
        System.out.println(response.getBody());
        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(0, listicator.getLists().listCount());
    }

    @Test
    public void adminCanCreateListsViaApiWithAList(){

        apiRequest.setUserDetails("admin", "password");

        String sentRequest = dbq("{'lists':[{'title':'title'},{'title':'title2'}]}");

        apiRequest.setVerb("post");
        apiRequest.setBody(sentRequest);

        System.out.println(sentRequest);

        ApiResponse response = api.setLists(apiRequest, apiResponse);

        Assert.assertNotEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.CREATED_201,apiResponse.getStatus());

        Assert.assertEquals(2, listicator.getLists().listCount());

        for(ListicatorList aList : listicator.getLists().getAsList()){
            Assert.assertEquals(aList.getOwner(), "admin");
        }
    }

    @Test
    public void canCreateListsViaApiWithListsXML(){


        apiRequest.setUserDetails("user", "password");

        apiRequest.setRequestFormat(ApiRequestResponseFormat.XML);
        String sentRequest = dbq("<lists><list><title>title</title><description>description</description></list></lists>");

        apiRequest.setVerb("post");
        apiRequest.setBody(sentRequest);

        System.out.println(sentRequest);

        ApiResponse response = api.setLists(apiRequest, apiResponse);

        Assert.assertNotEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.CREATED_201,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());

        // check that the user is marked as the owner
        ListOfListicatorLists lists = listicator.getLists();

    }



    @Test
    public void canCreateListViaApiWithAListJSON(){


        apiRequest.setUserDetails("user", "password");
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        String sentRequest = dbq("{'title':'title','description':'this description'}");

        apiRequest.setVerb("post");
        apiRequest.setBody(sentRequest);

        System.out.println(sentRequest);


        ApiResponse response = api.setLists(apiRequest, apiResponse);

        Assert.assertNotEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.CREATED_201,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());

    }



    @Test
    public void listCreationWithGuid() throws IOException {



        apiRequest.setUserDetails("user", "password");
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        String forcedGuid = "0001-sdfidif-werewore";
        String sentRequest = dbq("{'title':'title4','author':'author2', 'guid':'" + forcedGuid + "'}");

        apiRequest.setVerb("post");
        apiRequest.setBody(sentRequest);

        System.out.println(sentRequest);


        ApiResponse response = api.setLists(apiRequest, apiResponse);

        Assert.assertNotEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.CREATED_201,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());

        for(ListicatorList list : listicator.getLists().getAsList()){
            Assert.assertEquals(forcedGuid, list.getGUID());
        }

    }

    @Test
    public void listEntityCreationWithGuidInUrl() throws IOException {


        apiRequest.setUserDetails("user", "password");
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        String forcedGuid = "0001-sdfidif-werewore";
        String sentRequest = dbq("{'title':'title4','author':'author2'}");

        String[] parts = {forcedGuid};
        apiRequest.setPathParts(parts);
        apiRequest.setBody(sentRequest);
        apiRequest.setVerb("post");

        System.out.println(sentRequest);


        ApiResponse response = api.partialAmendList(apiRequest, apiResponse);

        Assert.assertEquals(HttpStatusCode.CREATED_201,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());

        for(ListicatorList list : listicator.getLists().getAsList()){
            Assert.assertEquals(forcedGuid, list.getGUID());
        }
    }

    @Test
    public void bug008_cannotCreatelistEntityWithGuidInUrl() throws IOException {


        FeatureToggles.BUG_008_FIXED.setState(false);

        apiRequest.setUserDetails("user", "password");
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        String forcedGuid = "0001-sdfidif-werewore";
        String sentRequest = dbq("{'title':'title4','author':'author2'}");

        String[] parts = {forcedGuid};
        apiRequest.setPathParts(parts);
        apiRequest.setBody(sentRequest);
        apiRequest.setVerb("post");

        System.out.println(sentRequest);


        ApiResponse response = api.partialAmendList(apiRequest, apiResponse);

        Assert.assertEquals(HttpStatusCode.NOT_FOUND_404,apiResponse.getStatus());

        Assert.assertEquals(0, listicator.getLists().listCount());

        FeatureToggles.BUG_008_FIXED.setState(true);
    }

    @Test
    public void bug009_listEntityCreationWithGuidInUrlReturns200() throws IOException {


        FeatureToggles.BUG_009_FIXED.setState(false);

        apiRequest.setUserDetails("user", "password");
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        String forcedGuid = "0001-sdfidif-werewore";
        String sentRequest = dbq("{'title':'title4','author':'author2'}");

        String[] parts = {forcedGuid};
        apiRequest.setPathParts(parts);
        apiRequest.setBody(sentRequest);
        apiRequest.setVerb("post");

        System.out.println(sentRequest);


        ApiResponse response = api.partialAmendList(apiRequest, apiResponse);

        Assert.assertEquals(HttpStatusCode.OK_200,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());

        for(ListicatorList list : listicator.getLists().getAsList()){
            Assert.assertEquals(forcedGuid, list.getGUID());
        }

        FeatureToggles.BUG_009_FIXED.setState(true);
    }

    @Test
    public void canCreateListViaApiWithASingleListXML(){


        apiRequest.setVerb("post");
        apiRequest.setUserDetails("user", "password");
        apiRequest.setRequestFormat(ApiRequestResponseFormat.XML);
        String sentRequest = dbq("<list><title>title</title><description>description</description></list>");

        apiRequest.setBody(sentRequest);

        System.out.println(sentRequest);


        ApiResponse response = api.setLists(apiRequest, apiResponse);

        Assert.assertEquals(HttpStatusCode.CREATED_201,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());


        // check resourse is added in the body
        Assert.assertNotEquals("", response.getBody());
        System.out.println(response.getBody());



        // check location is returned as a URI
        Assert.assertTrue(response.getHeaders().containsKey(ApiResponse.HEADER_LOCATION));
        String newGuid = response.getHeaders().get(ApiResponse.HEADER_LOCATION).replaceFirst("/lists/", "");

        TestApiRequest getRequest = new TestApiRequest();
        String []id = {newGuid};
        apiRequest.setPathParts(id);

        ApiResponse apiReadResponse = new TestApiResponse();
        ApiResponse readResponse = api.getList(getRequest, apiReadResponse);

        readResponse.getBody().contains("title:title");


    }







  





}
