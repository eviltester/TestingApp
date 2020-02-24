
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
        }catch(e){}
    }

    function areYouSureYouWantToDeleteAuthor(authorname, authorid, anElement){
        authorname = (typeof authorname !== 'undefined') ?  authorname : "the author";
        if(confirm("Are you sure you want to delete " + authorname + "?")){
            var api=new PulperApi(new CookieAccess().getCookie("X-API-AUTH"));
            api.deleteAuthor(authorid, renderDeleteSuccess, renderErrorMessage);
        }

        removeMeFromList(anElement);

        // do not submit form - all work done via JS XHTTP
        return false;
    }


    function areYouSureYouWantToDeleteBook(title, bookid, anElement){
        title = (typeof title !== 'undefined') ?  title : "the book";
        if(confirm("Are you sure you want to delete " + title + "?")){
            var api=new PulperApi(new CookieAccess().getCookie("X-API-AUTH"));
            api.deleteBook(bookid, renderDeleteSuccess, renderErrorMessage);
        }

        removeMeFromList(anElement);

        // do not submit form - all work done via JS XHTTP
        return false;
    }

    function areYouSureYouWantToDeleteSeries(title, seriesid, anElement){
        title = (typeof title !== 'undefined') ?  title : "the series";
        if(confirm("Are you sure you want to delete " + title + "?")){
            var api=new PulperApi(new CookieAccess().getCookie("X-API-AUTH"));
            api.deleteSeries(seriesid, renderDeleteSuccess, renderErrorMessage);
        }

        removeMeFromList(anElement);

        // do not submit form - all work done via JS XHTTP
        return false;
    }

    function areYouSureYouWantToDeletePublisher(named, publisherid, anElement){
        named = (typeof named !== 'undefined') ?  named : "the publisher";
        if(confirm("Are you sure you want to delete " + named + "?")){
            var api=new PulperApi(new CookieAccess().getCookie("X-API-AUTH"));
            api.deletePublisher(publisherid, renderDeleteSuccess, renderErrorMessage);
        }else{

        }

        // debugging - adhoc testing
        // removeMeFromList(anElement);

        // do not submit form - all work done via JS XHTTP
        return false;
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
