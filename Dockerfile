FROM openjdk:17-oracle

COPY build/libs/*.jar /opt/app.jar

ENTRYPOINT ["java", "-jar", "/opt/app.jar"]