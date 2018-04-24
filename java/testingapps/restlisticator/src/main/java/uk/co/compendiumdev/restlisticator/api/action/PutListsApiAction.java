package uk.co.compendiumdev.restlisticator.api.action;

import uk.co.compendiumdev.restlisticator.api.ApiRequest;
import uk.co.compendiumdev.restlisticator.api.ApiResponse;
import uk.co.compendiumdev.restlisticator.api.UserAuthenticator;
import uk.co.compendiumdev.restlisticator.api.payloads.ListicatorListPayload;
import uk.co.compendiumdev.restlisticator.api.payloads.convertor.PayloadConvertor;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;
import uk.co.compendiumdev.restlisticator.domain.users.User;

public class PutListsApiAction {
    private final ApiRequest apiRequest;
    private final ApiResponse apiResponse;
    private PayloadConvertor payloadConvertor;
    private TheListicator listicator;
    private UserAuthenticator userAuthenticator;

    public PutListsApiAction(ApiRequest apiRequest, ApiResponse apiResponse) {
        this.apiRequest = apiRequest;
        this.apiResponse = apiResponse;
    }


    public PutListsApiAction setConvertor(PayloadConvertor convertor) {
        this.payloadConvertor = convertor;
        return this;
    }

    public PutListsApiAction setListicator(TheListicator listicator) {
        this.listicator = listicator;
        return this;
    }

    public ApiResponse perform() {


        // make sure this is authentication protected i.e. user has to be authenticated
        User user = userAuthenticator.getAuthenticatedUser(apiRequest);
        if(user==null){
            apiResponse.setAsUnauthenticated();
            return apiResponse;
        }


        ListicatorListPayload payload = payloadConvertor.convertFromPayloadStringToPatchPayloadForList(apiRequest.getBody(), apiRequest.getRequestFormat());
        if(payload==null){
            apiResponse.setAsBadRequest("Could not convert payload");
            return apiResponse;
        }

        // all values must be set on a put except GUID
        String putValidationErrors = "";

        if(payload.amendedDate==null){
            putValidationErrors = putValidationErrors + ", amendedDate";
        }
        if(payload.createdDate==null){
            putValidationErrors = putValidationErrors + ", createdDate";
        }
        if(payload.title==null){
            putValidationErrors = putValidationErrors + ", title";
        }
        if(payload.title==null){
            putValidationErrors = putValidationErrors + ", description";
        }
        if(payload.guid==null){
            String []path = apiRequest.getPathParts();
            if(path.length==0){
                putValidationErrors = putValidationErrors + ", guid";
            }else{
                payload.guid=path[0]; // guid was set in the url
            }
        }

        if(putValidationErrors.length()>0){
            apiResponse.setAsBadRequest("Could not PUT when null value for: " + putValidationErrors.substring(1));
            return apiResponse;
        }


        ListicatorList newList = new ListicatorList(payload.title, payload.description);
        newList.forceSetAmendedDate(payload.amendedDate);
        newList.forceSetCreatedDate(payload.createdDate);
        newList.forceSetGuid(payload.guid);
        newList.forceSetOwner(user.getUsername());


        ListicatorList existingList =  listicator.getList(payload.guid);
        if(existingList!=null){
            // amend existing list return 200
            existingList.overrideContents(newList);
            apiResponse.setAsSuccessful_OK();
        }else{
            // create new return 201
            listicator.addList(newList);
            apiResponse.setAsCreated();
        }

        return apiResponse;
    }



    public PutListsApiAction setUserAuthenticator(UserAuthenticator userAuthenticator) {
        this.userAuthenticator = userAuthenticator;
        return this;
    }
}
