FROM openjdk:17
EXPOSE 8080
ARG JAR_FILE=build/libs/Online-Medicine-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} online-pharmacy
ENTRYPOINT ["java", "-jar", "online-pharmacy"]