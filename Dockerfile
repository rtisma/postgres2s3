###############################
# Maven builder
###############################
# -alpine-slim image does not support --release flag
FROM adoptopenjdk/openjdk11:jdk-11.0.6_10-alpine-slim as builder

# Build song-server jar
COPY . /srv
WORKDIR /srv
RUN ./mvnw clean package -DskipTests

###############################
# Postgres2s3
###############################
FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine

# Paths
ENV APP_HOME /srv
ENV APP_LOGS $APP_HOME/logs
ENV JAR_FILE $APP_HOME/app.jar
ENV APP_USER app
ENV APP_UID 9999
ENV APP_GID 9999

RUN addgroup -S -g $APP_GID $APP_USER  \
    && adduser -S -u $APP_UID -G $APP_USER $APP_USER  \
    && mkdir -p $APP_HOME $APP_LOGS \
    && chown -R $APP_UID:$APP_GID $APP_HOME \
	&& apk update \
	&& apk add postgresql \
	&& rm -rf /var/cache/apk/*

COPY --from=builder /srv/target/*-exec.jar $JAR_FILE

USER $APP_UID

WORKDIR $APP_HOME

CMD java -Dlog.path=$APP_LOGS \
        -jar $JAR_FILE \
        --spring.config.location=classpath:/application.yml
