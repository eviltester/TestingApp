package com.javafortesters.pulp.spark.app.versioning;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * A specific config where AppVersionSettings have values e.g. true / false
 */
public class AppVersionConfig {

    Map<String, AppVersionConfigValue> configSettings = new HashMap();
    int displayedVersionNumber = 1;

    public boolean getSettingAsBoolean(final AppVersionSettings appVersionSetting) {

        return configSettings.get(appVersionSetting.getKey()).getBoolean();
    }

    public static AppVersionConfig defaultConfig(){

        final AppVersionConfig config = new AppVersionConfig();

        for(AppVersionSettings setting : EnumSet.allOf(AppVersionSettings.class)){

            config.setSetting(setting, setting.getDefault());
        //
        }

        int displayedVersionNumber = 1;

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
                retConfig.setSetting(AppVersionSettings.HTML_TAGS_EASY_TO_AUTOMATE, true);
                retConfig.setDisplayedVersionNumber(2);

                break;

        }

        return retConfig;
    }

    private void setDisplayedVersionNumber(final int version) {
        displayedVersionNumber = version;
    }
}


