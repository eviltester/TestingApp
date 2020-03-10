package com.javafortesters.pulp.spark.app.versioning;

import java.util.*;

/**
 * A specific config where AppVersionSettings have values e.g. true / false
 */
public class AppVersionConfig {

    private Map<String, AppVersionConfigValue> configSettings = new HashMap();
    int displayedVersionNumber=1;
    private Map<String, String> configVariables = new HashMap();



    public boolean getSettingAsBoolean(final AppVersionSettings appVersionSetting) {

        return configSettings.get(appVersionSetting.getKey()).getBoolean();
    }

    public static AppVersionConfig defaultConfig(){

        final AppVersionConfig config = new AppVersionConfig();

        for(AppVersionSettings setting : EnumSet.allOf(AppVersionSettings.class)){

            config.setSetting(setting, setting.getDefault());
        }

        config.setVariable("COPYRIGHTDATE", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        config.setDisplayedVersionNumber(1);
        return config;
    }

    private void setSetting(final AppVersionSettings setting, final Boolean aBoolean) {
        configSettings.put(setting.getKey(), new AppVersionConfigValue(setting, aBoolean));
    }

    private void setVariable(final String variableName, final String value) {
        configVariables.put(variableName, value);
    }

    public static AppVersionConfig getVersionConfigFor(int version){

        final AppVersionConfig retConfig = defaultConfig();


        switch(version){
            case 1:
                // use all the defaults - careful now
                retConfig.setDisplayedVersionNumber(1);
                retConfig.setVariable("COPYRIGHTDATE","2018");
                break;

            case 2:
                // specific version 2 config here
                retConfig.setSetting(AppVersionSettings.TITLE_LINKS_TO_BOOKS_LIST, false);
                retConfig.setSetting(AppVersionSettings.AMEND_LINKS_SHOWN_IN_LIST, false);
                retConfig.setSetting(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE, false);
                retConfig.setVariable("COPYRIGHTDATE","2018");
                retConfig.setDisplayedVersionNumber(2);

                break;

            case 3:
                // specific version 3 config here
                retConfig.setSetting(AppVersionSettings.TITLE_LINKS_TO_BOOKS_LIST, false);
                retConfig.setSetting(AppVersionSettings.AMEND_LINKS_SHOWN_IN_LIST, false);
                retConfig.setSetting(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE, true);
                retConfig.setVariable("COPYRIGHTDATE","2018");
                retConfig.setDisplayedVersionNumber(3);

                break;

            case 4:
                // specific version 4 config here
                retConfig.setSetting(AppVersionSettings.TITLE_LINKS_TO_BOOKS_LIST, false);
                retConfig.setSetting(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE, true);
                retConfig.setSetting(AppVersionSettings.AMEND_LINKS_SHOWN_IN_LIST, true);
                retConfig.setSetting(AppVersionSettings.DELETE_LINKS_SHOWN_IN_LIST, true);
                retConfig.setVariable("COPYRIGHTDATE","2018");
                retConfig.setDisplayedVersionNumber(4);
                break;
            case 5:
                // specific version 5 config here
                // basically the bug fixed 'complete' app using form submissions
                retConfig.setSetting(AppVersionSettings.TITLE_LINKS_TO_BOOKS_LIST, false);
                retConfig.setSetting(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE, true);
                retConfig.setSetting(AppVersionSettings.AMEND_LINKS_SHOWN_IN_LIST, true);
                retConfig.setSetting(AppVersionSettings.DELETE_LINKS_SHOWN_IN_LIST, true);
                retConfig.setSetting(AppVersionSettings.LISTS_SHOWING_CORRECT_NUMBER_OF_THINGS, true);
                retConfig.setVariable("COPYRIGHTDATE","2019");
                retConfig.setDisplayedVersionNumber(5);
                break;
            case 6:
                // specific version 6 config here
                // this is going to use the fetch approach to issue form requests via JavaScript
                retConfig.setSetting(AppVersionSettings.TITLE_LINKS_TO_BOOKS_LIST, false);
                retConfig.setSetting(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE, true);
                retConfig.setSetting(AppVersionSettings.AMEND_LINKS_SHOWN_IN_LIST, true);
                retConfig.setSetting(AppVersionSettings.DELETE_LINKS_SHOWN_IN_LIST, true);
                retConfig.setSetting(AppVersionSettings.LISTS_SHOWING_CORRECT_NUMBER_OF_THINGS, true);
                retConfig.setVariable("COPYRIGHTDATE","2019");
                retConfig.setDisplayedVersionNumber(6);
                break;
            case 7:
                // specific version 7 config here
                // this is going to use some API requests via JavaScript
                retConfig.setSetting(AppVersionSettings.TITLE_LINKS_TO_BOOKS_LIST, false);
                retConfig.setSetting(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE, true);
                retConfig.setSetting(AppVersionSettings.AMEND_LINKS_SHOWN_IN_LIST, true);
                retConfig.setSetting(AppVersionSettings.DELETE_LINKS_SHOWN_IN_LIST, true);
                retConfig.setSetting(AppVersionSettings.LISTS_SHOWING_CORRECT_NUMBER_OF_THINGS, true);
                retConfig.setVariable("COPYRIGHTDATE","2019");
                retConfig.setDisplayedVersionNumber(7);
                break;
            case 8:
                // specific version 8 config here
                // this is going to use API requests via JavaScript for search
                retConfig.setSetting(AppVersionSettings.TITLE_LINKS_TO_BOOKS_LIST, false);
                retConfig.setSetting(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE, true);
                retConfig.setSetting(AppVersionSettings.AMEND_LINKS_SHOWN_IN_LIST, true);
                retConfig.setSetting(AppVersionSettings.DELETE_LINKS_SHOWN_IN_LIST, true);
                retConfig.setSetting(AppVersionSettings.LISTS_SHOWING_CORRECT_NUMBER_OF_THINGS, true);
                retConfig.setVariable("COPYRIGHTDATE","2020");
                retConfig.setDisplayedVersionNumber(8);
                break;
            case 9:
                // specific version 9 config here
                // this is going to use all API requests via JavaScript
                retConfig.setSetting(AppVersionSettings.TITLE_LINKS_TO_BOOKS_LIST, false);
                retConfig.setSetting(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE, true);
                retConfig.setSetting(AppVersionSettings.AMEND_LINKS_SHOWN_IN_LIST, true);
                retConfig.setSetting(AppVersionSettings.DELETE_LINKS_SHOWN_IN_LIST, true);
                retConfig.setSetting(AppVersionSettings.LISTS_SHOWING_CORRECT_NUMBER_OF_THINGS, true);
                retConfig.setVariable("COPYRIGHTDATE","2020");
                retConfig.setDisplayedVersionNumber(9);
                break;
            case 10:
                // specific version 10 config here
                // fixed version of the API code
                retConfig.setSetting(AppVersionSettings.TITLE_LINKS_TO_BOOKS_LIST, false);
                retConfig.setSetting(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE, true);
                retConfig.setSetting(AppVersionSettings.AMEND_LINKS_SHOWN_IN_LIST, true);
                retConfig.setSetting(AppVersionSettings.DELETE_LINKS_SHOWN_IN_LIST, true);
                retConfig.setSetting(AppVersionSettings.LISTS_SHOWING_CORRECT_NUMBER_OF_THINGS, true);
                retConfig.setVariable("COPYRIGHTDATE","2020");
                retConfig.setDisplayedVersionNumber(10);
                break;
            default:
                // use all the defaults - careful now
                // this is because we didn't configure the version
                retConfig.setDisplayedVersionNumber(-1);
        }

        return retConfig;
    }

    public static void setKnownBugsVersionConfigFor(final int appVersion, final KnownBugs knownBugs) {
        knownBugs.setDefault();

        switch(appVersion) {
            case 4:
                // first time we are deleting authors so deleting a house author deletes book
                // regardless of any other authors
                knownBugs.setBugPresenceTo(KnownBugs.Bug.DELETE_BOOK_WHEN_DELETING_HOUSE_AUTHOR, true);
                break;

            case 9:
                // we added CRUD access via API and that brought back the bug
                // about deleting house author
                knownBugs.setBugPresenceTo(KnownBugs.Bug.DELETE_BOOK_WHEN_DELETING_HOUSE_AUTHOR, true);
                break;

            default:
                // leave knownBugs at default level
        }

        if(appVersion<=3){
            // series faqs has a template error in v3 and below
            knownBugs.setBugPresenceTo(KnownBugs.Bug.TEMPLATE_ERROR_IN_SERIES_FAQ, true);
        }

        if(appVersion<5){
            // did not fix page title bug until version 5
            knownBugs.setBugPresenceTo(KnownBugs.Bug.FAQ_PAGE_TITLE_ALWAYS_AUTHORS, true);
        }

    }

    private void setDisplayedVersionNumber(final int version) {
        displayedVersionNumber = version;
    }

    public String getVariableValue(final String name) {
        if(configVariables.containsKey(name)){
            return configVariables.get(name);
        }
        return "";
    }
}


