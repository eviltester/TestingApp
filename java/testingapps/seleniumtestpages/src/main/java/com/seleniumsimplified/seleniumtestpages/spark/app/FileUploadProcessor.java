package com.seleniumsimplified.seleniumtestpages.spark.app;

import com.seleniumsimplified.seleniumtestpages.ResourceReader;
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
    private boolean prettyHtml;


    public FileUploadProcessor(Request req, Response res, Boolean allowupload) {
        this.req = req;
        this.res = res;
        this.allowupload = allowupload; // need to configure not uploading when deployed to server
        this.prettyHtml=false;
    }

    public FileUploadProcessor prettyOutput(){
        this.prettyHtml=true;
        return this;
    }

    public String post() {


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


        if(!prettyHtml) {
            StringBuilder html = new StringBuilder();

            if (isImage) {
                html.append("<h2>You uploaded this image:</h2><img src='/upload/" + reportedName + "'>\n");
            } else {
                html.append("<h2>You uploaded this file:</h2><a href='/upload/" + reportedName + "'>" + reportedName + "</a>\n");
            }

            return html.toString();
        }

        return prettyPrintedOutput(reportedName, isImage);


    }

    private String prettyPrintedOutput(final String reportedName, final boolean isImage) {
        String htmlPage = new ResourceReader().asString("/web/styled/template.html");
        htmlPage = htmlPage.replace("<!-- TITLE -->", "Uploaded Results Page" );

               /*  Copy and paste this into the string for auto formatting
    <h1>Uploaded File</h1>

    <div class="explanation">
        <p>You uploaded a file. This is the result.
        </p>
    </div>

    <div class="centered">
        <h2>You uploaded this !FILETYPE!:</h2>
        <div>
        !DOWNLOADLINK!
        </div>
        <div class="form-label">
            <button class="styled-click-button" id="goback" onclick="window.history.back()">Upload Another</button>
        </div>
    </div>
         */

        String fileType="file";
        String fileLink="";

        if (isImage) {
            fileType = "image";
            fileLink = String.format("<img style='max-width: 50%%' src='/upload/%s'>", reportedName);
        } else {
            fileLink = String.format("<a href='/upload/%s'>%s</a>", reportedName, reportedName);
        }

        String bodyContent = "<h1>Uploaded File</h1>\n" +
                "\n" +
                "    <div class=\"explanation\">\n" +
                "        <p>You uploaded a file. This is the result.\n" +
                "        </p>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"centered\">\n" +
                "        <h2>You uploaded this !FILETYPE!:</h2>\n" +
                "        <div>\n" +
                "        !DOWNLOADLINK!\n" +
                "        </div>\n" +
                "        <div class=\"form-label\">\n" +
                "            <button class=\"styled-click-button\" id=\"goback\" onclick=\"window.history.back()\">Upload Another</button>\n" +
                "        </div>\n" +
                "    </div>";

        bodyContent = bodyContent.replace("!FILETYPE!", fileType);
        bodyContent = bodyContent.replace("!DOWNLOADLINK!", fileLink);
        htmlPage = htmlPage.replace("<!-- BODY CONTENT -->", bodyContent);
        return htmlPage;
    }
}
