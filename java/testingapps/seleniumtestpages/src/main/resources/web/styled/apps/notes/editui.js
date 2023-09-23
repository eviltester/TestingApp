//todo move all the UI functionality and hooks into here

NoteTakerUI = function() {

    this.showLoadedNotes = function(notetaker, parentDivId="list-of-notes",
                                    showEditButton=true, showDesc=false,
                                    deleteNote=function(){}, editNote=function(){}
    ){
        listParent = document.getElementById(parentDivId);
        listItems = notetaker.notes.map(
            note => {
                elem = document.createElement("div");
                elem.classList.add("note-in-list");
                elem.setAttribute("data-key", note.id);

                elembdelete = document.createElement("button");
                elembdelete.setAttribute("data-key", note.id);
                elembdelete.innerText="X";
                elembdelete.classList.add("delete-note-in-list");
                elembdelete.addEventListener("click",deleteNote);

                if(showEditButton){
                    elembedit = document.createElement("button");
                    elembedit.setAttribute("data-key", note.id);
                    elembedit.innerText="Edit";
                    elembedit.classList.add("edit-note-in-list");
                    elembedit.addEventListener("click",editNote);
                }

                elemp = document.createElement("p");
                elemp.classList.add("title-note-in-list");
                elemp.setAttribute("data-key", note.id);
                elemp.innerText = note.title;

                if(showDesc){
                    console.log(note.note);
                }

                elem.appendChild(elembdelete)
                if(showEditButton) {
                    elem.appendChild(elembedit)
                }
                elem.appendChild(elemp)
                return elem;
            }
        )
        listParent.replaceChildren(...listItems);
    }
}