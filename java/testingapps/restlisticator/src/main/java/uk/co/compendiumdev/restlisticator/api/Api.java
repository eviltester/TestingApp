package uk.co.compendiumdev.restlisticator.api;

import uk.co.compendiumdev.restlisticator.api.action.*;
import uk.co.compendiumdev.restlisticator.api.payloads.FeatureTogglePayload;
import uk.co.compendiumdev.restlisticator.api.payloads.ListOfFeatureTogglesPayload;
import uk.co.compendiumdev.restlisticator.api.payloads.convertor.PayloadConvertor;
import uk.co.compendiumdev.restlisticator.documentation.DocumentationReader;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;
import uk.co.compendiumdev.restlisticator.domain.users.User;
import uk.co.compendiumdev.restlisticator.domain.users.UserAccessPermission;
import uk.co.compendiumdev.restlisticator.domain.users.UserAccessPermissions;
import uk.co.compendiumdev.restlisticator.http.ApiEndPoint;
import uk.co.compendiumdev.restlisticator.http.HttpVerb;
import uk.co.compendiumdev.restlisticator.sparkrestserver.SparkApiRequest;
import uk.co.compendiumdev.restlisticator.sparkrestserver.SparkApiResponse;
import uk.co.compendiumdev.restlisticator.testappconfig.FeatureToggles;

import java.util.HashMap;
import java.util.Map;

public class Api {

    // TODO split this into App - listicator, usermanager and Api(wrapper around app)
    private TheListicator listicator;
    private String documentationHTML;
    private Map<String, User> users = new HashMap<>();

    private PayloadConvertor payloadConvertor=new PayloadConvertor();

    public Api(TheListicator theListicator) {
        resetApi(theListicator);

        // cache documentation
        DocumentationReader docs = new DocumentationReader();
        this.documentationHTML = docs.getInstructionsAsHTMLPage();
    }

    public void resetApi(final TheListicator theListicator) {
        users = new HashMap<>();
        this.listicator = theListicator;

        // create some default users

        // superadmin can do everything that is allowed ever
        users.put("superadmin", new User("superadmin","password").setPermissions(UserAccessPermissions.getSuperAdmin()));

        // admin can not toggle features on and off
        UserAccessPermissions permissions = UserAccessPermissions.getSuperAdmin();
        UserAccessPermission permission = permissions.forEndpoint(ApiEndPoint.FEATURE_TOGGLES);
        permission.setAll(false).setCan(HttpVerb.GET);
        users.put("admin", new User("admin","password").setPermissions(permissions));

        // normal user
        addNormalUser("user", "password");
    }

    public void addNormalUser(String username, String password){
        users.put(username, new User(username, password));
    }


    public ApiResponse getHeartbeat(ApiRequest request, ApiResponse response) {
        response.setAsSuccessful_OK();
        response.setContentTypeBasedOnAccept(request.getResponseFormat());
        return response;
    }



    public ApiResponse getLists(ApiRequest request, ApiResponse response) {
//        response.setAsSuccessful_OK();
//        response.setBody(payloadConvertor.convert(listicator.getLists(), request.getResponseFormat()));
//        response.setContentTypeBasedOnAccept(request.getResponseFormat());
//        return response;
//
        response.setContentTypeBasedOnAccept(request.getResponseFormat());

        return new GetListsApiAction(request, response).
                setConvertor(payloadConvertor).
                setListicator(listicator).
                perform();

    }

    public ApiResponse setLists(ApiRequest apiRequest, ApiResponse apiResponse) {

        apiResponse.setContentTypeBasedOnAccept(apiRequest.getResponseFormat());

        return new SetListsApiAction(apiRequest, apiResponse).
                        setConvertor(payloadConvertor).
                        setListicator(listicator).
                        setUserAuthenticator(new UserAuthenticator(users)).
                    perform();
    }

    public ApiResponse putList(ApiRequest apiRequest, ApiResponse apiResponse) {

        apiResponse.setContentTypeBasedOnAccept(apiRequest.getResponseFormat());
        return new PutListsApiAction(apiRequest, apiResponse).
                setConvertor(payloadConvertor).
                setListicator(listicator).
                setUserAuthenticator(new UserAuthenticator(users)).
                perform();
    }

    public ApiResponse deleteList(ApiRequest apiRequest, ApiResponse apiResponse) {


        apiResponse.setContentTypeBasedOnAccept(apiRequest.getResponseFormat());
        if(!apiRequestHasPathArgument(apiRequest)){
            apiResponse.setAsBadRequest("A list guid is required to allow processing of this request");
            return apiResponse;
        }

        // User must be authenticated to call this api request
        User authenticatedUser = new UserAuthenticator(users).getAuthenticatedUser(apiRequest);
        if(authenticatedUser==null){
            apiResponse.setAsUnauthenticated();
            return apiResponse;
        }

        // only allow users with correct permission
        if(!authenticatedUser.permissions().can(HttpVerb.DELETE, ApiEndPoint.LISTS)){
            apiResponse.setAsUnauthorized();
            return apiResponse;
        }

        return new DeleteListApiAction(apiRequest, apiResponse).
                        setListicator(listicator).
                        setUserAuthenticator(new UserAuthenticator(users)).
                    perform();

    }

    public ApiResponse getList(ApiRequest apiRequest, ApiResponse apiResponse) {


        apiResponse.setContentTypeBasedOnAccept(apiRequest.getResponseFormat());

        if(!apiRequestHasPathArgument(apiRequest)){
            apiResponse.setAsBadRequest("A list guid is required to allow processing of this request");
            return apiResponse;
        }

        return new GetSpecificListApiAction(apiRequest, apiResponse).
                        setConvertor(payloadConvertor).
                        setListicator(listicator).
                perform();
    }


    public ApiResponse partialAmendList(ApiRequest apiRequest, ApiResponse apiResponse) {

        // suitable for POST
        apiResponse.setContentTypeBasedOnAccept(apiRequest.getResponseFormat());
        return partialAmendList(apiRequest, apiResponse, false);
    }

    public ApiResponse patchAmendList(ApiRequest apiRequest, ApiResponse apiResponse) {

        // suitable for PATCH
        apiResponse.setContentTypeBasedOnAccept(apiRequest.getResponseFormat());
        return partialAmendList(apiRequest, apiResponse, true);
    }


    private ApiResponse partialAmendList(ApiRequest apiRequest, ApiResponse apiResponse, Boolean enforceListMustExist) {


        apiResponse.setContentTypeBasedOnAccept(apiRequest.getResponseFormat());

        if(!apiRequestHasPathArgument(apiRequest)){
            apiResponse.setAsBadRequest("A list guid is required to allow processing of this request");
            return apiResponse;
        }

        // PATCH the list must exist
        if(FeatureToggles.BUG_007_FIXED.getState()) {
            if (enforceListMustExist) {
                ListicatorList list = listicator.getList(apiRequest.getPathParts()[0]);
                if (list == null) {
                    apiResponse.setAsNotFound();
                    return apiResponse;
                }
            }
        }

        // User must be authenticated to call this api request
        User authenticatedUser = new UserAuthenticator(users).getAuthenticatedUser(apiRequest);
        if(authenticatedUser==null){
            apiResponse.setAsUnauthenticated();
            return apiResponse;
        }

        if(FeatureToggles.BUG_005_FIXED.getState()) {
            // only allow users with correct permission
            if (!authenticatedUser.permissions().can(apiRequest.getVerb(), ApiEndPoint.LISTS)) {
                apiResponse.setAsUnauthorized();
                return apiResponse;
            }
        }

        return new PartialAmendListApiAction(apiRequest, apiResponse).
                    setConvertor(payloadConvertor).
                    setUserAuthenticator(new UserAuthenticator(users)).
                    setListicator(listicator).
                perform();
    }



    private boolean apiRequestHasPathArgument(ApiRequest apiRequest) {
        String []path = apiRequest.getPathParts();

        if(path==null)
            return false;

        if(path.length==0)
            return false;

        return true;
    }


    public ApiResponse getFeatureToggles(ApiRequest apiRequest, ApiResponse apiResponse) {


        apiResponse.setContentTypeBasedOnAccept(apiRequest.getResponseFormat());

        if(FeatureToggles.BUG_006_FIXED.getState()) {
            User authenticatedUser = new UserAuthenticator(users).getAuthenticatedUser(apiRequest);
            if(authenticatedUser==null){
                apiResponse.setAsUnauthenticated();
                return apiResponse;
            }
        }

        ListOfFeatureTogglesPayload theToggles = new ListOfFeatureTogglesPayload();
        for(FeatureToggles toggle : FeatureToggles.values()){
            FeatureTogglePayload payload = new FeatureTogglePayload(toggle.name(),toggle.getState());
            theToggles.toggles.add(payload);
        }

        apiResponse.setAsSuccessful_OK();
        apiResponse.setBody(payloadConvertor.convert(theToggles, apiRequest.getResponseFormat()));
        return apiResponse;

    }

    public ApiResponse setFeatureToggles(ApiRequest apiRequest, ApiResponse apiResponse) {

        apiResponse.setContentTypeBasedOnAccept(apiRequest.getResponseFormat());

        // User must be authenticated to call this api request
        User authenticatedUser = new UserAuthenticator(users).getAuthenticatedUser(apiRequest);
        if(authenticatedUser==null){
            apiResponse.setAsUnauthenticated();
            return apiResponse;
        }

        // only allow users with correct permission
        if(!authenticatedUser.permissions().can(HttpVerb.POST, ApiEndPoint.FEATURE_TOGGLES)){
            apiResponse.setAsUnauthorized();
            return apiResponse;
        }

        return new SetFeatureTogglesApiAction(apiRequest, apiResponse).
                        setConvertor(payloadConvertor).
                    perform();
    }

    public ApiResponse getUserDetails(ApiRequest apiRequest, ApiResponse apiResponse) {

        apiResponse.setContentTypeBasedOnAccept(apiRequest.getResponseFormat());

        // MUST be a path for the username
        if(!apiRequestHasPathArgument(apiRequest)){
            apiResponse.setAsBadRequest("A username is required to allow processing of this request");
            return apiResponse;
        }

        // User must be authenticated to call this api request
        User authenticatedUser = new UserAuthenticator(users).getAuthenticatedUser(apiRequest);
        if(authenticatedUser==null){
            apiResponse.setAsUnauthenticated();
            return apiResponse;
        }

        return new GetUserDetailsApiAction(apiRequest, apiResponse).
                        setConvertor(payloadConvertor).
                        setAuthenticatedUser(authenticatedUser).
                        setUserAuthenticator(new UserAuthenticator(users)).
                    perform();
    }

    public ApiResponse createUserDetails(ApiRequest apiRequest, ApiResponse apiResponse) {
        apiResponse.setContentTypeBasedOnAccept(apiRequest.getResponseFormat());

        // User must be authenticated to call this api request
        User authenticatedUser = new UserAuthenticator(users).getAuthenticatedUser(apiRequest);
        if(authenticatedUser==null){
            apiResponse.setAsUnauthenticated();
            return apiResponse;
        }

        CreateUsersApiAction action = new CreateUsersApiAction(apiRequest, apiResponse);
        action.setConvertor(payloadConvertor).
                setAuthenticatedUser(authenticatedUser).
                setUserAuthenticator(new UserAuthenticator(users));

        ApiResponse response = action.perform();

        if(response.getStatus()==201) {
            addNormalUser(action.userToAdd.getUsername(), action.userToAdd.getPassword());
        }

        return response;
    }

    public ApiResponse getUsers(ApiRequest apiRequest, ApiResponse apiResponse) {

        apiResponse.setContentTypeBasedOnAccept(apiRequest.getResponseFormat());

        return new GetUsersApiAction(apiRequest, apiResponse).
                setConvertor(payloadConvertor).
                setUserAuthenticator(new UserAuthenticator(users)).
                perform();
    }

    public ApiResponse setUserPassword(ApiRequest apiRequest, ApiResponse apiResponse) {
        return setUserField(apiRequest, apiResponse, "password");

    }

    public ApiResponse setUserApiKey(ApiRequest apiRequest, ApiResponse apiResponse) {
        return setUserField(apiRequest, apiResponse, "apikey");
    }

    public ApiResponse setUserField(ApiRequest apiRequest, ApiResponse apiResponse, String fieldName) {
        // MUST be a path for the username
        if(!apiRequestHasPathArgument(apiRequest)){
            apiResponse.setAsBadRequest("A username is required to allow processing of this request");
            return apiResponse;
        }

        // User must be authenticated to call this api request
        User authenticatedUser = new UserAuthenticator(users).getAuthenticatedUser(apiRequest);
        if(authenticatedUser==null){
            apiResponse.setAsUnauthenticated();
            return apiResponse;
        }

        return new SetUserPasswordApiAction(apiRequest, apiResponse).
                setConvertor(payloadConvertor).
                setUserAuthenticator(new UserAuthenticator(users)).
                setAuthenticatedUser(authenticatedUser).
                setFieldToAmend(fieldName).
                perform();
    }

    public boolean isMethodAllowed(String path, SparkApiRequest sparkApiRequest) {

        // by default the following are allowed
        HashMap <String,String>pathVerbs = new HashMap<String,String>();

        pathVerbs.put(ApiEndPoint.LISTS.getPath(),"|get|post|put|options|");
        pathVerbs.put(ApiEndPoint.LISTS.getPath("*"), "|get|put|post|patch|delete|options|");
        pathVerbs.put(ApiEndPoint.HEARTBEAT.getPath(),"|get|options|");
        pathVerbs.put(ApiEndPoint.FEATURE_TOGGLES.getPath(),"|get|post|options|");
        pathVerbs.put(ApiEndPoint.USERS.getPath(""),"|get|post|options|");
        pathVerbs.put(ApiEndPoint.USERS.getPath("*"),"|get|options|");
        pathVerbs.put(ApiEndPoint.USERS.getPath("*/password"),"|put|options|");
        pathVerbs.put(ApiEndPoint.USERS.getPath("*/apikey"),"|put|options|");

        // some may be protected by authentication in which case if not provided then 401 would be sent back

        // if authenticated user does not have permission then 405 would be sent back

        // TODO: add some feature toggles here to have bugs around valid verbs being disallowed


        String allowed = pathVerbs.get(path);

        if(allowed!=null) {
            return allowed.contains(sparkApiRequest.getVerb().toLowerCase());
        }

        System.out.println("Warning: have not created allowable check for " + path);
        return true;
    }


    public User getUser(String username) {
        return users.get(username);
    }

    public ApiResponse getDocumentation(SparkApiRequest sparkApiRequest, SparkApiResponse apiResponse) {


        apiResponse.setStatus(200);
        apiResponse.setHeader("Content-Type", "text/html");
        apiResponse.setBody(documentationHTML);
        return apiResponse;
    }

    public void setDocumentationDetails(Integer proxyport, String urlPrefix) {
        this.documentationHTML = documentationHTML.replace("/localhost:4567", "/localhost:" + proxyport + urlPrefix);
    }


}
