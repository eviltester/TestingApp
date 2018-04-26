package uk.co.compendiumdev.restlisticator.domain.users;

import uk.co.compendiumdev.restlisticator.http.ApiEndPoint;
import uk.co.compendiumdev.restlisticator.http.HttpVerb;

/**
 * Created by Alan on 16/07/2017.
 */
public class UserAccessPermissionBuilder {

    UserAccessPermission permission;

    public UserAccessPermissionBuilder(ApiEndPoint endpoint) {
        permission = new UserAccessPermission();
        permission.setEndpoint(endpoint);
    }

    public UserAccessPermission build() {
        return permission;
    }

    public UserAccessPermissionBuilder cannot(String verb) {
        permission.setPermission(verb, false);
        return this;
    }

    public UserAccessPermissionBuilder cannot(HttpVerb verb) {
        permission.setPermission(verb.getName(), false);
        return this;
    }

    public UserAccessPermissionBuilder withDefault(Boolean defaultValue) {
        permission.setAll(defaultValue);
        return this;
    }

    public UserAccessPermissionBuilder can(String verb) {
        permission.setPermission(verb, true);
        return this;
    }

    public UserAccessPermissionBuilder can(HttpVerb verb) {
        permission.setPermission(verb.getName(), true);
        return this;
    }


}
