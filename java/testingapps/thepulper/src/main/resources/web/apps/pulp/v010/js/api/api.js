function restoreToSubmitPost(formid = "createform"){
    // add a workaround in case of any Ajax Issues - add #form and refresh
    var hashvalue = location.hash;
    if(hashvalue==="#form"){
        document.getElementById(formid).onsubmit = null;
        document.getElementById(formid).setAttribute("onsubmit", "");
        document.getElementById(formid).setAttribute("action", "#form");
        document.getElementById(formid).setAttribute("method", "post");
    }
}

function PulperApi(XApiAuthCookieValue){

    this.xhttpapi = new XhttpApi(XApiAuthCookieValue);

    this.searchFor = function (searchterm, renderResults, renderErrorMessage){
        var aUrl = "/apps/pulp/api/books?searchterm="+encodeURIComponent(searchterm)
        this.get(aUrl, renderResults, renderErrorMessage);
    }

    this.createAuthorNamed = function (authorName, renderResults, renderErrorMessage){
        var obj = {"name":authorName};
        var aUrl = "/apps/pulp/api/authors";
        this.post(aUrl, obj, renderResults, renderErrorMessage);
    }

    this.amendAuthor = function (authorid, authorName, renderResults, renderErrorMessage){
        var obj = {"id": authorid, "name":authorName};
        var aUrl = "/apps/pulp/api/authors";
        this.post(aUrl, obj, renderResults, renderErrorMessage);
    }

    this.deleteAuthor = function (authorId, renderResults, renderErrorMessage){
        var aUrl = "/apps/pulp/api/authors/" + authorId;
        this.delete(aUrl, renderResults, renderErrorMessage);
    }

    this.createPublisherNamed = function (publisherName, renderResults, renderErrorMessage){
        var obj = {"publishers": [{"name" : publisherName}]};
        var aUrl = "/apps/pulp/api/publishers";
        this.post(aUrl, obj, renderResults, renderErrorMessage);
    }

    this.amendPublisher = function (publisherid, publisherName, renderResults, renderErrorMessage){
        var obj = {"id": publisherid, "name":publisherName};
        var aUrl = "/apps/pulp/api/publishers";
        this.post(aUrl, obj, renderResults, renderErrorMessage);
    }

    this.deletePublisher = function (publisherId, renderResults, renderErrorMessage){
        var aUrl = "/apps/pulp/api/publishers/" + publisherId;
        this.delete(aUrl, renderResults, renderErrorMessage);
    }

    this.createSeriesNamed = function (seriesName, renderResults, renderErrorMessage){
        var obj = {"name" : seriesName};
        var aUrl = "/apps/pulp/api/series";
        this.post(aUrl, obj, renderResults, renderErrorMessage);
    }

    this.amendSeries = function (seriesid, seriesName, renderResults, renderErrorMessage){
        var obj = {"id": seriesid, "name":seriesName};
        var aUrl = "/apps/pulp/api/series";
        this.post(aUrl, obj, renderResults, renderErrorMessage);
    }

    this.deleteSeries = function (seriesId, renderResults, renderErrorMessage){
        var aUrl = "/apps/pulp/api/series/" + seriesId;
        this.delete(aUrl, renderResults, renderErrorMessage);
    }

    this.createBook = function (title, authorid, publisherid, seriesid, serieslabel, year,
                                renderResults, renderErrorMessage){
        var obj = {"title":title,
                    "authors": [{"id" : authorid}],
                    "series": {"id" : seriesid},
                    "publisher": {"id" : publisherid},
                    "seriesId" : serieslabel,
                    "publicationYear" : year
                    };
        var aUrl = "/apps/pulp/api/books";
        this.post(aUrl, obj, renderResults, renderErrorMessage);
    }

    function existsWithValue(variable){
        if(typeof variable === "undefined"){
            return false;
        }
        return variable !== null;
    }
    this.amendBook = function (bookid, title, authorid, houseauthorid, publisherid, seriesid, serieslabel, year,
                                renderResults, renderErrorMessage){
        var obj = {"id": bookid};
        if(existsWithValue(title)){obj.title = title;}
        var authors = [];
        if(existsWithValue(authorid)){obj.authors = authors; authors.push({"id" : authorid}); }
        if(existsWithValue(houseauthorid)){obj.authors = authors; authors.push({"id" : houseauthorid}); }
        if(existsWithValue(seriesid)){obj.series = {"id" : seriesid};}
        if(existsWithValue(publisherid)){obj.publisher = {"id" : publisherid};}
        if(existsWithValue(serieslabel)){obj.seriesId = serieslabel;}
        if(existsWithValue(year)){obj.publicationYear = year;}

        var aUrl = "/apps/pulp/api/books";
        this.post(aUrl, obj, renderResults, renderErrorMessage);
    }

    this.deleteBook = function (bookId, renderResults, renderErrorMessage){
        var aUrl = "/apps/pulp/api/books/" + bookId;
        this.delete(aUrl, renderResults, renderErrorMessage);
    }

    this.get = function(aUrl, renderResults, renderErrorMessage){

        this.xhttpapi.getRequest(aUrl)
            .then(
                function(response) {
                    if(response.data) {
                        renderResults(response.data);
                        console.table(response.data.books);
                        return false;
                    }
                    throw new Error(response.errors.report[0].messages[0]);
                }
            )
            .catch(
                function(error){
                    renderErrorMessage(error);
                    return false;
                });

        return true;
    }

    this.post = function (aUrl, obj, renderResults, renderErrorMessage){

        this.xhttpapi.postRequest(aUrl, obj)
            .then(
                function(response) {
                    if(response.data) {
                        renderResults(response.data);
                        return false;
                    }
                    throw new Error(response.errors.report[0].messages[0]);
                }
            )
            .catch(
                function(error){
                    renderErrorMessage(error);
                    return false;
                });

        return true;
    }

    this.delete = function (aUrl, renderResults, renderErrorMessage){

        this.xhttpapi.deleteRequest(aUrl)
            .then(
                function(response) {
                    renderResults();
                    return false;
                }
            )
            .catch(
                function(error){
                    renderErrorMessage(error);
                    return false;
                });

        return true;
    }
}