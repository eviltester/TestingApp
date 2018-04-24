package uk.co.compendiumdev.restlisticator.api.payloads;


//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

//@XmlRootElement(name = "lists")
public class ListOfListicatorListsPayload {
//    @XmlElement(name="list")
    public List<ListicatorListPayload> lists = new ArrayList<>();
}
