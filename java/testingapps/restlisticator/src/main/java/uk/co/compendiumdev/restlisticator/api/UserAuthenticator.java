package uk.co.compendiumdev.restlisticator.api;

import uk.co.compendiumdev.restlisticator.domain.users.User;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Alan on 18/07/2017.
 */
public class UserAuthenticator {
    private final Map<String, User> users;

    public UserAuthenticator(Map<String, User> users) {
        this.users = users;
    }

    public boolean userIsAuthenticated(ApiRequest request) {
        return getAuthenticatedUser(request)!=null;
    }

    public User getAuthenticatedUser(ApiRequest request) {

        // can authenticate with username and password
        String username = request.getUsername();
        String password = request.getPassword();

        if(username!=null && password != null){
            User user = users.get(username);
            if(user!=null && user.passwordMatches(password)){
                return user;
            }
        }

        // can authenticate with API Key
        String authKey = request.getApiAuthKey();
        if(authKey!=null && authKey.length()>0){
            for(User user : users.values()){
                if(user.getApikey().contentEquals(authKey)){
                    return user;
                }
            }
        }

        // default to no you are not
        return null;
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public Collection< User> getUsers() {
        return users.values();
    }
}
