package uk.co.compendiumdev.restlisticator.documentation;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class DocumentationGenerator {
    public String getInstructionsAsMarkdown() {

        String resource = "documentation.md";
        InputStream inputStream = DocumentationGenerator.class.getResourceAsStream(resource);

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

    public String getInstructionsAsHTML() {
        MutableDataSet options = new MutableDataSet();

        // uncomment to set optional extensions
        //options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

        // uncomment to convert soft-breaks to hard breaks
        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        String markdown = getInstructionsAsMarkdown();

        // You can re-use parser and renderer instances
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }

    public String getInstructionsAsHTMLPage(String title) {
        StringBuilder page = new StringBuilder();
        page.append("<html><head><title>");
        page.append(title);
        page.append("</title></head><body>");
        page.append(getInstructionsAsHTML());
        page.append("</body>");
        return page.toString();
    }
}
