FROM eclipse-temurin:21
WORKDIR /home
COPY ./target/customer-management-system-3.5.3.jar customer-management-system.jar
ENTRYPOINT ["java", "-jar", "customer-management-system.jar"]