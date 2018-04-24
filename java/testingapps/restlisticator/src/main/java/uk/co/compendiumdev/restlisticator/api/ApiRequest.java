package uk.co.compendiumdev.restlisticator.api;

import uk.co.compendiumdev.restlisticator.api.payloads.ResultsFilter;

public interface ApiRequest {

        void setBody(String body);

        String getBody();

        void setResponseFormat(ApiRequestResponseFormat format);

        ApiRequestResponseFormat getResponseFormat();

        void setRequestFormat(ApiRequestResponseFormat xml);
        ApiRequestResponseFormat getRequestFormat();

        void setPathParts(String []parts);

        String[] getPathParts();

        String getUsername();
        String getPassword();
        void setUserDetails(String username, String password);

        String getVerb();
        void setVerb(String verb);

        String getApiAuthKey();

        void setQuery(String query);

        ResultsFilter getResultsFilter();

        String getQueryValue(String queryParam);

        String getQuery();
}
