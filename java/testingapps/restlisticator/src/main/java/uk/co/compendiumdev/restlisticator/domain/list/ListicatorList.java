package uk.co.compendiumdev.restlisticator.domain.list;


import uk.co.compendiumdev.restlisticator.domain.GUIDGenerator;
import uk.co.compendiumdev.restlisticator.domain.utils.ReflectionPatcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ListicatorList {

    private String guid;
    private String owner;
    private String createdDate;
    private String amendedDate;
    private String title;
    private String description;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    public ListicatorList(String title, String description) {

        this.title=title;
        if(this.title==null)
            this.title="";

        this.description=description;
        if(this.description==null)
            this.description="";
        
        this.guid = GUIDGenerator.getNextGUID();
        this.owner="";

        updateAmendedDate();
        this.createdDate = this.amendedDate;

    }

    private void updateAmendedDate(){
        Date date = new Date();
        this.amendedDate = dateFormat.format(date);
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getGUID() {
        return this.guid;
    }

    public String getOwner() {
        return owner;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getAmendedDate() {
        return amendedDate;
    }



    public void setTitle(String title) {
        this.title = title;
        updateAmendedDate();
    }

    public void setDescription(String description) {
        this.description = description;
        updateAmendedDate();
    }


    public void setOwner(String owner) {
        this.owner = owner;
        updateAmendedDate();
    }

    public void forceSetOwner(String owner) {
        this.owner = owner;
    }

    public void forceSetCreatedDate(String date) {
        this.createdDate = date;
    }

    public void forceSetAmendedDate(String date) {
        this.amendedDate=date;
    }

    public void forceSetGuid(String guid) {
        this.guid = guid;
    }

    public ListicatorList patch(Map<String, String> patches) {

        ReflectionPatcher patcher = new ReflectionPatcher(this, ListicatorList.class);
        patcher.patch(patches);

        if(patcher.getFieldsPatched().size()>0){
            updateAmendedDate();
        }

        // todo handle a mix of good and bad patches - should probably return a patch report message

        return this;
    }

    public ListicatorList overrideContents(ListicatorList putlist) {
        this.title = putlist.getTitle();
        this.description = putlist.getDescription();
        this.createdDate = putlist.getCreatedDate();
        this.owner = putlist.getOwner();
        this.amendedDate= putlist.getAmendedDate();
        return this;
    }
}
