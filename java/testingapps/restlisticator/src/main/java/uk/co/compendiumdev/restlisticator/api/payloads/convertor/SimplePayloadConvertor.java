package uk.co.compendiumdev.restlisticator.api.payloads.convertor;

import com.google.gson.Gson;
import uk.co.compendiumdev.restlisticator.api.ApiRequestResponseFormat;
import uk.co.compendiumdev.restlisticator.api.payloads.convertor.xml.MyXmlProcessor;


public class SimplePayloadConvertor<T> {

    private final Class<T> theClass;

    public SimplePayloadConvertor(Class<T> typeParameterClass){
        this.theClass = typeParameterClass;
    }
    public String convert(T payload, ApiRequestResponseFormat requestFormat){
        Gson gson = new Gson();

        if(requestFormat==ApiRequestResponseFormat.JSON){
            return gson.toJson(payload);
        }else{
            // https://stackoverflow.com/questions/5189690/how-to-serialize-and-de-serialize-objects-using-jaxb
            return new MyXmlProcessor().simplePayloadConversionConvert(theClass, payload);
        }
    }
}
