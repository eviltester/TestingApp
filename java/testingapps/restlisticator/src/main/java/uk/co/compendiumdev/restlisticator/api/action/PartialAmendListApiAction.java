package uk.co.compendiumdev.restlisticator.api.action;

import com.google.gson.JsonSyntaxException;
import uk.co.compendiumdev.restlisticator.api.ApiRequest;
import uk.co.compendiumdev.restlisticator.api.ApiResponse;
import uk.co.compendiumdev.restlisticator.api.UserAuthenticator;
import uk.co.compendiumdev.restlisticator.api.payloads.ListicatorListPayload;
import uk.co.compendiumdev.restlisticator.api.payloads.convertor.PayloadConvertor;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;
import uk.co.compendiumdev.restlisticator.domain.users.User;
import uk.co.compendiumdev.restlisticator.domain.users.UserAccessPermission;
import uk.co.compendiumdev.restlisticator.http.ApiEndPoint;
import uk.co.compendiumdev.restlisticator.testappconfig.FeatureToggles;

import java.util.Map;

/**
 * Created by Alan on 18/07/2017.
 */
public class PartialAmendListApiAction {
    private final ApiRequest apiRequest;
    private final ApiResponse apiResponse;
    private PayloadConvertor payloadConvertor;
    private TheListicator listicator;
    private UserAuthenticator userAuthenticator;

    public PartialAmendListApiAction(ApiRequest apiRequest, ApiResponse apiResponse) {
        this.apiRequest = apiRequest;
        this.apiResponse = apiResponse;
    }

    public PartialAmendListApiAction setConvertor(PayloadConvertor convertor) {
        this.payloadConvertor = convertor;
        return this;
    }

    public ApiResponse perform() {

        // requires path arguments otherwise 405

        // path argument 0 must be a list otherwise 404
        String guid = apiRequest.getPathParts()[0];
        ListicatorList list =  listicator.getList(guid);

        boolean isNew = false;

        User authenticatedUser = userAuthenticator.getAuthenticatedUser(apiRequest);

        if(list==null){
            if(FeatureToggles.BUG_008_FIXED.getState()){
                // guards on this are outside the amend action - amend will create if it does not exist
                list = new ListicatorList("","");
                list.forceSetGuid(guid);
                list.forceSetOwner(authenticatedUser.getUsername());
                isNew = true;
            } else {
                apiResponse.setAsNotFound();
                return apiResponse;
            }
        }

        // admins can amend any
        if(!authenticatedUser.permissions().can(UserAccessPermission.CAN_AMEND_ANY, ApiEndPoint.LISTS)){
            // check if user is the owner, or it is blank, or the admin created it
            if(list.getOwner() == null || list.getOwner().length()==0 || list.getOwner().contentEquals("admin")){
                // anyone can amend an unowned or admin owned list
            }else{
                if(!list.getOwner().contentEquals(authenticatedUser.getUsername())){
                    // you can't amend this
                    apiResponse.setAsUnauthorized();
                    return apiResponse;
                }
            }
        }



        Map<String, String> patches=null;

        try{
            patches = payloadConvertor.convertFromPayloadStringToPatchMap(apiRequest.getBody(), apiRequest.getRequestFormat());
            ListicatorListPayload patchObj = payloadConvertor.convertFromPayloadStringToPatchPayloadForList(apiRequest.getBody(), apiRequest.getRequestFormat());
        }catch(JsonSyntaxException e){
            // todo: add proper error message in response
        }

        if(patches==null) {
            apiResponse.setAsBadRequest("Payload could not be deserialised, is it in the correct format?");
            return apiResponse;
        }

        list = list.patch(patches);

        if(list==null){
            apiResponse.setAsBadRequest("Payload could not be applied");
        }else{
            if(isNew){
                listicator.addList(list);
                apiResponse.setAsCreated();
            }else {
                apiResponse.setAsSuccessful_OK();
            }
        }

        return apiResponse;
    }

    public PartialAmendListApiAction setListicator(TheListicator listicator) {
        this.listicator = listicator;
        return this;
    }

    public PartialAmendListApiAction setUserAuthenticator(UserAuthenticator userAuthenticator) {
        this.userAuthenticator = userAuthenticator;
        return this;
    }
}
