package uk.co.compendiumdev.restlisticator.api.payloads.convertor;

import com.google.gson.Gson;
import uk.co.compendiumdev.restlisticator.api.ApiRequestResponseFormat;
import uk.co.compendiumdev.restlisticator.api.payloads.convertor.xml.MyXmlProcessor;

/**
 * Created by Alan on 13/07/2017.
 */
public class SimplePayloadStringConvertor<T> {
    private final Class<T> theClass;

    public SimplePayloadStringConvertor(Class<T> classToConvertInto) {
        this.theClass = classToConvertInto;

    }

    public T convert(String body, ApiRequestResponseFormat requestFormat) {
        Gson gson = new Gson();

        try {
            if (requestFormat == ApiRequestResponseFormat.JSON) {
                return gson.fromJson(body, theClass);
            } else {
                return new MyXmlProcessor().simplePayloadStringConvertorConvert(body, theClass);
            }
            //return null;
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
