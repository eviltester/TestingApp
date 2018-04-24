package uk.co.compendiumdev.restlisticator.api.action;

import uk.co.compendiumdev.restlisticator.api.ApiRequest;
import uk.co.compendiumdev.restlisticator.api.ApiResponse;
import uk.co.compendiumdev.restlisticator.api.UserAuthenticator;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;
import uk.co.compendiumdev.restlisticator.domain.users.User;
import uk.co.compendiumdev.restlisticator.domain.users.UserAccessPermission;
import uk.co.compendiumdev.restlisticator.http.ApiEndPoint;

public class DeleteListApiAction {
    private final ApiRequest apiRequest;
    private final ApiResponse apiResponse;
    private TheListicator listicator;
    private UserAuthenticator userAuthenticator;

    public DeleteListApiAction(ApiRequest apiRequest, ApiResponse apiResponse) {
        this.apiRequest = apiRequest;
        this.apiResponse = apiResponse;
    }

    public DeleteListApiAction setListicator(TheListicator listicator) {
        this.listicator = listicator;
        return this;
    }

    public ApiResponse perform() {

        String guid = apiRequest.getPathParts()[0];

        ListicatorList listToDelete = listicator.getList(guid);

        if(listToDelete==null){
            apiResponse.setAsNotFound();
            return apiResponse;
        }

        User authenticatedUser = userAuthenticator.getAuthenticatedUser(apiRequest);

        if(!authenticatedUser.permissions().can(UserAccessPermission.CAN_DELETE_ANY, ApiEndPoint.LISTS)){
            // check if user is the owner, or it is blank, or the admin created it
            if(listToDelete.getOwner() == null || listToDelete.getOwner().length()==0 || listToDelete.getOwner().contentEquals("admin")){
                // anyone can delete an unowned or admin owned list
            }else{
                if(!listToDelete.getOwner().contentEquals(authenticatedUser.getUsername())){
                    // you can't delete this
                    apiResponse.setAsUnauthorized();
                    return apiResponse;
                }
            }
        }

        boolean didDelete = listicator.deleteList(guid);

        if(didDelete){
            apiResponse.setAsSuccessfulNoContent();
        }else{
            apiResponse.setAsNotFound();
        }

        return apiResponse;

    }

    public DeleteListApiAction setUserAuthenticator(UserAuthenticator userAuthenticator) {
        this.userAuthenticator = userAuthenticator;
        return this;
    }
}
