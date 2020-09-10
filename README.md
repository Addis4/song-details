# song-details
This project will help you connect with GraphQL using Maven Spring-Boot application. This sample project has many of the use cases which are common in any of the project based on GraphQL implementation.

**Prerequisites**  
- Java Development Kit (JDK), version 8 or higher.

- Maven

**Command to start the project**  
`mvn spring-boot:run`    
  

**Value you can fetch:**  
- id(Song Id)  
- name(Song Name)  
- genre(Song Genre)
- artist(Artist Details)  
 {id(Artist Id),  
 firstName(Artist/s First Name),   
 lastName(Artist/s Last Name)}


Json Formats for different Rest services are mentioned below :

1. Success Case:

Route(Method - POST) : http://localhost:8080/graphql

RawData(json): {
                   "query":"query{songById(id: \"song-1\"){id,name}}"
               }

2. Failure Case:

Route(Method - POST) : http://localhost:8080/graphql

RawData(json): {
                   "query":"query{songById(id: \"song-4\"){uid,Name}}"
               }