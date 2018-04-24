package com.seleniumsimplified.seleniumtestpages.php;

/**
 * Created by Alan on 15/06/2016.
 */
public class SearchUrl {
    public final String url;
    public final String displayUrl;
    public final String title;
    public final String description;

    public SearchUrl(String url, String displayUrl, String title, String description) {
        this.url = url;
        this.displayUrl = displayUrl;
        this.title = title;
        this.description = description;
    }
}
