FROM postgres
MAINTAINER natarajan <natarajanrodrigues@gmail.com>
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD 12345
ENV POSTGRES_DB suggestions
COPY create.sql /docker-entrypoint-initdb.d/
