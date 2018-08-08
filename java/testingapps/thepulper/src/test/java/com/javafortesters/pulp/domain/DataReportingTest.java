package com.javafortesters.pulp.domain;

import com.javafortesters.pulp.PulpApp;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.reader.PulpDataPopulator;
import com.javafortesters.pulp.reader.forseries.SavageReader;
import com.javafortesters.pulp.reporting.PulpReporter;
import com.javafortesters.pulp.reporting.ReportConfig;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

public class DataReportingTest {


    @Test
    public void canReportOnData(){

        PulpData books = new PulpData();
        PulpDataPopulator populator = new PulpDataPopulator(books);
        SavageReader reader = new SavageReader("/data/pulp/doc_savage_test.csv");
        populator.populateFrom(reader);


        PulpReporter reporter = new PulpReporter(books, new AppVersion(1));

        Collection<String> simpleReport = reporter.getBooksAsStrings();

        for(String reportLine : simpleReport){
            System.out.println(reportLine);
        }

        Assert.assertEquals(simpleReport.size(), books.books().count());

    }

    @Test
    public void haveBasicAppWrapperForBooks(){
        PulpApp app = new PulpApp();
        app.setAppVersion(1);
        app.readData(new SavageReader("/data/pulp/doc_savage_test.csv"));
        String report = app.reports().getBooksAsHtmlList();
        System.out.println(report);
        Assert.assertTrue(report.contains("<li>The Angry Canary | Lester Dent"));
    }


    @Test
    public void haveBasicAppWrapperForAuthors(){
        PulpApp app = new PulpApp();
        app.setAppVersion(1);
        app.readData(new SavageReader("/data/pulp/doc_savage_test.csv"));
        String report = app.reports().getAuthorsAsHtmlList();
        System.out.println(report);
        Assert.assertTrue(report.contains("<li>Lester Dent</li>"));
    }

    @Test
    public void simpleReportHasBasicHtmlStructure(){
        PulpApp app = new PulpApp();
        app.readData(new SavageReader("/data/pulp/doc_savage_test.csv"));
        app.reports().configure(ReportConfig.justStrings(new AppVersion(1)));
        String report = app.reports().getAuthorsAsHtmlList();
        System.out.println(report);

        Assert.assertTrue("Should start as basic html",report.contains("<html>"));
        Assert.assertTrue("Should start as basic html",report.contains("<head>"));
        Assert.assertTrue("Should have title", report.contains("<title>List of Authors</title>"));
        Assert.assertTrue("Should have well formed head", report.contains("</head>"));
        Assert.assertTrue("Should have body", report.contains("<body>"));
        Assert.assertTrue("HTML should end well", report.contains("</body>"));
        Assert.assertTrue("HTML should end well", report.contains("</html>"));
    }

    @Test
    public void haveBasicAppWrapperForPublishers(){
        PulpApp app = new PulpApp();
        app.setAppVersion(1);
        app.readData(new SavageReader("/data/pulp/doc_savage_test.csv"));
        String report = app.reports().getPublishersAsHtmlList();
        System.out.println(report);
        Assert.assertTrue(report.contains("<li>Street And Smith</li>"));
    }

    @Test
    public void haveBasicAppWrapperForYears(){
        PulpApp app = new PulpApp();
        app.setAppVersion(1);
        app.readData(new SavageReader("/data/pulp/doc_savage_test.csv"));
        String report = app.reports().getYearsAsHtmlList();
        System.out.println(report);
        Assert.assertTrue(report.contains("<li>1948</li>"));
        Assert.assertTrue(report.contains("<li>1949</li>"));
    }

    @Test
    public void haveBasicAppWrapperForSeries(){
        PulpApp app = new PulpApp();
        app.setAppVersion(1);
        app.readData(new SavageReader("/data/pulp/doc_savage_test.csv"));
        String report = app.reports().getSeriesNamesAsHtmlList();
        System.out.println(report);
        Assert.assertTrue(report.contains("<li>Doc Savage</li>"));
    }
}
