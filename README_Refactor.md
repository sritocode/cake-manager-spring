Cake Manager Micro Service - Changes
======================================

* Changed the application to use Spring Boot so the configurations are minimal
* Converted the Rest implementation to use Spring REST
* Database use Spring JPA which provides ready made repositories to perform CRUD operations
* Implemented common Exception handling using @ControllerAdvice --> AOP
* Simple controller using Thymeleaf for web
* Added tests to validate the rest and database layers
* Using in-memory H2 for storing data
* Using Spring Boot Plugin/Devtools for executing the server locally
* Using Logback for logging

Instructions to run application:
================================

To run a server locally execute the following command:

`mvn spring-boot:run`

and access the following URL:

`http://localhost:8080/`


Instructions to access end points:
=================================

1. The project loads some pre-defined data in to an in-memory database from classpath:cakes-initialLoad.json
2. Root '/' --> shows a welcome message
3. /cakes using GET method & Content-Type 'application/json;charset=UTF-8' returns all cakes from database
4. /cakes using POST method take a Json string (type: CakeEntity) from body and adds a new cake to data base
5. /cakes/{cakeTitle} using GET method returns the cake matched by provided title



NOTE
====
Refactor focused only on the backend capabilities.
Readability by human is excluded from the scope due to the lack of front end experience.


