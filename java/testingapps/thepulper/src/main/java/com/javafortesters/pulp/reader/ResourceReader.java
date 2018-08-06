package com.javafortesters.pulp.reader;

import java.io.InputStream;
import java.util.Scanner;

public class ResourceReader {


    public String asString(String resourceName){

        try {
            InputStream in = this.getClass().getResourceAsStream(resourceName);

            // http://web.archive.org/web/20140531042945/https://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
            Scanner scanner = new Scanner(in).useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return "";
            }
        }catch(Exception e){
            System.out.println("Error reading resource: " + resourceName);
            e.printStackTrace();
            return "";
        }
    }
}
