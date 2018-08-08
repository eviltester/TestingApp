package com.javafortesters.pulp.spark.app.versioning;

public class AppVersionConfigValue {

    /**
     * Config Value is an instantiated AppVersionSettings
     */

    AppVersionSettings represents;
    Boolean booleanValue;

    public AppVersionConfigValue(final AppVersionSettings setting, final Boolean aBoolean) {
        represents = setting;
        booleanValue = aBoolean;
    }

    public Boolean getBoolean() {
        return booleanValue;
    }
}
