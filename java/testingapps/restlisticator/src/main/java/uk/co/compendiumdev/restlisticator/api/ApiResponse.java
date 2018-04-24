package uk.co.compendiumdev.restlisticator.api;


import java.util.Map;

public interface ApiResponse {

    String HEADER_LOCATION = "location";

    void setStatus(int status);

    int getStatus();

    void setBody(String body);

    String getBody();

    Map<String,String> getHeaders();

    void setHeader(String headername, String headervalue);

    void setAsUnauthenticated();

    void setAsUnauthorized();

    void setAsBadRequest(String s);

    void setAsSuccessful_OK();

    void setAsNotFound();

    void setAsCreated();

    void setAsSuccessfulNoContent();

    void setContentTypeBasedOnAccept(ApiRequestResponseFormat responseFormat);

    void setAsConflict(String errorMessage);
}
