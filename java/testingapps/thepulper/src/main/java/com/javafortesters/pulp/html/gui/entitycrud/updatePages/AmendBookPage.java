package com.javafortesters.pulp.html.gui.entitycrud.updatePages;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;
import com.javafortesters.pulp.html.gui.snippets.PageSnippets;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.ResourceReader;

import java.util.Collection;


public class AmendBookPage {
    private final PulpBook book;
    private final PulpData books;

    public AmendBookPage(final PulpData theBooks, final PulpBook abook) {
        this.books = theBooks;
        this.book = abook;
    }

    private String output="";

    public void setOutput(final String output) {
        this.output=output;
    }

    public String asHTMLString() {

        StringBuilder pageOutput = new StringBuilder();

        pageOutput.append(new PageSnippets().getPageHead("Book Search"));
        pageOutput.append(new PageSnippets().getDropDownMenu());

        String pageToRender = new ResourceReader().asString("/web/apps/pulp/page-template/entity-crud/update/edit-book-book-content.html");

        MyTemplate template = new MyTemplate(pageToRender);


        template.replace("!!BOOKID!!", book.getId());
        template.replace("!!BOOKTITLE!!", book.getTitle());
        template.replace("!!PUBLICATIONYEAR!!", String.valueOf(book.getPublicationYear()));
        template.replace("!!SERIESIDENTIFIER!!", String.valueOf(book.getSeriesId()));


        StringBuilder options = new StringBuilder();

        //<!-- AUTHOR-ID-OPTIONS -->
        Boolean selected = false;
        final Collection<String> bookAuthorIds = book.getAllAuthorIndexes();

        for(PulpAuthor author : books.authors().getAllOrderedByName()){
            selected = false;
            if(bookAuthorIds.contains(author.getId())) {
                selected = true;
            }

            options.append(option(author.getId(), author.getName(), selected));
        }
        // Ignore for the moment - make a multi select
        template.replaceSection("<!-- AUTHOR-ID-OPTIONS -->", options.toString());


        //<!-- PUBLISHER-ID-OPTIONS -->
        options= new StringBuilder();
        for(PulpPublisher publisher : books.publishers().getAllOrderedByName()){
            selected = false;
            if(book.getPublisherIndex().contentEquals(publisher.getId())){
                selected = true;
            }
            options.append(option(publisher.getId(), publisher.getName(), selected));
        }
        template.replaceSection("<!-- PUBLISHER-ID-OPTIONS -->", options.toString());



        //<!-- SERIES-ID-OPTIONS -->
        options= new StringBuilder();
        for(PulpSeries series : books.series().getAllOrderedByName()){
            selected = false;
            if(book.getSeriesIndex().contentEquals(series.getId())){
                selected = true;
            }
            options.append(option(series.getId(), series.getName(), selected));
        }
        template.replaceSection("<!-- SERIES-ID-OPTIONS -->", options.toString());


        //<!-- HOUSE_AUTHOR-ID-OPTIONS -->
        for(PulpAuthor author : books.authors().getAllOrderedByName()){
            selected = false;
            if(author.getId() == book.getHouseAuthorIndex()) {
                selected = true;
            }
            options.append(option(author.getId(), author.getName(), selected));
        }
        template.replaceSection("<!-- HOUSE-AUTHOR-ID-OPTIONS -->", options.toString());

        template.replace("<!-- OUTPUT GOES HERE -->", output);
        //template.replace("<!-- FOOTER GOES HERE -->", new ResourceReader().asString("/web/apps/pulp/page-template/reports-list-widget.html"));

        pageOutput.append(template.toString());
        pageOutput.append(new PageSnippets().getPageFooter());
        return pageOutput.toString();

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

    private String option(final String id, final String name, boolean selected) {
        if(selected){
            return String.format("<option value='%s' selected='selected\'>%s</option>%n", id, name);
        }else{
            return option(id, name);
        }

    }

}
