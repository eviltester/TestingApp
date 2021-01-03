package com.javafortesters.pulp.domain;

import com.javafortesters.pulp.domain.faq.Faqs;
import com.javafortesters.pulp.domain.faq.SearchEngine;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;
import org.junit.Assert;
import org.junit.Test;

public class FaqTest {

    @Test
    public void faqsExist(){
        Assert.assertTrue(
                Faqs.getFaqsFor("author","bob", new AppVersion(AppVersion.DEFAULT_VERSION)).
                contains("Who was pulp author \"bob\"?"));
    }

    @Test
    public void searchEngine(){
        SearchEngine google = new SearchEngine("https://www.google.com/search?q=");
        Assert.assertEquals("https://www.google.com/search?q=", google.getSearchTerm());

        SearchEngine bing = SearchEngine.bing();
//        https://www.bing.com/search?q=test+this


    }


}