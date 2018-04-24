package uk.co.compendiumdev.restlisticator.domain.app;


import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TheListicator {

    // TODO: perhaps this should be a map to make read and delete easier?
    private final List<ListicatorList> lists= new ArrayList<>();

    public void addList(ListicatorList listicatorList) {
        lists.add(listicatorList);
    }

    public int listCount() {
        return lists.size();
    }

    public ListOfListicatorLists getLists() {
        ListOfListicatorLists newLists = new ListOfListicatorLists();
        newLists.addLists(lists);
        return newLists;
    }

    public boolean deleteList(String guid) {
        int foundIndex=-1;
        int atIndex=0;

        foundIndex = findIndexFor(guid);

        if(foundIndex!=-1){
            lists.remove(foundIndex);
            return true;
        }

        return false;
    }

    public ListicatorList getList(String guid) {
        int foundIndex = findIndexFor(guid);

        if(foundIndex!=-1){
            return lists.get(foundIndex);
        }

        return null;
    }

    private int findIndexFor(String guid) {
        int atIndex=0;

        for(ListicatorList list : lists){
            if(list.getGUID().contentEquals(guid)){
                return atIndex;
            }
            atIndex++;
        }

        return -1;
    }

    public ListicatorList patchList(String guid, Map<String, String> patches) {
        int foundIndex = findIndexFor(guid);

        if(foundIndex!=-1){
            return lists.get(foundIndex).patch(patches);
        }

        return null;
    }

    public ListicatorList putList(String guid, ListicatorList putlist) {
        int foundIndex = findIndexFor(guid);

        if(foundIndex==-1){
            // does not exist, create
            addList(putlist);
            return putlist;
        }else{
            return lists.get(foundIndex).overrideContents(putlist);
        }
    }
}
