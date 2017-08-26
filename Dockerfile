FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/ecssboot-0.0.1.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
