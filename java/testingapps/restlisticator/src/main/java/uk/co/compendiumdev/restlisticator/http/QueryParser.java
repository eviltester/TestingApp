package uk.co.compendiumdev.restlisticator.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alan on 19/07/2017.
 */
public class QueryParser {
    private final String originalQuery;
    private Map<String, String> attributeValuePairs;

    public QueryParser(String fromQuery) {

        this.attributeValuePairs = new HashMap<String,String>();

        this.originalQuery = fromQuery==null ? "" : fromQuery;

        if(this.originalQuery.length()==0)
            return;

        String query = this.originalQuery.trim();
        if(query.startsWith("?"))
            query = query.substring(1);

        String [] queryParts = query.split("&");

        for(String queryPart : queryParts){
            String []part = queryPart.split("=");
            if(part!=null){
                if(part.length==1){
                    attributeValuePairs.put(part[0], "");
                }
                if(part.length==2){
                    attributeValuePairs.put(part[0], part[1]);
                }
            }
        }


    }

    public int getAttributeCount() {
        return attributeValuePairs.size();
    }

    public boolean hasAttribute(String attributeName) {
        return attributeValuePairs.containsKey(attributeName);
    }

    public String getValueFor(String attributeName) {
        if(hasAttribute(attributeName)){
            try {
                return URLDecoder.decode(attributeValuePairs.get(attributeName), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return attributeValuePairs.get(attributeName);
            }
        }

        return "";
    }

    public List<String> getAttributeKeys() {

        List<String> attributeKeys = new ArrayList();

        // ignore some keys with special meaning - without, with
        for(String aKey : attributeValuePairs.keySet()){
            switch (aKey){
                case "without":
                case "with":
                    break;
                default:
                    attributeKeys.add(aKey);
                    break;
            }
        }

        return attributeKeys;
    }
}
