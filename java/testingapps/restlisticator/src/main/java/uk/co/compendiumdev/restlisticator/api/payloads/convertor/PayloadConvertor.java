package uk.co.compendiumdev.restlisticator.api.payloads.convertor;


import com.google.gson.Gson;
import org.json.XML;
import uk.co.compendiumdev.restlisticator.api.ApiRequestResponseFormat;
import uk.co.compendiumdev.restlisticator.api.payloads.*;
import uk.co.compendiumdev.restlisticator.api.payloads.convertor.xml.MyXmlProcessor;
import uk.co.compendiumdev.restlisticator.domain.app.ListOfListicatorLists;
import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;
import uk.co.compendiumdev.restlisticator.domain.utils.ReflectionPatcher;
import uk.co.compendiumdev.restlisticator.testappconfig.FeatureToggles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PayloadConvertor {

    Gson gson = new Gson();


    public String convert(ListOfListicatorLists lists, ApiRequestResponseFormat requestFormat) {
        ListOfListicatorListsPayload payload = new ListOfListicatorListsPayload();

        for (ListicatorList aList : lists.getAsList()) {
            ListicatorListPayload list = transformIntoPayloadObject(aList);
            payload.lists.add(list);
        }

        return new SimplePayloadConvertor<ListOfListicatorListsPayload>(ListOfListicatorListsPayload.class).convert(payload, requestFormat);
    }

    public String convert(ListOfListicatorListsPayload payload, ApiRequestResponseFormat requestFormat) {
        return new SimplePayloadConvertor<ListOfListicatorListsPayload>(ListOfListicatorListsPayload.class).convert(payload, requestFormat);
    }

    public String convert(ListicatorList aList, ApiRequestResponseFormat requestFormat) {

        ListicatorListPayload payload = transformIntoPayloadObject(aList);

        return new SimplePayloadConvertor<ListicatorListPayload>(ListicatorListPayload.class).convert(payload, requestFormat);

    }

    public String convert(ListicatorList aList, ApiRequestResponseFormat requestFormat, ResultsFilter resultsFilter) {
        ListicatorListPayload payload = transformIntoPayloadObject(aList);

        // TODO: set everything that is not in with to null
        // set everything that is in without  to null
        Map<String,String> patches = new HashMap<>();
        for(String field : resultsFilter.getWithoutFilterValues()){
            patches.put(field,null);
        }

        if(patches.size()>0)
            new ReflectionPatcher(payload, ListicatorListPayload.class).patch(patches);

        return new SimplePayloadConvertor<ListicatorListPayload>(ListicatorListPayload.class).convert(payload, requestFormat);

    }


    public String convert(ListOfFeatureTogglesPayload theToggles, ApiRequestResponseFormat responseFormat) {
        return new SimplePayloadConvertor(ListOfFeatureTogglesPayload.class).convert(theToggles, responseFormat);
    }

    public String convert(UserListPayload usersList, ApiRequestResponseFormat responseFormat) {
        return new SimplePayloadConvertor(UserListPayload.class).convert(usersList, responseFormat);
    }

    public String convert(UserPayload userDetails, ApiRequestResponseFormat responseFormat) {
        return new SimplePayloadConvertor(UserPayload.class).convert(userDetails, responseFormat);
    }


    public ListOfListicatorLists convertFromPayloadStringToListListicatorLists(String payload, ApiRequestResponseFormat requestFormat) {
        ListOfListicatorLists newLists=new ListOfListicatorLists();

        switch (requestFormat){
            case JSON:
                newLists = convertFromJsonPayloadStringToListListicatorLists(payload);
                break;
            case XML:
                newLists = convertFromXmlPayloadStringToListListicatorLists(payload);
                break;
        }

        return newLists;
    }

    private ListOfListicatorLists convertFromXmlPayloadStringToListListicatorLists(String payload) {
        ListOfListicatorLists newLists=new ListOfListicatorLists();

        try {
            newLists = new MyXmlProcessor().getListOfListicatorLists(payload);


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (newLists.listCount() == 0) {
            ListicatorList list = convertFromPayloadStringToListicatorListXML(payload);
            if(list!=null){
                newLists.addList(list);
            }
        }

        return newLists;
    }

    private ListicatorList convertFromPayloadStringToListicatorListXML(String payload) {
        ListicatorListPayload list=null;
        try {
            list = new MyXmlProcessor().getListicatorList(payload);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (list==null || (list.title == null && list.guid == null)) {
            // that didn't work
        } else {
            return new PayloadConvertor().transformFromPayLoadObject(list);
        }

        return null;
    }


    public ListicatorList convertFromPayloadStringToListicatorList(String payload, ApiRequestResponseFormat requestFormat){

        switch (requestFormat){
            case JSON:
                return convertFromPayloadStringToListicatorListJSON(payload);
            case XML:
                return convertFromPayloadStringToListicatorListXML(payload);
        }

        return null;
    }

    public UserPayload convertFromPayloadStringToUserPayload(String payload, ApiRequestResponseFormat requestFormat) {
        switch (requestFormat){
            case JSON:
                return convertFromPayloadStringToUserPayloadJSON(payload);
            case XML:
                return convertFromPayloadStringToUserPayloadXML(payload);
        }

        return null;
    }

    private UserPayload convertFromPayloadStringToUserPayloadXML(String payload) {
        UserPayload user=null;
        try {
            user= new MyXmlProcessor().getUserPayload(payload);
            return user;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private UserPayload convertFromPayloadStringToUserPayloadJSON(String payload) {
        try {
            UserPayload user = gson.fromJson(payload, UserPayload.class);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ListicatorList convertFromPayloadStringToListicatorListJSON(String payload) {
        try {
            ListicatorListPayload list = gson.fromJson(payload, ListicatorListPayload.class);
            if (list.title == null && list.guid == null) {
                // that didn't work
            } else {
                return new PayloadConvertor().transformFromPayLoadObject(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ListOfListicatorLists convertFromJsonPayloadStringToListListicatorLists(String payload) {
        ListOfListicatorLists newLists=new ListOfListicatorLists();

        try {
            ListOfListicatorListsPayload lists = gson.fromJson(payload, ListOfListicatorListsPayload.class);
            newLists = new PayloadConvertor().transformFromPayLoadObject(lists);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (newLists.listCount() == 0) {
            ListicatorList theList = convertFromPayloadStringToListicatorListJSON(payload);
            if(theList!=null){
                newLists.addList(theList);
            }
        }

        return newLists;
    }

    public ListOfListicatorLists transformFromPayLoadObject(ListOfListicatorListsPayload lists) {
        ListOfListicatorLists newListicatorList = new ListOfListicatorLists();

        for(ListicatorListPayload list : lists.lists){
            newListicatorList.addList(transformFromPayLoadObject(list));
        }

        return newListicatorList;
    }

    private ListicatorList transformFromPayLoadObject(ListicatorListPayload list) {
        ListicatorList newList = new ListicatorList(list.title, list.description);
        if(list.guid!=null){
            newList.forceSetGuid(list.guid);
        }
        return newList;
    }



    public ListicatorListPayload transformIntoPayloadObject(ListicatorList aList) {
        ListicatorListPayload payload = new ListicatorListPayload();
        payload.title = aList.getTitle();
        payload.guid = aList.getGUID();
        payload.description = aList.getDescription();
        payload.owner = aList.getOwner();

        if(FeatureToggles.BUG_003_FIXED.getState()) {
            payload.createdDate = aList.getCreatedDate();
            payload.amendedDate = aList.getAmendedDate();
        }
        return payload;
    }


    public Map<String, String> convertFromPayloadStringToPatchMap(String body, ApiRequestResponseFormat requestFormat) {

        try {
            if (requestFormat == ApiRequestResponseFormat.JSON) {
                return gson.fromJson(body, Map.class);
            } else {

                // brought in lightweight library to handle this, and this is a hack
                Map withHead = gson.fromJson(XML.toJSONObject(body).toString(), Map.class);
                ArrayList outer = new ArrayList();
                outer.addAll(withHead.keySet());
                return (Map<String, String>) withHead.get(outer.get(0));
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public ListicatorListPayload convertFromPayloadStringToPatchPayloadForList(String body, ApiRequestResponseFormat requestFormat) {

        return new SimplePayloadStringConvertor<>(ListicatorListPayload.class).convert(body, requestFormat);
    }

    public ListOfFeatureTogglesPayload convertFromPayloadStringToPatchPayloadForListOfFeatureTogglesPayload(String body, ApiRequestResponseFormat requestFormat) {

        return new SimplePayloadStringConvertor<ListOfFeatureTogglesPayload>(ListOfFeatureTogglesPayload.class).convert(body, requestFormat);

    }



}
