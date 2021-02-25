FROM adoptopenjdk/openjdk15:alpine-slim

# Create a docker user that processes can run as
RUN adduser --system --home /home/docker --disabled-password --shell /bin/bash docker
ENV USER docker
ENV HOME /home/docker

ARG APP_JAR=codechallenge-be-1.0.0.jar

# Add application jar
COPY /target/$APP_JAR ${HOME}/
RUN ln -sf ${HOME}/$APP_JAR ${HOME}/application.jar

WORKDIR ${HOME}

ENTRYPOINT ["java", "-jar", "application.jar"]
