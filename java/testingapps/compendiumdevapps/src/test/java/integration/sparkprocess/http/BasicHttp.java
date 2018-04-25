package integration.sparkprocess.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BasicHttp {

    private final String protocol;
    private final String host;
    private final int port;

    public BasicHttp(String protocol, String host, int port){
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    public boolean isPageAt(String path) {

        try {
            HttpURLConnection con = (HttpURLConnection)new URL(protocol,host, port, path).openConnection();
            int status = con.getResponseCode();
            String response = getResponseBody(con);
            System.out.println(status);
            System.out.println(response);
            if(status==200 && response.length()>0){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getPageContents(String path) {

        try {
            HttpURLConnection con = (HttpURLConnection)new URL(protocol,host, port, path).openConnection();
            String response = getResponseBody(con);
            System.out.println(response);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String getResponseBody(HttpURLConnection con) {
        BufferedReader in=null;

        // https://stackoverflow.com/questions/24707506/httpurlconnection-how-to-read-payload-of-400-response
        try {
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
        }catch(Exception e){
            // handle 400 exception messages
            InputStream stream = con.getErrorStream();
            if(stream!=null) {
                in = new BufferedReader(
                        new InputStreamReader(stream));
            }
        }

        String inputLine;
        StringBuffer responseBody = new StringBuffer();

        try{
            if(in!=null) {
                while ((inputLine = in.readLine()) != null) {
                    responseBody.append(inputLine);
                }
                in.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        return responseBody.toString();
    }
}
