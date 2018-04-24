package uk.co.compendiumdev.restlisticator.api;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;
import uk.co.compendiumdev.restlisticator.http.HttpStatusCode;

public class ListDeleteAPITest {

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
    public void cannotDeleteAListIfNotAuthenticated(){


        Assert.assertEquals(0, listicator.getLists().listCount());

        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        String []id = {list.getGUID()};

        apiRequest.setPathParts(id);

        ApiResponse response = api.deleteList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.UNAUTHORIZED_401,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());
    }

    @Test
    public void cannotDeleteAListIfIDoNotOwnIt(){


        apiRequest.setUserDetails("user", "password");
        Assert.assertEquals(0, listicator.getLists().listCount());

        ListicatorList list = new ListicatorList("test", "description");
        list.forceSetOwner("bob");
        listicator.addList(list);

        String []id = {list.getGUID()};

        apiRequest.setPathParts(id);

        ApiResponse response = api.deleteList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.FORBIDDEN_403,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());
    }

    @Test
    public void canDeleteAListIOwn(){


        apiRequest.setUserDetails("user", "password");
        Assert.assertEquals(0, listicator.getLists().listCount());

        ListicatorList list = new ListicatorList("test", "description");
        list.forceSetOwner("user");
        listicator.addList(list);

        String []id = {list.getGUID()};

        apiRequest.setPathParts(id);

        ApiResponse response = api.deleteList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.NO_CONTENT_204,apiResponse.getStatus());

        Assert.assertEquals(0, listicator.getLists().listCount());
    }

    @Test
    public void canDeleteAnAdminCreatedList(){


        apiRequest.setUserDetails("user", "password");
        Assert.assertEquals(0, listicator.getLists().listCount());

        ListicatorList list = new ListicatorList("test", "description");
        list.forceSetOwner("admin");
        listicator.addList(list);

        String []id = {list.getGUID()};

        apiRequest.setPathParts(id);

        ApiResponse response = api.deleteList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.NO_CONTENT_204,apiResponse.getStatus());

        Assert.assertEquals(0, listicator.getLists().listCount());
    }

    @Test
    public void canDeleteAnUnownedList(){


        apiRequest.setUserDetails("user", "password");
        Assert.assertEquals(0, listicator.getLists().listCount());

        ListicatorList list = new ListicatorList("test", "description");
        list.forceSetOwner("");
        listicator.addList(list);

        String []id = {list.getGUID()};

        apiRequest.setPathParts(id);

        ApiResponse response = api.deleteList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.NO_CONTENT_204,apiResponse.getStatus());

        Assert.assertEquals(0, listicator.getLists().listCount());
    }

    @Test
    public void adminCanDeleteAnyListViaApi(){


        Assert.assertEquals(0, listicator.getLists().listCount());

        ListicatorList list = new ListicatorList("test", "description");
        list.forceSetOwner("bob");
        listicator.addList(list);

        String []id = {list.getGUID()};

        apiRequest.setPathParts(id);

        apiRequest.setUserDetails("admin", "password");
        ApiResponse response = api.deleteList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.NO_CONTENT_204,apiResponse.getStatus());

        Assert.assertEquals(0, listicator.getLists().listCount());
    }

    @Test
    public void cannotDeleteAListIfGUIDWrongViaApi(){


        Assert.assertEquals(0, listicator.getLists().listCount());

        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        String []id = {list.getGUID()+"ccccccc"};

        apiRequest.setPathParts(id);

        apiRequest.setUserDetails("admin", "password");
        ApiResponse response = api.deleteList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(
                    HttpStatusCode.NOT_FOUND_404,
                    apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());

    }

    @Test
    public void cannotDeleteAListIfNoGUIDViaApi(){



        Assert.assertEquals(0, listicator.getLists().listCount());

        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        ApiResponse response = api.deleteList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(
                    HttpStatusCode.BAD_REQUEST_400,
                    apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());

    }
}
