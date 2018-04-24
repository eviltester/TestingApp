package uk.co.compendiumdev.restlisticator.http;

public enum ApiEndPoint {

    HEARTBEAT("/heartbeat"),
    LISTS("/lists"),
    FEATURE_TOGGLES("/feature-toggles"),
    USERS("/users");

    private final String path;

    ApiEndPoint(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getPath(String restOfPath) {
        return this.path + "/" + restOfPath;
    }
}
