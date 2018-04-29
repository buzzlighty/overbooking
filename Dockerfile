FROM java:openjdk-8-jre-alpine
MAINTAINER Kirill Bazarov <es.kelevra@gmail.com>

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/overbooking/overbooking.jar"]


ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/overbooking/overbooking.jar