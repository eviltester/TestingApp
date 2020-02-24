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

    this.createPublisherNamed = function (publisherName, renderResults, renderErrorMessage){
        var obj = {"publishers": [{"name" : publisherName}]};
        var aUrl = "/apps/pulp/api/publishers";
        this.post(aUrl, obj, renderResults, renderErrorMessage);
    }

    this.createSeriesNamed = function (seriesName, renderResults, renderErrorMessage){
        var obj = {"name" : seriesName};
        var aUrl = "/apps/pulp/api/series";
        this.post(aUrl, obj, renderResults, renderErrorMessage);
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
}