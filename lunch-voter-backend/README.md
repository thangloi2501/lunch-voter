# Lunch Voter (Backend)

## Tech stacks
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3](https://maven.apache.org)
- [Spring Boot 2.7.5](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot/2.7.5) (web, websocket, data JPA, test)
- Hibernate
- Embedded H2 database
- [Spock/Groovy](https://spockframework.org/) for unit testing
- [Lombok](https://projectlombok.org/)

## Clone source code and setup environment
- Install [Git](https://git-scm.com/).
- Install Maven 3 and Java 17 as mentioned above.
- Make sure JAVA_HOME environment variable is configured and pointing to Java 17 home. Make sure 
[Maven uses Java 17](https://mkyong.com/maven/maven-error-invalid-target-release-17/).

```bash
git clone https://github.com/thangloi2501/lunch-voter.git
cd lunch-voter/lunch-voter-backend
```

## Run the application
```bash
mvn clean
mvn spring-boot:run
```

## Test the application
```bash
mvn clean
mvn test
```

## API and Websocket documents
- REST APIs
- Websocket

## Contact
Loi Nguyen - loint.sg@gmail.com
