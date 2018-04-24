package uk.co.compendiumdev.restlisticator.api;

public enum ApiRequestResponseFormat {

    JSON("application/json"),
    XML("application/xml");

    private String contentTypeValue;


    ApiRequestResponseFormat(String contentType) {
        this.contentTypeValue = contentType;
    }

    public String getContentTypeValue() {
        return this.contentTypeValue;
    }
}
