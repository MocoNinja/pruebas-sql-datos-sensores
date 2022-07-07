FROM postgres:14-alpine

LABEL maintainer="MocoNinja"

ENV POSTGRES_USER "developer"
ENV POSTGRES_PASSWORD "developer"
ENV POSTGRES_DB "SENSOR_DATA"

ADD "./init.sql" "/docker-entrypoint-initdb.d/"
