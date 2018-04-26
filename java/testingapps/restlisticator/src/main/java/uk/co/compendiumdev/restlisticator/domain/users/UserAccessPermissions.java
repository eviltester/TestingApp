package uk.co.compendiumdev.restlisticator.domain.users;


import uk.co.compendiumdev.restlisticator.http.ApiEndPoint;
import uk.co.compendiumdev.restlisticator.http.HttpVerb;

import java.util.HashMap;
import java.util.Map;

public class UserAccessPermissions {

    // TODO: use end points in enum not permissions or add the prefix in permissions as well as enum

    Map<String, UserAccessPermission> permissions = new HashMap<>();

    public static UserAccessPermissions getDefaults(){

        UserAccessPermissions defaults = new UserAccessPermissions();

        defaults.add(new UserAccessPermissionBuilder(
                            ApiEndPoint.HEARTBEAT).
                                withDefault(false).
                                can(HttpVerb.GET).
                                can(HttpVerb.OPTIONS).
                        build());

        defaults.add(new UserAccessPermissionBuilder(
                            ApiEndPoint.LISTS).
                            withDefault(false).
                                can(HttpVerb.GET).
                                can(HttpVerb.POST).
                                can(HttpVerb.PUT).
                                can(HttpVerb.OPTIONS).
                                can(HttpVerb.DELETE).
                        build());

        defaults.add(new UserAccessPermissionBuilder(
                            ApiEndPoint.FEATURE_TOGGLES).
                            withDefault(false).
                                can(HttpVerb.GET).
                                can(HttpVerb.POST).
                                can(HttpVerb.OPTIONS).
                            build());

        defaults.add(new UserAccessPermissionBuilder(
                            ApiEndPoint.USERS).
                            withDefault(false).
                                can(HttpVerb.GET).
                                can(HttpVerb.OPTIONS).
                                can(HttpVerb.PUT).
                                can(HttpVerb.POST).
                            build());

        return defaults;

    }

    public static UserAccessPermissions getSuperAdmin() {

        UserAccessPermissions superAdmin = new UserAccessPermissions();

        // set all permissions for all endpoints to be true
        for(ApiEndPoint endpoint : ApiEndPoint.values()){

            superAdmin.add(new UserAccessPermissionBuilder(
                    endpoint).
                    withDefault(true).
                    build());
        }

        // special permissions
        superAdmin.forEndpoint(ApiEndPoint.USERS).setPermission(UserAccessPermission.READ_ANY_USER,true);
        superAdmin.forEndpoint(ApiEndPoint.USERS).setPermission(UserAccessPermission.UPDATE_ANY_USER,true);
        superAdmin.forEndpoint(ApiEndPoint.USERS).setPermission(UserAccessPermission.CREATE_USER,true);
        superAdmin.forEndpoint(ApiEndPoint.LISTS).setPermission(UserAccessPermission.POST_MULTIPLE_LISTS,true);
        superAdmin.forEndpoint(ApiEndPoint.LISTS).setPermission(UserAccessPermission.CAN_DELETE_ANY,true);
        superAdmin.forEndpoint(ApiEndPoint.LISTS).setPermission(UserAccessPermission.CAN_AMEND_ANY,true);

        return superAdmin;
    }

    private void add(UserAccessPermission permission) {
        permissions.put(permission.getEndpoint(), permission);
    }

    public boolean can(HttpVerb verb, ApiEndPoint endpoint) {

        UserAccessPermission permission = getPermission(endpoint);
        if(permission==null){
            return false;
        }

        return permission.can(verb);

    }

    private UserAccessPermission getPermission(ApiEndPoint endpoint) {
        for(UserAccessPermission perm : permissions.values()){
            if(perm.endpoint == endpoint){
                return perm;
            }
        }
        return null;
    }

    public UserAccessPermission forEndpoint(ApiEndPoint endpoint) {
        return permissions.get(endpoint.getPath());
    }

    public boolean can(String permissionName, ApiEndPoint endpoint) {

        UserAccessPermission permission =getPermission(endpoint);
        if(permission==null){
            return false;
        }

        return permission.can(permissionName);
    }
}
