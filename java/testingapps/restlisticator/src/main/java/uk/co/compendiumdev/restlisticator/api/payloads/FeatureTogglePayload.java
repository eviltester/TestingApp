package uk.co.compendiumdev.restlisticator.api.payloads;

//import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement(name = "toggle")
public class FeatureTogglePayload {
    public String key;
    public String value;

    public FeatureTogglePayload(){}

    public FeatureTogglePayload(String name, boolean state) {
        this.key = name;
        this.value = Boolean.toString(state);
    }
}
