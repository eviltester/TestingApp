package com.javafortesters.pulp.spark;

import com.javafortesters.pulp.html.HtmlReports;

public class AppVersion {

    private static final int MAX_VERSION = 2;

    int appVersion = 1;

    public AppVersion(final int version) {

        setAppVersion(version);
    }

    public void setAppVersion(int version){

        if(version > MAX_VERSION){
            return;
        }

        appVersion = version;
    }

    public String getAppVersion(){
        return String.format("v%03d", appVersion);
    }

    public int getAppVersionInt() {
        return appVersion;
    }

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
}
