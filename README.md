# Vertx-Hibernate-reactive

## APIs

### Project
* POST - **/projects** <br>
Request Payload :
```json
{
  "userId" : 1,
  "name" : "My Project"
}
```
* PUT - **/projects** <br>
Request Payload (only "name" value modifiable) :
```json
{
  "id" : 1,
  "name" : "My Project"
}
```
* GET -
  * By Id : **/projects/one/{id}**
  * By UserId : **/projects/user/{userId}**
* DELETE - **/projects/{id}**

## PostgreSQL

### Docker command
(If "my_postgres" is not already running in **Docker > Containers** list)
```
docker run -d \
--name my_postgres \
-e POSTGRES_PASSWORD=password \
-e POSTGRES_USER=postgres \
-e POSTGRES_DB=postgres \
-p 5432:5432 \
postgres
```
### DB Client commands
```
CREATE DATABASE hibernate;
# List all databases in postgresSQL
SELECT datname FROM pg_database;
```

## Vert.x
This application was generated using http://start.vertx.io
![vert.x-4.5.7](https://img.shields.io/badge/vert.x-4.5.7-purple.svg)

### Building
To launch your tests:
```
./mvnw clean test
```
To package your application:
```
./mvnw clean package
```
To run your application:
```
./mvnw clean compile exec:java
```

### Help
* [Vert.x Documentation](https://vertx.io/docs/)
* [Vert.x Stack Overflow](https://stackoverflow.com/questions/tagged/vert.x?sort=newest&pageSize=15)
* [Vert.x User Group](https://groups.google.com/forum/?fromgroups#!forum/vertx)
* [Vert.x Discord](https://discord.gg/6ry7aqPWXy)
* [Vert.x Gitter](https://gitter.im/eclipse-vertx/vertx-users)
