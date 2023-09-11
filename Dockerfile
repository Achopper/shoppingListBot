FROM eclipse-temurin:17-jdk-jammy
LABEL authors="achopper"

WORKDIR /.
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src
CMD ["./mvnw", "spring-boot:run"]