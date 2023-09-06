## Testing App

This testing app contains multiple applications.

To build, run from the root directory

```
mvn package
```

Each project will be built standalone, with a full app in the `testinapps\testingapp\target` directory.


## Dockerised Selenium Test Pages

The Selenium Test Pages project has been packaged as a docker image so can be run via docker.

After packaging the app with `mvn package`

From the root directory containing this readme.md run:

```
docker build -t seleniumtestpages -f ./docker/selenium-test-pages/Dockerfile .
```

Then:

```
docker run -p 4567:4567 seleniumtestpages
```

This will make the test pages app accessible on `localhost:4567`

---

