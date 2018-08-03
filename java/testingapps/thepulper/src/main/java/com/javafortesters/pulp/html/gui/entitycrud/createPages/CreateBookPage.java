package com.javafortesters.pulp.html.gui.entitycrud.createPages;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;
import com.javafortesters.pulp.html.gui.snippets.AppPageBuilder;
import com.javafortesters.pulp.html.gui.snippets.PageSnippets;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.ResourceReader;

public class CreateBookPage {

    private final PulpData books;

    public CreateBookPage(final PulpData theBooks){
        this.books = theBooks;
    }

    private String output="";

    public void setOutput(final String output) {
        this.output=output;
    }

    public String asHTMLString() {

        AppPageBuilder page = new AppPageBuilder("Create Book");


        String pageToRender = new ResourceReader().asString("/web/apps/pulp/v001/page-template/entity-crud/create/create-book-content.html");

        MyTemplate template = new MyTemplate(pageToRender);

        StringBuilder options = new StringBuilder();

        //<!-- AUTHOR-ID-OPTIONS -->
        for(PulpAuthor author : books.authors().getAllOrderedByName()){
            options.append(option(author.getId(), author.getName()));
        }
        template.replaceSection("<!-- AUTHOR-ID-OPTIONS -->", options.toString());


        //<!-- PUBLISHER-ID-OPTIONS -->
        options= new StringBuilder();
        for(PulpPublisher publisher : books.publishers().getAllOrderedByName()){
            options.append(option(publisher.getId(), publisher.getName()));
        }
        template.replaceSection("<!-- PUBLISHER-ID-OPTIONS -->", options.toString());



        //<!-- SERIES-ID-OPTIONS -->
        options= new StringBuilder();
        for(PulpSeries series : books.series().getAllOrderedByName()){
            options.append(option(series.getId(), series.getName()));
        }
        template.replaceSection("<!-- SERIES-ID-OPTIONS -->", options.toString());

        template.replace("<!-- OUTPUT GOES HERE -->", output);


        page.appendToBody(template.toString());
        return page.toString();


    }

    /**
     * <option value="id">name</option>
     * @param id
     * @param name
     * @return
     */
    private String option(final String id, final String name) {
        return String.format("<option value='%s'>%s</option>%n", id, name);
    }

}
