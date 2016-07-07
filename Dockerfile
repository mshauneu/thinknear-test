FROM java:8

MAINTAINER Mikhail Shauneu

RUN mkdir /work /work/config

COPY target/thinknear-test.jar /work/
COPY target/classes/application.yml /work/config/application.yml

EXPOSE 7777

WORKDIR /work

ENTRYPOINT java $JAVA_OPTIONS -jar ./thinknear-test.jar
