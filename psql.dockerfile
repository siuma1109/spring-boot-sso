FROM postgres:14

COPY ./init-sso-db.sh /docker-entrypoint-initdb.d/init-sso-db.sh

ENV POSTGRES_USER root
ENV POSTGRES_PASSWORD 1234
ENV POSTGRES_DB sso

EXPOSE 5432