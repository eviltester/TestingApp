package uk.co.compendiumdev.restlisticator.http;

/**
 * Created by Alan on 17/07/2017.
 */
public enum HttpVerb {

    GET("get"),
    PUT("put"),
    POST("post"),
    PATCH("patch"),
    DELETE("delete"),
    OPTIONS("options"),
    HEAD("head");

    private final String name;

    HttpVerb(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static HttpVerb find(String verbName) {
        for(HttpVerb verb: HttpVerb.values()){
            if(verb.getName().compareToIgnoreCase(verbName)==0){
                return verb;
            }
        }

        // todo: throw exception not found
        return null;
    }
}
