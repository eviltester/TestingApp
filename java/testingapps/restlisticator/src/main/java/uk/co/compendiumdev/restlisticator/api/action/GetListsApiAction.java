package uk.co.compendiumdev.restlisticator.api.action;

import uk.co.compendiumdev.restlisticator.api.ApiRequest;
import uk.co.compendiumdev.restlisticator.api.ApiResponse;
import uk.co.compendiumdev.restlisticator.api.payloads.ListOfListicatorListsPayload;
import uk.co.compendiumdev.restlisticator.api.payloads.ListicatorListPayload;
import uk.co.compendiumdev.restlisticator.api.payloads.convertor.PayloadConvertor;
import uk.co.compendiumdev.restlisticator.domain.app.ListOfListicatorLists;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;
import uk.co.compendiumdev.restlisticator.http.QueryParser;

/**
 * Created by Alan on 15/08/2017.
 */
public class GetListsApiAction implements ApiAction{

    private final ApiResponse apiResponse;
    private final ApiRequest apiRequest;
    private PayloadConvertor payloadConvertor;
    private TheListicator listicator;

    public GetListsApiAction(ApiRequest apiRequest, ApiResponse apiResponse) {
        this.apiRequest = apiRequest;
        this.apiResponse = apiResponse;
    }

    @Override
    public ApiResponse perform() {
        ListOfListicatorLists lists = listicator.getLists();

        ObjectQueryFilter queryFilter = new ObjectQueryFilter(new QueryParser(apiRequest.getQuery()));

        ListOfListicatorListsPayload payload = new ListOfListicatorListsPayload();

        for (ListicatorList aList : lists.getAsList()) {
            ListicatorListPayload list = payloadConvertor.transformIntoPayloadObject(aList);

            if(queryFilter.matches(list)) {
                payload.lists.add(list);
            }
        }

        String payloadAsString = payloadConvertor.convert(payload,  apiRequest.getResponseFormat());
        apiResponse.setBody(payloadAsString);
        apiResponse.setAsSuccessful_OK();

        return apiResponse;
    }

    public GetListsApiAction setConvertor(PayloadConvertor convertor) {
        this.payloadConvertor = convertor;
        return this;
    }

    public GetListsApiAction setListicator(TheListicator listicator) {
        this.listicator = listicator;
        return this;
    }
}
