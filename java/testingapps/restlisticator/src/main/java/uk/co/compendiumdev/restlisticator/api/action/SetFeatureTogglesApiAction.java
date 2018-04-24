package uk.co.compendiumdev.restlisticator.api.action;

import uk.co.compendiumdev.restlisticator.api.ApiRequest;
import uk.co.compendiumdev.restlisticator.api.ApiResponse;
import uk.co.compendiumdev.restlisticator.api.payloads.FeatureTogglePayload;
import uk.co.compendiumdev.restlisticator.api.payloads.ListOfFeatureTogglesPayload;
import uk.co.compendiumdev.restlisticator.api.payloads.convertor.PayloadConvertor;
import uk.co.compendiumdev.restlisticator.api.payloads.convertor.SimplePayloadConvertor;
import uk.co.compendiumdev.restlisticator.testappconfig.FeatureToggles;

/**
 * Created by Alan on 18/07/2017.
 */
public class SetFeatureTogglesApiAction {
    private final ApiResponse response;
    private final ApiRequest request;
    private PayloadConvertor payloadConvertor;


    public SetFeatureTogglesApiAction(ApiRequest apiRequest, ApiResponse apiResponse) {
        this.request = apiRequest;
        this.response = apiResponse;
    }

    public SetFeatureTogglesApiAction setConvertor(PayloadConvertor convertor) {
        this.payloadConvertor = convertor;
        return this;
    }

    public ApiResponse perform() {


        ListOfFeatureTogglesPayload toggles = payloadConvertor.convertFromPayloadStringToPatchPayloadForListOfFeatureTogglesPayload(
                request.getBody(), request.getRequestFormat());

        ListOfFeatureTogglesPayload responseToggles = new ListOfFeatureTogglesPayload();

        for(FeatureTogglePayload toggle : toggles.toggles){
            if(toggle.key!=null) {
                FeatureToggles thistoggle = FeatureToggles.valueOf(toggle.key);
                if (thistoggle != null) {
                    thistoggle.setState(Boolean.valueOf(toggle.value));

                    responseToggles.toggles.add(new FeatureTogglePayload(thistoggle.name(), thistoggle.getState()));
                }
            }
        }

        response.setAsSuccessful_OK();
        response.setBody(new SimplePayloadConvertor<ListOfFeatureTogglesPayload>(ListOfFeatureTogglesPayload.class).convert(responseToggles, request.getResponseFormat()));
        return response;
    }
}
