package uk.co.compendiumdev.restlisticator.api.action;

import uk.co.compendiumdev.restlisticator.api.ApiRequest;
import uk.co.compendiumdev.restlisticator.api.ApiResponse;
import uk.co.compendiumdev.restlisticator.api.UserAuthenticator;
import uk.co.compendiumdev.restlisticator.api.payloads.UserPayload;
import uk.co.compendiumdev.restlisticator.api.payloads.convertor.PayloadConvertor;
import uk.co.compendiumdev.restlisticator.domain.users.User;
import uk.co.compendiumdev.restlisticator.domain.users.UserAccessPermission;
import uk.co.compendiumdev.restlisticator.http.ApiEndPoint;


public class GetUserDetailsApiAction implements ApiAction{
    private final ApiResponse apiResponse;
    private final ApiRequest apiRequest;
    private PayloadConvertor payloadConvertor;
    private UserAuthenticator userAuthenticator;
    private User authenticatedUser;

    public GetUserDetailsApiAction(ApiRequest apiRequest, ApiResponse apiResponse) {
        this.apiRequest = apiRequest;
        this.apiResponse = apiResponse;
    }

    public GetUserDetailsApiAction setConvertor(PayloadConvertor convertor) {
        this.payloadConvertor = convertor;
        return this;
    }

    public GetUserDetailsApiAction setUserAuthenticator(UserAuthenticator userAuthenticator) {
        this.userAuthenticator = userAuthenticator;
        return this;
    }


    public GetUserDetailsApiAction setAuthenticatedUser(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        return this;
    }
    
    public ApiResponse perform() {

        String username = apiRequest.getPathParts()[0];

        User targetUser = userAuthenticator.getUser(username);


        // make sure this is permission protected and user can access this user's details
        if(targetUser!=null){
            if(!authenticatedUser.getUsername().contentEquals(targetUser.getUsername())){
                // not same user - make sure they can do this
                if(!authenticatedUser.permissions().can(UserAccessPermission.READ_ANY_USER, ApiEndPoint.USERS)){
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



        UserPayload userDetails = new UserPayload();
        userDetails.username = targetUser.getUsername();
        userDetails.apikey = targetUser.getApikey();

        String payload= payloadConvertor.convert(userDetails,  apiRequest.getResponseFormat());
        apiResponse.setBody(payload);
        apiResponse.setAsSuccessful_OK();

        return apiResponse;
    }


}
