
if(typeof renderDeleteSuccess !== "function"){
    renderDeleteSuccess = function(){console.log("Deleted")};
}

if(typeof renderErrorMessage !== "function"){
    renderErrorMessage = function(message){console.log(message)};
}

    function removeMeFromList(anElement){
        try{
            var elemli = anElement.parentElement;
            var elemul = elemli.parentElement;

            elemul.removeChild(elemli);

            document.getElementById("item-list-count").innerText=elemul.childElementCount;
        }catch(e){}
    }


    function deleteAuthorFromForm(authorName, authorId, element){
        deleteAuthorFromList(authorName, authorId, element);
        return false;
    }

    function deleteAuthorFromList(authorname, authorid, anElement){
        if(deleteAuthorViaApi(authorname, authorid)){
            removeMeFromList(anElement);
            return true;
        }
        return false;
    }

    function deleteAuthorViaApi(authorname, authorid){
        if(areYouSureYouWantToDeleteAuthor(authorname)){
            var api=new PulperApi(new CookieAccess().getCookie("X-API-AUTH"));
            api.deleteAuthor(authorid, renderDeleteSuccess, renderErrorMessage);
            return true;
        }
        return false;
    }

    function areYouSureYouWantToDeleteAuthor(authorname, authorid, anElement){
        authorname = (typeof authorname !== 'undefined') ?  authorname : "the author";
        return confirm("Are you sure you want to delete " + authorname + "?");
    }

    function deleteBookFromForm(title, bookid, anElement){
        deleteBookFromList(title, bookid, anElement);
        return false;
    }

    function deleteBookFromList(title, bookid, anElement){
        if(deleteBookViaApi(title, bookid)){
            removeMeFromList(anElement);
            return true;
        }
        return false;
    }

    function deleteBookViaApi(title, bookid){
        if(areYouSureYouWantToDeleteBook(title)){
            var api=new PulperApi(new CookieAccess().getCookie("X-API-AUTH"));
            api.deleteBook(bookid, renderDeleteSuccess, renderErrorMessage);
            return true;
        }
        return false;
    }

    function areYouSureYouWantToDeleteBook(title){
        title = (typeof title !== 'undefined') ?  title : "the book";
        return confirm("Are you sure you want to delete " + title + "?");
    }

    function deleteSeriesFromForm(named, seriesid, anElement){
        deleteSeriesFromList(named, seriesid, anElement);
        return false;
    }

    function deleteSeriesFromList(named, seriesid, anElement){
        if(deleteSeriesViaApi(named, seriesid)){
            removeMeFromList(anElement);
            return true;
        }
        return false;
    }

    function deleteSeriesViaApi(named, seriesid){
        if(areYouSureYouWantToDeleteSeries(named)){
            var api=new PulperApi(new CookieAccess().getCookie("X-API-AUTH"));
            api.deleteSeries(seriesid, renderDeleteSuccess, renderErrorMessage);
            return true;
        }
        return false;
    }

    function areYouSureYouWantToDeleteSeries(title){
        title = (typeof title !== 'undefined') ?  title : "the series";
        return confirm("Are you sure you want to delete " + title + "?");
    }

    function deletePublisherFromForm(named, publisherid, anElement){
        deletePublisherFromList(named, publisherid, anElement);
        return false;
    }

    function deletePublisherFromList(named, publisherid, anElement){
        if(deletePublisherViaApi(named, publisherid)){
            removeMeFromList(anElement);
            return true;
        }
        return false;
    }

    function deletePublisherViaApi(named, publisherid){
        if(areYouSureYouWantToDeletePublisher(named)){
            var api=new PulperApi(new CookieAccess().getCookie("X-API-AUTH"));
            api.deletePublisher(publisherid, renderDeleteSuccess, renderErrorMessage);
            return true;
        }
        return false;
    }

    function areYouSureYouWantToDeletePublisher(named){
        named = (typeof named !== 'undefined') ?  named : "the publisher";
        return confirm("Are you sure you want to delete " + named + "?");
    }


/*

    Admin prompts

 */
    function areYouSureYouWantToShutdown(){
        if(confirm("Are you sure you want to shutdown the app?")){
            return true;
        }else{
            return false;
        }
    }
