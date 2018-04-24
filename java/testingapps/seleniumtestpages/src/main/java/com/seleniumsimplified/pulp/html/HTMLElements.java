package com.seleniumsimplified.pulp.html;

import javax.print.DocFlavor;

public class HTMLElements {
    public static String getLink(String text, String href) {
        return String.format("<a href='%s'>%s</a>", href, text);
    }
    public static String getLi(String text) {
        return String.format("<li>%s</li>%n", text);
    }

    public static String htmlHeadWithTitle(String title) {
        return String.format("%s%n%s", startHtml(), getHeadWithTitle(title));
    }

    private static String getHeadWithTitle(String title) {
        return String.format("<head>%n%s%n</head>",getTitle(title));
    }

    private static String startHtml() {
        return "<html>";
    }

    public static String getTitle(String title){
        return String.format("<title>%s</title>", title);
    }

    public static String startUl() {
        return "<ul>\n";

    }

    public static String endUl() {
        return "</ul>";
    }

    public static String startBody() {
        return "<body>\n";
    }

    public static String endBody() {
        return "</body>";
    }

    public static String endHTML() {
        return "</html>";
    }
}
