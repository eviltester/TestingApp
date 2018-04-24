package com.seleniumsimplified.pulp.html;

import java.util.Collection;

import static com.seleniumsimplified.pulp.html.HTMLElements.getLi;

public class HTMLReporter {

    public String getAsUl(Collection<String> simpleReport) {

        StringBuilder sb = new StringBuilder();

        sb.append(HTMLElements.startUl());

        for(String reportLine : simpleReport){
            sb.append(getLi(reportLine));
        }

        sb.append(HTMLElements.endUl());

        return sb.toString();

    }
}
