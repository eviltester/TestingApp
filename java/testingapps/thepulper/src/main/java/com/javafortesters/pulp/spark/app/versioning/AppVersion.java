package com.javafortesters.pulp.spark.app.versioning;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class AppVersion {

    /*
        TODO: have a 'bugs' list with the bugs on or off
        Bugs should be independent of versions so we can have the bug across different versions of the app
        Have bugs toggleable in an admin interface
        Have collections of bugs so that we can associate them with versions of the app if we want to
        e.g. release 1, release 2, etc.



     */
    public static final int MAX_VERSION = 7;
    public static final int DEFAULT_VERSION = 5;

    AppVersionConfig settings = new AppVersionConfig();

    int appVersion = DEFAULT_VERSION; // default starting app version
    private boolean versionAllowsShutdown=false;

    public AppVersion(final int version) {

        setAppVersion(version);
    }

    public static String asPathVersion(final int anAppVersion) {
        return String.format("v%03d", anAppVersion);
    }

    public void setAppVersion(int version){

        if(version > MAX_VERSION){
            return;
        }

        appVersion = version;
        settings = AppVersionConfig.getVersionConfigFor(version);
    }

    public String getAppVersion(){
        return asPathVersion(appVersion);
    }

    public int getAppVersionInt() {
        return appVersion;
    }

    /**
     * Try not to use this, instead create an AppVersionSettings entry and code to that
     * - this way we can create our own custom versions
     * @param version
     * @return
     */
    @Deprecated
    public boolean currentVersionIs(final int version) {
        return version == appVersion;
    }

    /**
     * Backwards compatability because this used to be a String
     * @return
     */
    @Override
    public String toString() {
        return getAppVersion();
    }

    public boolean are(final AppVersionSettings appVersionSetting) {
        return getSetting(appVersionSetting);
    }

    private boolean getSetting(final AppVersionSettings appVersionSetting) {
        return settings.getSettingAsBoolean(appVersionSetting);
    }

    public List<String> getCapabilities(final boolean where) {

        List<String> descriptions = new ArrayList<>();

        for(AppVersionSettings setting : EnumSet.allOf(AppVersionSettings.class)){
            if(settings.getSettingAsBoolean(setting)==where){
                descriptions.add(setting.getDescription());
            }
        }

        return descriptions;
    }

    public boolean allowsShutdown() {
        return this.versionAllowsShutdown;
    }

    public void willAllowShutdown(final boolean allowsShutdown) {
        this.versionAllowsShutdown=allowsShutdown;
    }
}
