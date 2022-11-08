# Car Management - A Demo Application

This is a demo Spring application which allows creating and retrieving data about cars via a REST
API.

# Build and Test

* Install Java 17 or higher
* Run `./mvnw install`

# Usage

Run the service locally via `.\mvnw spring-boot:run`.

The service supports creation of new cars as shown in the following

```bash
bash> curl -X POST -H "Content-Type: application/json" -d '\
{ \
  "brand": "Flexa", \
  "licensePlate": "L-CS8877E", \
  "manufacturer": "Carcorp", \
  "operationCity": "Newtown", \
  "status": "available" \
}' 127.0.0.1/cars
{
  "id": 12345,
  "brand": "Flexa",
  "licensePlate": "L-CS8877E",
  "manufacturer": "Carcorp",
  "operationCity": "Newtown",
  "status": "available",
  "createdAt": "2022-04-15T10:23:47.000Z",
  "lastUpdatedAt": "2022-04-15T10:23:47.000Z"
}
```

The returned JSON object contains the ID of the new car.

To overwrite properties of existing cars use `PUT` as shown in the following

```bash
bash> curl -X PUT -H "Content-Type: application/json" -d '\
{ \
  "operationCity": "Someotherplace" \
}' 127.0.0.1/cars/12345
{
  "id": 12345,
  "brand": "Flexa",
  "licensePlate": "L-CS8877E",
  "manufacturer": "Carcorp",
  "operationCity": "Someotherplace",
  "status": "available",
  "createdAt": "2022-04-15T13:23:11.000Z",
  "lastUpdatedAt": "2022-04-15T13:25:01.000Z"
}
```

The `PUT` response contains the new state of the addressed car.

The service also supports querying individual cars via `GET` as shown in the following

```bash
bash> curl 127.0.0.1/cars/12345
{
  "id": 12345,
  "brand": "Flexa",
  "licensePlate": "L-CS8877E",
  "manufacturer": "Carcorp",
  "operationCity": "Newtown",
  "status": "available",
  "createdAt": "2022-04-15T13:23:11.000Z",
  "lastUpdatedAt": "2022-04-15T13:23:11.000Z"
}
```

# Design Decisions

- Using `maven` because I know it. It has lots of flaws, `gradle` is probably the better alternative
  but I'm not yet familiar wit it (I'm more used to Scala's `sbt`).
- We use OpenAPI to define the API in a platform-agnostic way and generate the Spring code for
  implementing the API. This improves the confidence that API consumer and API endpoint have the
  same understanding of the offered API.
  - We do _not_ use Spring Data REST since this makes the backend implementation the ground truth  
    about what to expect from the service. This is okay if the API which we are writing is private
    to the service (i.e., because it is only consumed by a front-end service written by the same
    team). If the service is public and consumed by an unknown set of users, it is better to go
    API-first and formally define the API as we have done instead of having the API be whatever
    Spring Data REST gives us.
- End-to-end integration test with the full service running are not done here, since they are
  better done in a full deployment setup with the final service container. In the local unit tests,
  only the levels below the web access layer are tested.

# Further Reading

## Spring

### Spring Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.3/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.3/reference/htmlsingle/#web)

### Spring Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
