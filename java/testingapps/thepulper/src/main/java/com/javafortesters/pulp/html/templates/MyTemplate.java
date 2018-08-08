package com.javafortesters.pulp.html.templates;

public class MyTemplate {
    private final String original;
    private String template;

    public MyTemplate(String template) {
        this.template = template;
        this.original = template;
    }

    public MyTemplate replaceSection(String sectionMarker, String replacement) {

        int startOfSection = template.indexOf(sectionMarker);

        if(startOfSection==-1){
            // could not find it
            System.out.println("Could not find start of section " + sectionMarker);
            return this;
        }

        int endOfSection = template.indexOf(sectionMarker, startOfSection + sectionMarker.length());

        if(endOfSection==-1){
            // could not find it
            System.out.println("Could not find end of section " + sectionMarker);
            return this;
        }

        this.template = template.substring(0, startOfSection) + replacement + template.substring(endOfSection+sectionMarker.length());

        return this;
    }

    @Override
    public String toString() {
        return template;
    }

    public MyTemplate replace(String templateVariable, String replacement) {
        this.template = template.replace(templateVariable, replacement);
        return this;
    }

    public void reset() {
        template = original;
    }
}
