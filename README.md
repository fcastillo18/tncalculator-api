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

### Test the api
- Call the /signup endpoint: http://localhost:8080/api/auth/signup with a body like this:
    ```
    {
        "username": "admin",
        "email": "admin@mail.com",
        "password": "12345678",
        "role":["admin"]
    }
    ```
    You should get this response **"message": "User registered successfully!"**

- Call the /sigin endpoint: http://localhost:8080/api/auth/signin with the previous info:
    ```
    {
        "username": "admin",
        "password": "12345678"
    }
    ```
  You will get a response like this:
  ```
    {
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4NDk0NjU4NSwiZXhwIjoxNjg1MDMyOTg1fQ.OU-F377G7byeaeVnh03bDXhCRZVL70uqGhVJof2e7DA",
      "type": "Bearer",
      "id": 1,
      "username": "admin",
      "email": "admin@mail.com",
      "roles": [
        "ROLE_ADMIN"
      ]
    }
  ```
  - Can use Swagger by accessing this url: http://localhost:8080/swagger-ui/index.html#/
  - Take the token and use it to call the endpoints. If you are using Postman see this as an example:
   ![postman example.png](src%2Fmain%2Fresources%2Fstatic%2Fpostman%20example.png)



### Properties files