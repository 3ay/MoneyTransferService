FROM openjdk:11.0.7-jdk-slim
ADD target/MoneyTransferService-0.0.1-SNAPSHOT.jar /transfer_app.jar
WORKDIR /app
COPY src/main/resources/logDAO.json /app/src/main/resources/logDAO.json
EXPOSE 7070
ENTRYPOINT ["java","-jar","/transfer_app.jar"]