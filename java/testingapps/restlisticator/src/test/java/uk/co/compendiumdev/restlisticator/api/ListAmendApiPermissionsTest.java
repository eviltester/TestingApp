package uk.co.compendiumdev.restlisticator.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;
import uk.co.compendiumdev.restlisticator.http.HttpStatusCode;

import static uk.co.compendiumdev.strings.Quoter.dbq;

/**
 * Created by Alan on 10/09/2017.
 */
public class ListAmendApiPermissionsTest {

    TheListicator listicator;
    Api api;
    ApiRequest apiRequest;
    ApiResponse apiResponse;

    @Before
    public void setupData(){

        listicator = new TheListicator();
        api = new Api(listicator);
        apiRequest = new TestApiRequest();
        apiResponse = new TestApiResponse();
    }

    @Test
    public void adminCanAmendAnyList(){

        ListicatorList list = new ListicatorList("test", "description");
        list.forceSetOwner("bob");
        listicator.addList(list);

        String []id = {list.getGUID()};

        apiRequest.setVerb("post");
        apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(dbq("{'title':'new title','description':'new description','createdDate':'2013-03-04-05-06-07'}"));

        apiRequest.setUserDetails("admin", "password");
        ApiResponse response = api.partialAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.OK_200,apiResponse.getStatus());


        System.out.println(response.getBody());

        Assert.assertEquals("new title", list.getTitle());
        Assert.assertEquals("new description", list.getDescription());
        Assert.assertEquals("2013-03-04-05-06-07", list.getCreatedDate());
    }

    @Test
    public void userCanNotAmendListTheyAreNotOwnerFor(){

        ListicatorList list = new ListicatorList("test", "description");
        list.forceSetOwner("bob");
        listicator.addList(list);

        String []id = {list.getGUID()};

        apiRequest.setVerb("post");
        apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(dbq("{'title':'new title','description':'new description','createdDate':'2013-03-04-05-06-07'}"));

        apiRequest.setUserDetails("user", "password");
        ApiResponse response = api.partialAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.FORBIDDEN_403,apiResponse.getStatus());


        System.out.println(response.getBody());

        Assert.assertEquals("test", list.getTitle());
        Assert.assertEquals("description", list.getDescription());
    }

    @Test
    public void userCanAmendListTheyOwn(){

        ListicatorList list = new ListicatorList("test", "description");
        list.forceSetOwner("user");
        listicator.addList(list);

        String []id = {list.getGUID()};

        apiRequest.setVerb("post");
        apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(dbq("{'title':'new title','description':'new description','createdDate':'2013-03-04-05-06-07'}"));

        apiRequest.setUserDetails("user", "password");
        ApiResponse response = api.partialAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.OK_200,apiResponse.getStatus());


        System.out.println(response.getBody());

        Assert.assertEquals("new title", list.getTitle());
        Assert.assertEquals("new description", list.getDescription());
    }

    @Test
    public void userCanAmendUnownedList(){

        ListicatorList list = new ListicatorList("test", "description");
        list.forceSetOwner("");
        listicator.addList(list);

        String []id = {list.getGUID()};

        apiRequest.setVerb("post");
        apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(dbq("{'title':'new title','description':'new description','createdDate':'2013-03-04-05-06-07'}"));

        apiRequest.setUserDetails("user", "password");
        ApiResponse response = api.partialAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.OK_200,apiResponse.getStatus());


        System.out.println(response.getBody());

        Assert.assertEquals("new title", list.getTitle());
        Assert.assertEquals("new description", list.getDescription());
    }

    @Test
    public void userCanAmendAdminOwnedList(){

        ListicatorList list = new ListicatorList("test", "description");
        list.forceSetOwner("admin");
        listicator.addList(list);

        String []id = {list.getGUID()};

        apiRequest.setVerb("post");
        apiRequest.setPathParts(id);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(dbq("{'title':'new title','description':'new description','createdDate':'2013-03-04-05-06-07'}"));

        apiRequest.setUserDetails("user", "password");
        ApiResponse response = api.partialAmendList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.OK_200,apiResponse.getStatus());


        System.out.println(response.getBody());

        Assert.assertEquals("new title", list.getTitle());
        Assert.assertEquals("new description", list.getDescription());
    }
}
