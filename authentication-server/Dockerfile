FROM openjdk:17-oracle
EXPOSE 8080 9251

ADD target/spring-security-multi-factor-nexmo-authentication.jar spring-security-multi-factor-nexmo-authentication.jar
ADD docker-image-debug-entrypoint.sh docker-image-debug-entrypoint.sh

ENTRYPOINT ["sh", "/docker-image-debug-entrypoint.sh"]
