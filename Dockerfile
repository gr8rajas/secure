#Defining the Environment
#FROM openjdk:8
FROM openjdk:8-jre-buster

MAINTAINER Raj

#Define Arguments(sbt version)
ARG SBT_VERSION=1.3.0

#Retreiving Dependencies and verifying version compatibility
RUN \
  curl -L -o sbt-$SBT_VERSION.deb http://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt && \
  sbt sbtVersion

WORKDIR /Testing

#Move SourceCode to BaseImage
ADD . .

#Run the program to retrieve the Frequent Visitors, by default retrieves top 3 frequent Visitors
CMD sbt run
