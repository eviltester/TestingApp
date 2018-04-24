package uk.co.compendiumdev.restlisticator.api.action;

import uk.co.compendiumdev.restlisticator.http.QueryParser;

import java.lang.reflect.Field;

public class ObjectQueryFilter {
    private final QueryParser queryParser;

    public ObjectQueryFilter(QueryParser queryParser) {
        this.queryParser = queryParser;
    }

    public boolean matches(Object objectToMatch) {
        boolean matches = false;

        for(String attributeName : queryParser.getAttributeKeys()){

            String queryValue = queryParser.getValueFor(attributeName);
            boolean exactMatch = true;
            if(queryValue!=null){
                if(queryValue.startsWith("\"") && queryValue.endsWith("\"")){
                    exactMatch=false;
                    queryValue=queryValue.substring(1,queryValue.length()-1);
                }
            }

            try {
                Field field = objectToMatch.getClass().getDeclaredField(attributeName);

                try {
                    //field.setAccessible(true);
                    //if(field.isAccessible()) {
                        String value = (String) field.get(objectToMatch);
                        if(value==null)
                            return false;  // could not find a value in the set to match against

                        if(queryValue==null) {        // could not find a search term
                            return false;
                        }else{
                            if(exactMatch && !value.contentEquals(queryValue)){
                                return false;
                            }else{
                                if(!value.contains(queryValue)){
                                    return false;
                                }
                            }
                        }

                    //}
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }
}
