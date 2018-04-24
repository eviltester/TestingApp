package uk.co.compendiumdev.restlisticator.sparkrestserver.mock;


public class MockRequestRule {
    private String urlPattern;
    private int statusCode;

    public MockRequestRule whenUrlMatches(String urlPattern) {
        this.urlPattern = urlPattern;
        return this;
    }

    public MockRequestRule returnResponse() {
        return this;
    }

    public MockRequestRule withStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }
}
