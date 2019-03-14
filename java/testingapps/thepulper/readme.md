# The Pulper

The pulper is a simple app to use for exploratory testing and automating.

You can select the version of the app from the dropdown admin menu once the app is running.

Each user is provided with a unique session that lasts for 5 minutes of activity. If you do not do anything within that time your session will be deleted.

## Running locally

`java -jar thepulper-1-2-2-jar-with-dependencies.jar`

The actual .jar name may vary depending on the version or deployment mechanism.

For the purposes of this readme I will use `thepulper.jar`

### Running via a proxy

`java -jar thepulper.jar -proxy 8181`

The pulper will communicate via proxy 8181

### Allowing Shutdown

`java -jar thepulper.jar -allowshutdown`

A shutdown menu will be present which will allow shutting down the app. This is recommended for local use.

### Help

See the in built help menu for help and the different functionality associated with the versions can be seen listed.