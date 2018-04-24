package uk.co.compendiumdev.restlisticator.documentation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by Alan on 21/08/2017.
 */
public class DocumentationReader {
    public String getInstructionsAsHTMLPage() {

        String resource = "/uk/co/compendiumdev/restlisticator/documentation/documentation.html";
        InputStream inputStream = DocumentationReader.class.getResourceAsStream(resource);

        if ( inputStream == null )
            return "<strong>instructions not found</strong>";


        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                result.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
// StandardCharsets.UTF_8.name() > JDK 7
        String results = "<strong>encoding error not in utf-8</strong>";
        try {
            results = result.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        return results;
    }

}
