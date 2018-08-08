package com.javafortesters.pulp.html.gui.entitycrud.updatePages;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;
import com.javafortesters.pulp.html.templates.HtmlTemplates;
import com.javafortesters.pulp.html.gui.snippets.AppPageBuilder;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.VersionedResourceReader;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;

import java.util.Collection;


public class AmendBookPage {
    private final PulpBook book;
    private final PulpData books;
    private final AppVersion appversion;

    public AmendBookPage(final PulpData theBooks, final PulpBook abook, final AppVersion appversion) {
        this.books = theBooks;
        this.book = abook;
        this.appversion = appversion;
    }

    private String output="";

    public void setOutput(final String output) {
        this.output=output;
    }

    public String asHTMLString() {

        AppPageBuilder page = new AppPageBuilder("Amend Book", appversion);

        final VersionedResourceReader versionedReader = new VersionedResourceReader(appversion);
        String pageToRender = versionedReader.asString("/page-template/entity-crud/update/edit-book-book-content.html");
//        String pageToRender = new ResourceReader().asString("/web/apps/pulp/" + appversion.getAppVersion() + "/page-template/entity-crud/update/edit-book-book-content.html");

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

            options.append(option(author.getId(), author.getName(), "AUTHOR", selected));
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
            options.append(option(publisher.getId(), publisher.getName(), "PUBLISHER", selected));
        }
        template.replaceSection("<!-- PUBLISHER-ID-OPTIONS -->", options.toString());



        //<!-- SERIES-ID-OPTIONS -->
        options= new StringBuilder();
        for(PulpSeries series : books.series().getAllOrderedByName()){
            selected = false;
            if(book.getSeriesIndex().contentEquals(series.getId())){
                selected = true;
            }
            options.append(option(series.getId(), series.getName(), "SERIES",  selected));
        }
        template.replaceSection("<!-- SERIES-ID-OPTIONS -->", options.toString());


        //<!-- HOUSE_AUTHOR-ID-OPTIONS -->
        for(PulpAuthor author : books.authors().getAllOrderedByName()){
            selected = false;
            if(author.getId() == book.getHouseAuthorIndex()) {
                selected = true;
            }
            options.append(option(author.getId(), author.getName(), "HOUSEAUTHOR", selected));
        }
        template.replaceSection("<!-- HOUSE-AUTHOR-ID-OPTIONS -->", options.toString());

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

    private String option(final String id, final String name, String type, boolean selected) {

        final HtmlTemplates templates = new HtmlTemplates(appversion);
        MyTemplate template = new HtmlTemplates(appversion).getSelectOption();
        if(selected){
            template = new HtmlTemplates(appversion).getSelectOptionSelected();
        }

        template.replace("!!VALUE!!", id);
        template.replace("!!TEXT!!", name);

        //added in v002
        template.replace("!!ID!!", type + "id"+id);


        return template.toString();
    }

}
