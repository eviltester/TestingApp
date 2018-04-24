/**
 * Web Specific GUI functions
 */

function changeSloganTo(slogan){
    document.getElementById("bookinfo").setAttribute("style","display:none");
    document.getElementById("slogan").innerHTML=slogan;
    var encodedUri = encodeURI("https://twitter.com/intent/tweet?text="+slogan)+"%20%23EvilTesterSloganizer";
    document.getElementById("tweetthis").setAttribute("href", encodedUri);

    // add to history
    document.getElementById("sloganhistory").removeAttribute("style");
    var ul = document.getElementById("sloganhistorylist");
    var li = document.createElement("li");
    li.appendChild(document.createTextNode(slogan + " "));
    var lia = document.createElement("a");
    lia.setAttribute("href", encodedUri);
    lia.innerHTML="[tweet this]";
    li.appendChild(lia);
    ul.insertBefore(li, ul.childNodes[0]);
    //ul.appendChild(li);

}

function showBookAd(){
    if(randomNumberUpTo(25)>23){
        document.getElementById("bookinfo").setAttribute("style","display");
    }
}