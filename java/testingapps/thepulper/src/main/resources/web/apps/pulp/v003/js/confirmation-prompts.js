    function areYouSureYouWantToDeleteAuthor(authorname){
        authorname = (typeof authorname !== 'undefined') ?  authorname : "the author";
        if(confirm("Are you sure you want to delete " + authorname + "?")){
            return true;
        }else{
            return false;
        }
    }


    function areYouSureYouWantToDeleteBook(title){
        title = (typeof title !== 'undefined') ?  title : "the book";
        if(confirm("Are you sure you want to delete " + title + "?")){
            return true;
        }else{
            return false;
        }
    }

    function areYouSureYouWantToDeleteSeries(title){
        title = (typeof title !== 'undefined') ?  title : "the series";
        if(confirm("Are you sure you want to delete " + title + "?")){
            return true;
        }else{
            return false;
        }
    }

    function areYouSureYouWantToDeletePublisher(named){
        named = (typeof named !== 'undefined') ?  named : "the publisher";
        if(confirm("Are you sure you want to delete " + named + "?")){
            return true;
        }else{
            return false;
        }
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
