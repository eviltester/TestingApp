document.addEventListener('DOMContentLoaded', function() {
    htmlTableOfContents();
} );

// based on https://stackoverflow.com/a/41085566
function htmlTableOfContents( documentRef ) {
    var documentRef = documentRef || document;
    var toc = documentRef.getElementById("toc");
    if(!toc)
        return;
    var headings = [].slice.call(documentRef.body.querySelectorAll('h2, h3, h4, h5, h6'));
    headings.forEach(function (heading, index) {
        var ref = "toc" + index;
        if ( heading.hasAttribute( "id" ) )
            ref = heading.getAttribute( "id" );
        else
            heading.setAttribute( "id", ref );

        var link = documentRef.createElement( "a" );
        link.setAttribute( "href", "#"+ ref );
        link.textContent = heading.textContent;

        var div = documentRef.createElement( "div" );
        div.setAttribute( "class", heading.tagName.toLowerCase() );
        div.appendChild( link );
        toc.appendChild( div );
    });
}