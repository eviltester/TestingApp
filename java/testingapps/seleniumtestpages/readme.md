## Current Version

Released as part of v 1.3.6 of the compendium:

Download the `.jar` from the releases page

- [releases](https://github.com/eviltester/TestingApp/releases/tag/v1.3.6)

Run with:

`java -jar seleniumtestpages-1-3-2-jar-with-dependencies.jar`

## Hosted Version

There is a deployed version of the application on heroku:

- https://testpages.herokuapp.com

The file upload, and shutdown functionality have been disabled on the heroku version.

## Configuration

You can switch off the "/shutdown" by creating an environment variable:

- `SELENIUM_TEST_PAGES_DISALLOW_SHUTDOWN`

And you can switch off the file upload processing by creating an environment variable:

- `SELENIUM_TEST_PAGES_DISALLOW_UPLOAD`

The environment variables can have any value, I usually use 'disallow'


## Self contained localhost version of test pages

This `java` folder contains a localhost version of the selenium test pages.

Written using spark framework.

`mvn package` in the directory containing the `pom.xml` will build a `seleniumtestpages` `.jar` in the `target` folder.

This is likely to be a `snapshot` build.

e.g.

`seleniumtestpages-1.0-SNAPSHOT-jar-with-dependencies.jar`

Running this `jar` e.g. 

`java -jar seleniumtestpages-1.0-SNAPSHOT-jar-with-dependencies.jar`

Will start a spark & jetty webserver on port 4567

~~~~~~~~
C:\githubtarget>java -jar seleniumtestpages-1.0-SNAPSHOT-jar-with-dependencies.jar
[Thread-0] INFO org.eclipse.jetty.util.log - Logging initialized @186ms
[Thread-0] INFO spark.webserver.JettySparkServer - == Spark has ignited ...
[Thread-0] INFO spark.webserver.JettySparkServer - >> Listening on 0.0.0.0:4567
[Thread-0] INFO org.eclipse.jetty.server.Server - jetty-9.3.z-SNAPSHOT
[Thread-0] INFO org.eclipse.jetty.server.ServerConnector - Started ServerConnector@2684e3ce{HTTP/1.1,[http/1.1]}{0.0.0.0:4567}
[Thread-0] INFO org.eclipse.jetty.server.Server - Started @362ms
~~~~~~~~

visiting [localhost:4567](http://localhost:4567/) will display the root index.html for the test pages.

These are not the exact same test pages in the `./pages` folder in github (which use php and can be deployed to a web server), instead these have been implemented in Java and are stored as resources in the Java App.

This has been done to maintain backwards compatibility with the training courses but allow extension to create 'application' case study projects as well.

To make the backwards compatibility complete, if you visit

* [localhost:4567/selenium](http://localhost:4567/selenium) you will be redirected to a html page with the title "Selenium Simplified"
* you can visit any of the testpages under /selenium as well e.g.
    * [localhost:4567/selenium/alert.html](http://localhost:4567/selenium/alert.html)
	* this is because the test pages were deployed live to a root directory and a `/testpages` directory
* most of the pages under `/selenium/` will redirect to root, but because some of the code in the online course performs asserts on the urls, some of the pages will not redirect e.g.
    * [localhost:4567/selenium/basic_web_page.html](http://localhost:4567/selenium/basic_web_page.html)
	
	
Anyone using the code from scratch is probably better off starting from the [localhost:4567](http://localhost:4567/) url and using the links from there.

## Compatibility with course source code

If you wish to run the course source code against a [localhost:4567](http://localhost:4567/)  version of the pages then perform some "`Edit \ Find \ Replace in path`" (or `Ctrl + Shift + R`).

In the following order:

Replace:

* `http://www.compendiumdev.co.uk` with `http://localhost:4567`
* `http://compendiumdev.co.uk` with `http://localhost:4567`
* `DOMAIN = "www.compendiumdev.co.uk"` with `DOMAIN = "localhost:4567"`

This will allow most of the tests to run locally against the java app.

* At the moment the screenshot tests still use `seleniumsimplified.com` and there is some navigation on the internet with the frames tests - these will be amended later.

