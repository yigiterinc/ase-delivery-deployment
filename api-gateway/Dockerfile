FROM bellsoft/liberica-openjdk-alpine-musl:17

RUN mkdir -p /usr/local/ag
ADD api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar /usr/local/ag/

EXPOSE 8082

CMD echo "********************************************************"
CMD echo "Starting api-gateway"
CMD echo "********************************************************"

CMD java -Dserver.port=$SERVER_PORT \
     -jar /usr/local/ag/api-gateway-0.0.1-SNAPSHOT.jar
