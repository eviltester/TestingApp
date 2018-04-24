package uk.co.compendiumdev.restlisticator.api.payloads;

import uk.co.compendiumdev.restlisticator.http.QueryParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alan on 19/07/2017.
 */
public class ResultsFilter {

    private List<String> withFilterValues;
    private List<String> withoutFilterValues;

    public void setFromQuery(String fromQuery) {

        QueryParser queryParser = new QueryParser(fromQuery);

        withFilterValues = new ArrayList();
        withoutFilterValues = new ArrayList();

        if(queryParser.hasAttribute("with")){
            String []filters = queryParser.getValueFor("with").split(",");
            withFilterValues.addAll(Arrays.asList(filters));
        }

        if(queryParser.hasAttribute("without")){
            String []filters = queryParser.getValueFor("without").split(",");
            withoutFilterValues.addAll(Arrays.asList(filters));
        }
    }

    public List<String> getWithFilterValues() {
        return withFilterValues;
    }

    public List<String> getWithoutFilterValues() {
        return withoutFilterValues;
    }
}
