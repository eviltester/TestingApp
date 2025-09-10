# syntax=docker/dockerfile:1

FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /opt/app
COPY ./testingapps/restlisticator/target/rest-list-system-1-2-2-jar-with-dependencies.jar .
CMD ["java", "-jar", "rest-list-system-1-2-2-jar-with-dependencies.jar"]
EXPOSE 4567