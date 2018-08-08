package com.javafortesters.pulp.spark.app.versioning;

public class AppVersion {

    public static final int MAX_VERSION = 4;

    AppVersionConfig settings = new AppVersionConfig();

    int appVersion = 1; // default starting app version

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
}
