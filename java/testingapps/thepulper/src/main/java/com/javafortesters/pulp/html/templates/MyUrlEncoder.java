package com.javafortesters.pulp.html.templates;

import javax.print.DocFlavor;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MyUrlEncoder {
    public static String encode(String encodeThis) {
        try {
            return URLEncoder.encode(encodeThis,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Failed to encode " + encodeThis);
            e.printStackTrace();
            return encodeThis;
        }
    }
}
