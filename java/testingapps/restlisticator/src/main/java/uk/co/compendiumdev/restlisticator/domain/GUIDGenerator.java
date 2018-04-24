package uk.co.compendiumdev.restlisticator.domain;


import static java.util.UUID.randomUUID;

public class GUIDGenerator {
    public static String getNextGUID() {
        return randomUUID().toString();
    }
}
