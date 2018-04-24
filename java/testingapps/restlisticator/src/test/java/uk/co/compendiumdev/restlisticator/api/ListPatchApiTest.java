package uk.co.compendiumdev.restlisticator.api;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;
import uk.co.compendiumdev.restlisticator.http.HttpStatusCode;
import uk.co.compendiumdev.restlisticator.testappconfig.FeatureToggles;

import static uk.co.compendiumdev.strings.Quoter.dbq;

public class ListPatchApiTest {

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
    public void cannotPatchAListIfGUIDWrongViaApi(){



        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        String []id = {list.getGUID()+"ccccccc"};


        apiRequest.setVerb("patch");
        apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(dbq("{'title':'new title','description':'new description','createdDate':'2013-03-04-05-06-07'}"));


        apiRequest.setUserDetails("admin", "password");
        ApiResponse response = api.patchAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.NOT_FOUND_404,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());
    }

    @Test
    public void cannotPatchAListIfNotAuthenticated(){

        TheListicator listicator = new TheListicator();
        Api api = new Api(listicator);


        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        String []id = {list.getGUID()};

        ApiRequest apiRequest = new TestApiRequest();
        apiRequest.setVerb("patch");
        apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(dbq("{'title':'new title','description':'new description','createdDate':'2013-03-04-05-06-07'}"));

        ApiResponse apiResponse = new TestApiResponse();
        //apiRequest.setUserDetails("admin", "password");
        ApiResponse response = api.patchAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.UNAUTHORIZED_401,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());
    }

    @Test
    public void cannotPatchAListIfNoCorrectPermissions(){

        TheListicator listicator = new TheListicator();
        Api api = new Api(listicator);


        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        String []id = {list.getGUID()};

        ApiRequest apiRequest = new TestApiRequest();
        apiRequest.setVerb("patch");
        apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(dbq("{'title':'new title','description':'new description','createdDate':'2013-03-04-05-06-07'}"));

        ApiResponse apiResponse = new TestApiResponse();
        apiRequest.setUserDetails("user", "password");
        ApiResponse response = api.patchAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.FORBIDDEN_403,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());
    }


    @Test
    public void bug005_canPatchAListIfNoCorrectPermissions(){

        TheListicator listicator = new TheListicator();
        Api api = new Api(listicator);


        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        String []id = {list.getGUID()};

        ApiRequest apiRequest = new TestApiRequest();
        apiRequest.setVerb("patch");
        apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(dbq("{'title':'new title','description':'new description','createdDate':'2013-03-04-05-06-07'}"));


        // user without permissions can patch
        FeatureToggles.BUG_005_FIXED.setState(false);
        ApiResponse apiResponse = new TestApiResponse();
        apiRequest.setUserDetails("user", "password");
        ApiResponse response = api.patchAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.OK_200,apiResponse.getStatus());

        // fixed user without permissions can not patch
        FeatureToggles.BUG_005_FIXED.setState(true);
        apiResponse = new TestApiResponse();
        apiRequest.setUserDetails("user", "password");
        response = api.patchAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.FORBIDDEN_403,apiResponse.getStatus());


        Assert.assertEquals(1, listicator.getLists().listCount());
    }


    @Test
    public void canPatchAListWithGUID(){

        TheListicator listicator = new TheListicator();
        Api api = new Api(listicator);


        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        String []id = {list.getGUID()};

        ApiRequest apiRequest = new TestApiRequest();
        apiRequest.setVerb("patch");
        apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(dbq("{'title':'new title','description':'new description','createdDate':'2013-03-04-05-06-07'}"));

        ApiResponse apiResponse = new TestApiResponse();

        apiRequest.setUserDetails("admin", "password");
        ApiResponse response = api.patchAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.OK_200,apiResponse.getStatus());


        System.out.println(response.getBody());

        Assert.assertEquals("new title", list.getTitle());
        Assert.assertEquals("new description", list.getDescription());
        Assert.assertEquals("2013-03-04-05-06-07", list.getCreatedDate());
    }

    @Test
    public void canPatchAListWithInvalidFieldsGUID(){

        TheListicator listicator = new TheListicator();
        Api api = new Api(listicator);


        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        String []id = {list.getGUID()};

        ApiRequest apiRequest = new TestApiRequest();
        apiRequest.setVerb("patch");
        apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(dbq("{'title':'new title','descrisdfdfsption':'new description','createdDate':'2013-03-04-05-06-07'}"));

        ApiResponse apiResponse = new TestApiResponse();

        apiRequest.setUserDetails("admin", "password");
        ApiResponse response = api.patchAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.OK_200,apiResponse.getStatus());


        System.out.println(response.getBody());

        Assert.assertEquals("new title", list.getTitle());
        Assert.assertEquals("description", list.getDescription());
        Assert.assertEquals("2013-03-04-05-06-07", list.getCreatedDate());
    }

    @Test
    public void canPostAListWithInvalidFieldsGUID(){

        TheListicator listicator = new TheListicator();
        Api api = new Api(listicator);


        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        String []id = {list.getGUID()};

        ApiRequest apiRequest = new TestApiRequest();
        apiRequest.setVerb("post");
        apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(dbq("{'title':'new title','descrisdfdfsption':'new description','createdDate':'2013-03-04-05-06-07'}"));

        ApiResponse apiResponse = new TestApiResponse();

        apiRequest.setUserDetails("admin", "password");
        ApiResponse response = api.patchAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.OK_200,apiResponse.getStatus());


        System.out.println(response.getBody());

        Assert.assertEquals("new title", list.getTitle());
        Assert.assertEquals("description", list.getDescription());
        Assert.assertEquals("2013-03-04-05-06-07", list.getCreatedDate());
    }


    @Test
    public void canNotPatchAListWithInvalidInputJSON(){

        TheListicator listicator = new TheListicator();
        Api api = new Api(listicator);


        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        String []id = {list.getGUID()};

        ApiRequest apiRequest = new TestApiRequest();
        apiRequest.setVerb("patch");
        apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);

        // missing closing }
        apiRequest.setBody(dbq("{'title':'new title','descrisdfdfsption':'new description','createdDate':'2013-03-04-05-06-07'"));

        ApiResponse apiResponse = new TestApiResponse();

        apiRequest.setUserDetails("admin", "password");
        ApiResponse response = api.patchAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.BAD_REQUEST_400,apiResponse.getStatus());


        System.out.println(response.getBody());

        Assert.assertEquals("test", list.getTitle());
        Assert.assertEquals("description", list.getDescription());
        Assert.assertNotEquals("2013-03-04-05-06-07", list.getCreatedDate());
    }


    @Test
    public void canNotPatchAListWithInvalidInputXML(){

        TheListicator listicator = new TheListicator();
        Api api = new Api(listicator);


        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        String []id = {list.getGUID()};

        ApiRequest apiRequest = new TestApiRequest();
        apiRequest.setVerb("patch");
        apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.XML);

        // missing closing }
        apiRequest.setBody(dbq("<list><title>title</title><list>"));

        ApiResponse apiResponse = new TestApiResponse();

        apiRequest.setUserDetails("admin", "password");
        ApiResponse response = api.patchAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.BAD_REQUEST_400,apiResponse.getStatus());


        System.out.println(response.getBody());

        Assert.assertEquals("test", list.getTitle());
        Assert.assertEquals("description", list.getDescription());
        Assert.assertNotEquals("2013-03-04-05-06-07", list.getCreatedDate());
    }

    @Test
    public void canPatchAListWithXML(){

        TheListicator listicator = new TheListicator();
        Api api = new Api(listicator);


        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        String []id = {list.getGUID()};

        ApiRequest apiRequest = new TestApiRequest();
        apiRequest.setVerb("patch");
        apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.XML);

        // missing closing }
        apiRequest.setBody(dbq("<list><title>title</title></list>"));

        ApiResponse apiResponse = new TestApiResponse();
        apiRequest.setUserDetails("admin", "password");
        ApiResponse response = api.patchAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.OK_200,apiResponse.getStatus());


        System.out.println(response.getBody());

        Assert.assertEquals("title", list.getTitle());
        Assert.assertEquals("description", list.getDescription());
    }

    @Test
    public void cannotPatchAListWithNoGUID(){

        TheListicator listicator = new TheListicator();
        Api api = new Api(listicator);


        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        String []id = {list.getGUID()};

        ApiRequest apiRequest = new TestApiRequest();
        apiRequest.setVerb("patch");

        // do not set the GUID
        //apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.XML);

        // missing closing }
        apiRequest.setBody(dbq("<list><title>title</title></list>"));

        ApiResponse apiResponse = new TestApiResponse();

        ApiResponse response = api.patchAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.BAD_REQUEST_400,apiResponse.getStatus());


        System.out.println(response.getBody());
    }


    @Test
    public void cannotPatchANonExistantListWithNoGUID(){

        TheListicator listicator = new TheListicator();
        Api api = new Api(listicator);


        ListicatorList list = new ListicatorList("test", "description");
        //listicator.addList(list);

        String []id = {list.getGUID()};

        ApiRequest apiRequest = new TestApiRequest();
        apiRequest.setVerb("patch");
        apiRequest.setPathParts(id);
        apiRequest.setUserDetails("admin", "password");

        // do not set the GUID
        //apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.XML);

        // missing closing }
        apiRequest.setBody(dbq("<list><title>title</title></list>"));

        ApiResponse apiResponse = new TestApiResponse();

        ApiResponse response = api.patchAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.NOT_FOUND_404,apiResponse.getStatus());


        System.out.println(response.getBody());
    }


    @Test
    public void bog007_canPatchANonExistantListWithNoGUIDAndCreateList(){

        TheListicator listicator = new TheListicator();
        Api api = new Api(listicator);

        FeatureToggles.BUG_007_FIXED.setState(false);

        ListicatorList list = new ListicatorList("test", "description");
        //listicator.addList(list);

        String []id = {list.getGUID()};

        ApiRequest apiRequest = new TestApiRequest();
        apiRequest.setVerb("patch");
        apiRequest.setPathParts(id);
        apiRequest.setUserDetails("admin", "password");

        // do not set the GUID
        //apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.XML);

        // missing closing }
        apiRequest.setBody(dbq("<list><title>title</title></list>"));

        ApiResponse apiResponse = new TestApiResponse();

        ApiResponse response = api.patchAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.CREATED_201,apiResponse.getStatus());

        FeatureToggles.BUG_007_FIXED.setState(true);

        System.out.println(response.getBody());
    }


}
