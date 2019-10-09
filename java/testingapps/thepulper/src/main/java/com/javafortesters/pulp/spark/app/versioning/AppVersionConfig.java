package com.javafortesters.pulp.spark.app.versioning;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * A specific config where AppVersionSettings have values e.g. true / false
 */
public class AppVersionConfig {

    private Map<String, AppVersionConfigValue> configSettings = new HashMap();
    int displayedVersionNumber=1;

    public boolean getSettingAsBoolean(final AppVersionSettings appVersionSetting) {

        return configSettings.get(appVersionSetting.getKey()).getBoolean();
    }

    public static AppVersionConfig defaultConfig(){

        final AppVersionConfig config = new AppVersionConfig();

        for(AppVersionSettings setting : EnumSet.allOf(AppVersionSettings.class)){

            config.setSetting(setting, setting.getDefault());
        }

        config.setDisplayedVersionNumber(1);
        return config;
    }

    private void setSetting(final AppVersionSettings setting, final Boolean aBoolean) {
        configSettings.put(setting.getKey(), new AppVersionConfigValue(setting, aBoolean));
    }

    public static AppVersionConfig getVersionConfigFor(int version){

        final AppVersionConfig retConfig = defaultConfig();

        switch(version){
            case 1:
                // use all the defaults - careful now
                retConfig.setDisplayedVersionNumber(1);
                break;

            case 2:
                // specific version 2 config here
                retConfig.setSetting(AppVersionSettings.TITLE_LINKS_TO_BOOKS_LIST, false);
                retConfig.setSetting(AppVersionSettings.AMEND_LINKS_SHOWN_IN_LIST, false);
                retConfig.setSetting(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE, false);
                retConfig.setDisplayedVersionNumber(2);

                break;

            case 3:
                // specific version 3 config here
                retConfig.setSetting(AppVersionSettings.TITLE_LINKS_TO_BOOKS_LIST, false);
                retConfig.setSetting(AppVersionSettings.AMEND_LINKS_SHOWN_IN_LIST, false);
                retConfig.setSetting(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE, true);
                retConfig.setDisplayedVersionNumber(3);

                break;

            case 4:
                // specific version 4 config here
                retConfig.setSetting(AppVersionSettings.TITLE_LINKS_TO_BOOKS_LIST, false);
                retConfig.setSetting(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE, true);
                retConfig.setSetting(AppVersionSettings.AMEND_LINKS_SHOWN_IN_LIST, true);
                retConfig.setSetting(AppVersionSettings.DELETE_LINKS_SHOWN_IN_LIST, true);
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
                retConfig.setDisplayedVersionNumber(6);
                break;
            case 7:
                // specific version 7 config here
                // this is going to use the API requests via JavaScript
                retConfig.setSetting(AppVersionSettings.TITLE_LINKS_TO_BOOKS_LIST, false);
                retConfig.setSetting(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE, true);
                retConfig.setSetting(AppVersionSettings.AMEND_LINKS_SHOWN_IN_LIST, true);
                retConfig.setSetting(AppVersionSettings.DELETE_LINKS_SHOWN_IN_LIST, true);
                retConfig.setSetting(AppVersionSettings.LISTS_SHOWING_CORRECT_NUMBER_OF_THINGS, true);
                retConfig.setDisplayedVersionNumber(7);
                break;
            default:
                // use all the defaults - careful now
                // this is because we didn't configure the versino
                retConfig.setDisplayedVersionNumber(-1);

        }

        return retConfig;
    }

    private void setDisplayedVersionNumber(final int version) {
        displayedVersionNumber = version;
    }
}


