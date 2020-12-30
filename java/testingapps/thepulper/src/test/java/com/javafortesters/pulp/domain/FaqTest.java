package com.javafortesters.pulp.domain;

import com.javafortesters.pulp.domain.faq.Faqs;
import com.javafortesters.pulp.domain.faq.SearchEngine;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;
import org.junit.Assert;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FaqTest {

    @Test
    public void faqsExist(){
        Assert.assertTrue(
                Faqs.getFaqsFor("author", "bob",
                        new AppVersion(AppVersion.DEFAULT_VERSION)).
                        contains("Who was pulp author \"bob\"?"));

        Assert.assertTrue(
                Faqs.getFaqsForEvenFaster("author", "bob",
                        new AppVersion(AppVersion.DEFAULT_VERSION)).
                        contains("Who was pulp author \"bob\"?"));
    }

    @Test
    public void benchMarkFAQs() throws Exception {

        Options opt = new OptionsBuilder()
                // Specify which benchmarks to run.
                // You can be more specific if you'd like to run only one benchmark per test.
                .include(this.getClass().getName() + ".*")
                // Set the following options as needed
                .mode (Mode.AverageTime)
                .timeUnit(TimeUnit.MICROSECONDS)
                .warmupTime(TimeValue.seconds(1))
                .warmupIterations(2)
                .measurementTime(TimeValue.seconds(1))
                .measurementIterations(2)
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .build();

        new Runner(opt).run();
    }


    AppVersion version = new AppVersion(AppVersion.DEFAULT_VERSION);

    @Benchmark
    public void getFaqsFor(){
        Faqs.getFaqsFor("author", "bob", version);
    }

    @Benchmark
    public void getFaqsForFast(){
        Faqs.getFaqsForFast("author", "bob", version);
    }

    @Benchmark
    public void getFaqsForFaster(){
        Faqs.getFaqsForFaster("author", "bob", version);
    }

    @Benchmark
    public void getFaqsForEvenFaster(){
        Faqs.getFaqsForEvenFaster("author", "bob", version);
    }



    @Test
    public void searchEngine(){
        SearchEngine google = new SearchEngine("https://www.google.com/search?q=");
        Assert.assertEquals("https://www.google.com/search?q=", google.getSearchTerm());

        SearchEngine bing = SearchEngine.bing();
//        https://www.bing.com/search?q=test+this


    }


}
