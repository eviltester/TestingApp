package com.javafortesters.pulp.domain.faq;

public class SearchEngine {
    private final String query;

    public SearchEngine(String queryPrefill) {
        this.query = queryPrefill;
    }

            /*
            Many search engines will not allow embedding due to Content policies so may have to pick one
              e.g.s tick to bing to start with
            https://bing.com/search?q=
            http://www.dogpile.com/search/web?q=
            http://www.baidu.com/s?wd=
            https://www.wolframalpha.com/input/?i=
            http://www.info.com/serp?q=
            https://www.hotbot.com/web?q=
            http://www.gigablast.com/search?q=
            http://www.webcrawler.com/serp?q=
         */

    // The following search engines do not allow embedding
    // https://www.google.com/search?q=
    //https://duckduckgo.com/?q=
    // see /apps/iframe-search/iframe-search.html for a way to manually investigate which search engines work

    public static SearchEngine bing() {
        return new SearchEngine("https://bing.com/search?q=");
    }

    public static SearchEngine getDefault() {
        return SearchEngine.bing();
    }

    public String getSearchTerm() {
        return query;
    }
}
