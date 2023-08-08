# Store
## Challenge
Create a Java microservice to manage items from a shop. This microservice will expose an API to CRUD items. Item model is open, use your creativity.

## Startup
### Using IDE - IntelliJ IDEA
Application is easily runnable from main class: pl.wjanek.store.StoreApplication#run(). Simply run the `main()` method.
### Using Maven - Spring Boot plugin
Application can also be started by using `mvn spring-boot:run`. If you want to run the application as a daemon, run `mvn spring-boot:start` to start the application and `mvn spring-boot:stop` to stop it.
### Using Docker compose
Application can be run in a container. To build an image, run `mvn spring-boot:build-image`. After that, entire environment can be started by running the command: `cd docker; docker-compose up`.
### Using JAR file
Application can also be run from JAR file. Run `mvn package` which should result in `target/store-0.0.1-SNAPSHOT.jar` file. To start the application, simply run `java -jar target/store-0.0.1-SNAPSHOT.jar`.

## Expected outcome
1. one endpoint to create one item: `POST /item`
2. one endpoint to create multiple items: `POST /item/bulk`
3. one endpoint to delete an item: `DELETE /item/{id}`
4. one endpoint to update an item: `PATCH /item/{id}`
5. one endpoint to fetch an item by id, name and some other field of your choice: `GET /item/{id}`
6. one endpoint to fetch all items: `GET /item`

## Swagger
Swagger is available under following link: http://localhost:8080/swagger-ui/index.html.

## Security
For simplicity, endpoints are secured by Basic Authentication. That means every request should have the `Authentication` header with value `Basic base64(username:password)`. The default user credentials are:
- username: `user`
- password: `password`

These values are hardcoded into the sourcecode. 

## Configuration
Main configuration can be overridden by creating a `src/main/resource/application-localhost.yml` file.

### Cache
By default, caching in-memory is enabled, to change it to Redis-based caching, set the property `spring.cache.type` to `redis` or to `none` if cache should be disabled. This can also be overridden by setting up the `SPRING_CACHE_TYPE` environmental variable.

### RabbitMQ
Basic traces of HTTP requests are sent into a Rabbit queue called `traceQueue`. To disable this feature, set the property `app.rabbitmq.enabled` or environmental variable `APP_RABBITMQ_ENABLED` to `false`. Host of RabbitMQ can be overridden by property `spring.rabbitmq.host` or by environmental variable `SPRING_RABBITMQ_HOST`.

## Tests
To run tests, run `mvn test`.

## Steps unclear
> one endpoint to fetch an item by id, name and some other field of your choice;

As an item is identifiable by id, it is the only parameter needed to fetch single result. Additional fields will only provide potential filtering out of this single element, therefore I decided not to implement that.
