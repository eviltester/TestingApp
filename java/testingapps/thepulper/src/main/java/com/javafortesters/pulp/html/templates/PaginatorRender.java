package com.javafortesters.pulp.html.templates;

import com.javafortesters.pulp.reporting.filtering.PaginationDetails;

public class PaginatorRender {
    private final PaginationDetails paginationDetails;

    public PaginatorRender(PaginationDetails paginationDetails) {
        this.paginationDetails = paginationDetails;
    }

    public String renderAsText() {

        String paging = String.format("Showing All %d %s.", paginationDetails.getTotalItems(), paginationDetails.getItemsName());

        if (paginationDetails.isPaginated()){
            paging = String.format("Showing Page %d of %d (total items %d)", paginationDetails.getCurrentPage(), paginationDetails.getTotalPages(), paginationDetails.getTotalItems());
        }

        return paging;
    }

    public String renderAsClickable(String path){

        if(!paginationDetails.isPaginated()){
            return renderAsHtml();
        }

        StringBuilder render = new StringBuilder();
        render.append("<p>");
        for(int page=1; page<=paginationDetails.getTotalPages();page++){
            if(page==paginationDetails.getCurrentPage()){
                render.append(String.format(
                        "<strong>[%d]</strong> ",
                        page));
            }
            else{
                render.append(
                        String.format(
                                "[<a href='%s?%s'>%d</a>] ",
                                path,
                                new FilterRenderer(paginationDetails.getFilter()).asUrlArgsList(page),
                                page));
            }
        }
        render.append("</p>");

        return render.toString();
    }


    public String renderAsHtml() {
        return "<p>" + renderAsText() + "</p>";
    }
}
