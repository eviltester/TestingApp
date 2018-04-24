package uk.co.compendiumdev.restlisticator.api;

import uk.co.compendiumdev.restlisticator.testappconfig.FeatureToggles;

import java.util.HashMap;
import java.util.Map;

public abstract class GeneralApiResponse implements ApiResponse{

    protected int status;
    private String body="";
    private Map<String, String> headers = new HashMap<>();

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus(){
        return status;
    }

    public void setBody(String body){
        this.body = body;
    }

    public String getBody(){
        return this.body;
    }

    public Map<String, String> getHeaders(){
        return headers;
    }

    public void setHeader(String headername, String headervalue){
        headers.put(headername, headervalue);
    }

    /**
     * return 401 status
     */
    public void setAsUnauthenticated(){
        setStatus(401);

        if(FeatureToggles.BUG_004_FIXED.getState()) {
            // https://tools.ietf.org/html/rfc7235#section-4.1
            // A server generating a 401 (Unauthorized) response MUST send a WWW-Authenticate header field
            // set Authentication header
            setHeader("WWW-Authenticate", "Basic realm=\"simple\"");
        }
    }

    /**
     * return 403 status
     */

    public void setAsUnauthorized(){
        setStatus(403);
    }

    /**
     * return 400 status
     * @param errorMessage to be rendered in the payload body
     */
    public void setAsBadRequest(String errorMessage){
        // TODO - add the error message into the body of the response
        setStatus(400);
    }

    public void setAsConflict(String errorMessage){
        // TODO - add the error message into the body of the response
        setStatus(409);
    }

    // TODO 20x responses should support body being sent
    public void setAsSuccessful_OK(){
        setStatus(200);
    }

    public void setAsCreated(){
        // https://httpstatuses.com/201
        if(FeatureToggles.BUG_009_FIXED.getState()) {

            setStatus(201);
        }else{
            setStatus(200);
        }
    }



    public void setAsNotFound(){
        //https://httpstatuses.com/404
        if(FeatureToggles.BUG_010_FIXED.getState()) {
            setStatus(404);
        }else{
            setStatus(400);
        }
    }

    public void setAsSuccessfulNoContent(){
        // https://httpstatuses.com/204
        setStatus(204);
    }

    public void setContentTypeBasedOnAccept(ApiRequestResponseFormat responseFormat){

        if(FeatureToggles.BUG_011_FIXED.getState()) {
            this.setHeader("Content-Type", responseFormat.getContentTypeValue());
        }
    }


}
