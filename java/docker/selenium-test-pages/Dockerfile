# syntax=docker/dockerfile:1

FROM eclipse-temurin:11
ENV SELENIUM_TEST_PAGES_ALLOW_UPLOAD_FILE_SAVING TRUE
WORKDIR /opt/app
COPY ./testingapps/seleniumtestpages/target/seleniumtestpages-1-4-0-jar-with-dependencies.jar .
CMD ["java", "-jar", "seleniumtestpages-1-4-0-jar-with-dependencies.jar"]
EXPOSE 4567