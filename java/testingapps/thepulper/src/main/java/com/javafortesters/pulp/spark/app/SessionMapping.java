package com.javafortesters.pulp.spark.app;

import com.javafortesters.pulp.PulpApp;
import com.javafortesters.pulp.api.EntityResponse;
import com.javafortesters.pulp.reader.forseries.SavageReader;
import com.javafortesters.pulp.reader.forseries.SpiderReader;
import com.javafortesters.pulp.reader.forseries.TheAvengerReader;
import spark.Request;
import spark.Session;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.halt;

public class SessionMapping {

    private static final String SESSION_APP = "sessionPulpApp";
    private final boolean allowsShutdown;

    private Map<String, Session> api_session_mapping= new ConcurrentHashMap<>();

    private static final String SESSION_API_SECRET = "apisecret";

    // TODO: make max session configurable through environment variables
    private static final int MAX_SESSION_LENGTH = 60*3;  // set max session without interactivity to 5 minutes
    private static final long CHECK_FOR_EXPIRED_API_SESSIONS_EVERY_MILLIS = 60*1*1000;  // checkfor expired api sessions every 3 minutes
    private static final long CHECK_FOR_EXPIRED_GUI_SESSIONS_EVERY_MILLIS = 60*1*1000;  // checkfor expired GUI sessions every 2 minutes


    long lastDeleteExpiredCheck=0;
    long lastDeleteGUIExpiredCheck=0;

    public SessionMapping(final boolean allowsShutdown) {
        this.allowsShutdown = allowsShutdown;
    }

    public void deleteExpiredAPISessions() {

        long currentCheck = System.currentTimeMillis();
        long timeSinceLastCheck = currentCheck-lastDeleteExpiredCheck;
        long checkInXMilliseconds = timeSinceLastCheck-CHECK_FOR_EXPIRED_API_SESSIONS_EVERY_MILLIS;

        if(checkInXMilliseconds<0){
            System.out.println("Check for expired API sessions in milliseconds time " + (checkInXMilliseconds*-1));
            return;
        }

        lastDeleteExpiredCheck=currentCheck;

        for(String sessionKey : api_session_mapping.keySet()){
            Session session = api_session_mapping.get(sessionKey);
            System.out.println("checking session " + sessionKey);
            if(session==null){
                System.out.println("session is null removing... " + sessionKey);
                api_session_mapping.remove(sessionKey);
            }else {
                try {
                    System.out.println("checking session validity... " + sessionKey);
                    final PulpApp app = session.attribute(SESSION_APP);
                } catch (Exception e) {
                    System.out.println("automatically removed api session " + sessionKey);
                    api_session_mapping.remove(sessionKey);
                }
            }
        }
    }

    public void deleteAnyInvalidSessions(){

        long currentCheck = System.currentTimeMillis();
        long timeSinceLastCheck = currentCheck-lastDeleteGUIExpiredCheck;
        long checkInXMilliseconds = timeSinceLastCheck-CHECK_FOR_EXPIRED_GUI_SESSIONS_EVERY_MILLIS;

        if(checkInXMilliseconds<0){
            System.out.println("Check for expired GUI sessions in milliseconds time " + (checkInXMilliseconds*-1));
            return;
        }

        lastDeleteGUIExpiredCheck=currentCheck;

        List<String> deleteThese = new ArrayList<>();

        for(String sessionKey : api_session_mapping.keySet()){
            try {
                System.out.println("checking gui session " + sessionKey);
                Session session = api_session_mapping.get(sessionKey);
                System.out.println("session.lastAccessedTime() " + session.lastAccessedTime());
                System.out.println("session.creationTime() " + session.creationTime());
                System.out.println("session.maxInactiveInterval() " + session.maxInactiveInterval());
                System.out.println("current time " + System.currentTimeMillis());
                long currentTimeLastAccessedSeconds = ((System.currentTimeMillis()-session.lastAccessedTime())/1000/60);
                System.out.println("current time last accessed seconds" + ((System.currentTimeMillis()-session.lastAccessedTime())/1000/60));
                if(currentTimeLastAccessedSeconds > 60*5){
                    System.out.println("I SHOULD DELETE THIS SESSION");
                }

                PulpApp sessionPulpApp=session.attribute(SESSION_APP);
            }catch (Exception e){
                System.out.println("Delete invalid session " + sessionKey);
                System.out.println(e.getMessage());
                deleteThese.add(sessionKey);
            }
        }

        for(String sessionKey :  deleteThese){
            Session session=null;

            try{
                System.out.println("Clearing session " + sessionKey);
                session = api_session_mapping.get(sessionKey);
            }catch(Exception e){
                System.out.println("Could not clear down session " + sessionKey);
                System.out.println(e.getMessage());
            }
            try {
                System.out.println("Nulling app attribute " + sessionKey);
                session.attribute(SESSION_APP, null);
            } catch (Exception e) {
                System.out.println("FAILED Nulling app attribute " + sessionKey);
                e.printStackTrace();
            }
            try {
                System.out.println("Removing app attribute " + sessionKey);
                session.removeAttribute(SESSION_APP);
            } catch (Exception e) {
                System.out.println("FAILED Removing app attribute " + sessionKey);
                e.printStackTrace();
            }
            try {
                System.out.println("Invalidating session " + sessionKey);
                session.invalidate();
            } catch (Exception e) {
                System.out.println("FAILED Invalidating session" + sessionKey);
                e.printStackTrace();
            }


            api_session_mapping.remove(sessionKey);
            System.out.println("Deleted session " + sessionKey);
        }
    }

    public PulpApp createSessionFor(Request req, final String xapiauth_sessionid) {
        PulpApp sessionPulpApp;

        Session session=null;

        session = req.session();
        session.maxInactiveInterval(MAX_SESSION_LENGTH);

        sessionPulpApp = new PulpApp();
        sessionPulpApp.setApiRootUrl("/apps/pulp/api");
        sessionPulpApp.getAppVersion().willAllowShutdown(this.allowsShutdown);
        sessionPulpApp.readData(new SavageReader("/data/pulp/doc_savage.csv"));
        sessionPulpApp.readData(new SpiderReader("/data/pulp/the_spider.csv"));
        sessionPulpApp.readData(new TheAvengerReader("/data/pulp/the_avenger.csv"));

        session.attribute(SESSION_APP, sessionPulpApp);

        // For an API to be valid you have to access the GUI first
        String appApiKey = xapiauth_sessionid;
        if(xapiauth_sessionid.length()==0) {
            appApiKey = sessionPulpApp.getAPISecret();
        }else{
            sessionPulpApp.setApiSecret(xapiauth_sessionid);
        }

        session.attribute(SESSION_API_SECRET, appApiKey);

        System.out.println(appApiKey);
        api_session_mapping.put(appApiKey, session);


        System.out.println("Session count " + api_session_mapping.size());

        return sessionPulpApp;
    }


    public PulpApp getPulpApp(Request req){

        final PulpApp app = getPulpAppSession(req);

        if(app==null){
            return null;
        }

        if(req.queryParams().contains("v")){
            // ensure that any pulp app we return has this version
            try {
                app.setAppVersion(Integer.parseInt(req.queryParams("v")));
            }catch(Exception e){
                // swallow exception
            }
        }
        return getPulpAppSession(req);
    }

    public Collection<String> getCookieValueFromRequest(final Request request, final String cookieName) {
        final String header = request.headers("Cookie");
        final List<String> cookieValues = new ArrayList();

        if(header==null || header.length()==0){
            return cookieValues;
        }

        final String[] cookies = header.split(";");

        for(String cookie : cookies){
            final String[] nameValue = cookie.split("=");
            if(nameValue.length>=2) {
                if (nameValue[0].trim().equalsIgnoreCase(cookieName.trim())) {
                    cookieValues.add(nameValue[1].trim());
                }
            }
        }

        return cookieValues;
    }

    public PulpApp getPulpAppSession(Request req){

        PulpApp sessionPulpApp;

        Session session=null;

        deleteAnyInvalidSessions();

        // if the request contains a cookie or header of X-API-AUTH then try to find that session
        final Collection<String> cookieValues = getCookieValueFromRequest(req, "X-API-AUTH");
        for(String authcode : cookieValues){
            session = api_session_mapping.get(authcode);
            if(session!=null){
                break;
            }
        }

        session = nullifyIfNotValid(session);

        // if there is an X-API-AUTH header then find that session
        if(session==null){
            final String header = req.headers("X-API-AUTH");
            if(header!=null) {
                session = api_session_mapping.get(header);
            }
        }

        session = nullifyIfNotValid(session);

        // do we have a gui session?
        Session guiSession = req.session(false);

        if(guiSession==null && session!=null){
            // could not find aGUI session but we found an API session
            // create the GUI session and associate it with the App from the API session
            guiSession = req.session();
            guiSession.maxInactiveInterval(MAX_SESSION_LENGTH);
            sessionPulpApp = session.attribute(SESSION_APP);
            guiSession.attribute(SESSION_APP, sessionPulpApp);

            // a GUI request was made, is the API still valid? or has it been removed?
            if (api_session_mapping.get(sessionPulpApp.getAPISecret()) == null) {
                api_session_mapping.put(sessionPulpApp.getAPISecret(), guiSession);
            }

            return sessionPulpApp;

        }

        if(guiSession==null && session==null){
            // no GUI session, and no API session, create default GUI Session
            return createSessionFor(req, "");
        }

        if(guiSession!=null && session==null){
            // we have a gui session, and no api session so return the app
            // no GUI session, and no API session, create default GUI Session
            sessionPulpApp = guiSession.attribute(SESSION_APP);
            // a GUI request was made, is the API still valid? or has it been removed?
            if (api_session_mapping.get(sessionPulpApp.getAPISecret()) == null) {
                api_session_mapping.put(sessionPulpApp.getAPISecret(), guiSession);
            }

            return sessionPulpApp;
        }

        if(guiSession!=null && session!=null){
            // we have a gui session, and an api session
            // replace the gui session app with the api session app
            // no GUI session, and no API session, create default GUI Session
            sessionPulpApp = session.attribute(SESSION_APP);
            guiSession.attribute(SESSION_APP, sessionPulpApp);
            // a GUI request was made, is the API still valid? or has it been removed?
            if (api_session_mapping.get(sessionPulpApp.getAPISecret()) == null) {
                api_session_mapping.put(sessionPulpApp.getAPISecret(), session);
            }

            return sessionPulpApp;
        }

        halt(500, new EntityResponse().setErrorStatus(500, "Error finding associated pulp app").getResponseBody());
        return null;
    }

    private Session nullifyIfNotValid(final Session session) {
        if(session!=null){
            // check if the session is valid by trying to use it
            try{
                session.attribute(SESSION_APP);
            }catch(Exception e){
                // session is invalid, do not use it
                return null;
            }
        }
        return session;
    }



    public PulpApp getPulpAppForApi(String api_auth_header){
        final Session session = api_session_mapping.get(api_auth_header);
        PulpApp sessionPulpApp=null;
        try{
            // access session to see if it is still alive
            sessionPulpApp = session.attribute(SESSION_APP);

            hackGuiSessionToKeepItAliveFromApi(session, System.currentTimeMillis());


        }catch(Exception e){
            // session is not valid
            if(session!=null) {
                try {
                    session.invalidate();
                }catch(Exception invalidateException){

                }
            }
            api_session_mapping.remove(api_auth_header);

            halt(401, new EntityResponse().setErrorStatus(401, "X-API-AUTH header is invalid - check in the GUI").getResponseBody());
        }
        return sessionPulpApp;
    }

    public String getSessionCookieForApi(String api_auth_header){
        final Session session = api_session_mapping.get(api_auth_header);
        PulpApp sessionPulpApp=null;
        try{
            // access session to see if it is still alive
            sessionPulpApp = session.attribute(SESSION_APP);

            return hackGuiSessionToKeepItAliveFromApi(session, System.currentTimeMillis());

        }catch(Exception e){
        }
        return "";
    }

    private String hackGuiSessionToKeepItAliveFromApi(final Session session, final long currentTimeMillis){
        // wanted to have the API keep the session alive
        // tried to use reflection to change lastaccessedtime on the session byt that didn't extend time
        // instead, pull out the JSESSIONID and send it back in SET-COOKIE responses

        try{

            Field subsession = session.getClass().getDeclaredField("session");
            subsession.setAccessible(true);
            org.eclipse.jetty.server.session.Session theSessionWithData = (org.eclipse.jetty.server.session.Session) subsession.get(session);
            Field theData = theSessionWithData.getClass().getDeclaredField("_sessionData");
            theData.setAccessible(true);
            final org.eclipse.jetty.server.session.SessionData theSessionData = (org.eclipse.jetty.server.session.SessionData) theData.get(theSessionWithData);

            return theSessionData.getId();

        }catch(NoSuchFieldException e){
            // couldn't do it
            System.out.println("Could not keep session alive");
        } catch (IllegalAccessException e) {
            System.out.println("Could not keep session alive - illegal access");
            e.printStackTrace();
        }

        return "";

    }

    public Session getSessionForX_API_AUTH(final String x_api_auth_header) {
        return api_session_mapping.get(x_api_auth_header);
    }
}
