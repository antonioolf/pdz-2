FROM gradle:7.0.0 as builder
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

FROM openjdk:latest
COPY --from=builder /app/build/libs/pdz-2-0.1-all.jar /subscription.jar
EXPOSE 8085
CMD ["java", "-jar", "subscription.jar"]
