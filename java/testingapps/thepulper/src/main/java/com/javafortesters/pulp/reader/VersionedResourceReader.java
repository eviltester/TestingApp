package com.javafortesters.pulp.reader;

import com.javafortesters.pulp.spark.app.versioning.AppVersion;

import java.nio.file.Paths;

public class VersionedResourceReader {
    private final AppVersion appversion;
    private final String pathPrefix;

    public VersionedResourceReader(final AppVersion anAppversion) {
        this.appversion = anAppversion;
        this.pathPrefix = "/web/apps/pulp/";
    }

    /**
     * Start at current version and drop down versions looking for the file, if not found return "";
     * @param pathPostfix
     * @return
     */
    public String asString(final String pathPostfix) {

        int foundInVersion = this.appversion.getAppVersionInt();

        String foundInPathVersion = AppVersion.asPathVersion(foundInVersion);

        ResourceReader reader = new ResourceReader();

        while(foundInVersion>=0 && !reader.doesResourceExist(versionedFullPath(foundInPathVersion, pathPostfix))){
            foundInVersion--;
            foundInPathVersion = AppVersion.asPathVersion(foundInVersion);
        }

        if(foundInVersion<=0){
            //System.out.println(String.format("Could not find %s %s %s", pathPrefix, this.appversion.getAppVersion(), pathPostfix));
            return "";

        }else{
            return reader.asString(versionedFullPath(AppVersion.asPathVersion(foundInVersion), pathPostfix));
        }
    }

    public String versionedFullPath(String versionPathPart, String pathPostFix){
        return Paths.get(pathPrefix, versionPathPart, pathPostFix).toString();
    }
}
