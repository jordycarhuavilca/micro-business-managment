# Fetching latest version of Java
FROM openjdk:22

  # Setting up work directory
WORKDIR /app

  # Copy jar from directory target to image directory
COPY ./target/CiberCine.jar /app

  # Exposing port 8080
EXPOSE 8080

  # Starting the application
ENTRYPOINT ["java","-jar","CiberCine.jar","--spring.profiles.active=prod"]