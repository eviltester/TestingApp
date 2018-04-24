package uk.co.compendiumdev.restlisticator.sparkrestserver.integration;


import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.ApiEndPointNames;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.http.HttpResponse;

import java.io.IOException;

import static uk.co.compendiumdev.strings.Quoter.dbq;

public class ListsEndpointTest extends SparkIntegrationTest{

    @Test
    public void listsSupportsOptions(){

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        HttpResponse response = http.options(ApiEndPointNames.LISTS);
        Assert.assertEquals(200, response.statusCode);
        Assert.assertEquals("", response.body);
        Assert.assertEquals("GET, POST, PUT", response.getHeaders().get("Allow"));
    }

    @Test
    public void cannotUseIncorrectVerbOnLists(){

        HttpResponse response = http.delete(ApiEndPointNames.LISTS);
        Assert.assertEquals(405, response.statusCode);

        response = http.patch(ApiEndPointNames.LISTS, "");
        Assert.assertEquals(405, response.statusCode);

        response = http.head(ApiEndPointNames.LISTS);
        Assert.assertEquals(405, response.statusCode);

        response = http.trace(ApiEndPointNames.LISTS);
        Assert.assertEquals(405, response.statusCode);

        response = http.connect(ApiEndPointNames.LISTS);
        Assert.assertEquals(405, response.statusCode);
    }

    @Test
    public void listCreationWithJustTitleUsingXml() throws IOException {

        String xmlList = dbq("<list><title>title4</title></list>");

        http.setHeader(http.HEADER_CONTENT_TYPE,http.CONTENT_XML);
        http.setBasicAuth("user", "password");
        HttpResponse response = http.post(ApiEndPointNames.LISTS, xmlList);
        Assert.assertEquals(201, response.statusCode);
        Assert.assertTrue(response.getHeaders().containsKey("Location"));
    }



    @Test
    public void userAuthenticationOnListCreation() {

        http.setHeader(http.HEADER_CONTENT_TYPE, http.CONTENT_XML);
        http.setBasicAuth("superadmin", "passward");
        String xmlList = dbq("<toggles><toggle><key>BUG_001_FIXED</key><value>false</value></toggle></toggles>");
        HttpResponse response = http.post(ApiEndPointNames.FEATURE_TOGGLES, xmlList);

        // incorrect password is authentication failure
        Assert.assertEquals(401, response.statusCode);

        http.setHeader(http.HEADER_CONTENT_TYPE, http.CONTENT_XML);
        http.setBasicAuth("superadmon", "passward");
        xmlList = dbq("<toggles><toggle><key>BUG_001_FIXED</key><value>false</value></toggle></toggles>");
        response = http.post(ApiEndPointNames.FEATURE_TOGGLES, xmlList);

        // incorrect user is authentication failure
        Assert.assertEquals(401, response.statusCode);
    }


    @Test
    public void listCreationCanReturnLocationHeader() throws IOException {

        http.setHeader(http.HEADER_CONTENT_TYPE,http.CONTENT_XML);
        HttpResponse response = http.get(ApiEndPointNames.FEATURE_TOGGLES);
        Assert.assertEquals(200, response.statusCode);
        System.out.println(response.body);


        http.setHeader(http.HEADER_CONTENT_TYPE,http.CONTENT_XML);
        http.setBasicAuth("superadmin", "password");
        String xmlList = dbq("<toggles><toggle><key>BUG_001_FIXED</key><value>false</value></toggle></toggles>");
        response = http.post(ApiEndPointNames.FEATURE_TOGGLES,xmlList);
        Assert.assertEquals(200, response.statusCode);

        xmlList = dbq("<list><title>title4</title></list>");

        http.setHeader(http.HEADER_CONTENT_TYPE,http.CONTENT_XML);
        response = http.post(ApiEndPointNames.LISTS, xmlList);
        Assert.assertEquals(201, response.statusCode);
        // no location header if we haven't fixed the bug
        Assert.assertFalse(response.getHeaders().containsKey("Location"));


        // set toggle back to normal otherwise other tests might fail
        setFeatureToggleViaAPI("BUG_001_FIXED", "true");
    }



    @Test
    public void listCreationDefaultContentTypeJSON() throws IOException {

        String jsonList = dbq("{'title':'title4','description':'description2'}");

        http.headers.remove(http.HEADER_CONTENT_TYPE);
        http.setBasicAuth("user", "password");
        HttpResponse response = http.post(ApiEndPointNames.LISTS, jsonList);
        Assert.assertEquals(201, response.statusCode);
    }

}
