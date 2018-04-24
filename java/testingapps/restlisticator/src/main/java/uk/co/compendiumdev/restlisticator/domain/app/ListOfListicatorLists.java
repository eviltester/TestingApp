package uk.co.compendiumdev.restlisticator.domain.app;

import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;

import java.util.ArrayList;
import java.util.List;

// TODO - remains to be seen if we actually need this or if a List<ListicatorList> lists on TheListicator is good enough
public class ListOfListicatorLists {

    private final List<ListicatorList> lists= new ArrayList<>();

    public void addLists(List<ListicatorList> lists) {
        this.lists.addAll(lists);
    }

    public void addList(ListicatorList list) {
        this.lists.add(list);
    }

    public List<ListicatorList> getAsList() {
        return lists;
    }

    public int listCount() {
        return lists.size();
    }
}
