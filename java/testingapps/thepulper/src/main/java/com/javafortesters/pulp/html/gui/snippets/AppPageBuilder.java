package com.javafortesters.pulp.html.gui.snippets;

public class AppPageBuilder {

    private final String title;
    private final StringBuilder pageOutput;
    private final StringBuilder pageBody;

    public AppPageBuilder(String aTitle){
        this.title = aTitle;
        pageOutput = new StringBuilder();

        pageOutput.append(new PageSnippets().getPageHead(aTitle));
        pageOutput.append(new PageSnippets().getDropDownMenu());

        pageBody = new StringBuilder();
    }


    public void appendToBody(final String stringToAdd) {
        pageBody.append(stringToAdd);
    }

    @Override
    public String toString() {
        StringBuilder renderedPage = new StringBuilder(pageOutput.toString());

        renderedPage.append(pageBody);

        renderedPage.append(new PageSnippets().getPageFooter());

        return renderedPage.toString();
    }

}
