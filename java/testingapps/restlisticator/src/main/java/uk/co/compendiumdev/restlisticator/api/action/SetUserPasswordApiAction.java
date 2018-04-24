package uk.co.compendiumdev.restlisticator.api.action;

import uk.co.compendiumdev.restlisticator.api.ApiRequest;
import uk.co.compendiumdev.restlisticator.api.ApiResponse;
import uk.co.compendiumdev.restlisticator.api.UserAuthenticator;
import uk.co.compendiumdev.restlisticator.api.payloads.UserPayload;
import uk.co.compendiumdev.restlisticator.api.payloads.convertor.PayloadConvertor;
import uk.co.compendiumdev.restlisticator.domain.users.User;
import uk.co.compendiumdev.restlisticator.domain.users.UserAccessPermission;
import uk.co.compendiumdev.restlisticator.http.ApiEndPoint;
import uk.co.compendiumdev.restlisticator.testappconfig.FeatureToggles;

/**
 * Created by Alan on 15/08/2017.
 */
public class SetUserPasswordApiAction implements ApiAction{

    private final ApiResponse apiResponse;
    private final ApiRequest apiRequest;
    private PayloadConvertor payloadConvertor;
    private UserAuthenticator userAuthenticator;
    private User authenticatedUser;
    private String fieldToAmend;

    public SetUserPasswordApiAction(ApiRequest apiRequest, ApiResponse apiResponse) {
        this.apiRequest = apiRequest;
        this.apiResponse = apiResponse;
    }

    @Override
    public ApiResponse perform() {
        String username = apiRequest.getPathParts()[0];

        User targetUser = userAuthenticator.getUser(username);

        // make sure this is permission protected and user can access this user's details
        if(targetUser!=null){
            if(!authenticatedUser.getUsername().contentEquals(targetUser.getUsername())){
                // not same user - make sure they can do this
                if(!authenticatedUser.permissions().can(UserAccessPermission.UPDATE_ANY_USER, ApiEndPoint.USERS)){
                    apiResponse.setAsUnauthorized();
                    return apiResponse;
                };
            }
        }

        // if target user does not exist then return 404
        if(targetUser==null){
            apiResponse.setAsNotFound();
            return apiResponse;
        }


        UserPayload userDetails = payloadConvertor.convertFromPayloadStringToUserPayload(apiRequest.getBody(), apiRequest.getRequestFormat());

        if(userDetails==null){
            apiResponse.setAsBadRequest("Could not parse payload");
            return apiResponse;
        }

        if(fieldToAmend==null){
            apiResponse.setStatus(501);
            return apiResponse;
        }

        switch (fieldToAmend) {
            case "password":
                return setPassword(userDetails, apiResponse, targetUser);
            case "apikey":
                return setApiKey(userDetails, apiResponse, targetUser);
        }

        apiResponse.setAsBadRequest("Don't think you set anything");
        return apiResponse;
    }

    private ApiResponse setApiKey(UserPayload userDetails, ApiResponse apiResponse, User targetUser) {
        if(userDetails.apikey==null){
            apiResponse.setAsBadRequest("Could not identify apikey to set in message");
            return apiResponse;
        }

        int minLengthOfApiKey = 9;
        if(FeatureToggles.BUG_012_FIXED.getState()){
            minLengthOfApiKey=10;
        }

        if(userDetails.apikey.trim().length()<minLengthOfApiKey){
            apiResponse.setAsBadRequest("ApiKey must be a minimum of 10 characters");
            return apiResponse;
        }

        targetUser.setApiKey(userDetails.apikey);
        apiResponse.setAsSuccessfulNoContent();
        return apiResponse;
    }

    private ApiResponse setPassword(UserPayload userDetails, ApiResponse apiResponse, User targetUser) {
        if(userDetails.password==null){
            apiResponse.setAsBadRequest("Could not identify password to set in message");
            return apiResponse;
        }

        if(userDetails.password.trim().length()<6){
            apiResponse.setAsBadRequest("Password must be a minimum of 6 characters");
            return apiResponse;
        }

        targetUser.setPassword(userDetails.password);
        apiResponse.setAsSuccessfulNoContent();
        return apiResponse;
    }

    public SetUserPasswordApiAction setAuthenticatedUser(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        return this;
    }

    public SetUserPasswordApiAction setConvertor(PayloadConvertor convertor) {
        this.payloadConvertor = convertor;
        return this;
    }

    public SetUserPasswordApiAction setUserAuthenticator(UserAuthenticator userAuthenticator) {
        this.userAuthenticator = userAuthenticator;
        return this;
    }

    public SetUserPasswordApiAction setFieldToAmend(String fieldToAmend) {
        this.fieldToAmend = fieldToAmend;
        return this;
    }
}
