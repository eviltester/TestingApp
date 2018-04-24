package uk.co.compendiumdev.restlisticator.sparkrestserver;

import spark.Response;
import uk.co.compendiumdev.restlisticator.api.GeneralApiResponse;


public class SparkApiResponse extends GeneralApiResponse {

    private final Response response;

    public SparkApiResponse(Response response) {
        this.response = response;
    }

    @Override
    public void setStatus(int status) {
        super.setStatus(status);
        this.response.status(status);
    }

    @Override
    public void setHeader(String headername, String headervalue){
        super.setHeader(headername, headervalue);
        this.response.header(headername, headervalue);
    }
    
}
