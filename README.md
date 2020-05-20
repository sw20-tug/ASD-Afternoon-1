# ASD-Afternoon-1
This is the LANG (Vocabulary Trainer) repository for the team *ASD: Afternoon 1*.

##Requirements
* JDK 11
* Maven
* [SceneBuilder](https://gluonhq.com/products/scene-builder/)
* [H2 Database Engine](https://h2database.com/html/main.html)

##Getting Started
 * Open the project in Eclipse or IntelliJ IDEA.
###Backend 
 * Run `mvnw spring-boot:run` to compile and run the server
 * (Execute `curl localhost:8080/vocab/` to test the connection to the server.)
###Frontend
 * Run `mvnw javafx:run` to compile and run the frontend GUI.
###Tests
* Run `mvnw surefire:test` to discover and execute all tests. 