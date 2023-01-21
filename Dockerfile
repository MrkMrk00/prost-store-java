FROM amazoncorretto:17-alpine-jdk AS build

WORKDIR /build
COPY . .

# Frontend build (have to be first -> static is coppied with java build)
RUN apk add --update nodejs npm
RUN npm install && npm run build

# Java build
RUN ./gradlew clean bootJar

FROM amazoncorretto:17-alpine

WORKDIR /app
COPY --from=build /build/build/ ./

ENV APP_ENV="prod"

RUN export DB_PASSWD=$(cat resources/main/db_passwd)
RUN rm -f resources/main/db_passwd

ENTRYPOINT ["java", "-jar", "libs/prost.jar"]