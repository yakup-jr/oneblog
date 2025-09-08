FROM amazoncorretto:21.0.4-alpine

WORKDIR /app

COPY ./app/build/libs/oneblog.jar ./oneblog.jar

LABEL author="yakup_jr"

ENTRYPOINT ["java", "-jar", "oneblog.jar"]
