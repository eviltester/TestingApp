function Admin(){

    // Admin : AdminPass
    validCredentials = {username : "QWRtaW4=", password: "QWRtaW5QYXNz"};

    this.checkAuthDetails = function(username, password){
        if(btoa(username) !== validCredentials.username){
            return false;
        }
        if(btoa(password) !== validCredentials.password){
            return false;
        }
        return true;
    }

    this.logout = function(){
        document.cookie = "loggedin=; max-age=-5; path=/;";
        location.reload();
    }

    this.login =  function(username, password, remember, incorrectCallback){

        incorrectCallback = incorrectCallback || function(){};

        if(admin.checkAuthDetails(username, password)){
            admin.setLogin(username, remember);
            window.location.href = "adminview.html";
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

    // TODO: too tightly coupled to GUI
    this.activateLoginLink= function(){

        if(this.isLoggedIn()){
            a = document.getElementById("navadminlogout");
            a.onclick = function(){new Admin().logout()};
            a.setAttribute("href","#");

            a = document.getElementById("navadminlogin");
            a.innerText = "Admin Login";
            a.setAttribute("href", "adminview.html");

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