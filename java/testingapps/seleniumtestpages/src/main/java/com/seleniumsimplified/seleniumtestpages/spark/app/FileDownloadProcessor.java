package com.seleniumsimplified.seleniumtestpages.spark.app;

import spark.Request;
import spark.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FileDownloadProcessor {
    private final Request request;
    private final Response response;

    public FileDownloadProcessor(final Request req, final Response res) {
        this.request=req;
        this.response = res;
    }

    public  Object get(String filename) {

        if(!filename.equalsIgnoreCase("textfile.txt")){
            this.response.status(404);
            return null;
        }

        String contents = "This is a generated text file.\n" +
                "\n" +
                "Downloaded from https://testapps.heroku.com\n" +
                "\n" +
                "Remember to visit https://EvilTester.com for all your testing edufication.";

        this.response.header("Content-Disposition", "inline; filename=\"" + filename + "\"");
        this.response.type("text/plain");
        HttpServletResponse raw = this.response.raw();

        try {
            raw.getOutputStream().write(contents.getBytes());
            raw.getOutputStream().flush();
            raw.getOutputStream().close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.response;
    }


}
