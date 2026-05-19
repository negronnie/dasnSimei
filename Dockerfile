FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn package -DskipTests -q

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /build/target/dasnSimei-*.jar app.jar

ENV DB_URL=""
ENV DB_USER=""
ENV DB_PASS=""

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
