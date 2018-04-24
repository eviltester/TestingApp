package uk.co.compendiumdev.restlisticator.sparkrestserver.utils;

import org.junit.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SimpleMessageProcessor {
    public String getXmlElement(String element, String body) {

        Pattern elementGetter = Pattern.compile(String.format(".*<%1$s>(.*)</%1$s>.*", element));
        Matcher m = elementGetter.matcher(body);
        Assert.assertTrue(m.find());

        return m.group(1);
    }
}
