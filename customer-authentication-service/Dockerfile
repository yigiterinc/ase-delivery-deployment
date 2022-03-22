FROM bellsoft/liberica-openjdk-alpine-musl:17

RUN mkdir -p /usr/local/cas
ADD customer-authentication-service/target/customer-authentication-service-0.0.1-SNAPSHOT.jar /usr/local/cas/

EXPOSE 8081

CMD echo "********************************************************"
CMD echo "Wait for mongodb to be available"
CMD echo "********************************************************"

CMD echo $MONGODB_STATUS_HOST $MONGODB_STATUS_PORT
CMD while ! nc -z $MONGODB_STATUS_HOST $MONGODB_STATUS_PORT; do \
  printf 'mongodb is still not available. Retrying...\n'; \
  sleep 3; \
done;

CMD echo "********************************************************"
CMD echo "Starting customer-authentication-service"
CMD echo "********************************************************"

CMD java -Dserver.port=$SERVER_PORT \
     -Dspring.data.mongodb.uri=$MONGODB_URI \
     -jar /usr/local/cas/customer-authentication-service-0.0.1-SNAPSHOT.jar
