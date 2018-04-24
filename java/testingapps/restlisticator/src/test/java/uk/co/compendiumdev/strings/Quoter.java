package uk.co.compendiumdev.strings;

/**
 * Created by Alan on 05/07/2017.
 */
public class Quoter {

    public static String dbq(String singleQuoted) {
        return singleQuoted.replaceAll("'","\"");
    }
}
