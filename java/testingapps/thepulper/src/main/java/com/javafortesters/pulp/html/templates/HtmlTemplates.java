package com.javafortesters.pulp.html.templates;

import com.javafortesters.pulp.spark.app.versioning.AppVersion;
import com.javafortesters.pulp.spark.app.versioning.AppVersionSettings;

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


        if(appversion.are(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE)){
            templateString  = "<option name='!!ID!!' value='!!VALUE!!'>!!TEXT!!</option>\n";
        }

        return new MyTemplate(templateString);
    }


    public MyTemplate getSelectOptionSelected() {
        String templateString = "<option value='!!VALUE!!' selected>!!TEXT!!</option>";

        if(appversion.are(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE)){
            templateString  = "<option name='!!ID!!' value='!!VALUE!!' selected='selected'>!!TEXT!!</option>\n";
        }

        return new MyTemplate(templateString);
    }

    public MyTemplate getSearchResultMessage() {
        String templateString ="<p>!!MESSAGE!!</p>";

        if(appversion.are(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE)){
            templateString="<p id='searchresultmessage' class='errormessage'>!!MESSAGE!!</p>";
        }

        return new MyTemplate(templateString);
    }

    public MyTemplate getLi() {
        String templateString ="<li>!!TEXT!!</li>";

        if(appversion.are(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE)){
            templateString="<li id='!!ID!!' class='!!CLASS!!'>!!TEXT!!</li>";
        }

        return new MyTemplate(templateString);
    }

    public MyTemplate getErrorMessage() {
        String templateString ="<h2>!!MESSAGE!!</h2>";

        if(appversion.are(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE)){
            templateString="<h2 name='error'>!!MESSAGE!!</h2>";
        }

        return new MyTemplate(templateString);
    }

    public MyTemplate getNamedNewTabLink() {
        String templateString ="<a target='_blank' href='!!HREF!!'>!!LINKTEXT!!</a>";


        if(appversion.are(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE)){
            templateString="<a name='!!NAME!!' target='_blank' rel='noreferrer' href='!!HREF!!'>!!LINKTEXT!!</a>";
        }

        return new MyTemplate(templateString);
    }

    public MyTemplate getTableTag() {
        String templateString = "<table>";

        if(appversion.are(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE)){
            templateString = "<table id='!!ID!!' name='!!NAME!!'>";
        }

        return new MyTemplate(templateString);
    }

    public MyTemplate getSpanTag() {
        String templateString = "!!TEXT!!";  // don't actually output the span

        if(appversion.are(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE)){
            templateString = "<span id='!!ID!!'>!!TEXT!!</span>";
        }

        return new MyTemplate(templateString);
    }

    public MyTemplate getUlTag() {
        return getUlTag("");
    }

    public MyTemplate getUlTag(String styleClass) {
        String className="";
        String idincluded = "";

        if(styleClass!=null && styleClass.trim().length()>0){
            className = " class='" + styleClass + "'";
        }

        if(appversion.are(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE)){
            idincluded = " id='!!ID!!'";
        }

        String templateString = String.format("<ul%s%s>",className, idincluded);  // don't actually output the span

        return new MyTemplate(templateString);
    }
}
