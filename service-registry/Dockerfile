FROM bellsoft/liberica-openjdk-alpine-musl:17

RUN mkdir -p /usr/local/sr
ADD service-registry/target/service-registry-0.0.1-SNAPSHOT.jar /usr/local/sr/

EXPOSE 8761

CMD echo "********************************************************"
CMD echo "Starting service-registry"
CMD echo "********************************************************"

CMD java -Dserver.port=$SERVER_PORT \
     -jar /usr/local/sr/service-registry-0.0.1-SNAPSHOT.jar
