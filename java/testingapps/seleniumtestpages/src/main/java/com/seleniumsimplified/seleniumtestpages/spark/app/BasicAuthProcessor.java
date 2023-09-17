package com.seleniumsimplified.seleniumtestpages.spark.app;

import com.seleniumsimplified.seleniumtestpages.ResourceReader;
import spark.Request;
import spark.Response;

import java.util.Base64;
import java.util.Locale;

public class BasicAuthProcessor {

    private final Request request;
    private final Response response;
    private final String validUsername;
    private final String validPassword;
    private String reason;

    public BasicAuthProcessor(final Request req,
                              final Response res,
                              final String validUsername,
                              final String validPassword) {
        this.request=req;
        this.response = res;
        this.validUsername = validUsername;
        this.validPassword = validPassword;

    }

    public String get(){

        if(!isAuthenticated()) {
            response.header("WWW-Authenticate", "Basic");
            response.status(401);
        }

        if(request.headers().contains("Accept")) {
            String accept = request.headers("Accept").toLowerCase(Locale.ROOT);
            if (accept.contains("json")){
                response.header("Content-Type", "application/json");
                return jsonOutput(isAuthenticated(), this.reason);
            }
        }

        // by default return the html
        return prettyPrintedOutput(isAuthenticated(), this.reason);
    }

    private String jsonOutput(final boolean authenticated, final String reason) {
        String auth = "true";
        if(!authenticated){
            auth="false";
        }
        return "{\"authenticated\":" + auth + ", \"reason\":\"" + reason + "\"}";
    }

    private boolean isAuthenticated(){
        if (!request.headers().contains("Authorization")){
            this.reason = "There was no Authorization header present in the request.";
            return false;
        }

        try {
            String authHeader = request.headers("Authorization");

            if(!authHeader.contains("Basic")){
                this.reason = "The Authorization header was not Basic authentication";
                return false;
            }

            int endOfbasicPosition = authHeader.indexOf("Basic") + 5;

            String basicAuthDetails = authHeader.substring(endOfbasicPosition).trim();
            String usernamePassword = new String(Base64.getDecoder().decode(basicAuthDetails));
            String[] details = usernamePassword.split(":");

            if(details == null || details.length<2){
                this.reason = "Could not find a username and password in the encoded string.";
                return false;
            }

            String username = details[0];
            String password = details[1];

            if(this.validUsername.equals(username) && this.validPassword.equals(password)){
                this.reason = "Username and Password in the Basic Auth header were the expected values";
                return true;
            }else{
                this.reason =
                        "Username (" + username + ") and Password (" + password + ")" +
                        " in the Basic Auth header were not the expected values";
                return false;
            }

        }catch(Exception e){

            System.out.println(e.getMessage());
            return false;
        }

    }

    private String prettyPrintedOutput(final boolean isAuthenticated, final String aReason) {
        String htmlPage = new ResourceReader().asString("/web/styled/template.html");
        htmlPage = htmlPage.replace("<!-- TITLE -->", "Basic Authentication Results Page" );

               /*  Copy and paste this into the string for auto formatting
    <h1>Results of Basic Authentication</h1>

    <div class="explanation">
        <p>This page is the result of accessing the basic authentication protected page.
        </p>
        <p>You were <span id="status">!AUTHENTICATION_STATUS!</span></p>
    </div>

    <div class="centered">
        <h2>Authentication Status Reason</h2>
        <p id="reason">!AUTHENTICATION_REASON!</p>
    </div>
         */
        String bodyContent = "<h1>Results of Basic Authentication</h1>\n" +
                "\n" +
                "    <div class=\"explanation\">\n" +
                "        <p>This page is the result of accessing the basic authentication protected page.\n" +
                "        </p>\n" +
                "        <p>You were <span id=\"status\">!AUTHENTICATION_STATUS!</span></p>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"centered\">\n" +
                "        <h2>Authentication Status Reason</h2>\n" +
                "        <p id=\"reason\">!AUTHENTICATION_REASON!</p>\n" +
                "    </div>";

        String aboutNav = "<a href='/styled/auth/basic-auth-test.html'>Page</a>\n" +
                "<a href='/styled/page?app=basicauth&t=About'>About</a>";

        htmlPage = htmlPage.replace("<!-- APPNAVIGATION CONTENT -->", aboutNav);

        String authStatus = "Authenticated";
        if(!isAuthenticated){
            authStatus = "Not " + authStatus;
        }

        bodyContent = bodyContent.replace("!AUTHENTICATION_STATUS!", authStatus);
        bodyContent = bodyContent.replace("!AUTHENTICATION_REASON!", aReason);
        htmlPage = htmlPage.replace("<!-- BODY CONTENT -->", bodyContent);
        return htmlPage;
    }
}
