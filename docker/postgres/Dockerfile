FROM postgres:15.4
ENV POSTGRES_PASSWORD=root
ENV POSTGRES_USER=postgres

WORKDIR opt/app

COPY data.sql sql/
COPY schema.sql sql/

