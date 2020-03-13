package com.javafortesters.pulp.spark.app;

import com.javafortesters.pulp.PulpApp;
import com.javafortesters.pulp.api.EntityResponse;
import com.javafortesters.pulp.reader.forseries.SavageReader;
import com.javafortesters.pulp.reader.forseries.SpiderReader;
import com.javafortesters.pulp.reader.forseries.TheAvengerReader;
import spark.Request;
import spark.Session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.halt;

// TODO: trim down logging
// TODO: introduce a logout as a way of having user triggered session deletion and tidy up
// TODO: create an admin page showing number of sessions
// TODO: create a detailed admin page showing settings, number of sessions, created, last used, life, and button to [trim]

/*
    This got overly complicated - do not store sessions with the mapping
    map: sessionid x pulp app x last accessed time

    request (with session) -> find the app for this session id

    we will track last accessed using system time
    and if system time goes beyond the point we want,
    we will delete this from the system
 */



public class SessionMapping {

    public static final String SESSION_ATTRIBUTE = "validity";
    private final boolean allowsShutdown;

    // instead of storing apps in sessions I am going to create an
    // object to map them
    private Map<String, SessionAppMapping> api_session_mapping= new ConcurrentHashMap<>();

    // TODO: make max session configurable through environment variables
    public static final int MAX_SESSION_LENGTH = 60*5;  // set max session without interactivity to 5 minutes
    private static final long CHECK_FOR_EXPIRED_GUI_SESSIONS_EVERY_MILLIS = 60*1*1000;  // checkfor expired sessions every minute


    long lastDeleteExpiredCheck=0;
    long lastDeleteGUIExpiredCheck=0;

    public SessionMapping(final boolean allowsShutdown) {
        this.allowsShutdown = allowsShutdown;
    }

    public SessionAppMapping findSessionMapping(String sessionId){
        for(SessionAppMapping mapping : api_session_mapping.values()){
            if(mapping.sessionIdMatches(sessionId)){
                return mapping;
            }
        }
        return null;
    }

    public long checkForExpiredGUISessionPeriod(){

        // this is a millisecond period
        // with fewer sessions we can extend this time period
        // make the delete dependent on time based on number of sessions e.g. < 100, 15 mins,
        // 101 - 300, 5 mins, 300 - 400 3 mins, 400+ 1min

        int numberOfSessions = api_session_mapping.size();

        if(numberOfSessions<100){
            return 900000L; //60*15*1000; // 15 mins
        }

        if(numberOfSessions<300){
            return 300000L; //60*5*1000; // 5 mins
        }

        if(numberOfSessions<400){
            return 180000L; //60*3*1000; // 3 mins
        }

        // return default
        return CHECK_FOR_EXPIRED_GUI_SESSIONS_EVERY_MILLIS;
    }

    int deletedSessionCount=0;

    public void deleteAnyInvalidSessions(){

        long currentCheck = System.currentTimeMillis();
        long timeSinceLastCheck = currentCheck-lastDeleteGUIExpiredCheck;
        long checkInXMilliseconds = timeSinceLastCheck-checkForExpiredGUISessionPeriod();

        if(checkInXMilliseconds<0){
            System.out.println("Check for expired sessions in milliseconds time " + (checkInXMilliseconds*-1));
            return;
        }

        lastDeleteGUIExpiredCheck=currentCheck;

        List<String> deleteThese = new ArrayList<>();

        for(String sessionKey : api_session_mapping.keySet()){
            System.out.println("checking session " + sessionKey);
            SessionAppMapping session = api_session_mapping.get(sessionKey);
            if(session.shouldRemove()){
                System.out.println("Delete invalid session " + sessionKey);
                session.debugOutput();
                deleteThese.add(sessionKey);
            }
        }

        for(String sessionKey :  deleteThese){
            System.out.println("Deleting invalid session " + sessionKey);
            SessionAppMapping session = api_session_mapping.get(sessionKey);
            session.invalidateForRemoval();
            api_session_mapping.remove(sessionKey);
            System.out.println("Deleted session " + sessionKey);
            deletedSessionCount++;
        }

        System.out.println("Total Sessions active : " + api_session_mapping.size());

        if(deletedSessionCount>100) {
            deletedSessionCount=0;
            // tell system that some garbage collection might be wise
            System.gc();
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

        // For an API to be valid you have to access the GUI first
        String appApiKey = xapiauth_sessionid;
        if(xapiauth_sessionid.length()==0) {
            appApiKey = sessionPulpApp.getAPISecret();
        }else{
            sessionPulpApp.setApiSecret(xapiauth_sessionid);
        }

        SessionAppMapping mapping = new SessionAppMapping(session.id(), sessionPulpApp);

        System.out.println("Created new Session");
        System.out.println("JSESSIONID: " + session.id());
        System.out.println("API-KEY: " + appApiKey);

        api_session_mapping.put(appApiKey, mapping);

        System.out.println("Session count " + api_session_mapping.size());

        return sessionPulpApp;
    }


    /*
        given a request, return the associated pulp app
     */
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

        SessionAppMapping sessionMapping=null;

        // delete any old unused sessions
        deleteAnyInvalidSessions();

        // we can use the app for any session if we know what the X-API-AUTH is
        // if the request contains a cookie or header of X-API-AUTH then try to find that session
        final Collection<String> cookieValues = getCookieValueFromRequest(req, "X-API-AUTH");
        for(String authcode : cookieValues){
            sessionMapping = api_session_mapping.get(authcode);
            if(sessionMapping!=null){
                break;
            }
        }

        // if there is an X-API-AUTH header then find that session
        if(sessionMapping==null){
            final String header = req.headers("X-API-AUTH");
            if(header!=null) {
                sessionMapping = api_session_mapping.get(header);
            }
        }

        // do we have an existing gui session?
        Session guiSession = req.session(false);
        SessionAppMapping guiSessionMapping;

        if(guiSession==null && sessionMapping!=null){

            // could not find aGUI session but we found an API session
            // create the GUI session and associate it with the App from the API session
            guiSession = req.session();
            guiSession.maxInactiveInterval(MAX_SESSION_LENGTH);
            sessionPulpApp = sessionMapping.getApp();

            guiSessionMapping = new SessionAppMapping(guiSession.id(), sessionPulpApp);

            api_session_mapping.put(sessionPulpApp.getAPISecret(), guiSessionMapping);

            return sessionPulpApp;
        }

        if(guiSession==null && sessionMapping==null){
            // no GUI session, and no API session, create new default GUI Session
            return createSessionFor(req, "");
        }

        if(guiSession!=null && sessionMapping==null){
            // we have an existing gui session, and no api session so return the app

            guiSessionMapping = findSessionMapping(guiSession.id());
            if(guiSessionMapping!=null){
                sessionPulpApp = guiSessionMapping.getApp();
                return sessionPulpApp;
            }
        }

        if(guiSession!=null && sessionMapping!=null){
            // we have an existing gui session, and an api session
            // replace the gui session app with the api session app
            // no GUI session, and no API session, create default GUI Session
            sessionPulpApp = sessionMapping.getApp();
            guiSessionMapping = new SessionAppMapping(guiSession.id(), sessionPulpApp);

            api_session_mapping.put(sessionPulpApp.getAPISecret(), guiSessionMapping);

            return sessionPulpApp;
        }

        halt(500, new EntityResponse().setErrorStatus(500, "Error finding associated pulp app").getResponseBody());
        return null;
    }

    public PulpApp getPulpAppForApi(String api_auth_header){
        final SessionAppMapping sessionMapping = api_session_mapping.get(api_auth_header);
        if(sessionMapping==null){
            halt(401, new EntityResponse().setErrorStatus(401, "X-API-AUTH header is invalid - check in the GUI").getResponseBody());
        }
        return sessionMapping.getApp();
    }

    public String getSessionCookieForApi(String api_auth_header){
        final SessionAppMapping sessionMapping = api_session_mapping.get(api_auth_header);
        if(sessionMapping!=null){
            return sessionMapping.getSessionId();
        }
        return "";
    }

    public SessionAppMapping getSessionForX_API_AUTH(final String x_api_auth_header) {
        return api_session_mapping.get(x_api_auth_header);
    }


}
