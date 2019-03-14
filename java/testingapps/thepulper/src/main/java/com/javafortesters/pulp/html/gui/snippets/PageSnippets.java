package com.javafortesters.pulp.html.gui.snippets;

import com.javafortesters.pulp.html.templates.MyTemplate;
import com.javafortesters.pulp.reader.VersionedResourceReader;
import com.javafortesters.pulp.spark.app.versioning.AppVersion;

public class PageSnippets {


    // e.g. v001
    private final AppVersion appversion;
    private final String snippetsPathPrefix;

    public PageSnippets(final AppVersion appversionDetails) {
        this.appversion = appversionDetails;
        snippetsPathPrefix = String.format("/web/apps/pulp/%s/page-template/snippets/", this.appversion.getAppVersion());
    }

    public String getPageHead(String title){
        StringBuilder report = new StringBuilder();

        final VersionedResourceReader versionedReader = new VersionedResourceReader(appversion);
        String pageToRender = versionedReader.asString("/page-template/snippets/page-header.html");
//        String pageToRender = new ResourceReader().asString(snippetsPathPrefix + "page-header.html");
        MyTemplate template = new MyTemplate(pageToRender);
        template.replace("<!-- TITLE -->", title);
        report.append(template.toString());
        return report.toString();
    }

    public String getDropDownMenu() {
        StringBuilder report = new StringBuilder();

        //<!-- List of Versions -->
        //                <li id="menu-set-version-1"><a href="/apps/pulp/gui/admin/version/1">v001</a></li>
        //                <li id="menu-set-version-2"><a href="/apps/pulp/gui/admin/version/2">v002</a></li>
        //                <li id="menu-set-version-3"><a href="/apps/pulp/gui/admin/version/3">v003</a></li>
        final VersionedResourceReader versionedReader = new VersionedResourceReader(appversion);
        String pageToRender = versionedReader.asString("/page-template/snippets/dropdownmenu.html");

        StringBuilder versions = new StringBuilder();
        for(int version=1; version <= AppVersion.MAX_VERSION; version++){
            versions.append(String.format("<li id=\"menu-set-version-%d\"><a href=\"/apps/pulp/gui/admin/version/%d\">%s</a></li>", version, version, AppVersion.asPathVersion(version)));
        }

        String shutdownMenuItem = "<li id=\"menu-admin-shutdown\"><a href=\"/shutdown\" onclick=\"return areYouSureYouWantToShutdown()\">Shutdown</a></li>";

        MyTemplate template = new MyTemplate(pageToRender);
        template.replace("<!-- List of Versions -->", versions.toString());
        if(appversion.allowsShutdown()) {
            template.replace("<!-- Allow Shutdown -->", shutdownMenuItem);
        }
        report.append(template.toString());
        return report.toString();
    }

    public String getPageFooter() {

        StringBuilder report = new StringBuilder();

        final VersionedResourceReader versionedReader = new VersionedResourceReader(appversion);
        String pageToRender = versionedReader.asString("/page-template/snippets/page-footer.html");
//        String pageToRender = new ResourceReader().asString(snippetsPathPrefix + "page-footer.html");
        MyTemplate template = new MyTemplate(pageToRender);
        template.replace("<!-- VERSION -->", appversion.getAppVersion());
        report.append(template.toString());
        return report.toString();
    }

    public String getSnippet(final String snippet) {

        try {
            final VersionedResourceReader versionedReader = new VersionedResourceReader(appversion);
            final String pageToRender = versionedReader.asString( "/page-template/snippets/"+snippet);
//            final String pageToRender = new ResourceReader().asString( snippetsPathPrefix + snippet);
            final MyTemplate template = new MyTemplate(pageToRender);
            return template.toString();
        }catch(Exception e){
            System.out.println("SNIPPET: " + snippet);
            System.out.println(e.getMessage());
        }

        return "";
    }
}
