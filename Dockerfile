FROM openjdk:17
EXPOSE 9090
ADD build/libs/Online-Medicine-0.0.1-SNAPSHOT.jar pharmacy
COPY src/main/resources/photos src/main/resources/photos
ENTRYPOINT ["java", "-jar","pharmacy"]