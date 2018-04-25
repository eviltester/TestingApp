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


import java.io.IOException;
import java.util.UUID;

import static uk.co.compendiumdev.strings.Quoter.dbq;

public class ListsEntityEndpointTest{

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
    public void cannotUseIncorrectVerbOnSpecificList(){

        String apiUrl = ApiEndPointNames.LISTS + "/12345";

        HttpResponse response = http.head(apiUrl);
        Assert.assertEquals(405, response.statusCode);

        response = http.trace(apiUrl);
        Assert.assertEquals(405, response.statusCode);

        response = http.connect(apiUrl);
        Assert.assertEquals(405, response.statusCode);
    }

    @Test
    public void listsEntitySupportsOptions(){

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        HttpResponse response = http.options(ApiEndPointNames.LISTS + "/1234567");
        //TODO should be 404 if it does not exist
        Assert.assertEquals(200, response.statusCode);
        Assert.assertEquals("", response.body);
        Assert.assertEquals("GET, POST, PUT, PATCH, DELETE", response.getHeaders().get("Allow"));
    }

    @Test
    public void canReadAListViaApi() throws IOException {

        String guid = "12345678-asdfsdfsd";
        String xmlList = dbq(String.format("<list><guid>%s</guid><title>title4</title></list>", guid));

        http.setHeader(http.HEADER_CONTENT_TYPE,http.CONTENT_XML);
        http.setBasicAuth("user", "password");
        HttpResponse response = http.post(ApiEndPointNames.LISTS, xmlList);
        Assert.assertEquals(201, response.statusCode);

        http.setHeader(http.HEADER_ACCEPT, http.CONTENT_JSON);
        response = http.get(ApiEndPointNames.LISTS+"/"+guid);
        Assert.assertEquals(200, response.statusCode);
        Assert.assertTrue(response.body.contains(dbq(String.format("'guid':'%s'",guid))));

        response = http.get(ApiEndPointNames.LISTS+"/"+guid+"sdfsdfnsfd");
        Assert.assertEquals(404, response.statusCode);
    }

    @Test
    public void canReadAFilteredListWithoutFieldsViaApi() throws IOException {

        String guid = "12345678-asdfsdfsd";
        String xmlList = dbq(String.format("<list><guid>%s</guid><title>title4</title></list>", guid));

        http.setHeader(http.HEADER_CONTENT_TYPE,http.CONTENT_XML);
        http.setBasicAuth("user", "password");
        HttpResponse response = http.post(ApiEndPointNames.LISTS, xmlList);
        Assert.assertEquals(201, response.statusCode);

        http.setHeader(http.HEADER_ACCEPT, http.CONTENT_JSON);
        response = http.get(ApiEndPointNames.LISTS+"/"+guid + "?without=guid");
        Assert.assertEquals(200, response.statusCode);
        Assert.assertFalse(response.body.contains(dbq(String.format("'guid':'%s'",guid))));

    }

    @Test
    public void listDeletionWithJustTitleUsingXml() throws IOException {

        String guid = "12345678-asdfsdfsd";
        String xmlList = dbq(String.format("<list><guid>%s</guid><title>title4</title></list>", guid));

        http.setHeader(http.HEADER_CONTENT_TYPE,http.CONTENT_XML);
        http.setBasicAuth("user", "password");
        HttpResponse response = http.post(ApiEndPointNames.LISTS, xmlList);
        Assert.assertEquals(201, response.statusCode);

        http.setHeader(http.HEADER_ACCEPT, http.CONTENT_JSON);
        response = http.get(ApiEndPointNames.LISTS);
        Assert.assertEquals(200, response.statusCode);
        Assert.assertTrue(response.body.contains(dbq(String.format("'guid':'%s'",guid))));

        // normal user cannot delete - make user admin
        http.setBasicAuth("admin", "password");
        response = http.delete(ApiEndPointNames.LISTS+"/"+guid);
        Assert.assertEquals(204, response.statusCode);

        http.setHeader(http.HEADER_ACCEPT, http.CONTENT_JSON);
        response = http.get(ApiEndPointNames.LISTS);
        Assert.assertEquals(200, response.statusCode);
        Assert.assertTrue(!response.body.contains(dbq(String.format("'guid':'%s'",guid))));

        // cannot delete something that does not exist - get a 404
        response = http.delete(ApiEndPointNames.LISTS+"/"+guid);
        Assert.assertEquals(404, response.statusCode);

        // error did not pass in an id
        response = http.delete(ApiEndPointNames.LISTS);
        Assert.assertEquals(405, response.statusCode);

    }



    // TODO: need a list creation test on post to entity endpoint

    @Test
    public void listPatchUsingXml() throws IOException {

        String guid = UUID.randomUUID().toString();
        String xmlList = dbq(String.format("<list><guid>%s</guid><title>title4</title><description>this is a desc</description></list>", guid));

        // create
        http.setHeader(http.HEADER_CONTENT_TYPE,http.CONTENT_XML);
        http.setBasicAuth("user", "password");
        HttpResponse response = http.post(ApiEndPointNames.LISTS, xmlList);
        Assert.assertEquals(201, response.statusCode);

        // need permissions to patch
        String patchList = dbq(String.format("<list><title>title this is it</title><description>this is my desc</description></list>", guid));
        http.setHeader(http.HEADER_ACCEPT, http.CONTENT_JSON);
        http.setBasicAuth("admin", "password");
        response = http.patch(ApiEndPointNames.LISTS + "/" + guid, patchList);
        Assert.assertEquals(200, response.statusCode);

        http.setHeader(http.HEADER_ACCEPT, http.CONTENT_XML);
        response = http.get(ApiEndPointNames.LISTS + "/" + guid);
        Assert.assertEquals(200, response.statusCode);
        Assert.assertTrue(response.body.contains(dbq("<description>this is my desc</description>")));
        Assert.assertTrue(response.body.contains(dbq("<title>title this is it</title>")));

        // cannot delete something that does not exist - get a 404
        response = http.patch(ApiEndPointNames.LISTS+"/"+guid+"werjwerjwerw",xmlList);
        Assert.assertEquals(404, response.statusCode);

        // error did not pass in an id
        response = http.patch(ApiEndPointNames.LISTS, xmlList);
        Assert.assertEquals(405, response.statusCode);
    }


    @Test
    public void listPutUsingXml() throws IOException {

        String guid = UUID.randomUUID().toString();
        String xmlList = dbq(String.format("<list><guid>%s</guid><title>title4</title><description>this is a desc</description><createdDate>2017-07-07-19-34-23</createdDate><amendedDate>2017-07-07-19-34-24</amendedDate></list>", guid));

        // create put at root
        http.setBasicAuth("user", "password");
        http.setHeader(http.HEADER_CONTENT_TYPE,http.CONTENT_XML);
        HttpResponse response = http.put(ApiEndPointNames.LISTS, xmlList);
        Assert.assertEquals(201, response.statusCode);

        http.setHeader(http.HEADER_ACCEPT, http.CONTENT_XML);
        response = http.get(ApiEndPointNames.LISTS + "/" + guid);
        Assert.assertEquals(200, response.statusCode);


        // amend with a put
        xmlList = dbq("<list><title>new title</title><description>new desc</description><createdDate>2017-07-07-19-34-23</createdDate><amendedDate>2017-07-07-19-34-24</amendedDate></list>");

        // amend this one
        http.setHeader(http.HEADER_CONTENT_TYPE,http.CONTENT_XML);
        response = http.put(ApiEndPointNames.LISTS + "/" + guid, xmlList);
        Assert.assertEquals(200, response.statusCode);

        http.setHeader(http.HEADER_ACCEPT, http.CONTENT_XML);
        response = http.get(ApiEndPointNames.LISTS + "/" + guid);
        Assert.assertEquals(200, response.statusCode);
        Assert.assertTrue(response.body.contains(dbq("<title>new title</title>")));
        Assert.assertTrue(response.body.contains(dbq("<description>new desc</description>")));


        // create with a put to a GUID that does not exist
        guid = UUID.randomUUID().toString();

        String patchList = dbq("<list><title>title this is it</title><description>this is my desc</description><createdDate>2017-07-07-19-34-23</createdDate><amendedDate>2017-07-07-19-34-24</amendedDate></list>");
        http.setHeader(http.HEADER_ACCEPT, http.CONTENT_JSON);
        response = http.put(ApiEndPointNames.LISTS + "/" + guid, patchList);
        Assert.assertEquals(201, response.statusCode);

        http.setHeader(http.HEADER_ACCEPT, http.CONTENT_XML);
        response = http.get(ApiEndPointNames.LISTS + "/" + guid);
        Assert.assertEquals(200, response.statusCode);
        Assert.assertTrue(response.body.contains(dbq("<description>this is my desc</description>")));
        Assert.assertTrue(response.body.contains(dbq("<title>title this is it</title>")));

    }


}
