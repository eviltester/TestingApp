package uk.co.compendiumdev.restlisticator.domain.users;

import java.util.UUID;

/**
 * Created by Alan on 16/07/2017.
 */
public class User {
    private final String username;
    private UserAccessPermissions permissions;
    private String password;
    private String authKey;

    public User(String username, String password) {

        this(   username,
                password,
                UserAccessPermissions.getDefaults()
            );
    }

    public User(String username, String password, UserAccessPermissions permissions) {

        this.username=username;
        this.password=password;
        this.permissions = permissions;
        this.authKey = UUID.randomUUID().toString();

        System.out.println("User : " + username + " - " + authKey);
    }

    public String getUsername() {
        return username;
    }

    public boolean passwordMatches(String password) {
        return this.password.contentEquals(password);
    }

    public UserAccessPermissions permissions() {
           return permissions;
    }

    public User setPermissions(UserAccessPermissions permissions){
        this.permissions = permissions;
        return this;
    }

    public String getApikey() {
        return authKey;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setApiKey(String apiKey) {
        this.authKey = apiKey;
    }

    public String getPassword() {
        return password;
    }
}
