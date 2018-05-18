package com.seleniumsimplified.seleniumtestpages.spark.app;

import spark.Request;
import spark.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class UploadedFile {
    private final Request req;
    private final Response res;

    // this makes the app super insecure - never release this to a public server ever
    public UploadedFile(Request req, Response res) {
        this.req = req;
        this.res = res;
    }

    public  Object get(String path) {
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // naive avoid directory traversals
        String readPath = path.replaceAll("\\.\\./", "");

        String filename = readPath.replace("upload/","");

        res.header("Content-Disposition", "inline; filename=\"" + filename + "\"");
        res.type(getHeaderTypeFor(readPath));
        HttpServletResponse raw = res.raw();

        try {
            raw.getOutputStream().write(bytes);
            raw.getOutputStream().flush();
            raw.getOutputStream().close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    // crude file type header addition
    private String getHeaderTypeFor(String path) {
        int subType = path.lastIndexOf(".");
        String type = path.substring(subType+1);

        String imageTypes[] = {"gif", "png", "jpeg", "jpg", "bmp"};

        if(Arrays.asList(imageTypes).contains(type)){
            return "image/" + type;
        }

        String textTypes[] = {"txt", "md", "html", "css", "js", "java"};

        if(Arrays.asList(imageTypes).contains(type)){
            return "text/plain";
        }

        return "application/"+type;
    }
}
