package com.seleniumsimplified.seleniumtestpages;

import java.io.InputStream;
import java.util.Scanner;

public class ResourceReader {


    public String asString(String resourceName){

        InputStream in = this.getClass().getResourceAsStream(resourceName);

        // http://web.archive.org/web/20140531042945/https://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        Scanner scanner = new Scanner(in).useDelimiter("\\A");

        if(scanner.hasNext()) {
            return scanner.next();
        }else{
            return "";
        }

    }
}
