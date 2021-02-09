FROM postgres:11
ENV POSTGRES_DB world
ENV POSTGRES_USER root
ENV POSTGRES_PASSWORD password
ADD init.sql /docker-entrypoint-initdb.d/
