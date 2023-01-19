FROM amazoncorretto:17-alpine-jdk AS build

WORKDIR /build
COPY . .

# Java build
RUN ./gradlew clean bootJar

# Frontend build
RUN apk add --update nodejs npm
RUN npm install && npm run build

FROM amazoncorretto:17-alpine

WORKDIR /app
COPY --from=build /build/build/ ./

ENV APP_ENV="prod"
ENTRYPOINT ["java", "-jar", "libs/prost.jar"]