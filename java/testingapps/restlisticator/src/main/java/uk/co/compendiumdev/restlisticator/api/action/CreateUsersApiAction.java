package uk.co.compendiumdev.restlisticator.api.action;

import uk.co.compendiumdev.restlisticator.api.ApiRequest;
import uk.co.compendiumdev.restlisticator.api.ApiResponse;
import uk.co.compendiumdev.restlisticator.api.UserAuthenticator;
import uk.co.compendiumdev.restlisticator.api.payloads.UserPayload;
import uk.co.compendiumdev.restlisticator.api.payloads.convertor.PayloadConvertor;
import uk.co.compendiumdev.restlisticator.domain.users.User;
import uk.co.compendiumdev.restlisticator.domain.users.UserAccessPermission;
import uk.co.compendiumdev.restlisticator.http.ApiEndPoint;

/**
 * Created by Alan on 16/08/2017.
 */
public class CreateUsersApiAction {
    private final ApiResponse apiResponse;
    private final ApiRequest apiRequest;
    private PayloadConvertor payloadConvertor;
    private UserAuthenticator userAuthenticator;
    private User authenticatedUser;
    public User userToAdd;

    public CreateUsersApiAction(ApiRequest apiRequest, ApiResponse apiResponse) {
        this.apiRequest = apiRequest;
        this.apiResponse = apiResponse;
    }

    public CreateUsersApiAction setConvertor(PayloadConvertor convertor) {
        this.payloadConvertor = convertor;
        return this;
    }

    public CreateUsersApiAction setUserAuthenticator(UserAuthenticator userAuthenticator) {
        this.userAuthenticator = userAuthenticator;
        return this;
    }


    public CreateUsersApiAction setAuthenticatedUser(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        return this;
    }

    public ApiResponse perform() {


        // make sure this is permission protected and user can access this user's details
        if(!authenticatedUser.permissions().can(UserAccessPermission.CREATE_USER, ApiEndPoint.USERS)){
            apiResponse.setAsUnauthorized();
            return apiResponse;
        };


        UserPayload userDetails = payloadConvertor.convertFromPayloadStringToUserPayload(apiRequest.getBody(), apiRequest.getRequestFormat());

        if(userDetails == null){
            apiResponse.setAsBadRequest("Could not parse request");
            return apiResponse;
        }

        if(userDetails.username==null || userDetails.username.trim().length()<6 ){
            apiResponse.setAsBadRequest("Username needs to be minimum of 6 characters");
            return apiResponse;
        }

        if(userDetails.password==null || userDetails.password.trim().length()<6 ){
            apiResponse.setAsBadRequest("Password needs to be minimum of 6 characters");
            return apiResponse;
        }

        User user = userAuthenticator.getUser(userDetails.username);
        if(user!=null){
            // user exists
            apiResponse.setAsConflict("Username already exists");
            return apiResponse;
        }

        userToAdd = new User(userDetails.username, userDetails.password);

        // TODO Add details of created user here
        apiResponse.setAsCreated();

        return apiResponse;
    }
}
