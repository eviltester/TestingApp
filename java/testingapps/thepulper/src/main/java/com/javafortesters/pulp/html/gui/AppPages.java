package com.javafortesters.pulp.html.gui;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.html.gui.createPages.CreateAuthorPage;
import com.javafortesters.pulp.html.gui.createPages.CreateHeroPage;
import com.javafortesters.pulp.html.gui.createPages.CreatePublisherPage;

public class AppPages {
    private final PulpData books;

    public AppPages(final PulpData theBooks) {
        this.books = theBooks;
    }

    public String createBookPage() {
        // todo: last one to create since it joins author, publisher, and hero together
        //return return new createBookPage().asHTMLString();;
        return null;
    }

    public CreateAuthorPage createAuthorPage() {
        return new CreateAuthorPage();
    }

    public CreatePublisherPage createPublisherPage() {
        return new CreatePublisherPage();
    }

    public CreateHeroPage createHeroPage() {
        return new CreateHeroPage();
    }

}
