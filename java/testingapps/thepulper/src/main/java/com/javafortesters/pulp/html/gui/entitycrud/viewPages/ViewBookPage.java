package com.javafortesters.pulp.html.gui.entitycrud.viewPages;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.domain.objects.PulpPublisher;
import com.javafortesters.pulp.domain.objects.PulpSeries;
import com.javafortesters.pulp.html.gui.snippets.AppPageBuilder;
import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.VersionedResourceReader;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;

import java.util.Collection;

public class ViewBookPage {

    private final PulpBook book;
    private final PulpData books;
    private final AppVersion appversion;

    public ViewBookPage(final PulpData theBooks, final PulpBook abook, final AppVersion appversion) {
        this.books = theBooks;
        this.book = abook;
        this.appversion = appversion;
    }

    private String output="";

    public void setOutput(final String output) {
        this.output=output;
    }

    public String asHTMLString() {

        AppPageBuilder page = new AppPageBuilder("View Book", appversion);

        final VersionedResourceReader versionedReader = new VersionedResourceReader(appversion);
        String pageToRender = versionedReader.asString( "/page-template/entity-crud/read/view-book-book-content.html");
//        String pageToRender = new ResourceReader().asString("/web/apps/pulp/" + appversion.getAppVersion() + "/page-template/entity-crud/read/view-book-book-content.html");

        MyTemplate template = new MyTemplate(pageToRender);


        template.replace("!!BOOKID!!", book.getId());
        template.replace("!!BOOKTITLE!!", book.getTitle());
        template.replace("!!PUBLICATIONYEAR!!", String.valueOf(book.getPublicationYear()));
        template.replace("!!SERIESIDENTIFIER!!", String.valueOf(book.getSeriesId()));


        StringBuilder options = new StringBuilder();

        //<!-- AUTHOR-ID-OPTIONS -->
        Boolean selected = false;
        final Collection<String> bookAuthorIds = book.getAllAuthorIndexes();

        StringBuilder authorsInLi = new StringBuilder();

        String authorTemplate = "<li name = \"author-named\">Author: <a href=\"/apps/pulp/gui/view/author?author=!!AUTHORID!!\">!!AUTHORNAME!!</a></li>";
        final MyTemplate authorLi = new MyTemplate(authorTemplate);

        for(PulpAuthor author : books.authors().getAllOrderedByName()){
            if(bookAuthorIds.contains(author.getId())) {

                authorLi.replace("!!AUTHORID!!", author.getId());
                authorLi.replace("!!AUTHORNAME!!", author.getName());

                authorsInLi.append(authorLi.toString());
                authorsInLi.append(String.format("%n"));

                authorLi.reset();
            }
        }
        template.replace("<-- LIST OF AUTHORS -->", authorsInLi.toString());


        //<!-- PUBLISHER-ID-OPTIONS -->
        for(PulpPublisher publisher : books.publishers().getAllOrderedByName()){
            if(book.getPublisherIndex().contentEquals(publisher.getId())){
                template.replace("!!PUBLISHERID!!",publisher.getId());
                template.replace("!!PUBLISHERNAME!!",publisher.getName());
                break;
            }
        }



        //<!-- SERIES-ID-OPTIONS -->
        for(PulpSeries series : books.series().getAllOrderedByName()){
            if(book.getSeriesIndex().contentEquals(series.getId())){
                template.replace("!!SERIESID!!",series.getId());
                template.replace("!!SERIESNAME!!",series.getName());
                break;
            }
        }

        //<!-- HOUSE_AUTHOR-ID-OPTIONS -->
        for(PulpAuthor author : books.authors().getAllOrderedByName()){
            if(author.getId() == book.getHouseAuthorIndex()) {
                template.replace("!!HOUSEAUTHORID!!",author.getId());
                template.replace("!!HOUSEAUTHORNAME!!",author.getName());
                break;
            }
        }

        template.replace("<!-- OUTPUT GOES HERE -->", output);

        page.appendToBody(template.toString());
        return page.toString();
    }
}
