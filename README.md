# Car Management - A Demo Application

This is a demo Spring application which allows creating and retrieving data about cars via a REST
API.

# Build and Test

## Requirements

* Java 17 or higher

# Usage

The service supports creation of new cars as shown in the following

```bash
bash> curl -X POST -H "Content-Type: application/json" -d '\
{ \
“brand”: “Flexa”, \
“licensePlate”: “L-CS8877E”, \
"manufacturer": "Carcorp", \
"operationCity": "Newtown", \
“status”: “available”, \
}' 127.0.0.1/cars
{
  "id": 12345
}
```

The returned JSON object contains the ID of the new car.

To overwrite properties of existing cars use `PUT` as shown in the following

```bash
bash> curl -X PUT -H "Content-Type: application/json" -d '\
{ \
“brand”: “Flexa”, \
“licensePlate”: “L-CS8877E”, \
"manufacturer": "Carcorp", \
"operationCity": "Newtown", \
“status”: “available”, \
}' 127.0.0.1/cars/12345
{
“id”: 12345,
“brand”: “Flexa”,
“licensePlate”: “L-CS8877E”,
"manufacturer": "Carcorp",
"operationCity": "Newtown",
“status”: “available”,
“createdAt”: “ "2017-09-01T10:23:47.000Z",
“lastUpdatedAt”: “ "2022-04-15T13:23:11.000Z"
}
```

The `PUT` response contains the new state of the addressed car.

The service also supports querying individual cars via `GET` as shown in the following

```bash
bash> curl 127.0.0.1/cars/12345
{
“id”: 12345,
“brand”: “Flexa”,
“licensePlate”: “L-CS8877E”,
“status”: “available”,
“createdAt”: “ "2017-09-01T10:23:47.000Z",
“lastUpdatedAt”: “ "2022-04-15T13:23:11.000Z"
}
```

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
