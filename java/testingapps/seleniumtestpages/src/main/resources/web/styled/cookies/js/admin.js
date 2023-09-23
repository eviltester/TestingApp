function Admin(){

    // Admin : AdminPass
    const UserType = {ADMIN: "admin", SUPERADMIN: "super"};
    users = [
        {   username: "QWRtaW4=",
            type: UserType.ADMIN,
            password:"QWRtaW5QYXNz",
            loggedInPage: "adminview.html",
            validPages:["adminview.html"],
            bannedPages:["superadminview.html"],
        },
        {
            username: "U3VwZXJBZG1pbg==",
            type: UserType.SUPERADMIN,
            password: "QWRtaW5QYXNz",
            loggedInPage: "superadminview.html",
            validPages: ["adminview.html", "superadminview.html"],
            bannedPages: [],
        }
    ]

    this.getUserDetails = function(username, password){
        found = users.filter((user)=>{
            return user.username == btoa(username) && user.password == btoa(password)
        })
        if(found.length>0){
            return found[0];
        }else{
            return null
        }
    }

    this.getUserByName = function(username){
        found = users.filter((user)=> user.username == btoa(username))
        if(found.length>0){
            return found[0];
        }else{
            return null
        }
    }

    this.checkAuthDetails = function(username, password){
        aUser = this.getUserDetails(username, password);
        return aUser != null;
    }

    this.logout = function(){
        document.cookie = "loggedin=; max-age=-5; path=/;";
        location.reload();
    }

    this.login =  function(username, password, remember, incorrectCallback){

        incorrectCallback = incorrectCallback || function(){};

        if(admin.checkAuthDetails(username, password)){
            admin.setLogin(username, remember);
            window.location.href =
                admin.getUserDetails(username, password).loggedInPage;
        }else{
            incorrectCallback();
        }
    }

    this.setLogin = function(username, remember){
        var cookieAge = "";
        if(remember){
            cookieAge = "max-age=864000;" // 10 days
        }
        document.cookie = "loggedin=" + username + ";" + cookieAge + "path=/;";
    }

    this.isLoggedIn = function(){
        return document.cookie.indexOf('loggedin=')!=-1;
    }

    this.getLoggedInUrl = function(){
        try{
            theUser = this.getUserByName(this.loggedInUserName());
            return theUser.loggedInPage;
        }catch(e){
            return "adminlogin.html";
        }
    }

    this.loggedInUserName = function(){
        re_username = /loggedin=(.*);?/
        matches = document.cookie.match(re_username)
        if(matches==null) return "";
        if(matches.length>1) return matches[1];
        return "";
    }

    this.userCanAccess = function(aUrl){
        theUser = this.getUserByName(this.loggedInUserName());
        if(theUser){
            return theUser.validPages.includes(aUrl);
        }else{
            return false;
        }
    }

    // TODO: too tightly coupled to GUI
    this.activateLoginLink= function(){

        if(this.isLoggedIn()){
            a = document.getElementById("navadminlogout");
            a.onclick = function(){new Admin().logout()};
            a.setAttribute("href","#");

            a = document.getElementById("navadminlogin");
            a.innerText = "Admin Login";
            a.setAttribute("href", "adminview.html");

            theUser = this.getUserByName(this.loggedInUserName())
            if(theUser!=null){

                a = document.getElementById("navadminlogin");
                a.innerText = "Admin Login";
                a.setAttribute("href", this.getLoggedInUrl());

                viewlinks = document.querySelectorAll("a[id$='view']");
                for (var i = 0; i < viewlinks.length; i++) {
                    currentValue = viewlinks[i];
                    if(theUser.bannedPages.includes(
                        currentValue.getAttribute("href")))
                    {
                        // cannot access page
                        currentValue.removeAttribute("href")
                    }
                };
            }

        }else{
            a = document.getElementById("navadminlogout");
            a.onclick = null;
            a.removeAttribute("href");

            a = document.getElementById("navadminlogin");
            a.innerText = "Admin Login";
            a.setAttribute("href","adminlogin.html");

        }

    }
}