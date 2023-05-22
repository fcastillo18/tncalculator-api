# tncalculator-api

You must have [Docker](https://www.docker.com/products/docker-desktop/) installed on your machine to run this project.

### Local development
- Set the variable `spring.profiles.active=dev` within the `applicacion.properties`
- In the root project folder, do a `cd ./docker-local-db`
- Run `docker build -t mysql .`
- Run `docker run -d -p 3307:3306 --name mysqldb-dev -e MYSQL_ROOT_PASSWORD=admin mysql`

### Start the app with `docker-compose`
- Set the variable `spring.profiles.active=staging` within the `applicacion.properties`
- Create app.jar by running `./gradlew bootJar` in the console
- After that, run the command: `docker-compose up`
- Execute `docker ps` and wait until `tncalculator-api-server-1` &  `tncalculator-api-mysqldb-1` are up and running
- Can send a GET request to `http://localhost:8080/api/v1/user/all` to check that is working 

### Properties files