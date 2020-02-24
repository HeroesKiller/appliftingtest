FROM java:8
EXPOSE 8080
ADD /target/applmonitoring.jar applmonitoring.jar
ENTRYPOINT ["java","-jar","applmonitoring.jar"]