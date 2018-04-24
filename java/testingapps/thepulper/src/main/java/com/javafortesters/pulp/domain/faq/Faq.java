package com.javafortesters.pulp.domain.faq;

public class Faq {
    private final String template;

    public Faq(String faqTemplate) {
        this.template = faqTemplate;
    }

    public String getTemplate() {
        return template;
    }
}
