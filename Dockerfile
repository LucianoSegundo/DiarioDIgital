FROM ubuntu:latest AS build


ARG SENHA_DB=${SENHA_DB}
ARG URL_DB=${URL_DB}
ARG USUARIO_DB=${USUARIO_DB}

ENV SENHA_DB=${SENHA_DB}
ENV URL_DB=${URL_DB}
ENV USUARIO_DB=${USUARIO_DB}

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install 

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /target/diarioDigital-0.0.1-SNAPSHOT.jar app.jar


ENTRYPOINT [ "java", "-jar", "app.jar" ]