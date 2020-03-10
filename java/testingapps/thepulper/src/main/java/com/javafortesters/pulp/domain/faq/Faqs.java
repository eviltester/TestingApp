package com.javafortesters.pulp.domain.faq;

import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;
import com.javafortesters.pulp.spark.app.versioning.KnownBugs;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Faqs {

    private static String [] authorFaqs={
            "Who was pulp author \"!!name!!\"?",
            "What did pulp author \"!!name!!\" write?",
            "Pseudonyms used by pulp author \"!!name!!\"",
            "What books did pulp author \"!!name!!\" write?",
            "Is \"!!name!!\" pulp author available to download as pdf?",
            "Where can I download books by \"!!name!!\"?",
            "Buy reprints of magazines from author \"!!name!!\"?",
    };
    private static String [] publisherFaqs={
            "Who were pulp publisher \"!!name!!\"?",
            "What did pulp publisher \"!!name!!\" publish?",
            "Most popular magazines from pulp publisher \"!!name!!\"?",
            "Who wrote for pulp publisher \"!!name!!\"?",
            "What characters did pulp publisher \"!!name!!\" publish?",
            "Download scanned magazines from publisher \"!!name!!\"?",
            "Buy reprints of magazines from publisher \"!!name!!\"?",
    };
    private static String [] seriesFaqs={
            "Are there any \"!!name!!\" movies?",
            "List of \"!!name!!\" pulp magazines?",
            "Who wrote pulp series \"!!name!!\"?",
            "Who created pulp series \"!!name!!\"?",
            "Comics for pulp series \"!!name!!\"?",
            "Download scanned \"!!name!!\" magazines?",
            "Pulp \"!!name!!\" covers",
            "Buy reprints of \"!!name!!\" magazines?",
    };
    private static String [] booksFaqs={
            "Is \"!!name!!\" pulp available to download?",
            "Scanned version of \"!!name!!\" pulp?",
            "Read \"!!name!!\" pulp online?",
            "Reprints of \"!!name!!\" pulp?",
    };
    private static String [] yearFaqs={
            "What pulps were published in year \"!!name!!\"?",
            "What pulps were first published in year \"!!name!!\"?",
            "What pulp heros were created in year \"!!name!!\"?",
            "What pulp authors were writing in year \"!!name!!\"?"
    };

    public static List<String> getFaqsFor(String faqsFor, String searchTerm, AppVersion version) {
        List<String> faqs = new ArrayList<>();

        // adding all into the list and using contains makes it much more hackable e.g. series+author
        if(faqsFor.toLowerCase().contains("author")){
            faqs.addAll(Arrays.asList(Faqs.authorFaqs));
        }
        if(faqsFor.toLowerCase().contains("publish")){
            faqs.addAll(Arrays.asList(Faqs.publisherFaqs));
        }
        if(faqsFor.toLowerCase().contains("series")){

            List<String>faqsList = new ArrayList<>(Arrays.asList(Faqs.seriesFaqs));

            if(version.bugs().bugIsPresent(KnownBugs.Bug.TEMPLATE_ERROR_IN_SERIES_FAQ)){
                faqsList.add("Is \"!!name!\"! pulp available to download as pdf?");
            }else{
                faqsList.add("Is \"!!name!!\" pulp available to download as pdf?");
            }

            faqs.addAll(faqsList);
        }
        if(faqsFor.toLowerCase().contains("book")){
            List<String>faqsList = new ArrayList<>(Arrays.asList(Faqs.booksFaqs));
            faqs.addAll(faqsList);
        }
        if(faqsFor.toLowerCase().contains("year")){
            faqs.addAll(Arrays.asList(Faqs.yearFaqs));
        }


        for(int i = 0; i < faqs.size(); i++){
            MyTemplate template = new MyTemplate(faqs.get(i));
            faqs.set(i, template.replace("!!name!!", searchTerm).toString());
        }

        return faqs;
    }
}
