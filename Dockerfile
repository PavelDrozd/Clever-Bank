FROM tomcat:9-jdk17

WORKDIR /usr/local/tomcat/webapps
COPY /build/libs/clever.war clever.war