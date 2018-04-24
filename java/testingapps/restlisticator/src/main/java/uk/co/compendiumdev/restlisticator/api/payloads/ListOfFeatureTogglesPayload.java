package uk.co.compendiumdev.restlisticator.api.payloads;


//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

//@XmlRootElement(name = "toggles")
public class ListOfFeatureTogglesPayload {
//    @XmlElement(name="toggle")
    public List<FeatureTogglePayload> toggles = new ArrayList<>();
}
