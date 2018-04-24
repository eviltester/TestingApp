package uk.co.compendiumdev.restlisticator.documentation;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Alan on 18/08/2017.
 */
public class DocumentationTest {

    @Test
    public void canConvertMarkdownToHTML() throws IOException {

        DocumentationGenerator docs = new DocumentationGenerator();
        String expectedStart = "<html><head><title>REST Listicator Instructions</title></head><body>";
        System.out.println(docs.getInstructionsAsHTMLPage("REST Listicator Instructions"));
        Assert.assertEquals(expectedStart, docs.getInstructionsAsHTMLPage("REST Listicator Instructions").substring(0,expectedStart.length()));

        File documentationFile = new File(System.getProperty("user.dir") + File.separator + "docs" + File.separator + "documentation.html");
        documentationFile.createNewFile();
        PrintWriter out = new PrintWriter(documentationFile);
        out.print(docs.getInstructionsAsHTMLPage("REST Listicator Instructions"));
        out.close();

        // write to resources for production use
        String resourceFile = "/src/main/resources/uk/co/compendiumdev/restlisticator/documentation/documentation.html";
        resourceFile = resourceFile.replace("/", File.separator);
        documentationFile = new File(System.getProperty("user.dir") + resourceFile);
        documentationFile.createNewFile();
        out = new PrintWriter(documentationFile);
        out.print(docs.getInstructionsAsHTMLPage("REST Listicator Instructions"));
        out.close();
    }

}
