package uk.co.compendiumdev.restlisticator.api.payloads.convertor.xml;

import com.thoughtworks.xstream.XStream;
import uk.co.compendiumdev.restlisticator.api.payloads.*;
import uk.co.compendiumdev.restlisticator.api.payloads.convertor.PayloadConvertor;
import uk.co.compendiumdev.restlisticator.domain.app.ListOfListicatorLists;

//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;
//import java.io.StringReader;
//import javax.xml.bind.Marshaller;
//import java.io.StringWriter;

// Xstream security configuration
// https://stackoverflow.com/questions/44698296/security-framework-of-xstream-not-initialized-xstream-is-probably-vulnerable

// created to help me isolate the xml processing and see if I can remove JaxB since this adds 2.5 meg to the package size
public class MyXmlProcessor {

    XStream xstream;

    public MyXmlProcessor(){
        xstream = new XStream();
        xstream.alias("user", UserPayload.class);
        xstream.alias("users", UserListPayload.class);
        xstream.alias("lists", ListOfListicatorListsPayload.class);
        xstream.addImplicitCollection(ListOfListicatorListsPayload.class, "lists");
        xstream.alias("list", ListicatorListPayload.class);
        xstream.alias("toggles", ListOfFeatureTogglesPayload.class);
        xstream.addImplicitCollection(ListOfFeatureTogglesPayload.class, "toggles");
        xstream.alias("toggle", FeatureTogglePayload.class);
    }


    public UserPayload getUserPayload(String payload) {
        //JAXBContext context = null;
        try {

            UserPayload user = (UserPayload) xstream.fromXML(payload);
            return user;

            //context = JAXBContext.newInstance(UserPayload.class);
            //Unmarshaller m = context.createUnmarshaller();
            //return (UserPayload)m.unmarshal(new StringReader(payload));
            //} catch (JAXBException e) {
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ListOfListicatorLists getListOfListicatorLists(String payload) {
        //JAXBContext context = null;
        try {
            //context = JAXBContext.newInstance(ListOfListicatorListsPayload.class);
            //Unmarshaller m = context.createUnmarshaller();
            //ListOfListicatorListsPayload lists = (ListOfListicatorListsPayload)m.unmarshal(new StringReader(payload));
            ListOfListicatorListsPayload lists = (ListOfListicatorListsPayload) xstream.fromXML(payload);

           return new PayloadConvertor().transformFromPayLoadObject(lists);

            //} catch (JAXBException e) {
        }catch(Exception e){
            e.printStackTrace();
            return new ListOfListicatorLists();
        }
    }

    public ListicatorListPayload getListicatorList(String payload) {
        //JAXBContext context = null;
        try {
            //context = JAXBContext.newInstance(ListicatorListPayload.class);
            //Unmarshaller m = context.createUnmarshaller();
            //return (ListicatorListPayload)m.unmarshal(new StringReader(payload));

            ListicatorListPayload list = (ListicatorListPayload) xstream.fromXML(payload);
            return list;

            //} catch (JAXBException e) {
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public <T> String simplePayloadConversionConvert(Class<T> theClass, T payload) {
        //StringWriter writer = new StringWriter();
        //JAXBContext context = null;
        try {
//            context = JAXBContext.newInstance(theClass);
//            Marshaller m = context.createMarshaller();
//            m.marshal(payload, writer);
            String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
            return header + xstream.toXML(payload);
            //} catch (JAXBException e) {
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }

        //return writer.toString();
    }

    public <T> T simplePayloadStringConvertorConvert(String body, Class<T> theClass) {
        try {
            //JAXBContext context = JAXBContext.newInstance(theClass);
            //Unmarshaller m = context.createUnmarshaller();
            //return (T)m.unmarshal(new StringReader(body));
            return (T)xstream.fromXML(body);
            //} catch (JAXBException e) {
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
