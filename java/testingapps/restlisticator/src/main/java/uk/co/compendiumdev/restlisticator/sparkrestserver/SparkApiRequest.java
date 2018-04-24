package uk.co.compendiumdev.restlisticator.sparkrestserver;

import spark.Request;
import uk.co.compendiumdev.restlisticator.api.ApiRequestResponseFormat;
import uk.co.compendiumdev.restlisticator.api.GeneralApiRequest;
import uk.co.compendiumdev.restlisticator.http.BasicAuthHeader;
import uk.co.compendiumdev.restlisticator.http.HttpHeaders;


public class SparkApiRequest extends GeneralApiRequest{

    public SparkApiRequest(Request request) {

        identifyTheFormatOfTheRequestPayload(request);

        identifyTheDesiredFormatOfTheResponsePayload(request);

        setTheAuthenticationSchemeAndValues(request);

        identifyTheVerbUsedInTheRequest(request);

        setPathParts(request.splat());

        this.body = request.body();

        setQuery(request.queryString());

        storeAllTheHeaders(request);

        System.out.println(request.splat());
        System.out.println(request.body());

    }

    private void storeAllTheHeaders(Request request) {
        if(request.headers()!=null) {
            for (String header : request.headers()) {
                System.out.println(header);
            }
        }

    }

    private void identifyTheVerbUsedInTheRequest(Request request) {

        String verb = request.headers("X-HTTP-Method-Override");

        if(verb==null){
            verb = request.requestMethod();
        }

        setVerb(verb);

    }

    private void identifyTheDesiredFormatOfTheResponsePayload(Request request) {
        if(request.headers("Accept")!=null) {
            if (request.headers("Accept").contains("application/json")) {
                this.setResponseFormat(ApiRequestResponseFormat.JSON);
            }
            if (request.headers("Accept").contains("xml")) {
                this.setResponseFormat(ApiRequestResponseFormat.XML);
            }
        }
    }

    private void identifyTheFormatOfTheRequestPayload(Request request) {

        if(request.headers("Content-Type")!=null) {
            if (request.headers("Content-Type").contains("application/json")) {
                this.setRequestFormat(ApiRequestResponseFormat.JSON);
            }
            if (request.headers("Content-Type").contains("/xml")) {
                this.setRequestFormat(ApiRequestResponseFormat.XML);
            }
        }

    }


    public void setTheAuthenticationSchemeAndValues(Request request) {
        String apiAuthKey = request.headers("X-API-AUTH");
        if(apiAuthKey!=null) {
            this.setApiAuthKey(apiAuthKey);
        }

        String username = "";
        String password = "";

        BasicAuthHeader auth = new BasicAuthHeader(request.headers(HttpHeaders.AUTHORIZATION));
        if(auth.isBasicAuthHeader()){
            username = auth.getUsername();
            password = auth.getPassword();
        }

        this.setUserDetails(username, password);
    }
}
