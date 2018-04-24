package uk.co.compendiumdev.restlisticator.sparkrestserver.integration;

import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Alan on 18/08/2017.
 */
public class DocumentationTest extends SparkIntegrationTest {

        @Test
        public void canCallRootAndGetHTMLInstructions(){

            http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
            HttpResponse response = http.get("");
            Assert.assertEquals(200, response.statusCode);
            Assert.assertTrue(response.body.startsWith("<html><head><title>REST Listicator"));
        }

    @Test
    public void canCallRootAndGetHTMLInstructionsRoot(){

        http.setHeader(http.HEADER_ACCEPT,http.CONTENT_XML);
        HttpResponse response = http.get("/");
        Assert.assertEquals(200, response.statusCode);
        Assert.assertTrue(response.body.startsWith("<html><head><title>REST Listicator"));
    }


}
