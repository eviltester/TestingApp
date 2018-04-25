package uk.co.compendiumdev.restlisticator.sparkrestserver.integration.apicalls;

import org.junit.Assert;
import spark.Spark;
import uk.co.compendiumdev.http.HttpMessageSender;
import uk.co.compendiumdev.http.HttpResponse;
import uk.co.compendiumdev.restlisticator.sparkrestserver.restapi.ApiEndPointNames;


import static uk.co.compendiumdev.strings.Quoter.dbq;

public class IntegrationApiCall {

    public static void setFeatureToggleViaAPI(String toggleName, String trueFalseValue) {
        HttpMessageSender http=new HttpMessageSender("http://" + "localhost:" + Spark.port());
        http.setHeader(http.HEADER_CONTENT_TYPE,http.CONTENT_XML);
        http.setBasicAuth("superadmin", "password");
        String xmlList = dbq(String.format("<toggles><toggle><key>%s</key><value>%s</value></toggle></toggles>", toggleName, trueFalseValue));
        HttpResponse response = http.post(ApiEndPointNames.FEATURE_TOGGLES, xmlList);
        Assert.assertEquals(200, response.statusCode);
    }
}

