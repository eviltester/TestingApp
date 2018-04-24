package uk.co.compendiumdev.restlisticator.api;

import uk.co.compendiumdev.restlisticator.api.payloads.ResultsFilter;
import uk.co.compendiumdev.restlisticator.http.QueryParser;

import java.util.Arrays;

public abstract class GeneralApiRequest implements ApiRequest{

    protected String body;
    private String credentialsPassword;
    private String credentialsUsername;

    // by default use JSON
    private ApiRequestResponseFormat responseFormat = ApiRequestResponseFormat.JSON;
    private ApiRequestResponseFormat requestFormat = ApiRequestResponseFormat.JSON;
    private String[] pathParts = {};
    private String verb;
    private String apiAuthKey="";
    private String query;

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setResponseFormat(ApiRequestResponseFormat format){
        this.responseFormat = format;
    }

    public ApiRequestResponseFormat getResponseFormat(){
        return responseFormat;
    }

    public void setRequestFormat(ApiRequestResponseFormat format){
        this.requestFormat = format;
    };

    public ApiRequestResponseFormat getRequestFormat(){
        return requestFormat;
    }

    public void setPathParts(String []parts){
        if(parts!=null) {
            this.pathParts = Arrays.copyOf(parts, parts.length);
        }
    }

    public String[] getPathParts(){
        return pathParts;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getVerb() {
        return verb;
    }

    public String getUsername(){
        return credentialsUsername;
    }
    public String getPassword(){
        return credentialsPassword;
    }

    public void setUserDetails(String username, String password){
        this.credentialsUsername = username;
        this.credentialsPassword = password;
    };

    public void setApiAuthKey(String apiAuthKey) {
        this.apiAuthKey = apiAuthKey;
    }

    public String getApiAuthKey(){
        return this.apiAuthKey;
    }

    public void setQuery(String query){
        this.query = query;
    }

    public ResultsFilter getResultsFilter(){

        ResultsFilter filter = new ResultsFilter();

        filter.setFromQuery(this.query);

        return filter;

    }

    public String getQueryValue(String queryParam){

        // TODO: move the query parsing into a new class
        if(query==null)
            return null;

        QueryParser qp = new QueryParser(query);

        if(!qp.hasAttribute(queryParam))
            return null;

        return qp.getValueFor(queryParam);

    }

    public String getQuery(){
        return query;
    }
    
}
