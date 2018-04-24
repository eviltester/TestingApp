package uk.co.compendiumdev.restlisticator.api.action;

import uk.co.compendiumdev.restlisticator.api.ApiRequest;
import uk.co.compendiumdev.restlisticator.api.ApiResponse;
import uk.co.compendiumdev.restlisticator.api.payloads.ResultsFilter;
import uk.co.compendiumdev.restlisticator.api.payloads.convertor.PayloadConvertor;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;

/**
 * Created by Alan on 18/07/2017.
 */
public class GetSpecificListApiAction {
    private final ApiResponse apiResponse;
    private final ApiRequest apiRequest;
    private PayloadConvertor payloadConvertor;
    private TheListicator listicator;

    public GetSpecificListApiAction(ApiRequest apiRequest, ApiResponse apiResponse) {
        this.apiRequest = apiRequest;
        this.apiResponse = apiResponse;
    }

    public GetSpecificListApiAction setConvertor(PayloadConvertor convertor) {
        this.payloadConvertor = convertor;
        return this;
    }

    public GetSpecificListApiAction setListicator(TheListicator listicator) {
        this.listicator = listicator;
        return this;
    }

    public ApiResponse perform() {
        
        String guid = apiRequest.getPathParts()[0];
        ListicatorList list =  listicator.getList(guid);

        if(list==null){
            apiResponse.setAsNotFound();
            return apiResponse;
        }

        ResultsFilter resultsFilter = apiRequest.getResultsFilter();

        String payload= payloadConvertor.convert(list,
                            apiRequest.getResponseFormat(), resultsFilter);

        if( payload==null || payload.length()==0){
            // something went wrong converting payload
            apiResponse.setAsBadRequest("Could not convert payload");
        }else{
            apiResponse.setBody(payload);
            apiResponse.setAsSuccessful_OK();
        }

        return apiResponse;
    }
}
