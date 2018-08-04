package com.javafortesters.pulp.html;

import java.util.Collection;

public class HTMLReporter {

    public String getAsUl(Collection<String> simpleReport) {

        StringBuilder sb = new StringBuilder();

        sb.append("<ul>\n");

        for(String reportLine : simpleReport){
            sb.append(String.format("<li>%s</li>%n", reportLine));
        }

        sb.append("</ul>");

        return sb.toString();

    }
}
