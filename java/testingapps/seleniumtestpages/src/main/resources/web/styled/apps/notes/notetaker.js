/*
    Saves notes in local storage
 */

NoteTaker = function() {

    keyprefix = "etnotebook";
    notebookName = "default";

    // default to localstorage
    storage = localStorage;

    this.notes = [];

    this.configure = function(aNotebookName = "default", useLocalStorage = true){
        if(aNotebookName && aNotebookName.length>1) {
            notebookName = aNotebookName;
        }
        if(useLocalStorage){
            storage = localStorage;
        }else{
            storage = sessionStorage;
        }
    }

    this.getNote = function(aKey = ""){
        return this.notes.find((item) => item.id == aKey);
    }

    this.deleteNote = function(aKey = ""){
        aNote = this.getNote(aKey);
        if(!aNote) return false;

        // remove from array
        this.notes = this.notes.filter(item => item.id !== aNote.id)
        // remove from persistence
        this.deleteSavedNote(aNote);
        return true;
    }

    this.updateNote = function(aKey = "", aTitle = "", aNote = ""){
        if(!aKey || aKey.length < 1){ return false}
        if(!aTitle || aTitle.length < 1){ return false}
        if(!aNote || aNote.length < 1){ return false}

        savedNote = this.getNote(aKey);
        if(!savedNote) return false;

        savedNote.title = aTitle;
        savedNote.note = aNote;

        this.saveNote(savedNote);

        return true;
    }

    this.generateKey = function(){
        return Date.now().toString() + "-" + Math.random().toString().substring(2);
    }

    this.addNote = function(aTitle = "", aNote = "", aKey = ""){
        if(!aTitle || aTitle.length < 1){ return false}
        if(!aNote || aNote.length < 1){ return false}

        anId=aKey;
        if(aKey==""){
            anId = this.generateKey();
        }

        if(this.getNote(anId)){
            // provided key exists so generate a new one
            anId = this.generateKey();
        }

        aNoteToAdd = {id: anId, title: aTitle, note: aNote};

        this.notes.push(aNoteToAdd)
        this.saveNote(aNoteToAdd)

        return true;
    }

    this.clear = function () {
        this.notes = [];
        this.purgeStorage();
        return true;
    }

    // Persistence

    this.aNotePrefix = function(){
        return keyprefix + "-" + notebookName + "-";
    }

    this.purgeStorage = function(){
        for (var i = storage.length-1; i >=0; i--){
            forKey = storage.key(i)
            if(forKey.startsWith(this.aNotePrefix())) {
                storage.removeItem(forKey);
            }
        }
    }

    this.load = function(){

        this.notes = [];

        for (var i = 0; i < storage.length; i++){
            forKey = storage.key(i)
            if(forKey.startsWith(this.aNotePrefix())) {
                aNote = JSON.parse(storage.getItem(forKey));
                // backwards compatibility, added date later
                if(!aNote.date){
                    aNote.date=Date.now();
                }
                this.notes.push(aNote);
            }
        }
    }

    this.save = function(){
        this.notes.forEach((note,index)=>{
            this.saveNote(note);
        })
    }

    this.saveNote= function(aNote){
        if(aNote && aNote.id && aNote.title && aNote.note){
            aNote.date = Date.now();
            storage.setItem(this.aNotePrefix() + aNote.id,
                JSON.stringify(aNote)
            )
        }
    }

    this.deleteSavedNote= function(aNote){
        if(aNote && aNote.id){
            storage.removeItem(this.aNotePrefix() + aNote.id)
        }
    }
}