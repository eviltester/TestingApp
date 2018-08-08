package com.javafortesters.pulp.html.gui;

import com.javafortesters.pulp.PulpApp;
import com.javafortesters.pulp.reader.forseries.SavageReader;
import com.javafortesters.pulp.reporting.ReportConfig;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;
import org.junit.Assert;
import org.junit.Test;

public class AlertSearchPageTest {

    @Test
    public void haveSearchPage(){
        PulpApp app = new PulpApp();
        app.readData( new SavageReader("/data/pulp/doc_savage_test.csv"));

        AlertSearchPage page = new AlertSearchPage(new AppVersion(1));

        //System.out.println(page.asHTMLString());

        Assert.assertNotEquals("",page.asHTMLString(ReportConfig.justStrings(new AppVersion(1))));
    }

    @Test
    public void haveSearchPageWhichCanShowSearches(){
        PulpApp app = new PulpApp();
        app.readData( new SavageReader("/data/pulp/doc_savage_test.csv"));

        AlertSearchPage page = new AlertSearchPage(new AppVersion(1)).setSearchTerms("title", "contains", "The");
        page.setConfirmSearch(true);
        page.setDataFrom(app.books());

        String pageToRender = page.asHTMLString(ReportConfig.justStrings(new AppVersion(1)));
        System.out.println(pageToRender);

        Assert.assertNotEquals("",pageToRender);

    }
}
