package com.javafortesters.pulp.html.templates;

import com.javafortesters.pulp.spark.AppVersion;

/**
 * Used to encapsulate inline templates a much as possible
 */
public class HtmlTemplates {

    private final AppVersion appversion;

    public HtmlTemplates(final AppVersion appversion) {
        this.appversion = appversion;
    }

    public MyTemplate getSelectOption() {
        String templateString = "<option value='!!VALUE!!'>!!TEXT!!</option>";

        if(appversion.currentVersionIs(2)){
            templateString  = "<option name='!!ID!!' value='!!VALUE!!'>!!TEXT!!</option>\n";
        }

        return new MyTemplate(templateString);
    }


    public MyTemplate getSelectOptionSelected() {
        String templateString = "<option value='!!VALUE!!' selected>!!TEXT!!</option>";

        if(appversion.currentVersionIs(2)){
            templateString  = "<option name='!!ID!!' value='!!VALUE!!' selected='selected'>!!TEXT!!</option>\n";
        }

        return new MyTemplate(templateString);
    }

    public MyTemplate getSearchResultMessage() {
        String templateString ="<p>!!MESSAGE!!</p>";

        if(appversion.currentVersionIs(2)){
            templateString="<p id='searchresultmessage' class='errormessage'>!!MESSAGE!!</p>";
        }

        return new MyTemplate(templateString);
    }

    public MyTemplate getLi() {
        String templateString ="<li>!!TEXT!!</li>";

        if(appversion.currentVersionIs(2)){
            templateString="<li id='!!ID!!' class='!!CLASS!!'>!!TEXT!!</li>";
        }

        return new MyTemplate(templateString);
    }

    public MyTemplate getErrorMessage() {
        String templateString ="<h2>!!MESSAGE!!</h2>";

        if(appversion.currentVersionIs(2)){
            templateString="<h2 name='error'>!!MESSAGE!!</h2>";
        }

        return new MyTemplate(templateString);
    }

    public MyTemplate getNamedNewTabLink() {
        String templateString ="<a target='_blank' href='!!HREF!!'>!!LINKTEXT!!</a>";


        if(appversion.currentVersionIs(2)){
            templateString="<a name='!!NAME!!' target='_blank' rel='noreferrer' href='!!HREF!!'>!!LINKTEXT!!</a>";
        }

        return new MyTemplate(templateString);
    }

    public MyTemplate getTableTag() {
        String templateString = "<table>";

        if(appversion.currentVersionIs(2)){
            templateString = "<table id='!!ID!!' name='!!NAME!!'>";
        }

        return new MyTemplate(templateString);
    }

    public MyTemplate getSpanTag() {
        String templateString = "!!TEXT!!";  // don't actually output the span

        if(appversion.currentVersionIs(2)){
            templateString = "<span id='!!ID!!'>!!TEXT!!</span>";
        }

        return new MyTemplate(templateString);
    }

    public MyTemplate getUlTag() {
        String templateString = "<ul>";  // don't actually output the span

        if(appversion.currentVersionIs(2)){
            templateString = "<ul id='!!ID!!'>";
        }

        return new MyTemplate(templateString);
    }
}
