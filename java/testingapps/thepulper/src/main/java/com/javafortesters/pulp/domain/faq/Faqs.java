package com.javafortesters.pulp.domain.faq;

import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;
import com.javafortesters.pulp.spark.app.versioning.KnownBugs;
import org.openjdk.jmh.annotations.Benchmark;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class Faqs {

    private static final String [] authorFaqs={
            "Who was pulp author \"!!name!!\"?",
            "What did pulp author \"!!name!!\" write?",
            "Pseudonyms used by pulp author \"!!name!!\"",
            "What books did pulp author \"!!name!!\" write?",
            "Is \"!!name!!\" pulp author available to download as pdf?",
            "Where can I download books by \"!!name!!\"?",
            "Buy reprints of magazines from author \"!!name!!\"?",
    };
    private static final String [] publisherFaqs={
            "Who were pulp publisher \"!!name!!\"?",
            "What did pulp publisher \"!!name!!\" publish?",
            "Most popular magazines from pulp publisher \"!!name!!\"?",
            "Who wrote for pulp publisher \"!!name!!\"?",
            "What characters did pulp publisher \"!!name!!\" publish?",
            "Download scanned magazines from publisher \"!!name!!\"?",
            "Buy reprints of magazines from publisher \"!!name!!\"?",
    };
    private static final String [] seriesFaqs={
            "Are there any \"!!name!!\" movies?",
            "List of \"!!name!!\" pulp magazines?",
            "Who wrote pulp series \"!!name!!\"?",
            "Who created pulp series \"!!name!!\"?",
            "Comics for pulp series \"!!name!!\"?",
            "Download scanned \"!!name!!\" magazines?",
            "Pulp \"!!name!!\" covers",
            "Buy reprints of \"!!name!!\" magazines?",
    };
    private static final String [] booksFaqs={
            "Is \"!!name!!\" pulp available to download?",
            "Scanned version of \"!!name!!\" pulp?",
            "Read \"!!name!!\" pulp online?",
            "Reprints of \"!!name!!\" pulp?",
    };
    private static final String [] yearFaqs={
            "What pulps were published in year \"!!name!!\"?",
            "What pulps were first published in year \"!!name!!\"?",
            "What pulp heros were created in year \"!!name!!\"?",
            "What pulp authors were writing in year \"!!name!!\"?"
    };

    private static List<String> getAuthorFaqs(){
        return Arrays.asList(Faqs.authorFaqs);
    }

    private static List<String> getPublisherFaqs(){
        return Arrays.asList(Faqs.publisherFaqs);
    }

    private static List<String> getBookFaqs(){
        return Arrays.asList(Faqs.booksFaqs);
    }

    private static List<String> getYearFaqs() {
        return Arrays.asList(Faqs.yearFaqs);
    }

    private static List<String> getSeriesFaqs(AppVersion version){
        List<String>faqsList = new ArrayList<>(Arrays.asList(Faqs.seriesFaqs));

        if(version.bugs().bugIsPresent(KnownBugs.Bug.TEMPLATE_ERROR_IN_SERIES_FAQ)){
            faqsList.add("Is \"!!name!\"! pulp available to download as pdf?");
        }else{
            faqsList.add("Is \"!!name!!\" pulp available to download as pdf?");
        }

        return faqsList;
    }

    private static List<String> getFAQTemplatesFor(String faqsFor, AppVersion version){
        List<String> faqs = new ArrayList<>();

        String faqsForLC = faqsFor.toLowerCase();

        // adding all into the list and using contains makes it much more hackable e.g. series+author
        if(faqsForLC.contains("author")){
            faqs.addAll(getAuthorFaqs());
        }
        if(faqsForLC.contains("publish")){
            faqs.addAll(getPublisherFaqs());
        }
        if(faqsForLC.contains("series")){
            faqs.addAll(getSeriesFaqs(version));
        }
        if(faqsForLC.contains("book")){
            faqs.addAll(getBookFaqs());
        }
        if(faqsForLC.contains("year")){
            faqs.addAll(getYearFaqs());
        }

        return faqs;
    }

    public static List<String> getFaqsFor(String faqsFor, String searchTerm, AppVersion version) {

        List<String> faqs = getFAQTemplatesFor(faqsFor, version);

        for(int i = 0; i < faqs.size(); i++){
            MyTemplate template = new MyTemplate(faqs.get(i));
            faqs.set(i, template.replace("!!name!!", searchTerm).toString());
        }

        return faqs;
    }
    
}
