package uk.co.compendiumdev.restlisticator.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;
import uk.co.compendiumdev.restlisticator.testappconfig.FeatureToggles;

import static uk.co.compendiumdev.strings.Quoter.dbq;

/**
 * Created by Alan on 06/07/2017.
 */
public class ListReadApiTest {



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
    public void canGetEmptyListsViaAPIWithJson(){

        apiRequest.setResponseFormat(ApiRequestResponseFormat.JSON);

        ApiResponse response = api.getLists(
                apiRequest,
                new TestApiResponse());

        Assert.assertEquals("{\"lists\":[]}", response.getBody());
    }

    @Test
    public void canGetEmptyListsViaAPIWithXML(){


        apiRequest.setResponseFormat(ApiRequestResponseFormat.XML);

        ApiResponse response = api.getLists(
                apiRequest,
                new TestApiResponse());

        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><lists/>",
                response.getBody());
    }

    @Test
    public void canGetListsViaApiWithAListResponseJSON(){

        listicator.addList(new ListicatorList("title", "desc"));

        Assert.assertEquals(1, listicator.getLists().listCount());

        apiRequest.setBody("");
        apiRequest.setResponseFormat(ApiRequestResponseFormat.JSON);
        ApiResponse response = api.getLists(apiRequest, apiResponse);

        System.out.println(response.getBody());

        Assert.assertEquals(200,apiResponse.getStatus());
        Assert.assertTrue(response.getBody().startsWith(dbq("{'lists':[") +
                dbq("{'guid':" )));

    }

    @Test
    public void canGetListsViaApiWithAListResponseXML(){

        listicator.addList(new ListicatorList("title", "desc"));


        Assert.assertEquals(1, listicator.getLists().listCount());


        apiRequest.setBody("");
        apiRequest.setResponseFormat(ApiRequestResponseFormat.XML);
        ApiResponse response = api.getLists(apiRequest, apiResponse);

        System.out.println(response.getBody());

        Assert.assertEquals(200,apiResponse.getStatus());
        Assert.assertTrue(response.getBody().startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<lists>"));
        Assert.assertTrue(response.getBody().contains("<list>"));

    }

    @Test
    public void canGetListsViaApiWithMultipleListResponseXML(){


        listicator.addList(new ListicatorList("title", "description"));
        listicator.addList(new ListicatorList("title2", "description2"));
        listicator.addList(new ListicatorList("title3", "description3"));

        Assert.assertEquals(3, listicator.getLists().listCount());


        apiRequest.setBody("");
        apiRequest.setResponseFormat(ApiRequestResponseFormat.XML);

        ApiResponse response = api.getLists(apiRequest, apiResponse);

        System.out.println(response.getBody());

        Assert.assertEquals(200,apiResponse.getStatus());
        Assert.assertTrue( "incorrect Start " + response.getBody(),
                response.getBody().startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+
                        "<lists>"));

        // spot checks
        Assert.assertTrue("did not contain list " + response.getBody(), response.getBody().contains(
                "<title>title</title>"));

        Assert.assertTrue("did not contain list " + response.getBody(), response.getBody().contains(
                "<description>description2</description>"));


    }

    @Test
    public void cannotReadAListIfGUIDWrongViaApi(){


        Assert.assertEquals(0, listicator.getLists().listCount());

        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        String []id = {list.getGUID()+"ccccccc"};

        apiRequest.setPathParts(id);

        ApiResponse response = api.getList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(404,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());
    }

    @Test
    public void bug010_400_insteadOf_404_cannotReadAListIfGUIDWrongViaApi(){


        FeatureToggles.BUG_010_FIXED.setState(false);


        Assert.assertEquals(0, listicator.getLists().listCount());

        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        String []id = {list.getGUID()+"ccccccc"};

        apiRequest.setPathParts(id);

        ApiResponse response = api.getList(apiRequest, apiResponse);

        Assert.assertEquals("", response.getBody());
        Assert.assertEquals(400,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());

        FeatureToggles.BUG_010_FIXED.setState(true);
    }


    @Test
    public void canReadAListWithGUID(){


        Assert.assertEquals(0, listicator.getLists().listCount());

        ListicatorList list = new ListicatorList("test", "description");
        listicator.addList(list);

        String []id = {list.getGUID()};

        apiRequest.setResponseFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setPathParts(id);

        ApiResponse response = api.getList(apiRequest, apiResponse);

        System.out.println(response.getBody());
        
        // todo should parse response to make sure it is valid
        Assert.assertTrue(response.getBody().contains(dbq("'title':'test'")));
        Assert.assertTrue(response.getBody().contains(dbq("'description':'description'")));
        Assert.assertTrue(response.getBody().contains(dbq("'guid':'"+ list.getGUID() +"'")));
        Assert.assertEquals(200,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());

    }

    @Test
    public void canReadAListWithFilteredFieldsUsingGUID(){


        Assert.assertEquals(0, listicator.getLists().listCount());

        ListicatorList list = new ListicatorList("test", "description");
        list.forceSetOwner("bob");
        listicator.addList(list);

        String []id = {list.getGUID()};

        apiRequest.setResponseFormat(ApiRequestResponseFormat.JSON);
        apiRequest.setPathParts(id);

        apiRequest.setQuery("without=title,description");

        ApiResponse response = api.getList(apiRequest, apiResponse);

        System.out.println(response.getBody());

        // todo should parse response to make sure it is valid
        Assert.assertFalse(response.getBody().contains(dbq("'title':'test'")));
        Assert.assertFalse(response.getBody().contains(dbq("'description':'description'")));
        Assert.assertTrue(response.getBody().contains(dbq("'guid':'"+ list.getGUID() +"'")));
        Assert.assertTrue(response.getBody().contains(dbq("'owner':'bob'")));
        Assert.assertEquals(200,apiResponse.getStatus());

        Assert.assertEquals(1, listicator.getLists().listCount());

    }


    @Test
    public void canGetPartialFilteredListsViaApiWithMultipleListResponseXML(){

        listicator.addList(new ListicatorList("title", "description"));
        listicator.addList(new ListicatorList("titleis2", "description2"));
        listicator.addList(new ListicatorList("titleis3", "description3"));

        Assert.assertEquals(3, listicator.getLists().listCount());


        apiRequest.setBody("");
        apiRequest.setQuery("title=\"is\"");
        apiRequest.setResponseFormat(ApiRequestResponseFormat.XML);

        ApiResponse response = api.getLists(apiRequest, apiResponse);

        System.out.println(response.getBody());

        Assert.assertEquals(200,apiResponse.getStatus());
        Assert.assertTrue( "incorrect Start " + response.getBody(),
                response.getBody().startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+
                        "<lists>"));

        // spot checks
        Assert.assertFalse("should not contain list " + response.getBody(), response.getBody().contains(
                "<title>title</title>"));
        Assert.assertTrue("should contain list titleis2" + response.getBody(), response.getBody().contains(
                "<title>titleis2</title>"));
        Assert.assertTrue("should contain list titleis3" + response.getBody(), response.getBody().contains(
                "<title>titleis3</title>"));
    }

    @Test
    public void canGetExactFilteredListsViaApiWithMultipleListResponseXML(){


        listicator.addList(new ListicatorList("title", "description"));
        listicator.addList(new ListicatorList("titleis2", "description2"));
        listicator.addList(new ListicatorList("titleis3", "description3"));

        Assert.assertEquals(3, listicator.getLists().listCount());



        apiRequest.setBody("");
        apiRequest.setQuery("title=titleis2");
        apiRequest.setResponseFormat(ApiRequestResponseFormat.XML);

        ApiResponse response = api.getLists(apiRequest, apiResponse);

        System.out.println(response.getBody());

        Assert.assertEquals(200,apiResponse.getStatus());
        Assert.assertTrue( "incorrect Start " + response.getBody(),
                response.getBody().startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+
                        "<lists>"));

        // spot checks
        Assert.assertFalse("should not contain list " + response.getBody(), response.getBody().contains(
                "<title>title</title>"));
        Assert.assertTrue("should contain list titleis2" + response.getBody(), response.getBody().contains(
                "<title>titleis2</title>"));
        Assert.assertFalse("should not contain list titleis3" + response.getBody(), response.getBody().contains(
                "<title>titleis3</title>"));
    }
}
