package uk.co.compendiumdev.restlisticator.http;

import java.util.Base64;

public class BasicAuthHeader {
    private String username;
    private String password;

    public BasicAuthHeader(String headerDetails) {
        configureFrom(headerDetails);
    }

    private void configureFrom(String headerDetails) {

        username = null;
        password = null;

        if(headerDetails==null)
            return;
        
        String parseHeaderDetails = headerDetails.trim();

        try{
            if(headerDetails != null && parseHeaderDetails.startsWith("Basic")) {
                // remove Basic
                String b64Credentials = parseHeaderDetails.substring("Basic".length()).trim();

                String credentials = new String(Base64.getDecoder().decode(b64Credentials));

                // no colon, just return
                if(!credentials.contains(":"))
                    return;

                String[] credentialsParsed = credentials.split(":");
                if(credentialsParsed.length>0) {
                    username = credentialsParsed[0];
                    if(username.length()==0)
                        username = null;
                }
                if(credentialsParsed.length>1)
                    password = credentialsParsed[1];
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error trying to configure BasicAuthHeader from " + headerDetails);
        }
    }


    public static String getEncoded(String username, String password){
            String basicAuth = username + ":" + password;
            basicAuth = Base64.getEncoder().encodeToString(basicAuth.getBytes());
            return "Basic " + basicAuth;
    }

    public boolean isBasicAuthHeader() {
        return username!=null && password!=null;
    }

    public String getUsername() {
        return username!=null ? username : "";
    }

    public String getPassword() {
        return password !=null ? password : "";
    }
}
