package uk.co.compendiumdev.restlisticator.api.action;

import uk.co.compendiumdev.restlisticator.api.ApiRequest;
import uk.co.compendiumdev.restlisticator.api.ApiResponse;
import uk.co.compendiumdev.restlisticator.api.UserAuthenticator;
import uk.co.compendiumdev.restlisticator.api.payloads.UserListPayload;
import uk.co.compendiumdev.restlisticator.api.payloads.UserPayload;
import uk.co.compendiumdev.restlisticator.api.payloads.convertor.PayloadConvertor;
import uk.co.compendiumdev.restlisticator.domain.users.User;
import uk.co.compendiumdev.restlisticator.http.QueryParser;

public class GetUsersApiAction implements ApiAction{

    private final ApiResponse apiResponse;
    private final ApiRequest apiRequest;
    private PayloadConvertor payloadConvertor;
    private UserAuthenticator userAuthenticator;

    public GetUsersApiAction(ApiRequest apiRequest, ApiResponse apiResponse) {
        this.apiRequest = apiRequest;
        this.apiResponse = apiResponse;
    }

    @Override
    public ApiResponse perform() {

        UserListPayload usersList = new UserListPayload();

        ObjectQueryFilter queryFilter = new ObjectQueryFilter(new QueryParser(apiRequest.getQuery()));

        for(User user : userAuthenticator.getUsers()){
            UserPayload userDetails = new UserPayload();
            userDetails.username = user.getUsername();

            if(queryFilter.matches(userDetails)){
                usersList.users.add(userDetails);
            }
        }

        String payload= payloadConvertor.convert(usersList,  apiRequest.getResponseFormat());
        apiResponse.setBody(payload);
        apiResponse.setAsSuccessful_OK();

        return apiResponse;
    }

    public GetUsersApiAction setConvertor(PayloadConvertor convertor) {
        this.payloadConvertor = convertor;
        return this;
    }

    public GetUsersApiAction setUserAuthenticator(UserAuthenticator userAuthenticator) {
        this.userAuthenticator = userAuthenticator;
        return this;
    }


}
