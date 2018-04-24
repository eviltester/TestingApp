package uk.co.compendiumdev.restlisticator.api;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;
import uk.co.compendiumdev.restlisticator.http.HttpStatusCode;

import java.util.UUID;

import static uk.co.compendiumdev.strings.Quoter.dbq;

public class ListPutAPITest {

    // put to non existent when guid is given in the url

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
    public void canPUTtoANonExistantGUIDInURLToCreateNewJSON(){

        apiRequest.setUserDetails("user", "password");

        String sentRequest = dbq("{'title':'title'," +
                "'description':'thisdesc','createdDate':'2017-07-19-16-17-47','amendedDate':'2017-07-19-16-17-47'"+
                "}");

        String []guid= {UUID.randomUUID().toString()};
        apiRequest.setPathParts(guid);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(sentRequest);

        System.out.println(sentRequest);

        ApiResponse response = api.putList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(201,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());

        // does it have the GUID we created?
        Assert.assertNotNull(listicator.getList(guid[0]));
    }


    @Test
    public void canNotPUTIfNoUserDetailsSupplied(){


        String sentRequest = dbq("{'title':'title'," +
                "'description':'no user details supplied','createdDate':'2017-07-19-16-17-47','amendedDate':'2017-07-19-16-17-47'"+
                "}");

        String []guid= {UUID.randomUUID().toString()};
        apiRequest.setPathParts(guid);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(sentRequest);

        System.out.println(sentRequest);

        ApiResponse response = api.putList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(HttpStatusCode.UNAUTHORIZED_401,apiResponse.getStatus());

    }

    @Test
    public void canPUTtoNoGUIDToCreateNewJSON(){


        String sentRequest = dbq("{'title':'title'," +
                "'guid':'12435','description':'thisdesc','createdDate':'2017-07-19-16-17-47','amendedDate':'2017-07-19-16-17-47'"+
                                "}");

        apiRequest.setUserDetails("user", "password");
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(sentRequest);

        System.out.println(sentRequest);

        ApiResponse response = api.putList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(201,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());
    }

    @Test
    public void canNotPUTWithPartialDetails(){


        String sentRequest = dbq("{'title':'title'}");

        apiRequest.setUserDetails("user", "password");
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(sentRequest);

        System.out.println(sentRequest);

        ApiResponse response = api.putList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(400,apiResponse.getStatus());

        Assert.assertEquals(0, listicator.getLists().listCount());
    }

    @Test
    public void canPUTtoNoGUIDToCreateNewXML(){


        String sentRequest = dbq("<list><guid>12345</guid><title>title</title><description>desc</description>" +
                            "<createdDate>2017-07-19-16-17-47</createdDate>" +
                            "<amendedDate>2017-07-19-16-17-47</amendedDate>" +
                            "</list>");

        apiRequest.setUserDetails("user", "password");

        apiRequest.setRequestFormat(ApiRequestResponseFormat.XML);
        apiRequest.setBody(sentRequest);

        System.out.println(sentRequest);

        ApiResponse response = api.putList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(201,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());
    }

    @Test
    public void failToCreateWhenMalformedJSON(){


        String sentRequest = dbq("<list><title>title</title></list>");

        apiRequest.setUserDetails("user", "password");

        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(sentRequest);

        System.out.println(sentRequest);

        ApiResponse response = api.putList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(400,apiResponse.getStatus());

        Assert.assertEquals(0, listicator.getLists().listCount());
    }

    @Test
    public void failToCreateWhenMalformedXML(){



        String sentRequest = dbq("<list><title>title</title><list>");

        apiRequest.setUserDetails("user", "password");
        apiRequest.setRequestFormat(ApiRequestResponseFormat.XML);
        apiRequest.setBody(sentRequest);

        System.out.println(sentRequest);

        ApiResponse response = api.putList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(400,apiResponse.getStatus());

        Assert.assertEquals(0, listicator.getLists().listCount());
    }

    @Test
    public void canPUTtoExistantGUIDToAmendJSON(){


        apiRequest.setUserDetails("user", "password");
        ListicatorList existing = new ListicatorList("title", "Description");
        listicator.addList(existing);


        String sentRequest = dbq("{'title':'amended title', 'description':'" + existing.getDescription() + "'" +
                            ",'createdDate':'" +existing.getCreatedDate() + "'" +
                            ",'amendedDate':'" + existing.getAmendedDate()  + "'" +
                            "}");

        String []guid= {existing.getGUID()};
        apiRequest.setPathParts(guid);
        apiRequest.setRequestFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setBody(sentRequest);

        System.out.println(sentRequest);

        ApiResponse response = api.putList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(200,apiResponse.getStatus());
        Assert.assertEquals("amended title",existing.getTitle());
        Assert.assertEquals("Description",existing.getDescription());

        Assert.assertEquals(1, listicator.getLists().listCount());
    }
}
