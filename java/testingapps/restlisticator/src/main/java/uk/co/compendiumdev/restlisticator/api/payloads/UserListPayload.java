package uk.co.compendiumdev.restlisticator.api.payloads;

//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

//@XmlRootElement(name = "users")
public class UserListPayload {
//    @XmlElement(name="user")
    public List<UserPayload> users = new ArrayList<>();
}
