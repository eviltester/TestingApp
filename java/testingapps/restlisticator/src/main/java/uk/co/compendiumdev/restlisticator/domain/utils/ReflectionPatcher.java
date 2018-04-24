package uk.co.compendiumdev.restlisticator.domain.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ReflectionPatcher {

    private final Object thing;
    private final Class theClass;

    private List<String> fieldsInError = new ArrayList<>();
    private List<String> fieldsPatched = new ArrayList<>();

    public ReflectionPatcher(Object thing, Class theClass) {
        this.thing = thing;
        this.theClass = theClass;

    }

    public void patch(Map<String, String> patches) {
        for(String fieldName : patches.keySet()){
            boolean hadToSetAccessible = false;
            Field declaration=null;
            Field field=null;

            try {
                declaration = theClass.getDeclaredField(fieldName);
                if(!declaration.isAccessible()){
                    hadToSetAccessible = true;
                    declaration.setAccessible(true);
                }
                declaration.set(thing, patches.get(fieldName));
                fieldsPatched.add(fieldName);

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                fieldsInError.add(fieldName + " - did not exist" );
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                fieldsInError.add(fieldName + " - could not access");
            }finally {
                if(hadToSetAccessible=true && declaration!=null){
                    declaration.setAccessible(false);
                }
            }
        }
    }

    public List<String> getFieldsInError() {
        return fieldsInError;
    }

    public List<String> getFieldsPatched() {
        return fieldsPatched;
    }
}
