package com.javafortesters.pulp.reader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ResourceReader {


    // TODO: this looks like it should really close stuff
    public String asString(String resourceName){

        try {
            InputStream in = this.getClass().getResourceAsStream(osIndependentResourcePath(resourceName));

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

    private String osIndependentResourcePath(String path){
        return path.replaceAll("\\\\","/");
    }

    public boolean doesResourceExist(final String path) {
        try {

            InputStream in = this.getClass().getResourceAsStream(osIndependentResourcePath(path));
            if(in==null){
                return false;
            }
            in.close();
            return true;

        }catch(FileNotFoundException e){
            return false;

        }catch(IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }

    }
}
