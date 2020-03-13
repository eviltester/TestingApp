package com.javafortesters.pulp.spark.app;

import com.javafortesters.pulp.PulpApp;

public class SessionAppMapping {
    String sessionKey;
    PulpApp app;
    long createdTime;
    long lastAccessedTime;

    static long MAX_SESSION_TIME_MILLIS = SessionMapping.MAX_SESSION_LENGTH * 1000;

    public SessionAppMapping(final String sessionKey, final PulpApp sessionPulpApp) {
        this.sessionKey = sessionKey;
        this.app = sessionPulpApp;
        createdTime = System.currentTimeMillis();
        updateAccessTime();
    }

    private void updateAccessTime() {
        lastAccessedTime = System.currentTimeMillis();
    }

    public boolean shouldRemove() {

        // if it doesn't have an app
        if(this.app==null){
            return true;
        }

        // if it is too old
        long timeSinceLastUsageMillis = System.currentTimeMillis() - lastAccessedTime;
        if(timeSinceLastUsageMillis > MAX_SESSION_TIME_MILLIS) {
            return true;
        }

        return false;
    }

    public void invalidateForRemoval() {
        if(app!=null){
            app.invalidate();
        }

        app=null;
    }


    public long lastAccessedXMillisAgo(){
    return System.currentTimeMillis() - lastAccessedTime;
    }

    public void debugOutput() {
        System.out.println("session.lastAccessedTime() " + lastAccessedTime);
        System.out.println("session.creationTime() " + createdTime);
        long currentTimeLastAccessedSeconds = ( lastAccessedXMillisAgo() / 1000);
        System.out.println("current time - last accessed seconds " + currentTimeLastAccessedSeconds);
        if (currentTimeLastAccessedSeconds > (60 * 5)) {
            System.out.println("I SHOULD DELETE THIS SESSION");
        }
    }

    public PulpApp getApp() {
        updateAccessTime();
        return app;
    }

    public boolean sessionIdMatches(final String id) {
        return sessionKey.equalsIgnoreCase(id);
    }


    public String getSessionId() {
        return sessionKey;
    }

    public void hackCreatedTime(final long overridetime) {
        createdTime = overridetime;
        lastAccessedTime=createdTime;
    }
}