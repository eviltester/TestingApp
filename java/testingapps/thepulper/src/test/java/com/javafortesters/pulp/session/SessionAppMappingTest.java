package com.javafortesters.pulp.session;

import com.javafortesters.pulp.PulpApp;
import com.javafortesters.pulp.spark.app.SessionAppMapping;
import org.junit.Assert;
import org.junit.Test;

public class SessionAppMappingTest {

    @Test
    public void canUpdateLastAccessedWhenAccessingApp(){

        final SessionAppMapping appMapping = new SessionAppMapping("key", new PulpApp());
        appMapping.hackCreatedTime(System.currentTimeMillis()-6000);
        final long lastAccessed = appMapping.lastAccessedXMillisAgo();
        System.out.println(lastAccessed);

        Assert.assertTrue(lastAccessed + " is not greater than 5000", lastAccessed>5000);
        appMapping.getApp();
        final long accessedAgain = appMapping.lastAccessedXMillisAgo();
        System.out.println(accessedAgain);
        Assert.assertTrue(accessedAgain<1000);

    }
}
