package uk.co.compendiumdev.restlisticator.domain.users;

import uk.co.compendiumdev.restlisticator.http.ApiEndPoint;
import uk.co.compendiumdev.restlisticator.http.HttpVerb;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alan on 16/07/2017.
 */
public class UserAccessPermission {

    public static final String READ_ANY_USER = "CanReadAnyUser";
    public static final String UPDATE_ANY_USER = "CanAmendAnyUser";
    public static final String POST_MULTIPLE_LISTS = "CanCreateMultipleLists";
    public static final String CREATE_USER = "CanCreateUser";
    public static final String CAN_DELETE_ANY = "CanDeleteAny";
    public static final String CAN_AMEND_ANY = "CanAmendAny";


    ApiEndPoint endpoint;
    Map<String, Boolean> permissions = new HashMap<>();

    public UserAccessPermission(){

        for(HttpVerb verb : HttpVerb.values()){
            permissions.put(verb.getName(), true);
        }
    }

    public boolean can(String verb) {
        String lcVerb = verb.toLowerCase();

        if(permissions.containsKey(lcVerb)){
            return permissions.get(lcVerb);
        }
        return false;
    }

    public boolean can(HttpVerb verb) {
        return can(verb.getName());
    }

    public UserAccessPermission setEndpoint(ApiEndPoint endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getEndpoint() {
        return endpoint.getPath();
    }

    public UserAccessPermission setPermission(String key, Boolean permitted){
        String lcKey = key.toLowerCase();

        if(permissions.containsKey(lcKey)){
            permissions.replace(lcKey, permitted);
        }else{
            permissions.put(lcKey, permitted);
        }

        return this;
    }

    public UserAccessPermission setCannot(String verb) {
        return setPermission(verb, false);
    }

    public UserAccessPermission setAll(Boolean all) {

        for(String verb : permissions.keySet()){
            permissions.replace(verb, all);
        }

        return this;
    }


    public UserAccessPermission setCannot(HttpVerb verb) {
        return setCannot(verb.getName());
    }

    public UserAccessPermission setCan(HttpVerb verb) {
        return setPermission(verb.getName(), true);
    }
}
