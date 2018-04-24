package uk.co.compendiumdev.restlisticator.api.action;

import uk.co.compendiumdev.restlisticator.api.ApiRequest;
import uk.co.compendiumdev.restlisticator.api.ApiResponse;
import uk.co.compendiumdev.restlisticator.api.UserAuthenticator;
import uk.co.compendiumdev.restlisticator.api.payloads.convertor.PayloadConvertor;
import uk.co.compendiumdev.restlisticator.domain.app.ListOfListicatorLists;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;
import uk.co.compendiumdev.restlisticator.domain.users.User;
import uk.co.compendiumdev.restlisticator.domain.users.UserAccessPermission;
import uk.co.compendiumdev.restlisticator.http.ApiEndPoint;
import uk.co.compendiumdev.restlisticator.http.HttpVerb;
import uk.co.compendiumdev.restlisticator.testappconfig.FeatureToggles;

/**
 * Created by Alan on 18/07/2017.
 */
public class SetListsApiAction {
    private final ApiRequest apiRequest;
    private final ApiResponse apiResponse;
    private PayloadConvertor payloadConvertor;
    private TheListicator listicator;
    private UserAuthenticator userAuthenticator;

    public SetListsApiAction(ApiRequest apiRequest, ApiResponse apiResponse) {
        this.apiRequest = apiRequest;
        this.apiResponse = apiResponse;
    }

    public SetListsApiAction setConvertor(PayloadConvertor payloadConvertor){
        this.payloadConvertor = payloadConvertor;
        return this;
    }

    public SetListsApiAction setListicator(TheListicator listicator){
        this.listicator = listicator;
        return this;
    }

    public SetListsApiAction setUserAuthenticator(UserAuthenticator userAuthenticator) {
        this.userAuthenticator = userAuthenticator;
        return this;
    }

    public ApiResponse perform() {


        // make sure this is authentication protected i.e. user has to be authenticated
        User user = userAuthenticator.getAuthenticatedUser(apiRequest);
        if(user==null){
            apiResponse.setAsUnauthenticated();
            return apiResponse;
        }

        // check user permissions

        if(!user.permissions().can(HttpVerb.POST, ApiEndPoint.LISTS)){
            apiResponse.setAsUnauthorized();
            return apiResponse;
        }


        ListOfListicatorLists newLists= payloadConvertor.convertFromPayloadStringToListListicatorLists(
                apiRequest.getBody(),
                apiRequest.getRequestFormat());

        String listId="";


        // requires special permission to create multiple lists
        if(newLists.listCount()>1){
            if(!user.permissions().can(UserAccessPermission.POST_MULTIPLE_LISTS, ApiEndPoint.LISTS)){
                apiResponse.setAsUnauthorized();
                return apiResponse;
            }
        }


        for(ListicatorList list : newLists.getAsList()){

            list.forceSetOwner(user.getUsername());
            listicator.addList(list);
            listId = list.getGUID();
        }

        if(newLists.listCount()>0) {
            apiResponse.setAsCreated();
            if(FeatureToggles.BUG_001_FIXED.getState()) {
                apiResponse.setHeader("location", "/lists/" + listId);
            }
            if(FeatureToggles.BUG_002_FIXED.getState()) {
                apiResponse.setBody(payloadConvertor.convert(newLists, apiRequest.getResponseFormat()));
            }
        }else{
            // nothing created - what kind of request was that?
            apiResponse.setAsBadRequest("Nothing Created. Did not understand the request.");
        }
        return apiResponse;
    }


}
