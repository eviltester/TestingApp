package uk.co.compendiumdev.restlisticator.sparkrestserver.utils;

import org.junit.Assert;
import org.junit.Test;

public class SimpleMessageProcessorTest {

    @Test
    public void canGetElement(){

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><lists><list><guid>d81fe8d0-a6ab-43f0-9d55-c1abbd4f3793</guid><title>title</title><description>description</description><createdDate>2017-07-18-11-01-37</createdDate><amendedDate>2017-07-18-11-01-37</amendedDate></list></lists>";

        Assert.assertEquals("d81fe8d0-a6ab-43f0-9d55-c1abbd4f3793", new SimpleMessageProcessor().getXmlElement("guid", xml));
    }
}
