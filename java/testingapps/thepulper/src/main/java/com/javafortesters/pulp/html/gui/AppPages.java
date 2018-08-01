package com.javafortesters.pulp.html.gui;

import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.html.gui.createPages.CreateAuthorPage;
import com.javafortesters.pulp.html.gui.createPages.CreateBookPage;
import com.javafortesters.pulp.html.gui.createPages.CreateHeroPage;
import com.javafortesters.pulp.html.gui.createPages.CreatePublisherPage;

public class AppPages {
    private final PulpData books;

    public AppPages(final PulpData theBooks) {
        this.books = theBooks;
    }

    public CreateBookPage createBookPage() {
        return new CreateBookPage(books);
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
