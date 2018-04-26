package uk.co.compendiumdev.restlisticator.http;

public enum ApiEndPoint {

    DOCUMENTATION("/"),
    HEARTBEAT("/heartbeat"),
    LISTS("/lists"),
    FEATURE_TOGGLES("/feature-toggles"),
    USERS("/users");

    // api might be nested in another app so we need the ability to nest the api
    private static String prefix="";

    public static void setUrlPrefix(String prefix){
        String startSlash = "/";

        if(prefix.startsWith("/")){
            startSlash="";
        }

        if(prefix.endsWith("/")){
            // drop the last char
            prefix = prefix.substring(0, prefix.length()-1);
        }

        ApiEndPoint.prefix = startSlash + prefix;
    }

    public static void clearUrlPrefix(){
        prefix = "";
    }

    public static String getUrlPrefix() {
        return prefix;
    }

    private void setPath(String path) {
        this.path = path;
    }


    private String path;

    ApiEndPoint(String path) {
        this.path = path;
    }

    public String getPath() {
        return prefix + path;
    }

    public String getPath(String restOfPath) {
        return prefix + path + "/" + restOfPath;
    }
}
