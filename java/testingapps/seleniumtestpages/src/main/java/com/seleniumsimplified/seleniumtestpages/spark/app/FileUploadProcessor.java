package com.seleniumsimplified.seleniumtestpages.spark.app;

import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUploadProcessor {

    private final Request req;
    private final Response res;
    private final Boolean allowupload;
    private StringBuilder html;

    public FileUploadProcessor(Request req, Response res, Boolean allowupload) {
        this.req = req;
        this.res = res;
        this.allowupload = allowupload; // need to configure not uploading when deployed to server
    }

    public String post() {
        html = new StringBuilder();

        File uploadDir;
        Path tempFile=null;

        if(allowupload) {
            uploadDir = new File("upload");
            // create the upload directory if it does not exist
            uploadDir.mkdir();

            try {
                tempFile = Files.createTempFile(uploadDir.toPath(), "", "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));


        File uploadedFile=null;
        boolean isImage = false;

        String reportedName = "NoFileUploadsAllowed.txt";

        if(allowupload) {
            try (InputStream input = req.raw().getPart("filename").getInputStream()) { // getPart needs to use same "name" as input field in form

                Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);

                uploadedFile = new File(tempFile.toFile().getPath() + "_" + req.raw().getPart("filename").getSubmittedFileName());
                tempFile.toFile().renameTo(uploadedFile);

                if (req.raw().getPart("filename").getContentType().contains("image")) {
                    isImage = true;
                }

                reportedName = uploadedFile.getName();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }



        if(isImage) {
            html.append("<h2>You uploaded this image:</h2><img src='/upload/" + reportedName + "'>\n");
        }else{
            html.append("<h2>You uploaded this file:</h2><a href='/upload/" + reportedName + "'/>" + reportedName + "</a>\n");
        }

        return html.toString();
    }
}
