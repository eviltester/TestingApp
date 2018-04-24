package com.javafortesters.pulp.html.templates;

import org.junit.Assert;
import org.junit.Test;

public class MyTemplateTest
{

    @Test
    public void canReplaceASection(){
        String templateText = "Hello !!section!!Bob!!section!!, Welcome";

        MyTemplate template = new MyTemplate(templateText).replaceSection("!!section!!", "Alan");

        Assert.assertEquals("Hello Alan, Welcome", template.toString());
    }

    @Test
    public void canReplaceAString(){
        String templateText = "Hello !!variable!!, Welcome";

        MyTemplate template = new MyTemplate(templateText).replace("!!variable!!", "Bob");

        Assert.assertEquals("Hello Bob, Welcome", template.toString());
    }
}
