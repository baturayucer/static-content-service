
## Image Service

Image Service Spring Boot Application

### Tech Stack

Spring Boot Framework is used to implement web back-end logic. 

* Spring Boot 2.7.2
* Kotlin 1.7 / Java 17
* Postgres
* Maven
* Docker

## Installation

You'll need Docker installed in your local environment in order to start project up.
* To install Docker: https://www.docker.com/

### Run the Project

* It's possible to run the project directly by running this command:
  ```sh
  sh start-app.sh
  ```
  
* Or run the following commands in order:

  ```sh
  mvn clean install
  ```
  ```sh
  docker-compose up -d
  ```
  
* To stop the application: 
  ```sh
  sh stop-app.sh
  ```
* Or run the following command:

  ```sh
  docker-compose down
  ```

## Api Documentation

* Api documentation will be created automatically after application startup.
* Reach out Api Documentation via http://localhost:8080/api-doc.html

## Test Data
 - baturay.jpg
 - abcde.jpg
 - abcdefhij.jpg
 - _somedir_anotherdir_abcdef.jpg

## Important Note
Please use postman, if you want to download the actual images. 
Swagger does not provide multiple accept headers in the request, so you'll see the string equivalence of bytearrays.

Postman Collection: https://www.getpostman.com/collections/e92b8068dae6249a66a4


## Important Note
In order to connect to aws s3 environment, you need to provide aws credentials 
under ->  "~/.aws/credentials" path in your local machine.
Otherwise, application won't be able to start up.