# Entrant Portal
This repository contains a ready-to-use application. 
The server part is a REST API. The client is a SPA application built on Angular. 
Another client is the Python script, which generates a report in PDF document format.

## Features
- User registration in the system
- Filling in personal data about yourself by a user
- Submitting an application for directions of study at a university
- Control your rating in the list of user applications
- Verification of sent applications by administrator
- Automatic calculation of application rating
- Completion of the admission of entrants by automatic sending of an email based on their priority and exam results
- Generation of reporting information in PDF format

## Using
- Server Part
  - Java 8
  - Spring Boot
  - Spring Security
  - Spring Web
  - Spring Data JPA (Hibernate)
  - Spring Mail
  - JJWT
  - Project Lombok
  - H2 Database
  - Maven
- UI Part
  - Angular 10
  
## Do not forget
Add file `~\src\main\resources\application.yml` like:

```YML
spring:
  datasource:
    driverClassName: org.h2.Driver
    url: jdbc:h2:file:./data/demo
    login: sa
  jpa:
    show-sql: true
    open-in-view: false
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate.ddl-auto: create-drop
    properties:
      hibernate:
        format_sql: true
        default_batch_fetch_size: 20
        jdbc.batch_size: 20
  h2:
    console:
      enabled: true
      web-allow-other: true
    allowed.resources: /h2-console/**
  mail:
    host: <smtp host>
    port: <smtp port>
    username: <smtp username>
    password: <smtp password>

server:
  error:
    include-message: always

jwt:
  header: Authorization
  secret: <sercret-word>
  expiration: 604800 # sec = 7 days

```

## Build commands
`mvn spring-boot:run`

`ng serve`
